import java.util.function.*;
import java.util.*;
import java.io.IOException;

/** 
 * A Red Black Tree is a simple binary tree, that guarrantees search in O(lg n) time
 * since it uses the black-red nodes to maintain its balance, so that the tree height
 * is always h = lg n.
 */
public class Trees {
    public static void main(String[] args) {
        TreeDemo demo = new TreeDemo();
        demo.runTests();
        demo.runDemo();
    }
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
    public String debugString();
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
        
        // fix pointers appropriately
        node.parent = trailing;
        node.left = null;
        node.right = null;
        
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
        // if node has no children, we simply remove it
        if (node.left == null && node.right == null) {
            transplant(node, null);
        
        // if node has one child, we elevate the child to the node's position
        } else if (node.left == null && node.right != null) {
            transplant(node, node.right);
        
        } else if (node.left != null && node.right == null) {
            transplant(node, node.left);
                
        // if node has two children, then we find the node's successor (will be in the right subtree)
        // to replace the node. The left subtree will be the successor's new left subtree.
        } else {
            Node successor = minimumFrom(node.right);
            if (successor.parent != node) {
                transplant(successor, successor.right);
                successor.right = node.right;
                successor.right.parent = successor;
            }
            transplant(node, successor);
            successor.left = node.left;
            successor.left.parent = successor;
        }
    }
    
    private void transplant(Node targetTree, Node newSubtree) {
        // make parent of targetTree point to newSubtree
        if (targetTree.parent == null)
            // targetTree is the root
            root = newSubtree;
        else {
            // targetTree is either a left or right child of its parent
            if (targetTree == targetTree.parent.left)
                targetTree.parent.left = newSubtree;
            else
                targetTree.parent.right = newSubtree;
        }
        
        // fix parental pointer
        if (newSubtree != null)
            newSubtree.parent = targetTree.parent;
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
        printFrom(node.left,  prefix + " . ", " L ");
        printFrom(node.right, prefix + " . ", " R ");
    }
    
    public String debugString() {
        return debugStringFrom(root);
    }
    
    private String debugStringFrom(Node node) {
        if (node == null)
            return "";
        
        String s = "(" + node.key + "";
        if (node.left != null)
            s += ", L" + debugStringFrom(node.left);
        if (node.right != null)
            s += ", R" + debugStringFrom(node.right);
        s += ")";
        
        return s;
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
    
    public RBTree.Node randomNode() {
        return new RBTree.Node(randomString(10));
    }
}

class TreeDemo {
    public void runDemo() {
        RBTree tree = new RBTreeImpl();
        RBTreeBuilder builder = new RBTreeBuilder();
        for (int i = 0; i < 300; i++) {
            tree.add(builder.randomNode());
        }
        tree.print();
        
        System.out.println("Increasing order traversing");
        RBTree.Node n = tree.minimum();
        while (n != null) {
            System.out.println("- " + n.key);
            n = tree.successor(n);
        }
        
    }
    
    public void runTests() {
        RBTree.Node a = new RBTree.Node("a");
        RBTree.Node b = new RBTree.Node("b");
        RBTree.Node c = new RBTree.Node("c");
        RBTree.Node d = new RBTree.Node("d");
        
        RBTree tree = new RBTreeImpl();
        assertEquals("", tree.debugString());
        
        tree.add(b);
        assertEquals("(b)", tree.debugString());
        
        tree.remove(b);
        assertEquals("", tree.debugString());
        
        tree.add(b);
        tree.add(a);
        tree.add(d);
        assertEquals("(b, L(a), R(d))", tree.debugString());
        
        tree.remove(a);
        assertEquals("(b, R(d))", tree.debugString());
        
        tree.add(c);
        tree.add(a);
        assertEquals("(b, L(a), R(d, L(c)))", tree.debugString());
        
        tree.remove(d);
        assertEquals("(b, L(a), R(c))", tree.debugString());
        tree.remove(c);
        tree.add(d);
        tree.add(c);
        assertEquals("(b, L(a), R(d, L(c)))", tree.debugString());
        
        // validate traversing methods
        
        final StringBuilder steps = new StringBuilder();
        
        steps.setLength(0);
        tree.preOrderTraverse(n -> steps.append(n.key));
        assertEquals("badc", steps.toString());
        
        steps.setLength(0);
        tree.inOrderTraverse(n -> steps.append(n.key));
        assertEquals("abdc", steps.toString());
        
        steps.setLength(0);
        tree.postOrderTraverse(n -> steps.append(n.key));
        assertEquals("adcb", steps.toString());
        
        RBTree.Node m;
        
        m = tree.minimum();
        assertEquals("a", m.key);
        
        m = tree.successor(m);
        assertEquals("b", m.key);
        
        m = tree.successor(m);
        assertEquals("c", m.key);
        
        m = tree.successor(m);
        assertEquals("d", m.key);
        
        m = tree.maximum();
        assertEquals("d", m.key);
        
        m = tree.predecessor(m);
        assertEquals("c", m.key);
        
        m = tree.predecessor(m);
        assertEquals("b", m.key);
        
        m = tree.predecessor(m);
        assertEquals("a", m.key);
        
        System.out.println("Tests finished successfully");
    }
    private void pause() {
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }
    
    
    private void assertEquals(String expected, String value) {
        if (value == null && expected != null)
            throw new RuntimeException("Assertion failed, was expecting \"" + expected + "\", got null instead");
        
        if (value != null && expected == null)
            throw new RuntimeException("Assertion failed, was expecting null, got \"" + value + "\" instead");
        
        if (!value.equals(expected))
            throw new RuntimeException("Assertion failed, was expecting \"" + expected + "\", got \"" + value + "\" instead");
    }
    
    private void assertEquals(int expected, int value) {
        if (value != expected)
            throw new RuntimeException("Assertion failed, was expecting " + expected + ", got " + value + " instead");
    }
}


