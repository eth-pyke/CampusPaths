/**
 * 
 */
package hw5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * <b>Graph</b> represents an mutable graph with GraphNodes and GraphEdges that contain
 * Strings as their data.
 * Each GraphNode is connected with a GraphEdge.
 * 
 * @param <N> Type of data that each of the nodes in this must hold
 * @param <E> Type of data that each of the edges in this must hold
 */
public class Graph<N, E extends Comparable<E>> {
	
	/** Holds all the GraphNodes in this graph*/
	private Map<GraphNode<N>, Set<GraphEdge<E, N>>> nodes;
	
	private int numEdges;
	
	private final String name;
	
	// Abstraction Function:
	// Each node in Graph is either by itself or connected with another node using an edge
	// that can hold its own data.
	// Each node is located in 'nodes.'
	//
	// Representation Invariant:
	// For any index i such that nodes.get(i) != null
	
	/**
	 * @param name The name of the graph.
	 * @spec.effects Constructs a new empty Graph, 'empty'.
	 */
	public Graph(String name) {
		nodes = new HashMap<GraphNode<N>, Set<GraphEdge<E, N>>>();
		this.name = name;
		numEdges = 0;
		
		//checkRep();
	}
	
	/**
	 * @param name The name of the graph.
	 * @param start The GraphNode to start the graph with.
	 * @spec.requires start != null
	 * @spec.effects Constructs a new Graph with a given starting node.
	 */
	public Graph(String name, GraphNode<N> start) {
		nodes = new HashMap<GraphNode<N>, Set<GraphEdge<E, N>>>();
		nodes.put(start, new HashSet<GraphEdge<E, N>>());
		this.name = name;
		numEdges = 0;
		
		//checkRep();
	}

	/**
	 * Returns the name of the graph.
	 *
	 * @return name of graph.
	 */
	public String getName() {
		return name;
	}	
	
	/**
	 * Connects a new node and adds it to the graph.
	 * 
	 * @param data The data that is used to create the new GraphNode.
	 * @spec.requires no node in the graph contains the same data and data != null.
	 * @spec.effects new node is added to the graph and to 'nodes'
	 */
	public void addNode(N data) {
		GraphNode<N> temp = new GraphNode<N>(data);
		if (!nodes.containsKey(temp)) {
			nodes.put(temp, new HashSet<GraphEdge<E, N>>());
		}
		
		//checkRep();
	}
	
	/**
	 * Connects two nodes with a GraphEdge.
	 * 
	 * @param from The parent node data.
	 * @param to The child node data.
	 * @param data The data that is used to create the new edge between the two nodes.
	 * @spec.requires from != null, to != null, and from != to.
	 * @spec.effects from and to are connected with graph edge with the given data.
	 * @throws IllegalArgumentException is the nodes are not in the graph.
	 */
	public void addEdge(N from, N to, E data) {
		GraphNode<N> f = new GraphNode<N>(from);
		GraphNode<N> t = new GraphNode<N>(to);
		if (!nodes.containsKey(f) || !nodes.containsKey(t)) {
			throw new IllegalArgumentException("One of given nodes is not in the graph.");
		}
		
		GraphEdge<E, N> edge = new GraphEdge<E, N>(f, t, data);

		if (nodes.get(new GraphNode<N>(from)).add(edge)) {
			numEdges++;
		}		
		//checkRep();
	}
	
	/**
	 * Returns the GraphNode associated with the given string.
	 * 
	 * @param s The string that the GraphNode contains.
	 * @return the GraphNode in this thats data equals s.
	 * 		   If s does not match any node in nodes, returns null.
	 */
	public GraphNode<N> getNode(N s) {
		for (GraphNode<N> node : nodes.keySet()) {
			if (node.getData().equals(s)) {
				return node;
			}
		}
		return null;
	}
	
	/**
	 * Returns a set containing all of the children of the given node.
	 * 
	 * @param node The node whose children are returned.
	 * @return a set of all the children of node.
	 */
	public Set<GraphEdge<E, N>> getChildren(GraphNode<N> node) {
		Set<GraphEdge<E, N>> set = new TreeSet<GraphEdge<E, N>>(nodes.get(node));
		return set;
	}
	
	/**
	 * Return whether or not the graph contains a given node based on if the data is the same.
	 * 
	 * @param node A node that is being tested for to see if it is in the graph.
	 * @spec.requires node != null
	 * @return true if and only if the given node is a part of the graph.
	 */
	public Boolean contains(GraphNode<N> node) {
		return nodes.containsKey(node);
	}

	/**
	 * Return the number of nodes in the graph.
	 * 
	 * @return number of nodes in the graph.
	 */
	public int size() {
		return nodes.size();
	}
	
	/**
	 * Return the number of nodes in the graph.
	 * 
	 * @return true if the graph is empty.
	 */
	public Boolean isEmpty() {
		return nodes.isEmpty();
	}
	
	/**
	 * Return a copy of the list of the nodes in this.
	 * 
	 * @return new List&lt;GraphEdge&gt; containing all the nodes of this.
	 */
	public List<GraphNode<N>> listNodes() {
		List<GraphNode<N>> temp = new ArrayList<GraphNode<N>>(nodes.keySet());
		
		return temp;
	}
	
	/**
	 * Returns a string representation of the graph.
	 * 
	 * @return all elements of this graph in the format name: [x, y, z]
	 */	
	public String toString() {		
		String temp = name + ": [";
		
		Set<GraphNode<N>> set = new TreeSet<GraphNode<N>>(nodes.keySet());
		List<GraphNode<N>> list = new ArrayList<GraphNode<N>>(set);
		
		if (list.size() != 0) {
			temp = temp + list.get(0).getData();	
		}
		
		// {{Inv: temp contains name: [nodes.get(0), ..., nodes.get(i)]}}
		for (int i = 1; i < nodes.size(); i++) {
			temp = temp + ", " + list.get(i).getData();
		}
		return temp + "]";
	}
	
	/**
	 * Returns the total amount of edges in the graph.
	 * 
	 * @return number of GraphEdges in this.
	 */
	public int getNumOfEdges() {
		return numEdges;
	}
	
	/**
	 * Checks that the representation invariant holds (if any).
	 */
	private void checkRep() {
		//{{Inv: Each node in nodes so far != null}}
		for (GraphNode<N> node : nodes.keySet()) {
			assert (node != null) : "node == null";
		}
	}
	
}
