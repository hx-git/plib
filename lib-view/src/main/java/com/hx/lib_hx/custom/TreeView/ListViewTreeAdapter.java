package com.hx.lib_hx.custom.TreeView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hx.lib_hx.R;
import com.hx.lib_hx.custom.TreeView.base.Node;
import com.hx.lib_hx.custom.TreeView.base.TreeListViewAdapter;

import java.util.List;

/**
 *
 * 使用listview
 * Created by hx on 18-1-24.
 */

public class ListViewTreeAdapter<T> extends TreeListViewAdapter {

    public ListViewTreeAdapter(Context context, List datas, ListView tree, int defaultExpandLevel) throws IllegalAccessException {
        super(context, datas, tree, defaultExpandLevel);
    }

    @Override
    protected View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tree_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mIv = convertView.findViewById(R.id.iv);
            viewHolder.mTv = convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.mTv.setText(node.getLabel());
        if (node.getIcon() == -1) {
            viewHolder.mIv.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.mIv.setVisibility(View.VISIBLE);
            viewHolder.mIv.setImageResource(node.getIcon());
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView mIv;
        TextView mTv;
    }
}
