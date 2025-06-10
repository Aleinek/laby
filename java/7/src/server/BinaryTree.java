package server;

public class BinaryTree<T extends Comparable<T>> {
    private static class TreeNode<T> {
        T value;
        TreeNode<T> left, right;

        TreeNode(T value) {
            this.value = value;
        }
    }

    private TreeNode<T> root;

    public void insert(T value) {
        root = insertRec(root, value);
    }

    private TreeNode<T> insertRec(TreeNode<T> node, T value) {
        if (node == null) return new TreeNode<>(value);
        int cmp = value.compareTo(node.value);
        if (cmp < 0) node.left = insertRec(node.left, value);
        else if (cmp > 0) node.right = insertRec(node.right, value);
        return node;
    }

    public boolean search(T value) {
        return searchRec(root, value);
    }

    private boolean searchRec(TreeNode<T> node, T value) {
        if (node == null) return false;
        int cmp = value.compareTo(node.value);
        if (cmp == 0) return true;
        return (cmp < 0) ? searchRec(node.left, value) : searchRec(node.right, value);
    }

    public void delete(T value) {
        root = deleteRec(root, value);
    }

    private TreeNode<T> deleteRec(TreeNode<T> node, T value) {
        if (node == null) return null;

        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = deleteRec(node.left, value);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            TreeNode<T> minLarger = findMin(node.right);
            node.value = minLarger.value;
            node.right = deleteRec(node.right, minLarger.value);
        }
        return node;
    }

    private TreeNode<T> findMin(TreeNode<T> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    public String draw() {
        StringBuilder sb = new StringBuilder();
        drawRec(root, "", true, sb);
        return sb.toString();
    }

    private void drawRec(TreeNode<T> node, String prefix, boolean isTail, StringBuilder sb) {
        if (node == null) return;

        if (node.right != null) {
            drawRec(node.right, prefix + (isTail ? "│   " : "    "), false, sb);
        }

        sb.append(prefix)
        .append(isTail ? "└── " : "┌── ")
        .append(node.value)
        .append("\n");

        if (node.left != null) {
            drawRec(node.left, prefix + (isTail ? "    " : "│   "), true, sb);
        }
    }
}
