package com.lzj.sidebarviewdemo.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lzj.sidebarviewdemo.R
import com.lzj.sidebarviewdemo.bean.SortBean
import java.util.Locale

/**
 * 参考示例
 */
class SortAdapter(layoutResId: Int, private val mData: MutableList<SortBean>) :
    BaseQuickAdapter<SortBean, BaseViewHolder>(layoutResId, mData) {

    override fun convert(viewHolder: BaseViewHolder, item: SortBean) {

        //第一个字母显示
        if (viewHolder.layoutPosition == 0) {
            (viewHolder.getView<View>(R.id.tv_key)).visibility = View.VISIBLE
        } else {
            //首字母和上一个首字母是否相同,如果相同字母导航条就影藏,否则就显示
            val section = getSectionForPosition(viewHolder.layoutPosition)
            if (viewHolder.layoutPosition == getPositionForSection(section)) {
                (viewHolder.getView<View>(R.id.tv_key)).visibility = View.VISIBLE
            } else {
                (viewHolder.getView<View>(R.id.tv_key)).visibility = View.GONE
            }
        }
        viewHolder.setText(R.id.tv_key, item.first)
        viewHolder.setText(R.id.tv_name, item.name)
    }

    /**
     * 根据View的当前位置获取分类的首字母的Char ascii值
     */
    fun getSectionForPosition(position: Int): Int {
        return data[position].first[0].code
    }

    /**
     * 获取第一次出现该首字母的List所在的位置
     */
    fun getPositionForSection(section: Int): Int {
        for (i in data.indices) {
            val sortStr = data[i].first
            val firstChar = sortStr.uppercase(Locale.getDefault())[0]
            if (firstChar.code == section) {
                return i
            }
        }
        return -1
    }
}
