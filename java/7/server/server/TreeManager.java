package server;

import common.TreeType;
import java.util.HashMap;
import java.util.Map;

public class TreeManager {
    private final Map<TreeType, BinaryTree<?>> trees = new HashMap<>();

    public TreeManager() {
        trees.put(TreeType.INTEGER, new BinaryTree<Integer>());
        trees.put(TreeType.DOUBLE, new BinaryTree<Double>());
        trees.put(TreeType.STRING, new BinaryTree<String>());
    }

    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> BinaryTree<T> getTypedTree(TreeType type) {
        return (BinaryTree<T>) trees.get(type);
    }
}