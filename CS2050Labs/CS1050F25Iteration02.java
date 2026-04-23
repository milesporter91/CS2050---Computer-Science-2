//importing all three classes. One being the file, file exception, and Scanner class
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CS1050F25Iteration02
{
	public static void main(String[] args)
	{

		String INPUT_FILENAME = "course1030.txt";
		// Will check where the exception is in the code whether its in reading the
		// file, or further in
		try
		{
			System.out.println();
			// 1. Create course from file
			Course course = courseSetUp(INPUT_FILENAME);

			// 2. Compute final grades for all students
			 course.postFinalGrades();

			// 3. Display course information and all results
			 course.displayGradeRubric(course.getCategoryNames(), course.getCategoryWeights());
			 course.printReport();

			// Will catch the exception and give the error message if the file is not unable
			// to be found
		} catch (FileNotFoundException exception)
		{
			System.out.println("Error: Unable to find file " + INPUT_FILENAME);
		}

		INPUT_FILENAME = "course1050.txt";
		// Will do the same thing as it did for 1030 and try to find the exception
		try
		{
			System.out.println();
			// 1. Create course from file
			Course course = courseSetUp(INPUT_FILENAME);

			// 2. Compute final grades for all students
			 course.postFinalGrades();

			// 3. Display course information and all results
			 course.displayGradeRubric(course.getCategoryNames(), course.getCategoryWeights());
			 course.printReport();
			// Will try to catch the exception and give an error by catching the exception
		} catch (FileNotFoundException exception)
		{
			System.out.println("Error: Unable to find file " + INPUT_FILENAME);
		}
	}

	/**
	 * Reads course data and student data from the input file. File format: Line 1:
	 * courseName Line 2: numberCategories Line 3 categoryNames Line 4:
	 * categoryWeights Line 5: maxNumberStudents Remaining lines: Student
	 * Information firstName lastName categoryGrades
	 * 
	 * @param filename
	 * @return Course object
	 * @throws FileNotFoundException
	 */
	public static Course courseSetUp(String filename) throws FileNotFoundException
	{
		Scanner fileScanner = new Scanner(new File(filename));

		// Line 1: courseName
		String courseName = fileScanner.nextLine().trim();

		// Line 2: number of categories
		int numberCategories = fileScanner.nextInt();

		// Line 3: categoryNames
		// Create array to store categories
		// read categories from file into array
		String[] categoryNames = new String[numberCategories];
		for (int i = 0; i < categoryNames.length; i++)
		{
			categoryNames[i] = fileScanner.next();
		}

		// Line 4: categoryWeights
		double[] categoryWeights = new double[numberCategories];
		for (int i = 0; i < categoryWeights.length; i++)
		{
			categoryWeights[i] = fileScanner.nextDouble();
		}

		// Line 5: how many student records to read from file
		int maxStudents = fileScanner.nextInt();

		Course course = new Course(courseName, categoryNames, categoryWeights, maxStudents);

		// Read students
		while (fileScanner.hasNext())
		{
			String firstName = fileScanner.next();
			String lastName = fileScanner.next();

			double[] categoryGrades = new double[numberCategories];
			for (int i = 0; i < categoryGrades.length; i++)
			{
				categoryGrades[i] = fileScanner.nextDouble();
			}

			Student student = new Student(firstName, lastName, categoryGrades);
			boolean studentAdded = course.addStudent(student);
			if (!studentAdded)
			{
				System.out.println("Course is full. Can't add " + firstName + " " + lastName);
			}
		}

		fileScanner.close();
		return null;
	}
}

class Student
{

	private String firstName;
	private String lastName;
	private double finalPercent;
	private double[] categoryGrades;
	private char letter;

	// Storing the students names and category grades
	public Student(String firstName, String lastName, double[] categoryGrades)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.categoryGrades = categoryGrades;

		// Initializes final percent and letter before calculations are done
		this.finalPercent = 0.0;
		this.letter = ' ';
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public double[] getCategoryGrades()
	{
		return categoryGrades;
	}

	public double getFinalPercent()
	{
		return finalPercent;
	}

	public void setFinalPercent(double finalPercent)
	{
		this.finalPercent = finalPercent;
	}

	public char getLetter()
	{
		return letter;
	}

	public void setLetter(char letter)
	{
		this.letter = letter;
	}
}

class Course
{

	// List of course names, category names, category weights, stored student
	// objects and how many students
	// have been added to the course
	private String courseName;
	private String[] categoryNames;
	private double[] categoryWeights;
	private Student[] students;
	private int studentCount;

	public Course(String courseName, String[] categories, double[] weights, int maxStudents)
	{
		this.courseName = courseName;
		this.categoryNames = categories.clone();
		this.categoryWeights = weights.clone();
		this.students = new Student[maxStudents];
		this.studentCount = 0;
	}

	// Method for adding a student
	public boolean addStudent(Student specificStudent)
	{

		boolean studentAdd;
		// Checks to see if there's room left in the course. If there is room, increases
		// the number of students enrolled.
		if (studentCount < students.length)
		{
			students[studentCount] = specificStudent;
			studentCount++;
			studentAdd = true;
		} else
		{
			// If there is no room, it will prompt this error message
			System.out.println("Course is full, cannot add " + specificStudent.getFirstName() + " "
					+ specificStudent.getLastName());
			studentAdd = false;
		}
		return studentAdd;
	}

	// Method start of calculating the students final grade
	private double calculateFinalGrade(double[] grades)
	{
		double total = 0;

		// Multiply each category grade by its weight and add it to the total
		for (int index = 0; index < grades.length; index++)
		{
			total += grades[index] * categoryWeights[index];
		}
		return total;
	}

	// Method for finding the letter of the final grade
	private char letterGrade(double finalGrade)
	{
		char letterGrade = ' ';
		if (finalGrade >= 90)
		{
			letterGrade = 'A';
		} else if (finalGrade >= 80)
		{
			letterGrade = 'B';
		} else if (finalGrade >= 70)
		{
			letterGrade = 'C';
		} else if (finalGrade >= 60)
		{
			letterGrade = 'D';
		} else
		{
			letterGrade = 'F';
		}
		return letterGrade;

	}

	// Start of method for displaying the grading rubric
	public void displayGradeRubric(String[] categoryNames, double[] categoryWeights)
	{
		System.out.println("========================================");
		System.out.println("     CS1050 Final Grade Calculator");
		System.out.println("========================================");
		System.out.println("Grading Categories and Weights");

		// Loops through the categories and prints their weights
		for (int index = 0; index < categoryNames.length; index++)
		{
			System.out.println(categoryNames[index] + " : " + categoryWeights[index]);
		}
		System.out.println("----------------------------------------");
		System.out.println("Grading Scale");
		System.out.println("----------------------------------------");
		System.out.println("A: 90 or greater");
		System.out.println("B: 80 - 89.99)");
		System.out.println("C: 70 - 79.99)");
		System.out.println("D: 60 - 69.99)");
		System.out.println("F: less than 60)");
		System.out.println("----------------------------------------");
	}

	// Method for calculating maximum grade
	private double maximumGrade()
	{
		// Initializes maxGrade to a valid grade already in the array
		double maxGrade = students[0].getFinalPercent();

		// Looping through the students again, but for maximum grade.
		for (int index = 0; index < studentCount; index++)
		{
			// If the current students final grade is higher than the current max,
			// updates maxGrade to the higher value
			if (students[index].getFinalPercent() > maxGrade)
			{
				maxGrade = students[index].getFinalPercent();
			}
		}
		return maxGrade;
	}

	// Method for calculating minimum grade
	private double minimumGrade()
	{
		// Initializes minGrade to a valid grade already in the array
		double minGrade = students[0].getFinalPercent();

		// Looping through the students
		for (int index = 1; index < studentCount; index++)
		{
			// If the current students final grade is lower than the current min,
			// updates minGrade to the smaller value
			if (students[index].getFinalPercent() < minGrade)
			{
				minGrade = students[index].getFinalPercent();
			}
		}
		return minGrade;
	}

	// Method start for calculating the class average
	private double classAverage()
	{
		double sum = 0;

		// Adds each students final percentage to the running total
		for (int index = 0; index < studentCount; index++)
		{
			sum += students[index].getFinalPercent();
		}
		// Takes the sum of all the grades, then divides by amount of students to get
		// the average
		return sum / studentCount;
	}

	private Student topStudent()
	{
		// Sets the first student to the "top student" at index 0
		Student topStudent = students[0];

		// Compares each student's final percent to the current top student. If the
		// current student's
		// final is higher, updates it to the top student
		for (int index = 1; index < studentCount; index++)
		{
			if (students[index].getFinalPercent() > topStudent.getFinalPercent())
			{
				topStudent = students[index];
			}
		}
		return topStudent;
	}

	// Method for printing out the report of each student, the top student and the
	// class average
	// min and max grade
	public void printReport()
	{
		for (int index = 0; index < studentCount; index++)
		{
			Student specificStudent = students[index];

			// Displaying the students first and last name, as well as final grade and
			// letter grade.
			System.out.println("Student: " + specificStudent.getFirstName() + " " + specificStudent.getLastName()
					+ "  Final: " + specificStudent.getFinalPercent() + "  Letter: " + specificStudent.getLetter());
		}

		// Getting the information for the top student, then printing it.
		Student top = topStudent();
		System.out.println(
				"Top Student: " + top.getFirstName() + " " + top.getLastName() + " (" + top.getFinalPercent() + ")");

		// Printing out class average, lowest and highest grade
		System.out.println("Class Average: " + classAverage());
		System.out.println("Class Min: " + minimumGrade());
		System.out.println("Class Max: " + maximumGrade());
	}

	// Method for posting final grades
	public void postFinalGrades()
	{
		for (int index = 0; index < studentCount; index++)
		{
			Student specificStudent = students[index];

			// Calculate this student's weighted final percentage
			double finalPercent = calculateFinalGrade(specificStudent.getCategoryGrades());

			// Storing the final percentage and the final letter grade for the specific
			// student
			specificStudent.setFinalPercent(finalPercent);
			char letter = letterGrade(finalPercent);
			specificStudent.setLetter(letter);
		}
	}

	public String getCourseName() {
		return courseName;
	}

	public String[] getCategoryNames() {
		return categoryNames;
	}

	public double[] getCategoryWeights() {
		return categoryWeights;
	}

	public Student[] getStudents() {
		return students;
	}

	public int getStudentCount() {
		return studentCount;
	}

}