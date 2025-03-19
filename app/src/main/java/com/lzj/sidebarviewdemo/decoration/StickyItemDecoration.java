package com.lzj.sidebarviewdemo.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class StickyItemDecoration extends RecyclerView.ItemDecoration {

    private int mHeight;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private Rect mTextBounds;

    public StickyItemDecoration() {
        mHeight = 100;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#FF000000"));
        mTextPaint.setTextSize(48f);
        mTextBounds = new Rect();
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        String previousStickyHeaderName = null;
        String currentStickyHeaderName = null;
        int left = parent.getLeft();
        //Decoration 的右边位置
        int right = parent.getRight();
        //获取 RecyclerView 的 Item 数量
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            //判断上一个 position 粘性头部的文字与当前 position 的粘性头部文字是否相同，如果相同则跳过绘制
            int position = parent.getChildAdapterPosition(childView);
            currentStickyHeaderName = getStickyHeaderName(position);
            if (TextUtils.isEmpty(currentStickyHeaderName)) {
                continue;
            }
            if (position == 0 || i == 0) {
                //Decoration 的底边位置
                int bottom = Math.max(childView.getTop(), mHeight);
                //当当前 Decoration 的 Bottom 比下一个 View 的 Decoration 的 Top （即下一个 View 的 getTop() - mHeight）大时
                //就应该使当前 Decoration 的 Bottom 等于下一个 Decoration 的 Top，形成推动效果
                View nextChildView = parent.getChildAt(i + 1);
                String nextStickyHeaderName = getStickyHeaderName(position + 1);
                if (nextChildView != null && !TextUtils.equals(currentStickyHeaderName, nextStickyHeaderName) && bottom > (nextChildView.getTop() - mHeight)) {
                    bottom = nextChildView.getTop() - mHeight;
                }
                //Decoration 的顶边位置
                int top = bottom - mHeight;
                c.drawRect(left, top, right, bottom, mPaint);
                //绘制文字
                mTextPaint.getTextBounds(currentStickyHeaderName, 0, currentStickyHeaderName.length(), mTextBounds);
                c.drawText(currentStickyHeaderName, left, bottom - mHeight / 2 + mTextBounds.height() / 2, mTextPaint);
                continue;
            }
            previousStickyHeaderName = getStickyHeaderName(position - 1);
            if (!TextUtils.equals(previousStickyHeaderName, currentStickyHeaderName)) {
                //Decoration 的底边位置
                int bottom = Math.max(childView.getTop(), mHeight);
                //当当前 Decoration 的 Bottom 比下一个 View 的 Decoration 的 Top （即下一个 View 的 getTop() - mHeight）大时
                //就应该使当前 Decoration 的 Bottom 等于下一个 Decoration 的 Top，形成推动效果
                View nextChildView = parent.getChildAt(i + 1);
                String nextStickyHeaderName = getStickyHeaderName(position + 1);
                if (nextChildView != null && !TextUtils.equals(currentStickyHeaderName, nextStickyHeaderName) && bottom > (nextChildView.getTop() - mHeight)) {
                    bottom = nextChildView.getTop() - mHeight;
                }
                //Decoration 的顶边位置
                int top = bottom - mHeight;
                c.drawRect(left, top, right, bottom, mPaint);
                //绘制文字
                mTextPaint.getTextBounds(currentStickyHeaderName, 0, currentStickyHeaderName.length(), mTextBounds);
                c.drawText(currentStickyHeaderName, left, bottom - mHeight / 2 + mTextBounds.height() / 2, mTextPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //outRect 相当于 Item 的整体绘制区域,设置 left、top、right、bottom 相当于设置左上右下的内间距
        //如设置 outRect.top = 5 则相当于设置 paddingTop 为 5px。
        int position = parent.getChildAdapterPosition(view);
        String stickyHeaderName = getStickyHeaderName(position);
        if (TextUtils.isEmpty(stickyHeaderName)) {
            return;
        }
        if (position == 0) {
            outRect.top = mHeight;
            return;
        }
        String previousStickyHeaderName = getStickyHeaderName(position - 1);
        if (!TextUtils.equals(stickyHeaderName, previousStickyHeaderName)) {
            outRect.top = mHeight;
        }
    }

    public abstract String getStickyHeaderName(int position);
}