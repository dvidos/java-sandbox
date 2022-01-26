import java.util.*;

/**
 * Graphs are arbitrary collections of nodes or vertices, arbitrarily connected through edges.
 * Edges can be directed or undirected (bidirectional)
 * In graphs we care about moving from a node to another
 */



public class Main {

    

    public static void main(String argv[]) {
        Main m = new Main();
        try {
            m.run();
        } catch (Exception e) {
            m.log(e.toString());
        }
    }

    private void run() {
    
        Node a = Node.of("a");
        Node b = Node.of("b");
        Node c = Node.of("c");
        Node d = Node.of("d");
        Node e = Node.of("e");
        Node f = Node.of("f");
        List<Node> nodes = List.of(a, b, c, d, e, f);
        List<Edge> edges = List.of(
            Edge.of("a", "b"), Edge.of("a", "c"), Edge.of("a", "d"),
            Edge.of("b", "c"), 
            Edge.of("d", "e"), 
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

                
        // TODO: implement hasPathFromTo()
        // TODO: make a topological sort (a left-to-right way of traversing the whole graph)
        //       useful for example in Makefiles or build pipelines, where something has dependencies
        // TODO: implement discovering of strongly connected components.
        
        // make this more usable, see examples here: https://www.baeldung.com/java-graphs 
        //                                     and here: https://github.com/google/guava/wiki/GraphsExplained
      
        
    }

    private void log(String s) {
        System.out.println(s);
    }
}

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
    
    public Graph(List<Node> nodes, List<Edge> edges) {
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
                    
                    // we'll visit this in the future
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
                
                // let's dive right into this!
                depthFirstSearchFrom(neighbor, current, prefix + "    ");
            }
        }
        
        // we are done with this node
        current.end_timestamp = timestamp++;
        current.color = Color.BLACK;
    }
}

