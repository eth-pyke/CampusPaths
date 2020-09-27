package hw7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import hw5.Graph;
import hw5.GraphEdge;
import hw5.GraphNode;
import hw6.MarvelParser;
import hw6.MarvelParser.MalformedDataException;

/**
 * Program that allows users to find the least weighted path between characters in
 * the Marvel graph. The user is prompted to enter the name of the file
 * containing information about characters and comics along with the names of
 * two characters in question. If both characters exist, it will then display a
 * shortest path between the two characters.
 *
 * <p>This is not an ADT.</p>
 */
public class MarvelPaths2 {

	/**
     * Returns a graph object describing the contents of the given file. (See
     * the HW6 description for details on this construction.)
     *
     * @param fileName The simple file name of the .TSV file to read from.
     * @param characters A set of all characters in the graph.
     * @param books Map mapping books to all of the characters in each. 
     * @throws MalformedDataException if the file cannot be parsed.
     * @return A graph object constructed from the contents of given file.
     */
	public static Graph<String, Double> 
				   buildGraph(String fileName, Set<String> characters, Map<String,
						   	  List<String>> books) throws MalformedDataException {
		
		Map<List<String>, Double> edgeWeights = new HashMap<List<String>, Double>();
		
		// {{Inv: Each book so far has counted the connections between all of its characters}}
		for (String book : books.keySet()) {
			// Adds a connection between every character in book
			for (String c1 : books.get(book)) {
				for (String c2: books.get(book)) {
					if (!c2.equals(c1)) {
						List<String> path = new ArrayList<String>();
						path.add(c1);
						path.add(c2);
						
						if (!edgeWeights.containsKey(path)) {
							edgeWeights.put(path, 1.0);
						} else {
							edgeWeights.put(path, edgeWeights.get(path) + 1.0);
						}
					}
				}
			}
		}

		Graph<String, Double> graph = new Graph<String, Double>(fileName);
		
		// {{Inv: Each character so far has been added to graph}}
		for (String s : characters) {
			graph.addNode(s);
		}
		
		// {{Inv: Each path in edgeWeights so far has been added to graph}}
		for (List<String> path : edgeWeights.keySet()) {			
			double weight = 1.0 / edgeWeights.get(path);
			graph.addEdge(path.get(0), path.get(1), weight);
		}	
		return graph;
	}

  /**
   * Returns the shortest path from the given source character to the given
   * destination character via edges in the given graph.
   *
   * @param <N> generic type parameter that each Node holds. 
   * @param graph The graph in which to search for a path.
   * @param src Name of the node in the graph where the path must start.
   * @param dest Name of the node in the graph where the path must end.
   * @spec.requires graph is not null, src and dest name nodes in graph
   * @return Returns the shortest (and lexicographically least) path from
   *     the node with name matching src to the one with name matching dest.
   */
  public static <N> List<GraphEdge<Double, N>> 
  				shortestPath(Graph<N, Double> graph, N src, N dest) {
	  
	  Queue<ArrayList<GraphEdge<Double, N>>> active = new PriorityQueue<ArrayList<GraphEdge<Double, N>>>(
			  new Comparator<ArrayList<GraphEdge<Double, N>>>(){
				  @Override
				  public int compare(ArrayList<GraphEdge<Double, N>> o1, ArrayList<GraphEdge<Double, N>> o2) {
					  double o1Length = 0.0;
					  // {{Inv: each edge weight has been added together so far}}
					  for (GraphEdge<Double, N> edge : o1) {
						  o1Length += edge.getData();
					  }
					  double o2Length = 0.0;
					  // {{Inv: each edge weight has been added together so far}}
					  for (GraphEdge<Double, N> edge : o2) {
						  o2Length += edge.getData();
					  }
					  
					  if (o1Length - o2Length < 0) {
						  return -1;
					  } else if (o1Length - o2Length > 0) {
						  return 1;
					  } else {
						  return 0;
					  }
				  }
			  }
	  );
	  Set<GraphNode<N>> finished = new HashSet<GraphNode<N>>();
	  
	  ArrayList<GraphEdge<Double, N>> startingPath = new ArrayList<GraphEdge<Double, N>>();
	  active.add(startingPath);
	  
	  // {{Inv: have shortest path to every finished node and
	  //      active contains all paths of the form p + [c], where
	  //          p is a shortest path to some finished node n and
	  //          c is a child of n}}
	  while (!active.isEmpty()) {
		  List<GraphEdge<Double, N>> minPath = active.remove();
		  
		  // Gets last destination of the last edge in minPath
		  GraphNode<N> minDest;
		  if (minPath.isEmpty()) {
			  minDest = new GraphNode<N>(src);
		  } else {
			  minDest = minPath.get(minPath.size() - 1).getDestination();
		  }
		 
		  if (minDest.getData().equals(dest)) {
			  return minPath;
		  }
		  
		  if (finished.contains(minDest)) {
			  continue;
		  }
		  
		  
		  // Restore Inv by adding paths to non-finished children of minDest
		  //{{Inv: Each edge so far has been added to active if its destination was not 
		  //  already in set}}
		  for (GraphEdge<Double, N> edge : graph.getChildren(minDest)) {
			  GraphNode<N> temp = edge.getDestination();
			  if (!finished.contains(temp)) {
				  ArrayList<GraphEdge<Double, N>> newPath = new ArrayList<GraphEdge<Double, N>>(minPath);
				  newPath.add(edge);
				  active.add(newPath);
			  }
		  }
		  
		  finished.add(minDest);
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
      fileName = "src/test/resources/hw7/data/" + fileName;
    } else {
      System.err.printf("Error: file name must be simple (no '/'s)\n");
      System.exit(1);
    }

    // Build the graph from the file and report the time spent to the user.

    long startTime = System.currentTimeMillis();
    Set<String> characters = new HashSet<String>();
	Map<String, List<String>> books = new HashMap<String, List<String>>();
	MarvelParser.parseData(fileName, characters, books);
    Graph<String, Double> graph = buildGraph(fileName, characters, books);
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
    List<GraphEdge<Double, String>> path = shortestPath(graph, src, dest);
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
      double temp = 0.0;
      for (GraphEdge<Double, String> edge : path) {
    	  System.out.printf("  to  %s [in %s]\n",
             edge.getDestination().getData(), String.format("%.3f", edge.getData()));
    	  temp += edge.getData();
      }
      System.out.println(" in: " + String.format("%.3f", temp));
    }
  }
}

