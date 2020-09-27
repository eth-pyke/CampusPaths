package hw8;

import java.util.*;
import hw5.*;

public class MapModel {
	
	private final Graph<Coordinate, Double> graph;
	private final List<Building> buildings;
	
	/**
	 * Constructs a MapModel with given name, paths, and buildings.
	 * 
	 * @param name The name of this model.
	 * @param paths List of CampusPaths describing all possible paths.
	 * @param buildings List of Buildings showing all possible buildings on campus.
	 */
	public MapModel(String name, List<CampusPath> paths, List<Building> buildings) {
		graph = buildGraph(name, paths);
		this.buildings = buildings;
	}
	
	/**
	 * Returns a graph object describing the contents of the given file. (See
	 * the HW6 description for details on this construction.)
	 *
	 * @param name The given name of the graph being created.
	 * @param paths A list of all possible paths in the model.
	 * @return A graph object constructed from all the listed paths.
	 */
	private static Graph<Coordinate, Double> 
				   buildGraph(String name, List<CampusPath> paths) {		
		Graph<Coordinate, Double> graph2 = new Graph<Coordinate, Double>(name);
		
		for (CampusPath path : paths) {
			Coordinate origin = path.getOrigin();
			Coordinate dest = path.getDestination();
			if (!graph2.contains(new GraphNode<Coordinate>(origin))) {
				graph2.addNode(origin);
			}
			
			if (!graph2.contains(new GraphNode<Coordinate>(dest))) {
				graph2.addNode(dest);
			}
			
			graph2.addEdge(origin, dest, path.getDistance());
		}
		
		return graph2;
	}
	
	/**
	 * Gives user access to the graph.
	 * 
	 * @return graph associated with this model.
	 */
	public Graph<Coordinate, Double> getGraph() {
		return graph;
	}
	
	/**
	 * Gives user access to the list of buildings.
	 * 
	 * @return list of buildings in this.
	 */
	public List<Building> getBuildings() {
		return buildings;
	}
	
	/**
	 * Returns a building based on a given abbreviation.
	 * 
	 * @param abbrev The abbreviation of the building that is located.
	 * @return The building if the abbreviation matches.
	 * 		   Returns null otherwise.
	 */
	public Building getBuilding(String abbrev) {
		for (Building b : buildings) {
			if (abbrev.equals(b.getShortName())) {
				return b;
			}
		}
		return null;
	}
}