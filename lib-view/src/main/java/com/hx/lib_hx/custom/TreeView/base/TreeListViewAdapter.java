package com.hx.lib_hx.custom.TreeView.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by hx on 18-1-24.
 */

public abstract class TreeListViewAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<Node> mAllNodes;
    protected List<Node> mVisibleNodes;
    protected ListView mTree;

    public interface TreeListViewOnItemListener{
        void onClick(Node node, int position);
    }
    private TreeListViewOnItemListener mTreeListViewOnItemListener;

    public void setTreeListViewOnItemListener(TreeListViewOnItemListener listener) {
        mTreeListViewOnItemListener=listener;
    }
    public TreeListViewAdapter(Context context,List<T> datas, ListView tree ,int defaultExpandLevel) throws IllegalAccessException {
        mContext = context;
        mAllNodes = TreeHelper.sortDatas(datas,defaultExpandLevel);
        mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
        mTree = tree;
        mInflater = LayoutInflater.from(context);
        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mTreeListViewOnItemListener == null) {
                    expandOrCollapse(position);
                }else {
                    mTreeListViewOnItemListener.onClick(mVisibleNodes.get(position),position);
                }
            }
        });
    }

    /**
     * 点击收缩或者展开
     * @param position
     */
    private void expandOrCollapse(int position) {
        Node node = mVisibleNodes.get(position);

        if (node != null) {
            if (node.isLeaf()) {
                return;
            }

            node.setExpand(!node.isExpand());

            mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mVisibleNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mVisibleNodes.get(position);
        convertView=getConvertView(node,position,convertView,parent);
        convertView.setPadding(node.getLevel()*30,3,3,3);
        return convertView;
    }

    protected abstract View getConvertView(Node node,int position,View convertView,ViewGroup parent);
}
