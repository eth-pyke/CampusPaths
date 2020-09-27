package hw6;

import hw5.*;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;

import hw6.MarvelParser.MalformedDataException;

/**
 * Program that allows users to find the shortest path between characters in
 * the Marvel graph. The user is prompted to enter the name of the file
 * containing information about characters and comics along with the names of
 * two characters in question. If both characters exist, it will then display a
 * shortest path between the two characters.
 *
 * <p>This is not an ADT.</p>
 */
public class MarvelPaths {

	/**
     * Returns a graph object describing the contents of the given file. (See
     * the HW6 description for details on this construction.)
     *
     * @param fileName The simple file name of the .TSV file to read from.
     * @throws MalformedDataException if the file cannot be parsed.
     * @return A graph object constructed from the contents of given file.
     */
	public static Graph<String, String> buildGraph(String fileName) throws MalformedDataException {
		Set<String> characters = new HashSet<String>();
		Map<String, List<String>> books = new HashMap<String, List<String>>();
		MarvelParser.parseData(fileName, characters, books);
		
		Graph<String, String> graph = new Graph<String, String>(fileName);
		
		// {{Inv: Each character so far has been added to the graph}}
		for (String c : characters) {
			graph.addNode(c);
		}
		
		// {{Inv: Each book in books so far has been gone through and edges have been added to
		//        each character in them}}
		for (String book : books.keySet()) {
			// {{Inv: Each character so far has added an edge with every character in the book}}
			for (String c1 : books.get(book)) {
				// {{Inv: Each character so far has become the child of c1}}
				for (String c2 : books.get(book)) {
					if (!c1.equals(c2)) {
						graph.addEdge(c1, c2, book);
					}
				}
			}
		}
		
		return graph;
	}

  /**
   * Returns the shortest path from the given source character to the given
   * destination character via edges in the given graph.
   *
   * @param graph The graph in which to search for a path.
   * @param src Name of the node in the graph where the path must start.
   * @param dest Name of the node in the graph where the path must end.
   * @spec.requires graph is not null, src and dest name nodes in graph
   * @return Returns the shortest (and lexicographically least) path from
   *     the node with name matching src to the one with name matching dest.
   */
  public static List<GraphEdge<String, String>> shortestPath(Graph<String, String> graph, String src, String dest) {
  // ... implement BFS as described in HW6 ...
	  Queue<GraphNode<String>> q = new LinkedList<GraphNode<String>>();
	  
	  // Each key in m is a visited node
	  // Each value is a list containing the shortest path from start
	  Map<GraphNode<String>, List<GraphEdge<String, String>>> m = new HashMap<GraphNode<String>, List<GraphEdge<String, String>>>();
	  
	  q.add(graph.getNode(src));
	  m.put(graph.getNode(src), new ArrayList<GraphEdge<String, String>>());
	  
	  //{{Inv: m has a key for every node added to q so far
	  //		 (even those that have since been removed)
	  //	   The value associated with each key is the shortest path to that node}}
	  while (!q.isEmpty()) {
		  GraphNode<String> temp = q.remove();
		  if (temp.getData().equals(dest)) {
			  return m.get(temp);
		  }
		  
		  Set<GraphEdge<String, String>> set = new TreeSet<GraphEdge<String, String>>();
		  set.addAll(graph.getChildren(temp));
		  
		  for (GraphEdge<String, String> edge : set) {
			  if (!m.containsKey(edge.getDestination())) {
				  List<GraphEdge<String, String>> list = new ArrayList<GraphEdge<String, String>>(m.get(temp));
				  list.add(edge);
				  m.put(edge.getDestination(), list);
				  q.add(edge.getDestination());
			  }
		  }
	  }
	  
	  return null;
  }

  /**
   * Entry point for the program described in the class overview.
   *
   * @param args List of arguments passed at the command-line. Ignored.
   * @spec.requires args it not null
   * @spec.effects Interacts with the user by reading from and writing to the
   *     console (System.in and System.out).
   * @throws Exception for any I/O or malformed data error
   */
  public static void main(String[] args) throws Exception {

    // Create a buffered reader so that we can read full lines of input.
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    // Retrieve the name of the file to build the graph from.
    System.out.printf("Enter the name of the graph file (something.tsv): ");
    System.out.flush();
    String fileName = in.readLine().trim();

    // Construct the full file name from the simple name given.
    if (fileName.indexOf('/') < 0) {
      fileName = "src/main/resources/hw6/data/" + fileName;
    } else {
      System.err.printf("Error: file name must be simple (no '/'s)\n");
      System.exit(1);
    }

    // Build the graph from the file and report the time spent to the user.

    long startTime = System.currentTimeMillis();
    Graph<String, String> graph = buildGraph(fileName);
    long endTime = System.currentTimeMillis();

    double duration = (endTime - startTime) / 1000.;
    System.out.printf("Parsed graph in %.1f seconds\n", duration);
    System.out.printf(" - %d characters\n", graph.size());
    System.out.printf(" - %d pairs appeared together\n", (graph.getNumOfEdges())/2);
    System.out.println();

    // Read in the names of the two characters and check that they exist.

    System.out.printf("To find a path between two characters...\n");
    System.out.printf("Enter the first character's name: ");
    System.out.flush();
    String src = in.readLine().trim();
    if (!graph.contains(new GraphNode<String>(src))) {
      System.err.printf("Error: no such node\n");
      System.exit(1);
    }

    System.out.printf("Enter the second character's name: ");
    System.out.flush();
    String dest = in.readLine().trim();
    if (!graph.contains(new GraphNode<String>(dest))) {
      System.err.printf("Error: no such node\n");
      System.exit(1);
    }

    // Find the shortest path.

    startTime = System.currentTimeMillis();
    List<GraphEdge<String, String>> path = shortestPath(graph, src, dest);
    endTime = System.currentTimeMillis();

    duration = (endTime - startTime) / 1000.;
    System.out.printf("Found shortest path in %.1f seconds\n", duration);
    System.out.println();

    // Display the shortest path to the user.
    if (path == null) {
      System.out.printf("No path from %s to %s\n", src, dest);
    } else {
      System.out.printf("Shortest path:\n");
      System.out.printf(" from %s\n", src);
      for (GraphEdge<String, String> edge : path) {
    	  System.out.printf("  to  %s [in %s]\n",
             edge.getDestination().getData(), edge.getData());
      }
    }
  }
}
