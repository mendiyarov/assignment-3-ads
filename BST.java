import java.util.*;

public class BST<K extends Comparable<K>, V> implements Iterable<BST.Entry<K, V>>{
    private Node root;
    private int size;

    private static class Node<K, V> {
        private K key;
        private V val;
        private Node<K, V> left, right;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public void put(K key, V val) {
        root = put(root, key, val);
        size++;
    }

    public Iterator<BST.Entry<K, V>> iterator() {
        return new BSTIterator(root);
    }

    private class BSTIterator implements Iterator<BST.Entry<K, V>> {
        private Stack<Node<K, V>> stack;

        public BSTIterator(Node<K, V> root) {
            stack = new Stack<>();
            pushLeft(root);
        }

        private void pushLeft(Node<K, V> node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public BST.Entry<K, V> next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node<K, V> node = stack.pop();
            pushLeft(node.right);
            return new BST.Entry<>(node.key, node.val);
        }
    }

    private Node<K, V> put(Node<K, V> x, K key, V val) {
        if (x == null) return new Node<>(key, val);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        return x;
    }

    public V get(K key) {
        Node<K, V> x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.val;
        }
        return null;
    }

    public void delete(K key) {
        root = delete(root, key);
        size--;
    }

    private Node<K, V> delete(Node<K, V> x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node<K, V> t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        return x;
    }

    private Node<K, V> min(Node<K, V> x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    private Node<K, V> deleteMin(Node<K, V> x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        return x;
    }

    public Iterable<K> keys() {
        List<K> keys = new ArrayList<>();
        Stack<Node<K, V>> stack = new Stack<>();
        Node<K, V> current = root;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            keys.add(current.key);
            current = current.right;
        }

        return keys;
    }

    public int size() {
        return size;
    }

    public static class Entry<K, V> {
        private K key;
        private V val;

        public Entry(K key, V val) {
            this.key = key;
            this.val = val;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return val;
        }
    }

    public static void main(String[] args) {
        BST<Integer, String> bst = new BST<>();
        bst.put(5, "Five");
        bst.put(3, "Three");
        bst.put(7, "Seven");
        bst.put(2, "Two");
    

        System.out.println("Size: " + bst.size());

        System.out.println("In-order traversal:");
        for (BST.Entry<Integer, String> entry : bst) {
            System.out.println("Key is " + entry.getKey() + " and value is " + entry.getValue());
        }
    }
}
