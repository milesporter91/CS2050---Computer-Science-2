//add comments to explain code

import java.util.Scanner;

public class M03SwapMethod
{

	public static void main(String[] args) {
		
		System.out.println("Part 1: swap numbers and pass primitive datatype");
		
		int num1 = 7;
		int num2 = 82;

		System.out.println("---In main method Before calling swap");
		System.out.println ("num1 = " + num1);
		System.out.println ("num2 = " + num2);
		System.out.println("In main calling swap\n");
		
		swap(num1, num2);
		
		System.out.println("\n---In main method after calling swap");
		System.out.println ("num1 = " + num1);                  
		System.out.println ("num2 = " + num2);
		
		
		System.out.println("\nPart 2: get positive number and pass object");
		int number = 0; 
		int sum = 0; 
				
		Scanner keyboardInput = new Scanner(System.in);	
		
		System.out.println("Enter a positive number to sum numbers 1 to positive number:");
		number = enterPositiveNumber(keyboardInput); 										
		sum = calculateSum(number);
		
		System.out.printf("The sum of numbers from 1 to %d is %d", number, sum);		
		
		keyboardInput.close();
		
	} // main
	
	
	/*
	Swaps two numbers so the first number is stored in the second number variable
	and the second number variable is stored in the first.
	Displays first and second number before and after swap
	Parameter:  int num1 is first number, num2 is second number
	*/
	public static void swap (int num1, int num2) {
		
		System.out.println("\n***In swap method***");
		System.out.println("before swap ");
		System.out.println ("num1 = " + num1);                  
		System.out.println ("num2 = " + num2);
		
		// Swap the values
		int temp = num1;
		num1 = num2;
		num2 = temp;
		
		System.out.println("After swap:");
		System.out.println ("num1 = " + num1);                  
		System.out.println ("num2 = " + num2);
		System.out.println("*** End swap method ***");
	} // swap
	
	
	/*
	Repeats prompting for integer number until number is positive 
	Assumes only integer values are entered 
	Does not validate if doubles or non-numeric char entered
	Parameter:  Scanner input object to read in numbers
	Return: integer that is positive number
	*/
	public static int enterPositiveNumber (Scanner input) {
		
		int positiveNumber = 0;
		positiveNumber = input.nextInt();
		
		while(positiveNumber <= 0)
	    {
			System.out.println("You did not enter a positive number. Try again.");
			positiveNumber = input.nextInt();
			
	    }
		
		return positiveNumber;
		
	} // end of sum
	
	/*
	Description: Get sum of numbers from 1 to positiveNumber
	Parameter: Integer maxNumber
	Return: Integer totalSum
	*/	
	public static int calculateSum(int positiveNumber)
	{
		int totalSum= 0;
	
		for(int count = 1; count <= positiveNumber; count++)
		{
			totalSum = totalSum + count;			
		}
				
		return 1;
	}//end calculateSum

}