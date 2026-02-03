
public class TestLibrary {
	public static void main(String[] args) {
		Book book1 = new Book("Into The Wild", "Jon Krakaeur", 1996);
		System.out.println(book1.stringOfBookDetails());
	}
}

class Book {
	// Fields
	private String author;
	private String title;
	private int year;

	// Constructors
	Book(String title, String author, int year) {
		this.author = author;
		this.title = title;
		this.year = year;
	}

	// Instance Methods

	public String stringOfBookDetails() {
		String bookString = "Title: " + this.title + " Author: " + this.author + " Year: " + this.year;
		return bookString;
	}

	// Getters & Setters
	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public int getYear() {
		return year;
	}
}
