import java.util.ArrayList;
import java.util.Scanner;

//Main class to simulate the customer service queue system
public class L16LabCustomerServiceQueue {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		CustomerQueue serviceQueue = new CustomerQueue();
		boolean running = true;

		String[] menuItems = { "Add customer to queue", "Serve next customer", "View next customer", "Display queue",
				"Exit" };

		while (running) {
			displayMenu("--- Customer Service Queue System ---", menuItems);
			System.out.print("Enter your choice: ");

			if (scanner.hasNextInt()) {
				int choice = scanner.nextInt();
				scanner.nextLine(); // consume newline

				switch (choice) {
				case 1:
					System.out.print("Enter customer name: ");
					String name = scanner.nextLine();

					String issueType = selectIssueType(scanner);
					serviceQueue.enqueue(new Customer(name, issueType));

					System.out.println(name + " has entered the queue.");
					break;

				case 2:
					if (!serviceQueue.isEmpty()) {
						Customer servedCustomer = serviceQueue.dequeue();
						System.out.println("Now serving: " + servedCustomer);
					} else {
						System.out.println("Queue is empty. No customers to serve.");
					}
					break;

				case 3:
					if (!serviceQueue.isEmpty()) {
						System.out.println("Next in line: " + serviceQueue.peek());
					} else {
						System.out.println("Queue is empty. No customers in line.");
					}
					break;

				case 4:
					serviceQueue.displayQueue();
					break;

				case 5:
					running = false;
					System.out.println("Customer Service System closing...");
					break;

				default:
					System.out.println("Invalid choice! Please enter a number between 1-" + menuItems.length + ".");
				}
			} else {
				System.out.println("Invalid input! Please enter a number.");
				scanner.next(); // clear invalid input
			}
		}

		scanner.close();
	}

	private static void displayMenu(String title, String[] menuItems) {
		System.out.println("\n" + title);

		for (int i = 0; i < menuItems.length; i++) {
			System.out.println((i + 1) + ". " + menuItems[i]);
		}
	}

	private static String selectIssueType(Scanner scanner) {
		String[] issueTypes = { "Billing", "TechSupport", "General" };

		int choice = 0;
		boolean validInput = false;

		while (!validInput) {
			displayMenu("Select an issue type:", issueTypes);
			System.out.print("Enter your choice: ");

			if (scanner.hasNextInt()) {
				choice = scanner.nextInt();
				scanner.nextLine();

				if (choice >= 1 && choice <= issueTypes.length) {
					validInput = true;
				} else {
					System.out.println("Invalid choice! Please enter 1-" + issueTypes.length + ".");
				}
			} else {
				System.out.println("Invalid input! Please enter a number.");
				scanner.next();
			}
		}

		return issueTypes[choice - 1];
	}

}

class CustomerQueue {

	ArrayList<Customer> queue;

	public CustomerQueue() {
		queue = new ArrayList<>();
	}

	public void displayQueue() {
		for (Customer customer : queue) {
			System.out.println(customer);
		}

	}

	public void enqueue(Customer customer) {
		queue.add(customer);

	}

	public Customer dequeue() {
		int topIndex = queue.size() - 1;
		return queue.remove(topIndex);
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public Customer peek() {
		int topIndex = queue.size() - 1;
		return queue.get(topIndex);
	}

}

class Customer {
	private String name;
	private String issueType;

	public Customer(String name, String issueType) {
		this.name = name;
		this.issueType = issueType;
	}

	@Override
	public String toString() {
		return "Customer Name: " + this.name + " Issue Type: " + this.issueType;
	}

}