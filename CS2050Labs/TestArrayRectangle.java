/** 
 * This is an example of a rectangle class showing
 * instance and static variables and methods
 * Create an array of objects
 * Processing an array of objects
* */


public class TestArrayRectangle {

	public static void main(String[] args) {

		// Call static method to get the number of rectangles
		// No need for an instance object when using static methods
		System.out.println("\nNumber of rectangles: " + RectangleL23.getNumberOfRectangles());
		
		RectangleL23 rectangle1 = new RectangleL23();
		rectangle1.setLength(8.5);
		rectangle1.setWidth(5.2);
		

		System.out.printf("\nRectangle1 width: %.1f and height: %.1f\n",
				rectangle1.getWidth(), rectangle1.getLength());


		System.out.println("The area of the rectangle 1 is " + rectangle1.getArea());
		System.out.println("The perimeter of the rectangle 1 is " + rectangle1.getPerimeter());
		System.out.println("Number of rectangles: " + RectangleL23.getNumberOfRectangles());


		RectangleL23 rectangle2 = new RectangleL23(4, 5);

		System.out.println("Number of rectangles: " + RectangleL23.getNumberOfRectangles());
	
		//1 Fix problem in circle class 
		
		//2 Construct a third rectangle
		RectangleL23 rectangle3 = new RectangleL23(7, 8);
		
		System.out.println("Number of rectangles: " + RectangleL23.getNumberOfRectangles());
		

		//3 Declare an array called rectangles to store 3 rectangles
		final int arraySize = 3;
		RectangleL23[] rectangles = new RectangleL23[arraySize];

		//4 Assign rectangle1 to index 0, rectangle2 to index 1 and rectangle 3 to index 2.
		rectangles[0] = rectangle1;
		rectangles[1] = rectangle2;
		rectangles[2] = rectangle3;
		
		
		//the following are hard coded to see how to access methods of objects in an array 
		//5 set the length of the last rectangle using the index
		rectangles[2].setLength(10.4);
		
		//6 Display the area of the first rectangle using the index
		System.out.printf("\nThe area of the rectangle at index 2 is %.2f", rectangles[2].getArea());
		

		//7 Call method to display dimensions of each rectangle in the array of rectangles
		printRectangleInformation(rectangles);
		


		//8 Create a method to find  the  index of the rectangle with the largest length 

		int largestLengthIndex = getLargestLengthIndex(rectangles);
		System.out.println("\nThe rectangle with the largest length is at index " + largestLengthIndex);

		//9 Call a method to return the sum all the rectanlges perimeters

		  

	}// end main


	/**
	 * Method to print each rectangles dimensions
	 * @param rectanglesReference to an array of rectangles
	 */
	public static void printRectangleInformation(RectangleL23[] rectanglesReference) 
	{
		System.out.println("\nThis method will iterate through the array of rectangels");
		//add code to iterate through the array
		for (int index = 0; index< rectanglesReference.length; index++) 
		{
			//add code to print out the length and width of each rectangle			
			System.out.printf("Rectangle %d Length: %.2f width: %.2f  area:  %.2f  perimeter: %.2f", 
				index+1 , 
				rectanglesReference[index].getLength(),
				rectanglesReference[index].getWidth(),
				rectanglesReference[index].getArea(),
				rectanglesReference[index].getPerimeter());
			System.out.println();
		}
		
	}//end main method
	

	///////////// Notice the methods below are a part of the main method class
	

	/**
	 * Finds the index of the rectangle with the largest length
	 * Do not worry about duplicates
	 * @param rectanglesReference
	 * @return integer index of rectangles with largest length
	 */
	public static int getLargestLengthIndex(RectangleL23[] rectanglesReference) {
		
		int indexValue = 0;
		double largest= rectanglesReference[0].getLength();
		for (int i = 1; i< rectanglesReference.length; i++) {
			if ( largest < rectanglesReference[i].getLength() ) {
				largest = rectanglesReference[i].getLength();
				indexValue	= i;
			}
		}
		return indexValue;
	}
	
	/**
	 * Write a method to sum all the rectangles perimeters
	 * @param rectanglesReference to an array of rectangles
	 * 
	 */
		

} //end TestBasicRectangle Class	


//After Class containing main method you can put your other classes
/** 
 * Rectangle Class
 * */
class RectangleL23 {
	/** 
	 * Instance Variable
	 * attribute associated with instance of one object 
	 * */

	private double length = 1;
	private double width = 1;
	
	/** 
	 *  The number of the objects created
	 *  Static Variable
	 * Not associated with instance of one object
	 * not stored on the heap */
	private static int numberOfRectangles = 0;

	/** Default constructor */
	public RectangleL23() {
		numberOfRectangles++;
	}
	
	/** Construct a rectangle with a specified length and width 
	 * overloaded method*/
	public RectangleL23(double length, double width) {
		//call setters to ensure positive value 
		//this eliminates writing duplicate code
		setLength(length);
		setWidth(width);
		numberOfRectangles++;
	}

	/** Return Length */
	public double getLength() {
		return this.length;
	}

	/** Return Woidth */
	public double getWidth() {
		return this.width;
	}

	/** Set New Width */
	public void setWidth(double width) {
		if(width >= 0) 
		{
			this.width = width;
		}else
		{
			System.out.println("Need a positive numbers so width set to 1");
			width = 1;
		}
	}
	
	/** Set New Length */
	public void setLength(double length) {
		if(length >= 0) 
		{
			this.length = length;
		}else
		{
			System.out.println("Need a positive numbers so length set to 1");
			length = 1;
		}
	}
	/** Return the area of this rectangle */
	public double getArea() {
		return this.length * this.width;
	}

	/** Return the perimeter of this rectangle */
	public double getPerimeter() {
		return 2* (this.width + this.length);
	}
	
	/** Notice there is no setNumberOfRectangles
	 * This should not be set but the class handles keeping track*/

	/** Return numberOfObjects that have been created
	 * static method 
	 * do not need instance of an object to determine*/
	public static int getNumberOfRectangles() {
		return numberOfRectangles;
	} 

}// BasicRectangle Class