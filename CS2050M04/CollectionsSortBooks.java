import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * CS2050 Lab - Collections, Sorting, and Searching
 */
public class CollectionsSortBooks
{
	public static void main(String[] args)
	{
		// Step 1: Create a LinkedList to store books
		LinkedList<Book> bookInventory = new LinkedList<>();

		bookInventory.add(new Book("Unmasking AI", "Dr. Joy Buolamwini", 2023));
		bookInventory.add(new Book("Hello World", "Hannah Fry", 2018));
		bookInventory.add(new Book("The Mathematics of Love", "Hannah Fry", 2015));
		bookInventory.add(new Book("Weapons of Math Destruction", "Cathy O’Neil", 2016));
		bookInventory.add(new Book("Race After Technology", "Ruha Benjamin", 2019));

		System.out.println("Original LinkedList of books:");
		for (Book currentBook : bookInventory)
		{
			System.out.println(currentBook);
		}

		System.out.println();

		// Step 2: Convert the LinkedList to an ArrayList for sorting
		List<Book> books = new ArrayList<>(bookInventory);

		// Step 3: Sort by title using the natural ordering from Comparable
		System.out.println("Books sorted by title:");
		books.sort(null);
		for (Book currentBook : books)
		{
			System.out.println(currentBook);
		}

		System.out.println();

		// Step 4: Sort by year (newest to oldest)
		System.out.println("Books sorted by year (newest to oldest):");

		// TODO: Add code to sort books by year from newest to oldest
		books.sort(Comparator.comparingInt(Book::getYear).reversed());
		
		// TODO: Print the books after sorting
		for (Book book : books) {
			System.out.println(book);
		}

		System.out.println();

		// Step 5: Sort by author, then title
		System.out.println("Books sorted by author, then title:");

		// TODO: Add code to sort books by author, then title
		books.sort(Comparator.comparing(Book::getAuthor).thenComparing(Book::getTitle));

		// TODO: Print the books after sorting
		for (Book book : books) {
			System.out.println(book);
		}
		System.out.println();

		// Step 6: Search by author
		System.out.println("Searching for books by Hannah Fry:");
		List<Book> searchBooksResult = findBooksByAuthor(bookInventory, "Hannah Fry");
		printBooks(searchBooksResult, "Hannah Fry", -1);

		// Step 7: Search books by author and year
		List<Book> foundBooks = findBooks(bookInventory, "Hannah Fry", 2018);
		printBooks(foundBooks, "Hannah Fry", 2018);
	}

	/**
	 * Finds all books by a given author.
	 */
	public static List<Book> findBooksByAuthor(List<Book> inventory, String author)
	{
		List<Book> results = new ArrayList<>();

		for (Book currentBook : inventory)
		{
			if (currentBook.getAuthor().equalsIgnoreCase(author))
			{
				results.add(currentBook);
			}
		}

		return results;
	}

	/**
	 * Finds all books that match both the author and year.
	 */
	public static List<Book> findBooks(List<Book> inventory, String author, int year)
	{
		List<Book> results = new ArrayList<>();

		// TODO: Add code to find books that match both author and year
		for (Book book : inventory) {
			if(book.getAuthor().equalsIgnoreCase(author) && book.getYear() == year) {
				results.add(book);
			}
		}
		// TODO: Sort the matching books by title before returning
		results.sort(Comparator.comparing(Book::getAuthor));
		return results;
	}

	/**
	 * Prints a list of books for the given search.
	 */
	public static void printBooks(List<Book> books, String author, int year)
	{
		String label;
		if (year != -1)
		{
			label = " in " + year;
		} else
		{
			label = "";
		}

		if (books.isEmpty())
		{
			System.out.println("No books found by " + author + label + ".");
		} else
		{
			System.out.println("Books by " + author + label + ":");
			for (Book currentBook : books)
			{
				System.out.println(currentBook);
			}
		}

		System.out.println();
	}
}

/**
 * A Book has a title, author, and year. This class implements Comparable so
 * books can be sorted by title.
 */
class Book implements Comparable<Book>
{
	private String title;
	private String author;
	private int year;

	public Book(String title, String author, int year)
	{
		this.title = title;
		this.author = author;
		this.year = year;
	}

	public String getTitle()
	{
		return title;
	}

	public String getAuthor()
	{
		return author;
	}

	public int getYear()
	{
		return year;
	}

	@Override
	public int compareTo(Book other)
	{
		return this.title.compareTo(other.title);
	}

	@Override
	public String toString()
	{
		return title + " by " + author + " (" + year + ")";
	}
}