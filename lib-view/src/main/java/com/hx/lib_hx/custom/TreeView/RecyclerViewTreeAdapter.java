package com.hx.lib_hx.custom.TreeView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hx.lib_hx.R;
import com.hx.lib_hx.custom.TreeView.base.Node;
import com.hx.lib_hx.custom.TreeView.base.TreeHelper;

import java.util.List;

/**
 *
 * 使用recyclerview
 * Created by hx on 18-1-28.
 */

public class RecyclerViewTreeAdapter<T> extends RecyclerView.Adapter<RecyclerViewTreeAdapter.ViewHolder> {
    private List<Node> mAllNodes;
    private List<Node> mVisibleNodes;
    private LayoutInflater mInflater;
    private OnItemClickListener mListener;

    public RecyclerViewTreeAdapter(Context context, List<T> datas, int defaultExpandLevel) {
        mInflater = LayoutInflater.from(context);
        try {
            mAllNodes = TreeHelper.sortDatas(datas,defaultExpandLevel);
            mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }finally {
            System.out.println("");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.tree_item_layout,parent,false));
    }

    /**
     * 注意是 holder是具体的
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerViewTreeAdapter.ViewHolder holder, int position) {
        final int posi=position;
        final Node node=mVisibleNodes.get(position);
        int imgId=node.getIcon();
        if(imgId==-1){
            holder.iv.setVisibility(View.INVISIBLE);
        }else {
            holder.iv.setVisibility(View.VISIBLE);
            holder.iv.setImageResource(imgId);
        }
        holder.tv.setText(mVisibleNodes.get(position).getLabel());
        holder.itemView.setPadding(node.getLevel()*40,3,3,3);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeExpandState(posi);
                if(mListener!=null){
                    mListener.onItemClick(node,v,posi);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(mListener!=null){
                    mListener.onItemLongClick(v,posi);
                }
                return true;
            }
        });
    }
    private void changeExpandState(int position) {
        Node node=mVisibleNodes.get(position);
        node.setExpand(!node.isExpand());
        //Log.wtf(TAG, "changeExpandState: "+node.isExpand());
        mVisibleNodes= TreeHelper.filterVisibleNodes(mAllNodes);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mVisibleNodes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        public ViewHolder(View itemView) {
            super(itemView);
            iv=  itemView.findViewById(R.id.iv);
            tv= itemView.findViewById(R.id.tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener=listener;
    }

    public interface OnItemClickListener{
        void onItemClick(Node node, View v, int position);
        void onItemLongClick(View v, int position);
    }
}
