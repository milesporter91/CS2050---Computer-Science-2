import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Runs the Drone Delivery Hangar menu-driven application. This driver class
 * handles all user interaction, including loading drone data, displaying
 * inventory, searching, sorting, counting, ID lookup, and maintenance queue
 * processing.
 */
public class DeliveryDronesHangar {

	/**
	 * Starts the drone hangar program and controls the menu loop. Prompts the user
	 * for menu selections until the user chooses to exit.
	 * 
	 * @param args command-line arguments (not used)
	 */
	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		Hangar hangar = new Hangar();
		boolean running = true;

		while (running) {
			hangar.printMenu();
			if (kb.hasNextInt()) {

				int choice = kb.nextInt();
				kb.nextLine();
				switch (choice) {
				case 1:
					System.out.println("Enter file name: ");
					String fileName = kb.nextLine();
					hangar.readFromCSV(fileName);
					break;
				case 2:
					ArrayList<Drone> allDrones = hangar.getAllDrones();
					if (allDrones.isEmpty()) {
						System.out.println("Hangar is currently empty.");
					} else {
						System.out.println("Current Hangar Inventory:");
						for (Drone drone : allDrones) {
							System.out.println(drone);
						}
					}
					break;
				case 3:
					System.out.println("Please enter Manufacturer name: ");
					String manufacturerName = kb.nextLine();
					System.out.println("Please enter Drone Type(Priority/Standard): ");
					String droneType = kb.nextLine();
					ArrayList<Drone> searchResults = hangar.searchDronesByManufacturerAndType(droneType,
							manufacturerName);

					if (searchResults.isEmpty()) {
						System.out.println("No drones found.");
					} else {
						System.out.println("Drone results:");
						for (Drone drone : searchResults) {
							System.out.println(drone);
						}
					}
					break;
				case 4:
					ArrayList<Drone> sortedByPayload = hangar.getDronesSortedByPayloadCapacity();

					if (sortedByPayload.isEmpty()) {
						System.out.println("No drones available to sort.");
					} else {
						System.out.println("Sorted inventory by Payload Capacity:");
						for (Drone drone : sortedByPayload) {
							System.out.println(drone);
						}
					}
					break;
				case 5:
					ArrayList<Drone> sortedDrones = hangar.getDronesSortedByManufacturingYear();

					if (sortedDrones.isEmpty()) {
						System.out.println("No drones available to sort.");
					} else {
						System.out.println("Sorted inventory by Manufacturing Year:");
						for (Drone drone : sortedDrones) {
							System.out.println(drone);
						}
					}
					break;
				case 6:
					System.out.println("Please enter Manufacturer name: ");
					String manufacturerToCount = kb.nextLine();
					int count = hangar.getCountByManufacturer(manufacturerToCount);
					System.out.println("Total drones for manufacturer " + manufacturerToCount + ": " + count);
					break;
				case 7:
					System.out.println("Enter Drone ID: ");
					String droneId = kb.nextLine();

					Drone foundDrone = hangar.findDroneById(droneId);

					if (foundDrone == null) {
						System.out.println("No drone found with ID: " + droneId);
					} else {
						System.out.println("Drone found:");
						System.out.println(foundDrone);
					}
					break;
				case 8:
					System.out.println("Enter Drone ID to add to maintenance queue: ");
					String maintenanceId = kb.nextLine();

					boolean added = hangar.addDroneToMaintenanceQueue(maintenanceId);

					if (added) {
						System.out.println("Drone added to maintenance queue.");
					} else {
						System.out.println("No drone found with ID: " + maintenanceId);
					}
					break;
				case 9:
					Drone maintenanceDrone = hangar.processNextMaintenanceDrone();

					if (maintenanceDrone == null) {
						System.out.println("Maintenance queue is empty.");
					} else {
						System.out.println("Processing maintenance for:");
						System.out.println(maintenanceDrone);
					}
					break;
				case 10:
					System.out.println("Exiting program. Goodbye!");
					running = false;
					break;
				default:
					System.out.println("Invalid choice. Choose a menu option between 1-10.");
				}
			} else {
				System.out.println("Invalid input. Enter an integer.");
				kb.nextLine();
			}
		}
		kb.close();
	}

} // end of DeliveryDronesHangar

/**
 * Represents a drone hangar that stores and manages drone inventory. The hangar
 * uses an ArrayList for full inventory storage, a HashMap for fast drone ID
 * lookup, and a Queue for first-in, first-out maintenance processing.
 */
class Hangar {

	// Instance variables
	private ArrayList<Drone> drones = new ArrayList<>();
	private HashMap<String, Drone> droneMap = new HashMap<>();
	private Queue<Drone> maintenanceQueue = new LinkedList<>();
	private int nextDroneNumber = 1000;

	/**
	 * Generates the next unique drone ID using the format D1000, D1001, D1002,
	 * and so on.
	 * 
	 * @return the next unique drone ID
	 */
	private String generateDroneId() {
		String droneId = "D" + nextDroneNumber;
		nextDroneNumber++;
		return droneId;
	}

	/**
	 * Adds a drone to the hangar inventory and stores it in both the ArrayList and
	 * HashMap. The ArrayList keeps the full inventory, while the HashMap allows
	 * fast lookup by drone ID.
	 * 
	 * @param drone the drone to add to the hangar
	 */
	public void addDrone(Drone drone) {
		drones.add(drone);
		droneMap.put(drone.getDroneId(), drone);
	}

	/**
	 * Finds a drone by its unique drone ID using the HashMap lookup table.
	 * 
	 * @param droneId the unique drone ID to search for
	 * @return the matching Drone object, or null if no drone is found
	 */
	public Drone findDroneById(String droneId) {
		return droneMap.get(droneId.toUpperCase());
	}

	/**
	 * Adds a drone to the maintenance queue by first finding it through its drone
	 * ID. The queue stores drones in first-in, first-out order.
	 * 
	 * @param droneId the unique ID of the drone to add to the maintenance queue
	 * @return true if the drone was found and added, or false if no matching drone
	 *         was found
	 */
	public boolean addDroneToMaintenanceQueue(String droneId) {
		Drone drone = droneMap.get(droneId.toUpperCase());

		if (drone == null) {
			return false;
		}

		maintenanceQueue.add(drone);
		return true;
	}

	/**
	 * Removes and returns the next drone from the maintenance queue. The queue uses
	 * first-in, first-out order, so the first drone added is the first drone
	 * processed.
	 * 
	 * @return the next Drone in the maintenance queue, or null if the queue is empty
	 */
	public Drone processNextMaintenanceDrone() {
		return maintenanceQueue.poll();
	}

	/**
	 * Returns a copy of the current maintenance queue as an ArrayList.
	 * 
	 * @return an ArrayList containing the drones currently waiting for maintenance
	 */
	public ArrayList<Drone> getMaintenanceQueueList() {
		return new ArrayList<Drone>(maintenanceQueue);
	}

	/**
	 * Counts the total number of drones for the specified manufacturer. The
	 * comparison is case-insensitive.
	 * 
	 * @param manufacturerName the manufacturer name to search for
	 * @return the number of drones matching the manufacturer name
	 */
	public int getCountByManufacturer(String manufacturerName) {
		int countByManufacturerName = 0;
		for (Drone drone : drones) {
			if (drone.getManufacturerName().equalsIgnoreCase(manufacturerName)) {
				countByManufacturerName++;
			}
		}
		return countByManufacturerName;
	}

	/**
	 * Returns a copy of the full drone inventory. Returning a copy protects the
	 * original ArrayList from being changed outside the Hangar class.
	 * 
	 * @return an ArrayList containing all drones in the hangar inventory
	 */
	public ArrayList<Drone> getAllDrones() {
		return new ArrayList<Drone>(drones);
	}

	/**
	 * Searches the hangar inventory for drones matching the given manufacturer and
	 * drone type. Accepts Priority or P, and Standard or S, for drone type input.
	 * Returns an empty ArrayList if no matching drones are found.
	 * 
	 * @param droneType        the drone type to search for
	 * @param manufacturerName the manufacturer name to search for
	 * @return an ArrayList of drones matching the manufacturer and drone type
	 */
	public ArrayList<Drone> searchDronesByManufacturerAndType(String droneType, String manufacturerName) {
		ArrayList<Drone> droneResults = new ArrayList<>();

		for (Drone drone : drones) {
			boolean manufacturerMatches = drone.getManufacturerName().equalsIgnoreCase(manufacturerName);

			boolean typeMatches = false;

			if (droneType.equalsIgnoreCase("P") || droneType.equalsIgnoreCase("Priority")) {
				typeMatches = drone instanceof PriorityDrone;
			} else if (droneType.equalsIgnoreCase("S") || droneType.equalsIgnoreCase("Standard")) {
				typeMatches = drone instanceof StandardDrone;
			}

			if (manufacturerMatches && typeMatches) {
				droneResults.add(drone);
			}
		}

		return droneResults;

	}

	/**
	 * Returns a list of drones sorted by manufacturing year from oldest to newest.
	 * Sorting is performed on a copy of the inventory so the original order remains
	 * unchanged. Uses Collections.sort() with a Comparator.
	 * 
	 * @return an ArrayList of drones sorted by manufacturing year
	 */
	public ArrayList<Drone> getDronesSortedByManufacturingYear() {
		ArrayList<Drone> sortedDronesByManufacturingYear = new ArrayList<Drone>(drones);

		Collections.sort(sortedDronesByManufacturingYear, new Comparator<Drone>() {

			@Override
			public int compare(Drone drone1, Drone drone2) {
				return Integer.compare(drone1.getManufacturedYear(), drone2.getManufacturedYear());
			}
		});
		return sortedDronesByManufacturingYear;
	}

	/**
	 * Returns a list of drones sorted by payload capacity from lowest to highest.
	 * Sorting is performed on a copy of the inventory so the original order remains
	 * unchanged. Uses Collections.sort() with a Comparator.
	 * 
	 * @return an ArrayList of drones sorted by payload capacity
	 */
	public ArrayList<Drone> getDronesSortedByPayloadCapacity() {
		ArrayList<Drone> sortedDronesByPayloadCapacity = new ArrayList<Drone>(drones);

		Collections.sort(sortedDronesByPayloadCapacity, new Comparator<Drone>() {
			@Override
			public int compare(Drone drone1, Drone drone2) {
				return Double.compare(drone1.getPayloadKg(), drone2.getPayloadKg());
			}
		});
		return sortedDronesByPayloadCapacity;
	}

	/**
	 * Displays the main menu options for the drone hangar system.
	 */
	public void printMenu() {
		System.out.println("=== Drone Hangar Menu === ");
		System.out.println("1. Load Drones from CSV");
		System.out.println("2. Display Hangar Inventory");
		System.out.println("3. Search Drones (Manufacturer & Type)");
		System.out.println("4. View Inventory Sorted by Payload Capacity");
		System.out.println("5. View Inventory Sorted by Year");
		System.out.println("6. Count Drones by Manufacturer");
		System.out.println("7. Find Drone by ID");
		System.out.println("8. Add Drone to Maintenance Queue");
		System.out.println("9. Process Next Maintenance Drone");
		System.out.println("10. Exit");
		System.out.println("Enter your choice (1-10):");
	}

	/**
	 * Reads drone data from a CSV file and adds valid drone records to the hangar
	 * inventory. Each valid drone receives a generated unique ID. Invalid lines are
	 * skipped with an error message.
	 * 
	 * @param fileName the name of the CSV file to read
	 */
	public void readFromCSV(String fileName) {
		int lineNumber = 0;
		try (Scanner scanner = new Scanner(new File(fileName))) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				lineNumber++;

				if (line.isEmpty()) {
					continue;
				}

				String[] data = line.split(",");

				if (data.length != 4) {
					System.out.println("Skipping line " + lineNumber + ": incorrect number of fields.");
					continue;
				}

				String droneType = data[0].trim();
				String manufacturerName = data[1].trim();
				String manufacturedYearString = data[2].trim();
				String payloadKgString = data[3].trim();

				if (manufacturerName.isEmpty()) {
					System.out.println("Skipping line " + lineNumber + ": missing manufacturer name.");
					continue;
				}

				int manufacturedYear;
				double payloadKg;

				try {
					manufacturedYear = Integer.parseInt(manufacturedYearString);
					payloadKg = Double.parseDouble(payloadKgString);
				} catch (NumberFormatException e) {
					System.out.println("Skipping line " + lineNumber + ": year or payload is not a valid number.");
					continue;
				}

				if (manufacturedYear < 1917 || manufacturedYear > LocalDateTime.now().getYear()) {
					System.out.println("Skipping line " + lineNumber + ": invalid manufactured year.");
					continue;
				}

				if (payloadKg < 0) {
					System.out.println("Skipping line " + lineNumber + ": payload cannot be negative.");
					continue;
				}

				String droneId = generateDroneId();

				if (droneType.equalsIgnoreCase("P")) {
					addDrone(new PriorityDrone(droneId, manufacturerName, manufacturedYear, payloadKg));
				} else if (droneType.equalsIgnoreCase("S")) {
					addDrone(new StandardDrone(droneId, manufacturerName, manufacturedYear, payloadKg));
				} else {
					System.out.println("Skipping line " + lineNumber + ": invalid drone type. Use P or S.");
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
		}
	}

} // end of Hangar class

/**
 * Represents a general drone in the hangar inventory. Stores a unique drone ID,
 * manufacturer name, manufacturing year, and payload capacity. This is an
 * abstract superclass for specific drone types.
 */
abstract class Drone {
	// Instance variables
	private String manufacturerName;
	private int manufacturedYear;
	private double payloadKg;
	private String droneId;

	/**
	 * Constructs a Drone object with the specified ID, manufacturer, manufacturing
	 * year, and payload capacity.
	 * 
	 * @param droneId          the unique drone ID
	 * @param manufacturerName the drone manufacturer name
	 * @param manufacturedYear the year the drone was manufactured
	 * @param payloadKg        the payload capacity in kilograms
	 */
	public Drone(String droneId, String manufacturerName, int manufacturedYear, double payloadKg) {
		setDroneId(droneId);
		setManufacturerName(manufacturerName);
		this.manufacturedYear = manufacturedYear;
		this.payloadKg = payloadKg;
	}

	/**
	 * Returns the manufacturer name of the drone.
	 * 
	 * @return the manufacturer name
	 */
	public String getManufacturerName() {
		return manufacturerName;
	}

	/**
	 * Sets the manufacturer name of the drone.
	 * 
	 * @param manufacturerName the manufacturer name to set
	 */
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	/**
	 * Returns the manufacturing year of the drone.
	 * 
	 * @return the manufactured year
	 */
	public int getManufacturedYear() {
		return manufacturedYear;
	}

	/**
	 * Returns the payload capacity of the drone in kilograms.
	 * 
	 * @return the payload capacity
	 */
	public double getPayloadKg() {
		return payloadKg;
	}

	/**
	 * Returns the unique drone ID.
	 * 
	 * @return the drone ID
	 */
	public String getDroneId() {
		return droneId;
	}

	/**
	 * Sets the unique drone ID.
	 * 
	 * @param droneId the drone ID to set
	 */
	public void setDroneId(String droneId) {
		this.droneId = droneId;
	}

	/**
	 * Returns a formatted string representation of the drone.
	 * 
	 * @return a formatted string containing drone details
	 */
	@Override
	public abstract String toString();

} // end of abstract Drone superclass

/**
 * Represents a priority drone in the hangar inventory. Priority drones are one
 * concrete type of Drone.
 */
class PriorityDrone extends Drone {

	/**
	 * Constructs a PriorityDrone with the specified ID, manufacturer,
	 * manufacturing year, and payload capacity.
	 * 
	 * @param droneId          the unique drone ID
	 * @param manufacturerName the drone manufacturer name
	 * @param manufacturedYear the year the drone was manufactured
	 * @param payloadKg        the payload capacity in kilograms
	 */
	public PriorityDrone(String droneId, String manufacturerName, int manufacturedYear, double payloadKg) {
		super(droneId, manufacturerName, manufacturedYear, payloadKg);
	}

	/**
	 * Returns a formatted string representation of the priority drone.
	 * 
	 * @return a string containing the priority drone details
	 */
	@Override
	public String toString() {
		String toString = getDroneId() + " | Priority Drone - " + getManufacturerName() + " | Year: "
				+ getManufacturedYear() + " | Payload: " + getPayloadKg() + " kg";
		return toString;
	}

} // end of PriorityDrone class

/**
 * Represents a standard drone in the hangar inventory. Standard drones are one
 * concrete type of Drone.
 */
class StandardDrone extends Drone {

	/**
	 * Constructs a StandardDrone with the specified ID, manufacturer,
	 * manufacturing year, and payload capacity.
	 * 
	 * @param droneId          the unique drone ID
	 * @param manufacturerName the drone manufacturer name
	 * @param manufacturedYear the year the drone was manufactured
	 * @param payloadKg        the payload capacity in kilograms
	 */
	public StandardDrone(String droneId, String manufacturerName, int manufacturedYear, double payloadKg) {
		super(droneId, manufacturerName, manufacturedYear, payloadKg);
	}

	/**
	 * Returns a formatted string representation of the standard drone.
	 * 
	 * @return a string containing the standard drone details
	 */
	@Override
	public String toString() {
		String toString = getDroneId() + " | Standard Drone - " + getManufacturerName() + " | Year: "
				+ getManufacturedYear() + " | Payload: " + getPayloadKg() + " kg";
		return toString;
	}

} // end of StandardDrone class