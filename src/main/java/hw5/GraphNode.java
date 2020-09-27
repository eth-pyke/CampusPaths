/**
 * 
 */
package hw5;


/**
 * <b>GraphNode</b> represents an immutable node that is contained in Graph and
 * contains a given type as data. This can be connected with other nodes with
 * a GraphEdge.
 * 
 * @param <T> The type of data that this holds.
 */
public class GraphNode<T> implements Comparable<GraphNode<T>> {
	
	/** Holds the data of this*/
	private final T data;
	
	// Abstraction Function:
	// GraphNode holds data and is a part of Graph.
	// Can either be connected to nothing, or can be connected to other nodes with an edge.
	// All of its edges connecting to each of its children are located in 'children.'
	//
	// Representation Invariant:
	// data != null
	// For any index i such that children.get(i) != null
	// For any index i such that children.get(i).getDestination() != null
	
	/**
	 * @param d Data of type T that is made the data for the GraphNode.
	 * @spec.requires d != null
	 * @spec.effects Constructs a new GraphNode with Type d.
	 */
	public GraphNode(T d) {
		data = d;
		
		//checkRep();
	}
	
	
	/**
	 * Returns the data of this.
	 * 
	 * @return data
	 */
	public T getData() {
		return data;
	}
	
	/**
	 * Overrides and returns an int as this objects hashcode.
	 * 
	 * @return hashCode of this based of its data.
	 */
	@Override
	public int hashCode() {
		return data.hashCode();
	}
	
	/**
	 * Returns whether this node is equal to another node.
	 * 
	 * @param o A second node to be compared to.
	 * @return true if this and o hold the same data.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GraphNode<?>)) {
			return false;
		}
		
		GraphNode<?> n2 = (GraphNode<?>) o;
		
		return this.data.equals(n2.getData());
	}
	
	/**
	 * Completes the Comparable interface by comparing this to another GraphNode
	 * 
	 * @param n2 The node to be compared to.
	 * @spec.requires n2 != null
	 * @return returns the comparison of each nodes data.
	 */
	public int compareTo(GraphNode<T> n2) {
		return data.toString().compareTo(n2.getData().toString());
	}
	
	/**
	 * Returns a string representation of the GraphNode.
	 * 
	 * @return data located in the node.
	 */
	public String toString() {
		return data.toString();
	}
	
	/**
	 * Checks that the representation invariant holds (if any).
	 */
	private void checkRep() {
		assert (data != null) : "data == null";
	}
}
