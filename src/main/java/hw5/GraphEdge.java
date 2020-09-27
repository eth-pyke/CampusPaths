/**
 * 
 */
package hw5;

/**
 * <b>GraphEdge</b> represents an immutable graph edge that connects one GraphNode
 * with another GraphNode that can contain its own data.
 * 
 * @param <T> The type of data that this holds.
 * @param <N> The type of data each of the nodes hold.
 */
public class GraphEdge<T extends Comparable<T>, N> implements Comparable<GraphEdge<T, N>> {
	
	/** Holds the origin/starting point of the this*/
	private final GraphNode<N> from;
	
	/** Holds the destination/ending point of this*/
	private final GraphNode<N> to;
	
	/** Holds the data of this*/
	private final T data;
	
	// Abstraction Function:
	// GraphEdge holds data, must connect two GraphNodes, and is a part of Graph.
	// The parent node is located in 'from' and the child node is located in 'to.'
	//
	// Representation Invariant:
	// data != null
	// from != null && to != null
	
	/**
	 * @param f The beginning point of the edge.
	 * @param t The ending point of the edge.
	 * @param d The data that the edge holds.
	 * @spec.requires f != null and t != null
	 * @spec.effects Constructs a new graph edge from f to t with the given data d.
	 */
	public GraphEdge(GraphNode<N> f, GraphNode<N> t, T d) {
		from = f;
		to = t;
		data = d;
		
		//checkRep();
	}
	
	/**
	 * Returns the data of the edge.
	 * 
	 * @return data
	 */
	public T getData() {
		return data;
	}
	
	/**
	 * Returns where the edge starts at.
	 * 
	 * @return from
	 */
	public GraphNode<N> getOrigin() {
		return from;
	}
	
	/**
	 * Return where the edge ends at.
	 *
	 * @return to
	 */
	public GraphNode<N> getDestination() {
		return to;
	}
	
	/**
	 * Overrides and returns an int as this objects hashcode.
	 * 
	 * @return hashCode of this based of its data and destination.
	 */
	@Override
	public int hashCode() {
		return data.hashCode() + 2 * from.hashCode() + 3 * to.hashCode();
	}
	
	/**
	 * Returns whether this node is equal to another node.
	 * 
	 * @param o A second edge to be compared to.
	 * @return true if this and o hold the same data, origin, and destination.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GraphEdge<?, ?>)) {
			return false;
		}
		
		GraphEdge<?, ?> n2 = (GraphEdge<?, ?>) o;
		
		return data.equals(n2.getData()) && from.equals(n2.getOrigin()) && 
				           to.equals(n2.getDestination());
	}
	
	/**
	 * Completes the Comparable interface and compares this to another edge.
	 * 
	 * @param e2 The edge to be compared to.
	 * @spec.requires e2 != null
	 * @return If e2 has the same destination, returns the resulted of the the data being compared
	 *         to.
	 *         Otherwise compares the destination of each.
	 */
	public int compareTo(GraphEdge<T, N> e2) {
		int temp = to.toString().compareTo(e2.getDestination().toString());
		if (temp == 0) {
			return data.compareTo(e2.getData());
		}
		return temp;
	}
	
	/**
	 * Returns a String representation of the edge in the format of '&lt;from, to&gt;'
	 * 
	 * @return '&lt;from.getData(), to.getData()&gt;'
	 */
	public String toString() {
		return "<" + from.getData() + ", " + to.getData() + ">";
	}
	
	/**
	 * Checks that the representation invariant holds (if any).
	 */
	private void checkRep() {
		assert (data != null) : "data == null";
		assert (from != null) : "from == null";
		assert (to != null) : "to == null";
	}
}
