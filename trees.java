import java.util.function.*;

/** 
 * A Red Black Tree is a simple binary tree, that guarrantees search in O(lg n) time
 * since it uses the black-red nodes to maintain its balance, so that the tree height
 * is always h = lg n.
 */
public class Trees {
    public static void main(String[] args) { new TreeDemo()).run(); }
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
    
    public Node find(String key);
    public Node minimum();
    public Node maximum();
    public Node successor(Node node);
    public Node predecessor(Node node);
}

class RBTreeImplementation extends RBTree {
}

class RBTreeBuilder {
    public RBTree buildLargeRandomTree(int size) {
        return null;
    }
}

class TreeDemo {
    public void run() {
        // generate random trees
        // add, remove, 
        // test searching,
        // test traversing
    }
}