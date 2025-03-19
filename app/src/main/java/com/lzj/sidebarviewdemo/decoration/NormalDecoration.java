package com.lzj.sidebarviewdemo.decoration;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.HashMap;
import java.util.Map;

public abstract class NormalDecoration extends RecyclerView.ItemDecoration {
    protected String TAG = "QDX";
    private final Paint mHeaderTxtPaint;
    private final Paint mHeaderContentPaint;

    protected int headerHeight = 100;
    private int textPaddingLeft = 50;
    private int textSize = 50;
    private int textColor = -16777216;
    private int headerContentColor = -1118482;
    private final float txtYAxis;
    private RecyclerView mRecyclerView;
    private boolean isInitHeight = false;
    private final SparseIntArray stickyHeaderPosArray = new SparseIntArray();
    private GestureDetector gestureDetector;
    private final Map<Integer, View> headViewMap = new HashMap<>();

    private NormalDecoration.OnHeaderClickListener headerClickEvent;

    private final GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        public boolean onDown(MotionEvent e) {
            return false;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            for(int i = 0; i < NormalDecoration.this.stickyHeaderPosArray.size(); ++i) {
                int value = NormalDecoration.this.stickyHeaderPosArray.valueAt(i);
                float y = e.getY();
                if ((float)(value - NormalDecoration.this.headerHeight) <= y && y <= (float)value) {
                    if (NormalDecoration.this.headerClickEvent != null) {
                        NormalDecoration.this.headerClickEvent.headerClick(NormalDecoration.this.stickyHeaderPosArray.keyAt(i));
                    }

                    return true;
                }
            }

            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };
    private NormalDecoration.OnDecorationHeadDraw headerDrawEvent;
    private final Map<String, Drawable> imgDrawableMap = new HashMap();

    public NormalDecoration() {
        this.mHeaderTxtPaint = new Paint(1);
        this.mHeaderTxtPaint.setColor(this.textColor);
        this.mHeaderTxtPaint.setTextSize((float)this.textSize);
        this.mHeaderTxtPaint.setTextAlign(Paint.Align.LEFT);
        this.mHeaderContentPaint = new Paint(1);
        this.mHeaderContentPaint.setColor(this.headerContentColor);
        Paint.FontMetrics fontMetrics = this.mHeaderTxtPaint.getFontMetrics();
        float total = -fontMetrics.ascent + fontMetrics.descent;
        this.txtYAxis = total / 2.0F - fontMetrics.descent;
    }

    public void getItemOffsets(Rect outRect, View itemView, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, itemView, parent, state);
        if (this.mRecyclerView == null) {
            this.mRecyclerView = parent;
        }

        if (this.headerDrawEvent != null && !this.isInitHeight) {
            View headerView = this.headerDrawEvent.getHeaderView(0);
            headerView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            this.headerHeight = headerView.getMeasuredHeight();
            this.isInitHeight = true;
        }

        int position = parent.getChildAdapterPosition(itemView);
        String curHeaderName = this.getHeaderName(position);
        if (curHeaderName != null) {
            if (position == 0 || !curHeaderName.equals(this.getHeaderName(position - 1))) {
                outRect.top = this.headerHeight;
            }

        }
    }

    public abstract String getHeaderName(int var1);

    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        super.onDrawOver(canvas, recyclerView, state);
        if (this.mRecyclerView == null) {
            this.mRecyclerView = recyclerView;
        }

        if (this.gestureDetector == null) {
            this.gestureDetector = new GestureDetector(recyclerView.getContext(), this.gestureListener);
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return NormalDecoration.this.gestureDetector.onTouchEvent(event);
                }
            });
        }

        this.stickyHeaderPosArray.clear();
        int childCount = recyclerView.getChildCount();
        int left = recyclerView.getLeft() + recyclerView.getPaddingLeft();
        int right = recyclerView.getRight() - recyclerView.getPaddingRight();
        String firstHeaderName = null;
        int firstPos = 0;
        int translateTop = 0;

        for(int i = 0; i < childCount; ++i) {
            View childView = recyclerView.getChildAt(i);
            int pos = recyclerView.getChildAdapterPosition(childView);
            String curHeaderName = this.getHeaderName(pos);
            if (i == 0) {
                firstHeaderName = curHeaderName;
                firstPos = pos;
            }

            if (curHeaderName != null) {
                int viewTop = childView.getTop() + recyclerView.getPaddingTop();
                if (pos == 0 || !curHeaderName.equals(this.getHeaderName(pos - 1))) {
                    if (this.headerDrawEvent != null) {
                        if (this.headViewMap.get(pos) == null) {
                            View headerView = this.headerDrawEvent.getHeaderView(pos);
                            headerView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                            headerView.setDrawingCacheEnabled(true);
                            headerView.layout(0, 0, right, this.headerHeight);
                            this.headViewMap.put(pos, headerView);
                            canvas.drawBitmap(headerView.getDrawingCache(), (float)left, (float)(viewTop - this.headerHeight), null);
                        } else {
                            View headerView = this.headViewMap.get(pos);
                            canvas.drawBitmap(headerView.getDrawingCache(), (float)left, (float)(viewTop - this.headerHeight), null);
                        }
                    } else {
                        canvas.drawRect((float)left, (float)(viewTop - this.headerHeight), (float)right, (float)viewTop, this.mHeaderContentPaint);
                        canvas.drawText(curHeaderName, (float)(left + this.textPaddingLeft), (float)(viewTop - this.headerHeight / 2) + this.txtYAxis, this.mHeaderTxtPaint);
                    }

                    if (this.headerHeight < viewTop && viewTop <= 2 * this.headerHeight) {
                        translateTop = viewTop - 2 * this.headerHeight;
                    }

                    this.stickyHeaderPosArray.put(pos, viewTop);
                    Log.i(this.TAG, "绘制各个头部" + pos);
                }
            }
        }

        if (firstHeaderName != null) {
            canvas.save();
            canvas.translate(0.0F, (float)translateTop);
            if (this.headerDrawEvent != null) {
                if (this.headViewMap.get(firstPos) == null) {
                    View headerView = this.headerDrawEvent.getHeaderView(firstPos);
                    headerView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                    headerView.setDrawingCacheEnabled(true);
                    headerView.layout(0, 0, right, this.headerHeight);
                    this.headViewMap.put(firstPos, headerView);
                    canvas.drawBitmap(headerView.getDrawingCache(), (float)left, 0.0F, null);
                } else {
                    View headerView = this.headViewMap.get(firstPos);
                    canvas.drawBitmap(headerView.getDrawingCache(), (float)left, 0.0F, null);
                }
            } else {
                canvas.drawRect((float)left, 0.0F, (float)right, (float)this.headerHeight, this.mHeaderContentPaint);
                canvas.drawText(firstHeaderName, (float)(left + this.textPaddingLeft), (float)(this.headerHeight / 2) + this.txtYAxis, this.mHeaderTxtPaint);
            }

            canvas.restore();
            Log.i(this.TAG, "绘制悬浮头部");
        }
    }

    public void setOnHeaderClickListener(NormalDecoration.OnHeaderClickListener headerClickListener) {
        this.headerClickEvent = headerClickListener;
    }




    public void onDestory() {
        this.headViewMap.clear();
        this.imgDrawableMap.clear();
        this.stickyHeaderPosArray.clear();
        this.mRecyclerView = null;
        this.setOnHeaderClickListener(null);

    }



    public interface OnDecorationHeadDraw {
        View getHeaderView(int var1);
    }

    public interface OnHeaderClickListener {
        void headerClick(int var1);
    }
}