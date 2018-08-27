package com.hx.lib_hx.custom.TreeView.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hx on 2018/1/24.
 */

public class Node {
    private String mId;
    private String mPid;//上一级id
    private String mLabel;
    private int mLevel;
    private boolean mIsExpand=false;
    private int mIcon;
    private Node mParent;
    private List<Node> mChildren=new ArrayList<>();

    public Node(String id, String pid, String name) {
        mId = id;
        mPid = pid;
        mLabel = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPid() {
        return mPid;
    }

    public void setPid(String pid) {
        mPid = pid;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public Node getParent() {
        return mParent;
    }

    public void setParent(Node parent) {
        mParent = parent;
    }

    public List<Node> getChildren() {
        return mChildren;
    }

    public void setChildren(List<Node> children) {
        mChildren = children;
    }

    /**
     * 判断是否为根节点
     * @return
     */
    public boolean isRoot() {
        return mParent==null;
    }

    /**
     * 判断当前父节点的收缩状态
     * @return
     */
    public boolean isParentExpand() {
        if (mParent == null) {
            return false;
        }else {
            return mParent.isExpand();
        }
    }

    /**
     * 判断是否为叶子节点
     * @return
     */
    public boolean isLeaf() {
        return mChildren.size()==0;
    }

    public int getLevel() {
        return mParent==null?0:mParent.getLevel()+1;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public boolean isExpand() {
        return mIsExpand;
    }

    public void setExpand(boolean expand) {
        mIsExpand = expand;
        if (!expand) {
            for (Node node : mChildren) {
                node.setExpand(false);
            }
        }
    }

}
