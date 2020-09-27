package hw8;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

/** Represents a building and its location. */
public class Building implements Comparable<Building>{

    // Not an ADT
	
	@CsvBindByName
    private String shortName;
	
	@CsvBindByName
	private String longName;
	
	@CsvCustomBindByName(converter = CoordinateConverter.class)
    private Coordinate location;
	
	/**
	 * Constructs a building with empty names and a location of (0,0).
	 */
	public Building() {
		shortName = "";
		longName = "";
		location = new Coordinate();
	}
	
    /**
     * Creates a building with the given names and location.
     * 
     * @param abbrev The abbreviated name of the building.
     * @param name The full name of the building.
     * @param c The coordinate location of the building.
     */
    public Building(String abbrev, String name, Coordinate c) {
      shortName = abbrev;
      longName = name;
      location = c;
    }

    /**
     * Returns the coordinate of the building.
     * 
     * @return the location of the building.
     */
    public Coordinate getLocation() {
        return location;
    }
    
    /**
     * Sets the location of this building.
     * 
     * @param loc The new location of the building.
     */
    public void setLocation(Coordinate loc) {
    	location = loc;
    }
    
    /**
     * Returns the shortName of this building.
     * 
     * @return abbreviated name
     */
    public String getShortName() {
    	return shortName;
    }
    
    /**
     * Sets the short name for this building.
     * 
     * @param name The new short name of the building.
     */
    public void setShortName(String name) {
    	shortName = name;
    }
    
    /**
     * Returns the full name of this building.
     * 
     * @return full name of this
     */
    public String getLongName() {
    	return longName;
    }
    
    /**
     * Sets the full name of this building.
     * 
     * @param name The new long name of the building.
     */
    public void setLongName(String name) {
    	longName = name;
    }
    
    /**
     * Completes the comparable interface by comparing the names of two buildings.
     * 
     * @param b2 Building to be compared to.
     * @return the shortName of each compared to each other as long either are not
     * 			null.  Otherwise returns the non-null as larger.
     */
    public int compareTo(Building b2) {
		return shortName.compareTo(b2.getShortName());
	}
    
    /**
     * Returns a string representation of a building in the format of:
     * longName(shortName) (coordinate)
     * 
     * @return A string representation of this.
     */
    @Override 
    public String toString() {
    	return longName + "(" + shortName + ") " + location.toString();  // useful for debugging
    }
    
    /**
     * Determines whether this building is equal to another.
     * 
     * @param o Object to be compared to.
     * @return true if and only if each shortName's, longName's and locations are equal.
     */
    @Override 
    public boolean equals(Object o) {
    	if (!(o instanceof Building))
    		return false;
    	Building c = (Building) o;
    	return shortName.equals(c.getShortName()) && longName.equals(c.getLongName()) 
    							&& location.equals(c.getLocation());
    }
    
    /**
     * Returns hashCode of this.
     * 
     * @return hashCode of this.
     */
    @Override 
    public int hashCode() {
    	return 2 * longName.hashCode() + 3 * shortName.hashCode();
    }
}
