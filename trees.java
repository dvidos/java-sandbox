import java.util.function.*;
import java.util.*;
import java.nio.charset.*;

/** 
 * A Red Black Tree is a simple binary tree, that guarrantees search in O(lg n) time
 * since it uses the black-red nodes to maintain its balance, so that the tree height
 * is always h = lg n.
 */
public class Trees {
    public static void main(String[] args) { (new TreeDemo()).run(); }
}

interface RBTree {
    class Node { 
        String key;
        String value;
        Node left, right;
    }
    
    public void add(Node n);
    public void remove(Node n);
    public void clear();
    
    public void preOrderTraverse(Consumer<Node> consumer);
    public void inOrderTraverse(Consumer<Node> consumer);
    public void postOrderTraverse(Consumer<Node> consumer);
    
    public Node find(String key);        // O(lg n)
    public Node minimum();               // O(lg n)
    public Node maximum();               // O(lg n)
    public Node successor(Node node);    // O(lg n)
    public Node predecessor(Node node);  // O(lg n)
}

class RBTreeImpl implements RBTree {
    RBTree.Node root;
    
    public RBTreeImpl() {
        root = null;
    }
    
    public void add(Node n) {
    }
    
    public void remove(Node n) {
    }
    
    public void clear() {
        root = null;
    }
    
    public void preOrderTraverse(Consumer<Node> consumer) {
        preOrderFrom(root, consumer);
    }
    
    private void preOrderFrom(Node n, Consumer<Node> consumer) {
        if (n == null)
            return;
        consumer.accept(n);
        preOrderFrom(n.left, consumer);
        preOrderFrom(n.right, consumer);
    }
    
    public void inOrderTraverse(Consumer<Node> consumer) {
        inOrderFrom(root, consumer);
    }
    
    private void inOrderFrom(Node n, Consumer<Node> consumer) {
        if (n == null)
            return;
        preOrderFrom(n.left, consumer);
        consumer.accept(n);
        preOrderFrom(n.right, consumer);
    }
    
    public void postOrderTraverse(Consumer<Node> consumer) {
        postOrderFrom(root, consumer);
    }
    
    private void postOrderFrom(Node n, Consumer<Node> consumer) {
        if (n == null)
            return;
        preOrderFrom(n.left, consumer);
        preOrderFrom(n.right, consumer);
        consumer.accept(n);
    }
    
    public Node find(String key) {
        if (root == null)
            return null;
            
        return findFrom(root, key);
    }
    
    private Node findFrom(Node n, String key) {
        int cmp = n.key.compareTo(key);
        if (cmp == 0) {
            return n;
        } else if (cmp < 0) {
            return (n.left == null) ? null : findFrom(n.left, key);
        } else {
            return (n.right == null) ? null : findFrom(n.right, key);
        }
    }
    
    public Node minimum() {
        if (root == null)
            return null;
        
        Node n = root;
        while (n.left != null)
            n = n.left;
        return n;
    }
    
    public Node maximum() {
        if (root == null)
            return null;
        
        Node n = root;
        while (n.right != null)
            n = n.right;
        return n;
    }
    
    public Node successor(Node node) {
        return null;
    }
    
    public Node predecessor(Node node) {
        return null;
    }
}

class RBTreeBuilder {
    Random rand = new Random();
    
    public RBTree buildLargeRandomTree(int size) {
        return null;
    }
    
    public String randomString() {
        String allowed = "abcdefghijklmnopqrstuvwxyz";
        
        int len = rand.nextInt(16) + 1;
        char[] buff = new char[len];
        for (int i = 0; i < len; i++)
            buff[i] = allowed.charAt(rand.nextInt(allowed.length()));
        return new String(buff);
    }
    
    public int randomInt() {
        return rand.nextInt(10000);
    }
}

class TreeDemo {
    public void run() {
        // generate random trees
        // add, remove, 
        // test searching,
        // test traversing
        RBTreeBuilder builder = new RBTreeBuilder();
        for (int i = 0; i < 10; i++) {
            System.out.println(builder.randomString());
        }
    }
}