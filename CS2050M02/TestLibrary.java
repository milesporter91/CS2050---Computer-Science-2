/*
 * This is a library program that allows a user to create a library, with a set number of shelves and slots per shelf.
 * The user can also add books to the shelves, with Title, Author, and Year stored to be called on later
 * The program will alert the user if the current library is full, or if an error prevents a book from being added
 */

// Driver class


public class TestLibrary {

	// Main method
	public static void main(String[] args) {
		PrintBook book1 = new PrintBook("Into The Wild", "Jon Krakaeur", 1996);
		System.out.println(book1.toString());
		Library firstLibrary = new Library("Public Library", 1, 2);
		firstLibrary.addBook(book1);
		firstLibrary.printAllBooks();
		EBook book2 = new EBook("Empire of the Dawn", "Jay Kristoff", 2025);
		firstLibrary.addBook(book2);
		firstLibrary.printAllBooks();
		PrintBook book3 = new PrintBook("Empire of the Damned", "Jay Kristoff", 2024);
		firstLibrary.addBook(book3);
		System.out.println(firstLibrary.getName());
		firstLibrary.printAllBooks();
		firstLibrary.printOldest();
		Library secondLibrary = new Library("New York Library", 3, 3);
		secondLibrary.addBook(book3);
		secondLibrary.addBook(book1);
		secondLibrary.addBook(book2);
		secondLibrary.addBook(book1);
		secondLibrary.addBook(book3);
		secondLibrary.addBook(book2);
		secondLibrary.displayCountPerShelf();
		secondLibrary.printOldest();
	} // end of main()
} // End of TestLibrary

class Library {
	// Instance variables
	private String name;
	private Book[][] bookshelf;
	private int numberOfShelves;
	private int shelfCapacity;
	private int currentShelf;
	private int currentSlot;
	private int currentTotalBooks;
	private int totalBookCapacity;
	private boolean isFull;

	// Constructors
	public Library(String name, int shelves, int shelfCapacity) {
		if (name == null || name.isEmpty()) {
			this.name = "Test Library";
		} else {
			this.name = name;
		}
		if (shelves <= 0) {
			this.numberOfShelves = 1;
		} else {
			this.numberOfShelves = shelves;
		}
		if (shelfCapacity <= 0) {
			this.shelfCapacity = 1;
		} else {
			this.shelfCapacity = shelfCapacity;
		}
		this.totalBookCapacity = this.numberOfShelves * this.shelfCapacity;
		this.bookshelf = new Book[this.numberOfShelves][this.shelfCapacity];
		this.currentTotalBooks = 0;
		this.currentShelf = 0;
		this.currentSlot = 0;
		this.isFull = false;
	}

	// Instance Methods

	// Adds a non-null book object to a non-full Library object's bookshelf,
	// printing a message when the book is added
	// Returns a boolean value for if the book was added or not. Displays message if
	// book is invalid or bookshelf is full
	public boolean addBook(Book book) {
		boolean bookAdded = false;
		if (book == null) {
			System.out.println("Invalid book. No book added.");
			return bookAdded;
		}
		if (this.isFull == false) {
			this.bookshelf[this.currentShelf][this.currentSlot] = book;
			bookAdded = true;
			System.out.println(
					"Added " + book.toString() + " at shelf #" + (currentShelf + 1) + " slot #" + (currentSlot + 1));
			this.currentTotalBooks++;
			int nextIndex = currentTotalBooks;
			currentShelf = nextIndex / shelfCapacity;
			currentSlot = nextIndex % shelfCapacity;
			if (this.currentTotalBooks == this.totalBookCapacity) {
				this.isFull = true;
			}
		} else {
			System.out.println("Library is full. " + book.toString() + " not added.");
		}
		return bookAdded;
	}

	// Iterates through library, printing book details for non-null slots
	public void printAllBooks() {
		if (this.currentTotalBooks == 0) {
			System.out.println("There are currently no books in the library.");
		} else {

			for (int shelfCount = 0; shelfCount < numberOfShelves; shelfCount++) {
				int shelfNumber = shelfCount + 1;
				System.out.println("Shelf #" + shelfNumber);
				for (int slotCount = 0; slotCount < shelfCapacity; slotCount++) {
					int slotNumber = slotCount + 1;
					if (bookshelf[shelfCount][slotCount] != null) {
						System.out.println("Slot #" + slotNumber);
						System.out.println(this.bookshelf[shelfCount][slotCount].toString());
					}
				}
			}
		}
	}

	// Method iterates through a non-empty library, finding the book with the lowest
	// value for Year,
	// and assigning that book to the value oldestBook, then printing that book's
	// details. Prints a message if library is empty
	public void printOldest() {
		if (this.currentTotalBooks == 0) {
			System.out.println("Library is empty.");
		} else {
			int oldestBookYear = bookshelf[0][0].getYear();
			for (int compareShelf = 0; compareShelf < this.numberOfShelves; compareShelf++) {
				for (int compareIndex = 1; compareIndex < this.shelfCapacity; compareIndex++) {
					if (bookshelf[compareShelf][compareIndex] != null) {
						if (bookshelf[compareShelf][compareIndex].getYear() < oldestBookYear) {
							oldestBookYear = bookshelf[compareShelf][compareIndex].getYear();
						}
					}
				}
			}
			System.out.println("The oldest books in our library came out in " + oldestBookYear + ".");
			System.out.println("Oldest books: ");
			for (int i = 0; i < this.numberOfShelves; i++) {
				for (int j = 0; j < this.shelfCapacity; j++) {
					if (bookshelf[i][j] != null) {
						if (bookshelf[i][j].getYear() == oldestBookYear) {
							System.out.println(bookshelf[i][j].toString());
						}
					}
				}
			}
		}
	}

	// Method iterates through shelves and slots, counting how many slots are filled
	// with a book object and returning that summed value as an integer
	public void displayCountPerShelf() {
		int numberOfBooks = 0;
		for (int i = 0; i < this.numberOfShelves; i++) {
			numberOfBooks = 0;
			for (int j = 0; j < this.shelfCapacity; j++) {
				if (bookshelf[i][j] != null) {
					numberOfBooks++;
				}
			}
			if (numberOfBooks == 1) {
				System.out.println("Shelf " + (i + 1) + " has " + numberOfBooks + " book.");
			} else {
				System.out.println("Shelf " + (i + 1) + " has " + numberOfBooks + " books.");
			}
		}
	}

	// Iterates through bookshelf and counts books by non-null slots
//	private int countBooks() {
//		int numberOfBooks = 0;
//		for (int i = 0; i < this.numberOfShelves; i++) {
//			for (int j = 0; j < this.shelfCapacity; j++) {
//				if (bookshelf[i][j] != null) {
//					numberOfBooks++;
//				}
//			}
//		}
//		return numberOfBooks;
//	}

	// Getters & Setters
	public String getName() {
		return name;
	}
} // end of Library Class

abstract class Book {
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

	// Abstract Methods
	public abstract int getLoanDays();

	public abstract double getDailyLateFee();

	// Instance Methods
	public final double calculateLateFee(int daysLate) {
		double lateFee = 0;
		if (daysLate > 0) {
			lateFee = daysLate * getDailyLateFee();
		}
		return lateFee;
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

	// toString override
	@Override
	public String toString() {
		String bookString = "Title: " + this.title + " Author: " + this.author + " Year: " + this.year;
		return bookString;
	}
} // end of abstract Book class

class PrintBook extends Book {
	// Constructors
	public PrintBook(String title, String author, int year) {
		super(title, author, year);
	}

	// Getters & Setters
	public int getLoanDays() {
		return 21;
	}

	public double getDailyLateFee() {
		return .25;
	}

	// toString override
	@Override
	public String toString() {
		String bookString = "Title: " + this.getTitle() + " Author: " + this.getAuthor() + " Year: " + this.getYear();
		return bookString;
	}
} // end of PrintBook class

class EBook extends Book {
	// Constructors
	public EBook(String title, String author, int year) {
		super(title, author, year);
	}

	// Getters & Setters
	public int getLoanDays() {
		return 14;
	}

	public double getDailyLateFee() {
		return .10;
	}

	// toString override
	@Override
	public String toString() {
		String bookString = "Title: " + this.getTitle() + " Author: " + this.getAuthor() + " Year: " + this.getYear();
		return bookString;
	}
} // end of EBook class