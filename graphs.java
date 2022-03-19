import java.util.function.*;
import java.util.*;
import java.io.IOException;

/**
 * Graphs are arbitrary collections of nodes or vertices, arbitrarily connected through edges.
 * Edges can be directed or undirected (bidirectional)
 * In graphs we care about moving from a node to another
 */
public class Graphs {
    public static void main(String[] args) {
        GraphDemo demo = new GraphDemo();
        demo.runTests();
        demo.runDemo();
    }
}
class Node {
    String key;
    List<String> edgesToNeighbors;
    
    public Node(String key) {
        this.key = key;
        this.edgesToNeighbors = new ArrayList<String>();
    }
}
interface Graph {
    public void clear();
    public void addNode(Node node);
    public void addEdge(String fromKey, String toKey);
    public void removeNode(String key);
    
    public void depthFirstSearch(Node start, Consumer<Node> consumer);
    public void breadthFirstSearch(Node start, Consumer<Node> consumer);
    
    public List<Node> findPath(Node source, Node target);
    public boolean isAcyclic();
    
    public void fromAdjacencyMatrix(boolean[][] matrix);
    public boolean[][] toAdjacencyMatrix();
    public Graph invertDirections();
    public List<Node> topologicalOrder();
    public int numberOfTrees();
    
    public void print();
    public String debugString();
}
class GraphImpl implements Graph {
    List<Node> nodes = new ArrayList<Node>();
    
    public void clear() {
        nodes.clear();
    }
    
    public void addNode(Node node) {
        if (!nodes.contains(node))
            nodes.add(node);
    }
    
    public void addEdge(String fromKey, String toKey) {
        // get node, if none exists, create it
        Node fromNode = getNode(fromKey);
        if (fromNode == null) {
            fromNode = new Node(fromKey);
            addNode(fromNode);
        }
        
        if (!fromNode.edgesToNeighbors.contains(toKey))
            fromNode.edgesToNeighbors.add(toKey);
        
        // do the same for toKey
        Node toNode = getNode(toKey);
        if (toNode == null) {
            addNode(new Node(toKey));
        }
    }
    
    public void removeNode(String key) {
        for (Node n: nodes) {
            if (n.edgesToNeighbors.contains(key)) {
                n.edgesToNeighbors.remove(key);
            }
            if (n.key == key) {
                nodes.remove(n);
            }
        }
    }
    
    public void print() {
        for (Node n: nodes) {
            System.out.println("" + n.key + ":[" + String.join(",", n.edgesToNeighbors) + "]");
        }
    }
    
    public String debugString() {
        List<String> nodesStrings = new ArrayList<String>();
        for (Node n: nodes) {
            nodesStrings.add(n.key + "[" + String.join(",", n.edgesToNeighbors) + "]");
        }
        return String.join(" ", nodesStrings);
    }
    
    public void depthFirstSearch(Node start, Consumer<Node> consumer) {
        Stack<Node> stack = new Stack<Node>();
        Set<Node> visitedNodes = new HashSet<Node>();
        
        stack.push(start);
        while (stack.size() > 0) {
            // grab current from stack
            Node current = stack.pop();
            
            // call consumer
            consumer.accept(current);
            
            for (String key: current.edgesToNeighbors) {
                // find neighbors, put them on stack
                Node neighbor = getNode(key);
                if (neighbor == null)
                    throw new RuntimeException("Node keyed \"" + key + "\" not found");
                
                if (visitedNodes.contains(neighbor)) {
                    continue;
                }
                
                stack.push(neighbor);
                visitedNodes.add(neighbor);
            }
        }
    }
    
    public void breadthFirstSearch(Node start, Consumer<Node> consumer) {
        Queue<Node> queue = new LinkedList<Node>();
        Set<Node> visitedNodes = new HashSet<Node>();
        
        queue.add(start);
        while (queue.size() > 0) {
            // grab current from queue
            Node current = queue.poll();

            // call consumer
            consumer.accept(current);
            
            // find neighbors, put them on queue, to be visited later.
            for (String key: current.edgesToNeighbors) {
                // find neighbors, put them on stack
                Node neighbor = getNode(key);
                if (neighbor == null)
                    throw new RuntimeException("Node keyed \"" + key + "\" not found");
                
                if (visitedNodes.contains(neighbor)) {
                    continue;
                }
                
                queue.add(neighbor);
                visitedNodes.add(neighbor);
            }
        }
    }
    
    public List<Node> findPath(Node source, Node target) {
        // do a depth first to explore all accessible nodes
        return List.of();
    }
    
    public boolean isAcyclic() {
        // depth first search, looking for edges back towards gray nodes
        return false;
    }
    
    public void fromAdjacencyMatrix(boolean[][] matrix) {
    }
    
    public boolean[][] toAdjacencyMatrix() {
        return new boolean[0][0];
    }
    
    public Graph invertDirections() {
        return null;
    }
    
    public List<Node> topologicalOrder() {
        // depth first search, then sort by ... end timestamp?
        return null;
    }
    
    public int numberOfTrees() {
        // detect the number of trees or islands in the graph
        // essentially go over all nodes, do DFS, keeping visited nodes in a list.
        return 0;
    }
    
    private Node getNode(String key) {
        // we should use a Hashtable for faster access...
        for (Node n: nodes) {
            if (n.key.equals(key))
                return n;
        }
        
        return null;
    }
    
}



/*
public enum Color {
    BLACK, // visited node
    GRAY,  // under consideration
    WHITE // unvisited node
}

class Node {
    public String id;
    public Node parent;
    public Color color;
    public int distance;
    public int start_timestamp;
    public int end_timestamp;
    
    public Node(String id) {
        this.id = id;
        this.parent = null;
        this.color = Color.WHITE;
        this.distance = 0;
        this.start_timestamp = 0;
        this.end_timestamp = 0;
    }
    
    public static Node of(String id) {
        return new Node(id);
    }
    
    public void reset() {
        this.parent = null;
        this.color = Color.WHITE;
        this.distance = 0;
        this.start_timestamp = 0;
        this.end_timestamp = 0;
    }
}

class Edge {
    public String from;
    public String to;
    
    public Edge(String from, String to) {
        this.from = from;
        this.to = to;
    }
    
    public static Edge of(String from, String to) {
        return new Edge(from, to);
    }
}

class Graph {
    public List<Node> nodes;
    public List<Edge> edges;
    private int timestamp;
    
    public Graph1(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
        this.timestamp = 0;
    }
    
    public static Graph of(List<Node> nodes, List<Edge> edges) {
        return new Graph(nodes, edges);
    }
    
    public void reset() {
        for (Node node: nodes) {
            node.reset();
        }
    }
    
    public void print() {
        System.out.println("Nodes:");
        for (Node node: nodes) {
            System.out.println("  " + node.id);
        }
        System.out.println("Edges:");
        for (Edge edge: edges) {
            System.out.println("  " + edge.from + " --> " + edge.to);
        }
    }
    
    private List<Edge> getEdgesFrom(String from) {
        ArrayList<Edge> edgesFrom = new ArrayList<Edge>();
        for (Edge edge: edges) {
            if (edge.from.equals(from)) {
                edgesFrom.add(edge);
            }
        }
        return edgesFrom;
    }
    
    private Node getNode(String id) {
        for (Node node: nodes) {
            if (node.id.equals(id)) {
                return node;
            }
        }
        throw new RuntimeException("Cannot find node with id \"" + id + "\"");
    }
    
    public void breadthFirstSearch(Node start) {
        ArrayList<Node> candidates = new ArrayList<Node>();
        candidates.add(start);
        
        while (candidates.size() > 0) {
            Node current = candidates.get(0);
            candidates.remove(0);
            System.out.println("Visiting node " + current.id);
            
            // denote consideration color
            current.color = Color.GRAY;
            
            List<Edge> exitingEdges = getEdgesFrom(current.id);
            if (exitingEdges.size() == 0) {
                System.out.println("  Node " + current.id + " has no exiting edges");
            } else {
                for (Edge exitingEdge: exitingEdges) {
                    System.out.println("  Considering edge to " + exitingEdge.to);
                    Node neighbor = getNode(exitingEdge.to);
                    if (neighbor.color != Color.WHITE) {
                        System.out.println("  We 've considered this node, ignoring");
                        continue;
                    }
                    
                    neighbor.distance = current.distance + 1;
                    neighbor.parent = current;
                    neighbor.color = Color.GRAY;
                    
                    // we'll visit this in the future, not now as we'd do in DFS
                    candidates.add(neighbor);
                }
            }
            
            // we are done with this node
            current.color = Color.BLACK;
        }
    }
    
    public void printParentalPathTo(Node target) {
        if (target.parent != null) {
            printParentalPathTo(target.parent);
        }
        System.out.println("  " + target.id + " (distance: " + target.distance + ")");
    }
    
    public void depthFirstSearch(Node start) {
        ArrayList<Node> candidates = new ArrayList<Node>();
        candidates.add(start);
        timestamp = 0;
        
        depthFirstSearchFrom(start, null, "");
    }
    
    private void depthFirstSearchFrom(Node current, Node parent, String prefix) {
        System.out.println(prefix + "Visiting node " + current.id);
        
        // denote consideration color
        current.start_timestamp = timestamp++;
        current.color = Color.GRAY;
        
        List<Edge> exitingEdges = getEdgesFrom(current.id);
        if (exitingEdges.size() == 0) {
            System.out.println(prefix + "  Node " + current.id + " has no exiting edges");
        } else {
            for (Edge exitingEdge: exitingEdges) {
                System.out.println(prefix + "  Considering edge to " + exitingEdge.to);
                Node neighbor = getNode(exitingEdge.to);
                if (neighbor.color != Color.WHITE) {
                    System.out.println(prefix + "  We 've considered this node, ignoring");
                    continue;
                }
                
                neighbor.distance = parent == null ? 0 : parent.distance + 1;
                neighbor.parent = current;
                
                // let's dive right into this, instead of waiting as we'd do in BFS
                depthFirstSearchFrom(neighbor, current, prefix + "    ");
            }
        }
        
        // we are done with this node
        current.end_timestamp = timestamp++;
        current.color = Color.BLACK;
    }
}

*/

class GraphDemo {
    public void runTests() {
        Graph g = new GraphImpl();
        g.addEdge("a", "b");
        g.addEdge("a", "c");
        g.addEdge("b", "c");
        g.addEdge("c", "d");
        g.addEdge("f", "g");
        assertEquals("a[b,c] b[c] c[d] d[] f[g] g[]", g.debugString());
        
        System.out.println("Tests finished successfully");
    }
    
    public void runDemo() {
        Graph g = new GraphImpl();
        Node a = new Node("a");
        g.addNode(a);
        
        g.addEdge("a", "b");
        g.addEdge("a", "c");
        g.addEdge("b", "b1");
        g.addEdge("b", "b2");
        g.addEdge("c", "c1");
        g.addEdge("c", "c2");
        g.addEdge("c2", "c2");
        g.addEdge("c2", "c2a");
        g.addEdge("c2", "c2b");
        g.addEdge("a", "d");
        g.addEdge("e", "f");
        
        g.print();
        System.out.println(g.debugString());
        
        System.out.println("Breadth first search");
        g.breadthFirstSearch(a, (node) -> System.out.println(" - " + node.key));
        
        System.out.println("Depth first search");
        g.depthFirstSearch(a, (node) -> System.out.println(" - " + node.key));
        
        
        
        
        /*
            (a) --> (b)
             |     /
             V    /
            (c)  <
             |
             v
            (d)
             
            (e) --> (f)
        */
        
        /*
        Node a = Node.of("a");
        Node b = Node.of("b");
        Node c = Node.of("c");
        Node d = Node.of("d");
        Node e = Node.of("e");
        Node f = Node.of("f");
        List<Node> nodes = List.of(a, b, c, d, e, f);
        List<Edge> edges = List.of(
            Edge.of("a", "b"), Edge.of("a", "c"),
            Edge.of("b", "c"), 
            Edge.of("c", "d"),
            Edge.of("e", "f")
        );
        
        Graph g = Graph.of(nodes, edges);
        g.print();
        
        // make a breadth first search on a graph
        Node start = a;
        Node end = f;
        
        System.out.println("Running Breadth First Search, starting from " + start.id);
        g.breadthFirstSearch(start);
        System.out.println("Path from " + start.id + " (BFS start) to " + end.id);
        g.printParentalPathTo(end);
        System.out.println("");
        g.reset();
        
        System.out.println("Running Depth First Search, starting from " + start.id);
        g.depthFirstSearch(start);
        g.reset();
        */
                
        // TODO: implement hasPathFromTo()
        // TODO: make a topological sort (a left-to-right way of traversing the whole graph)
        //       useful for example in Makefiles or build pipelines, where something has dependencies
        // TODO: implement discovering of strongly connected components.
        
        // make this more usable, see examples here: https://www.baeldung.com/java-graphs 
        //                                     and here: https://github.com/google/guava/wiki/GraphsExplained
      
        
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