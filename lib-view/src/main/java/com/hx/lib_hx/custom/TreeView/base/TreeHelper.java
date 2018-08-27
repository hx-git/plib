package com.hx.lib_hx.custom.TreeView.base;

import com.hx.lib_hx.R;
import com.hx.lib_hx.custom.TreeView.annotation.TreeNodeId;
import com.hx.lib_hx.custom.TreeView.annotation.TreeNodeLabel;
import com.hx.lib_hx.custom.TreeView.annotation.TreeParentId;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * 增加异常处理
 * Created by hx on 2018/1/24.
 */

public class TreeHelper {
    private static <T> List<Node> convertDatasToNodes(List<T> datas) throws Exception {
        List<Node> nodes = new ArrayList<>();
        List<Node> rootNodes = new ArrayList<>();
        String id = null;
        String pid = null;
        String label = null;
        long startTime;
        long endTime;
        for (T t : datas) {
            Class<?> clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    field.setAccessible(true);
                    id = (String) field.get(t);
                }
//                else {
//                    throw new Exception("TreeNodeId not null");
//                }
                if (field.getAnnotation(TreeParentId.class) != null) {
                    field.setAccessible(true);
                    pid = (String) field.get(t);
                }
//                else {
//                    throw new Exception("TreeParentId not null");
//                }
                if (field.getAnnotation(TreeNodeLabel.class) != null) {
                    field.setAccessible(true);
                    label = (String) field.get(t);
                }
//                else {
//                    throw new Exception("TreeNodeLabel not null");
//                }
            }
            nodes.add(new Node(id, pid, label));
        }
        int size=nodes.size();
        int i,j;
        Node n,m;
        startTime = System.nanoTime();
        for (i=0;i<size;i++) {
            n = nodes.get(i);
            for (j=i+1;j<size;j++) {
                m = nodes.get(j);
                if (m.getId().equals(n.getPid())) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
                else if (m.getPid().equals(n.getId())) {
                    n.getChildren().add(m);
                    m.setParent(n);
                }

            }
            //设置node图标
            setNodeIcon(n);
            //是否为根
            if (n.isRoot()) {
                rootNodes.add(n);
            }
        }
        endTime =System.nanoTime();
        System.out.println("设置图标耗时间:"+(endTime-startTime));
        return rootNodes;
    }

    private static void setNodeIcon(Node nodeIcon) {
        if (nodeIcon.getChildren().size() > 0 && nodeIcon.isExpand()) {
            nodeIcon.setIcon(R.mipmap.item_open);
        } else if (nodeIcon.getChildren().size() > 0 && !nodeIcon.isExpand()) {
            nodeIcon.setIcon(R.mipmap.item_close);
        } else {
            nodeIcon.setIcon(-1);
        }
    }

    public static <T> List<Node> sortDatas(List<T> datas,int defaultExpandLevel) throws IllegalAccessException {
        List<Node> result = new ArrayList<>();
//        List<Node> convertNodes = convertDatasToNodes(datas);
        List<Node> rootNodes = null;
        try {
            rootNodes = convertDatasToNodes(datas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rootNodes == null) {
            return result;
        }
        for (Node node : rootNodes) {
            addNode(result,node,defaultExpandLevel,1);
        }
        return result;
    }

    private static List<Node> getRootNodes(List<Node> convertNodes) {
        List<Node> root = new ArrayList<>();
        for (Node node : convertNodes) {
            if (node.isRoot()) {
                root.add(node);
            }
        }
        return root;
    }

    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if (node.isLeaf()) {
            return;
        }
        if (defaultExpandLevel >= currentLevel) {
            node.setExpand(true);
        }
        for (int i=0;i<node.getChildren().size();i++) {
            addNode(result,node.getChildren().get(i),defaultExpandLevel,currentLevel+1);
        }
    }

    public static List<Node> filterVisibleNodes(List<Node> nodes) {
        List<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot() || node.isParentExpand()) {
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }
}
