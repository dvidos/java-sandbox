import java.util.*;


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
        System.out.println("Running Breads First Search");
        g.BreadthFirstSearch(a);
        System.out.println("Path from a (BFS start) to f: ");
        g.PrintParentalPathTo(f);
        
        
        // make a depth first search on a graph
        
        
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
    
    public Node(String id) {
        this.id = id;
        this.parent = null;
        this.color = Color.WHITE;
        this.distance = 0;
    }
    
    public static Node of(String id) {
        return new Node(id);
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
    
    public Graph(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }
    
    public static Graph of(List<Node> nodes, List<Edge> edges) {
        return new Graph(nodes, edges);
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
    
    public void BreadthFirstSearch(Node start) {
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
                    } else {
                        neighbor.distance = current.distance + 1;
                        neighbor.parent = current;
                        neighbor.color = Color.GRAY;
                        
                        // we'll visit this in the future
                        candidates.add(neighbor);
                    }
                }
            }
            
            // we are done with this node
            current.color = Color.BLACK;
        }
    }
    
    public void PrintParentalPathTo(Node target) {
        if (target.parent != null) {
            PrintParentalPathTo(target.parent);
        }
        System.out.println("  " + target.id + " (distance: " + target.distance + ")");
    }
    
    
    
    private void breadthFirstSearchInner(Node start, ArrayList<Node> candidates) {
    }
    
    public void DepthFirstSearch(Node start) {
        
    }
    
}

