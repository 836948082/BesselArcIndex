package com.runtai.besselarcindex.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.runtai.besselarcindex.R;
import com.runtai.besselarcindex.bean.Person;
import com.runtai.besselarcindexlibrary.view.LetterIndexer;

import java.util.LinkedHashMap;
import java.util.List;

public class PinnedHeaderListViewAdapter<T> extends BasePinnedHeaderAdapter<T> {

    public PinnedHeaderListViewAdapter(Context context, LinkedHashMap<String, List<T>> mMap, ListView listView,
                                       LetterIndexer letterIndexer, String[] constChar, int top, int bottom) {
        super(context, mMap);
        letterIndexer.setConstChar(constChar, top, bottom);
    }

    @Override
    protected View getListView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.person_item, null);
            viewHolder.header = (TextView) convertView.findViewById(R.id.pinnedheaderlistview_header);
            viewHolder.brandName = (TextView) convertView.findViewById(R.id.brand_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setBackgroundResource(R.drawable.app_listview_item_bg);

        if (datas != null) {
            int section = getSectionForPosition(position);
            Person brandItem = (Person) getItem(position);
            if (brandItem != null) {
                if (getPositionForSection(section) == position) {   //如果集合中字母对应的位置等于下标值，则显示字母，否则则隐藏
                    viewHolder.header.setVisibility(View.VISIBLE);
                    viewHolder.header.setText(sections.get(section));
                    viewHolder.header.setBackgroundColor(context.getResources().getColor(R.color.pinned_header_bg));
                } else {
                    viewHolder.header.setVisibility(View.GONE);
                }
                String brandName = brandItem.getName();
                if (null != brandName) {
                    viewHolder.brandName.setText(brandName);
                }
            }
        }

        return convertView;
    }

    /**
     * 设置头部固定布局和内容
     *
     * @param header  头部的布局
     * @param section 头部内容
     */
    @Override
    protected void setHeaderContent(View header, String section) {
        TextView textView = (TextView) header.findViewById(R.id.pinnedheaderlistview_header);
        textView.setText(section);
    }


    class ViewHolder {
        private TextView header;// 头部
        private TextView brandName;
    }

}
