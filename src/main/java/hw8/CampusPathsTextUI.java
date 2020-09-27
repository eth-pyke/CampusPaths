package hw8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import hw5.*;
import hw7.MarvelPaths2;

// TODO: add comments to this class

public class CampusPathsTextUI {

	public static void main(String[] args) {
		try {
			List<CampusPath> paths = 
					CampusDataParser.parsePathData("src/main/resources/hw8/campus_paths.tsv");
			List<Building> buildings = 
					CampusDataParser.parseBuildingData("src/main/resources/hw8/campus_buildings_new.tsv");
			
			MapModel map = new MapModel("Campus Map", paths, buildings);
			// Create a buffered reader so that we can read the users input.
		    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		    // Lists command and asks user to enter a command.
		    printMenu();
		    System.out.println();
		    String input = getCommand(in);
		    
		    // Checks for blank inputs or inputs starting with a # indicating a comment
		    while (input.trim().length() == 0 || input.charAt(0) == ('#')) {
		    	System.out.println(input);
		    	input = in.readLine().trim();
		    }
		    
		    // {{Inv: each user input so far has not been 'q'}}
		    while (!input.equals("q")) {
		    	if (input.equals("r")) {
		    		findRoute(map, in);	    	    
		    	} else if (input.equals("b")) {
		    		listBuildings(map);
		    	} else if (input.equals("m")) {
		    		printMenu();
		    	} else if (input.equals("q")) {
		    		continue;
		    	} else {
		    		System.out.println("Unknown option");
		    	}
		    	System.out.println();
		    	
			    input = getCommand(in);
			    
			    // Checks for blank inputs or inputs starting with a # indicating a comment
			    while (input.trim().length() == 0 || input.charAt(0) == ('#')) {
			    	System.out.println(input);
			    	input = in.readLine().trim();
			    }
		    }
		    
		} catch (IOException e) {
			System.err.println(e.toString());
			e.printStackTrace(System.err);
		}
	}
	
	public static String getCommand(BufferedReader in) throws IOException {
		System.out.printf("Enter an option ('m' to see the menu): ");
	    System.out.flush();
	    return in.readLine().trim();
	}
	
	/**
	 * Prints a menu of all possible commands the user can choose from.
	 */
	public static void printMenu() {
		System.out.println("Menu:");
	    System.out.println("\t" + "r to find a route");
	    System.out.println("\t" + "b to see a list of all buildings");
	    System.out.println("\t" + "q to quit");
	}
	
	/**
	 * Prompts the user to enter two building abbreviations and then finds a path
	 * between the two and writes out each step of the path and the total distance.
	 * 
	 * @param map Model to look at when finding the route.
	 * @param in BufferedReader to obtain the users input.
	 * @throws IOException if any I/O error occurs reading the file.
	 */
	public static void findRoute(MapModel map, BufferedReader in) throws IOException {
		System.out.printf("Abbreviated name of starting building: ");
	    System.out.flush();
	    String startString = in.readLine().trim();
	    Building start = map.getBuilding(startString);
	    System.out.printf("Abbreviated name of ending building: ");
	    System.out.flush();
	    String endString = in.readLine().trim();
	    Building end = map.getBuilding(endString);
	    
	    if (start == null) {
	    	System.out.println("Unknown building: " + startString);
	    }
	    
	    if (end == null) {
	    	System.out.println("Unknown building: " + endString);
	    }
	    
	    if (start != null && end != null) {
	    	System.out.println("Path from " + start.getLongName() + " to "
  				  + end.getLongName() + ":");
	    	List<GraphEdge<Double, Coordinate>> list = 
	    			MarvelPaths2.shortestPath(map.getGraph(), start.getLocation(), 
  												  end.getLocation());
	    	double totalDistance = 0;
  
	    	for (GraphEdge<Double, Coordinate> edge : list) {
	    		Coordinate startC = edge.getOrigin().getData();
	    		Coordinate endC = edge.getDestination().getData();
	    		System.out.println("\t" + "Walk " + (int)Math.round(edge.getData()) + " feet "
	    				+ compassDir(startC, endC) + " to (" 
	    				+ (int)Math.round(endC.getX()) + ", " 
	    				+ (int)Math.round(endC.getY()) + ")");
	    		totalDistance += edge.getData();
	    	}
	    	System.out.println("Total distance: " + (int)Math.round(totalDistance) + " feet");
	    }
	}
	
	/**
	 * Calculates and return a string representation of the direction of travel from
	 * a starting coordinate to an ending coordinate.
	 * 
	 * @param start The starting coordinate.
	 * @param end The ending coordinate.
	 * @return A string representation of the compass direction of the path.
	 */
	private static String compassDir(Coordinate start, Coordinate end) {
		double length = Math.sqrt(Math.pow(end.getX() - start.getX(), 2.0) + 
				  		Math.pow(end.getY() - start.getY(), 2.0));
		double angle = Math.asin(Math.abs((end.getY() - start.getY())) / length);
		
		// East side
		if (end.getX() > start.getX()) {
			// South side
			if (end.getY() > start.getY()) {
				if (angle < (Math.PI/ 8.0)) {
					return "E";
				} else if (angle < ((3.0 * Math.PI)/ 8.0)) {
					return "SE";
				}
				return "S";
			} else {
				if (angle < (Math.PI/ 8.0)) {
					return "E";
				} else if (angle < ((3.0 * Math.PI)/ 8.0)) {
					return "NE";
				}
				return "N";
			}
		} else {
			// South side
			if (end.getY() > start.getY()) {
				if (angle < (Math.PI/ 8.0)) {
					return "W";
				} else if (angle < ((3.0 * Math.PI)/ 8.0)) {
					return "SW";
				}
				return "S";
			} else {
				if (angle < (Math.PI/ 8.0)) {
					return "W";
				} else if (angle < ((3.0 * Math.PI)/ 8.0)) {
					return "NW";
				}
				return "N";
			}
		}	
	}
	
	/**
	 * Prints out the list of buildings in alphabetical order.
	 * 
	 * @param map MapModel containing all of the information needed.
	 */
	public static void listBuildings(MapModel map) {
		System.out.println("Buildings:");
		Set<Building> set = new TreeSet<Building>(map.getBuildings());
		
		for (Building b : set) {
			System.out.println("\t" + b.getShortName() + ": " + b.getLongName());
		}
	}
}
