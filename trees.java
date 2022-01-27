import java.util.function.*;
import java.util.*;
import java.io.IOException;

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
        public Node(String key) {
            this.key = key;
        }
        String key;
        String value;
        Node parent, left, right;
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
    
    public void print();
}


class RBTreeImpl implements RBTree {
    RBTree.Node root;
    
    public RBTreeImpl() {
        root = null;
    }
    
    public void add(Node node) {
        // maintain a trailing pointer as future parent
        Node trailing = null;
        Node target = root;
        
        // find the appropriate place to insert
        while (target != null) {
            trailing = target;
            int cmp = node.key.compareTo(target.key);
            target = cmp < 0 ? target.left : target.right;
        }
        
        node.parent = trailing;
        if (trailing == null) {
            root = node;
        } else {
            if (node.key.compareTo(trailing.key) < 0)
                trailing.left = node;
            else
                trailing.right = node;
        }
    }
    
    public void remove(Node node) {
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
        return findFrom(root, key);
    }
    
    private Node findFrom(Node node, String key) {
        if (node == null)
            return null;
        
        int cmp = node.key.compareTo(key);
        if (cmp == 0)
            return node;
        
        return cmp < 0 ? findFrom(node.left, key) : findFrom(node.right, key);
    }
    
    public Node minimum() {
        return minimumFrom(root);
    }
    
    private Node minimumFrom(Node node) {
        if (node == null)
            return null;
        
        while (node.left != null)
            node = node.left;
        return node;
    }
    
    public Node maximum() {
        return maximumFrom(root);
    }
    
    private Node maximumFrom(Node node) {
        if (node == null)
            return null;
        
        while (node.right != null)
            node = node.right;
        return node;
    }
    
    public Node successor(Node node) {
        if (node == null)
            return null;
        
        // if there is a right subtree, get the minimum of that
        if (node.right != null)
            return minimumFrom(node.right);
            
        // otherwise, go up to the first parent that points us to the left
        Node current = node.parent;
        while (current != null && node == current.right) {
            node = current;
            current = current.parent;
        }
        return current;
    }
    
    public Node predecessor(Node node) {
        if (node == null)
            return null;
        
        // if there is a left subtree, get the maximum of that
        if (node.left != null)
            return maximumFrom(node.left);
            
        // otherwise, go up to the first parent that points us to the right
        Node current = node.parent;
        while (current != null && node == current.left) {
            node = current;
            current = current.parent;
        }
        return current;
    }
    
    public void print() {
        printFrom(root, "", "");
    }
    
    private void printFrom(Node node, String prefix, String side) {
        if (node == null)
            return;

        System.out.println(prefix + side + "\"" + node.key + "\"");
        printFrom(node.left,  prefix + " .  ", " L  ");
        printFrom(node.right, prefix + " .  ", " R  ");
    }
}

class RBTreeBuilder {
    Random rand = new Random();
    
    public RBTree buildLargeRandomTree(int size) {
        return null;
    }
    
    public String randomString(int maxLen) {
        String allowed = "abcdefghijklmnopqrstuvwxyz";
        
        int len = rand.nextInt(maxLen) + 1;
        char[] buff = new char[len];
        for (int i = 0; i < len; i++)
            buff[i] = allowed.charAt(rand.nextInt(allowed.length()));
        return new String(buff);
    }
    
    public int randomInt(int maxValue) {
        return rand.nextInt(maxValue);
    }
}

class TreeDemo {
    public void run() {
        // generate random trees
        // add, remove, 
        // test searching,
        // test traversing
        RBTree tree = new RBTreeImpl();
        tree.add(new RBTree.Node("f"));
        tree.add(new RBTree.Node("h"));
        tree.add(new RBTree.Node("b"));
        tree.add(new RBTree.Node("g"));
        tree.add(new RBTree.Node("a"));
        tree.add(new RBTree.Node("c"));
        tree.add(new RBTree.Node("d"));
        tree.add(new RBTree.Node("i"));
        
        System.out.println("Printing Tree");
        tree.print();
        
        System.out.println("Pre Order Traversing");
        tree.preOrderTraverse(n -> System.out.println("- " + n.key));
        
        System.out.println("In Order Traversing");
        tree.inOrderTraverse(n -> System.out.println("- " + n.key));
        
        System.out.println("Post Order Traversing");
        tree.postOrderTraverse(n -> System.out.println("- " + n.key));
        
        RBTree.Node n;
        
        System.out.println("Minimum + Successor Traversing (increasing order)");
        n = tree.minimum();
        while (n != null) {
            System.out.println("- " + n.key);
            n = tree.successor(n);
        }
        //pause();
        
        
        System.out.println("Maximum + Predecessor Traversing (decreasing order)");
        n = tree.maximum();
        while (n != null) {
            System.out.println("- " + n.key);
            n = tree.predecessor(n);
        }
        //pause();
    }
    
    private void pause() {
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }
}


