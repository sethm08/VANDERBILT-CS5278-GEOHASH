package edu.vanderbilt.cs.live2;	// Creating the java package



public class GeoHash {

	/**
	 * This live session will focus on basic Java and some concepts important to
	 * functional programming, such as recursion.
	 *
	 * This class uses a main() method where we can write our own simple "experiments" to
	 * test how our code works. You are encouraged to modify the main method to play around
	 * with your code and understand it. When you have working code, you can extract it
	 * into a method. When you have working examples with assertions, you can extract them
	 * into tests.
	 *
	 * I have left some sample experiments in main() to help you understand the geohash
	 * algorithm.
	 *
	 *
	 * This class will provide an implementation of GeoHashes:
	 *
	 * https://www.mapzen.com/blog/geohashes-and-you/
	 * https://en.wikipedia.org/wiki/Geohash
	 *
	 *
	 * GeoHash Spatial Precision:
	 *
	 * https://releases.dataone.org/online/api-documentation-v2.0.1/design/geohash.html
	 */

	 /* Starting at 7:10 pm : Reading through directions in following methods
		- Encuraged to modify the main() method
			-Note java method vs function difference review:
				-Function: A reusable piece of code. Can have input data on which can operate with (arguments) and can return data of specified return type
				-Method: Similar to a function but has two constraints: 1) Methods are associated or related to the instance of the object it is called using
					2) Methods are limited to operating on data inside the class of which the method is contained
		- Take a step back and install/create a repository for trackable changes: done
		- Dissapointing, but have to start dinner (7:20pm)...chicken in the oven (7:26pm)
		- Two public static values LATITUDE_RANGE and LONGITUDE_RANGE defined below upon creation of a GeoHash class object
		- Open and review information on the GeoHashes:
			-Mapzen:
				- History: Origionally created by Gustavo Niemeyer in '08 and availiable in a public domain
					origionally created as part of a url-shortening service, it's functionality has progressed to
					use in unique identifiers, spacial indexing, searching, etc.
				- Function: elegant method of geograpic encoding by reducing 2D longitude/latitude values to a single alphanumeric string
					- Each additional character adds precision to the location.....and time for more dinner making (7:42pm)
					- Back at the computer (12/1 @ 6:14pm)...
					- Constructing a geohash: geohashes are a type of grid spatial index - recursively dividing the surface of earth into smaller and smaller grids with each additional bit (do they mean character?)
						-ex: divide an surface/area in half with the initial bit (1/0 value), divide each of those halfs in half again, with the second bit value referencing to which half of the upper area you are
						     directed toward and so on resulting in a binary sequence (10011011100010101100011101011010111) which then gets converted to the 'geohas base 32 character map'
						- pretty cool how it automatically makes similar locations have similar initial values
			-Wikipedia:
				- note, the 32 bit character map carries integers 0-9 and all lowercase letters EXCEPT  'a', 'i', 'l', and 'o'
				- Careful rounding.... min <= rounded_value <= max

		- Cool stuff to learn about, time to begin looking into the functions and what the objective(s) of this will be
		- Reading through how this is intended to be tested...
			- Test inputs/structure defined in GeoHasTest.java
			- Applies juint (not familiar with)
		- Picking it back up at 8:44 am on Saturday... Have covered the basics behind geohashing, going to take the next step in arranging the order in which to approach the problem
			- geohash1D method:
				- Inputs:	valueToHash: input value provided as a double
							valueRange: input value provided as a double array
							bitsOfPrecision: input value provided as integer
				- Outputs:	single boolean array
				- Function: Convert the input value (valueToHash) and precision constraints to convert with continued precision
							via a recursion until the precision defined as bitsOfPrecision is met.
		- Picking it back up now that I'm sitting at a hotel in Nashville (Vanderbilt isn't far down the road) @ 9:20 pm
			- Doing some  background checks - confirming both Java and Gradle are workign correctly since I'm on my work laptop rather than my home desktop.
				- Seems to be working!
				- Back to geohash1D method....
	 */


	public static final double[] LATITUDE_RANGE = { -90, 90 };
	public static final double[] LONGITUDE_RANGE = { -180, 180 };

	public static boolean initialBit = true;

	public static boolean[] geohash1D(double valueToHash, double[] valueRange, int bitsOfPrecision) {
		// It may help you to print out what is happening:
		//
		// System.out.println("hash " + valueToHash +" [" + valueRange[0] +"," +
		// valueRange[1] + "]");

		// @ToDo:
		//
		// Implement GeoHashing here for a single value (NOT! latitude, longitude pair).
		//
		// For those seeking an added challenge, use a recursive algorithm that uses a
		// base case and does not pass around a result array.
		//
		// For now, this method only needs to "geohash" either latitude or longitude
		// separately.
		//
		// You will be passed a valueToHash and a valueRange (e.g., the range of
		// longitudes or latitudes).
		//
		// The bits of precision is the number of bits that should be in your output
		// hash.
		//
		// We are approximating "bits" with a boolean array to make things simpler.
		//
		double valueInput = valueToHash;
		double[] bounds = {valueRange[0], valueRange[1]};
		boolean bitArray[] = new boolean[bitsOfPrecision];
		boolean bit = false;

		for (int i = 0; i < bitsOfPrecision; i++){
			
			double midpoint = (bounds[0] + bounds[1]) / 2;

			System.out.println("hash " + valueToHash + " [" + bounds[0] + ", " + bounds[1] + "] midpoint " + midpoint);

			if (valueInput >= midpoint) {
				bit = true;
				bounds[0] = midpoint;
			}
			else {
				bit = false;
				bounds[1] = midpoint;
			}
			bitArray[i] = bit;
		}

		return bitArray;
	}

	public static boolean[] geohash2D(double v1, double[] v1range, double v2, double[] v2range, int bitsOfPrecision) {

		// @ToDo:
		//
		// Separately compute individual 1D geohashes for v1 and v2 and then interleave them together
		// into a final combined geohash.
		//
		// The resulting geohash should have the number of bits specified by bitsOfPrecision.
		//
		boolean bitArray[] = new boolean[bitsOfPrecision];

		int nPlaces = bitsOfPrecision/2;

		boolean[] bitArray2 = geohash1D(v2, v2range, nPlaces);

		if (bitsOfPrecision % 2 != 0){
			nPlaces = bitsOfPrecision/2+1;
		}

		boolean[] bitArray1 = geohash1D(v1, v1range, nPlaces);
		
		for (int i = 0; i < nPlaces; i++){
			bitArray[2*i] = bitArray1[i];
			if (i < bitArray2.length) {
				bitArray[2*i+1] = bitArray2[i];
			}
		}

		return bitArray;
	}

	public static boolean[] geohash(double lat, double lon, int bitsOfPrecision) {
		return geohash2D(lat, LATITUDE_RANGE, lon, LONGITUDE_RANGE, bitsOfPrecision);
	}

	// This is a helper method that will make printing out
	// geohashes easier
	public static String toHashString(boolean[] geohash) {
		String hashString = "";
		for (boolean b : geohash) {
			hashString += (b ? "1" : "0");
		}
		return hashString;
	}

	// This is a convenience method to make it easy to get a string of 1s and 0s for a
	// geohash
	public static String geohashString(double valueToHash, double[] valueRange, int bitsOfPrecision) {
		return toHashString(geohash1D(valueToHash,valueRange,bitsOfPrecision));
	}

	// Faux testing for now
	public static void assertEquals(String v1, String v2) {
		if(!v1.contentEquals(v2)) {
			System.out.println("   Failure: v1 " + v1 + " v2 "+ v2);
			throw new RuntimeException(v1 + " != " + v2);
		}
		else{
			System.out.println("   Success!");
		}
	}

	public static void main(String[] args) {

		// Example of hand-coding a 3-bit geohash

		// 1st bit of the geohash
		double longitude = 0.0;
		double[] bounds = {LONGITUDE_RANGE[0], LONGITUDE_RANGE[1]};
		double midpoint = (bounds[0] + bounds[1]) / 2;
		boolean bit = false;

		if (longitude >= midpoint) {
			bit = true;
			bounds[0] = midpoint;
		}
		else {
			bit = false;
			bounds[1] = midpoint;
		}

		// 2nd bit of the geohash
		boolean bit2 = false;
		midpoint = (bounds[0] + bounds[1]) / 2;
		if (longitude >= midpoint) {
			bit2 = true;
			bounds[0] = midpoint;
		}
		else {
			bit2 = false;
			bounds[1] = midpoint;
		}

		// 3rd bit of the geohash
		boolean bit3 = false;
		midpoint = (bounds[0] + bounds[1]) / 2;
		if (longitude >= midpoint) {
			bit3 = true;
			bounds[0] = midpoint;
		}
		else {
			bit3 = false;
			bounds[1] = midpoint;
		}
		// Continue this process for however many bits of precision we need...


		// Faux testing for now
		assertEquals("100", toHashString(new boolean[] {bit, bit2, bit3}));

		// If you can get the 1D geohash to pass all of these faux tests, you should be in
		// good shape to complete the 2D version.
		assertEquals("00000", geohashString(LONGITUDE_RANGE[0], LONGITUDE_RANGE, 5));
		assertEquals("00000", geohashString(LATITUDE_RANGE[0], LATITUDE_RANGE, 5));
		assertEquals("11111", geohashString(LONGITUDE_RANGE[1], LONGITUDE_RANGE, 5));
		assertEquals("11111", geohashString(LATITUDE_RANGE[1], LATITUDE_RANGE, 5));
		assertEquals("10000", geohashString(0, LONGITUDE_RANGE, 5));
		assertEquals("11000", geohashString(90.0, LONGITUDE_RANGE, 5));
		assertEquals("11100", geohashString(135.0, LONGITUDE_RANGE, 5));
		assertEquals("11110", geohashString(157.5, LONGITUDE_RANGE, 5));
		assertEquals("11111", geohashString(168.75, LONGITUDE_RANGE, 5));
		assertEquals("01111", geohashString(-1, LONGITUDE_RANGE, 5));
		assertEquals("00111", geohashString(-91.0, LONGITUDE_RANGE, 5));
		assertEquals("00011", geohashString(-136.0, LONGITUDE_RANGE, 5));
		assertEquals("00001", geohashString(-158.5, LONGITUDE_RANGE, 5));
		assertEquals("00000", geohashString(-169.75, LONGITUDE_RANGE, 5));
	}
}
