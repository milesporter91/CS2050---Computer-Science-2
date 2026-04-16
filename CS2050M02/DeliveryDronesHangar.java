import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class DeliveryDronesHangar {

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
					hangar.displayHangarInventory();
					break;
				case 3:
					System.out.println("Please enter Manufacturer name: ");
					String manufacturerName = kb.nextLine();
					System.out.println("Please enter Drone Type(Priority/Standard): ");
					String droneType = kb.nextLine();
					hangar.searchDronesByManufacturerAndType(droneType, manufacturerName);
					break;
				case 4:
					hangar.generateReportSortedByPayloadCapacity();
					break;
				case 5:
					hangar.generateReportSortedByManufacturingYear();
					break;
				case 6:
					System.out.println("Please enter Manufacturer name: ");
					String manufacturerToCount = kb.nextLine();
					hangar.getCountByManufacturer(manufacturerToCount);
					break;
				case 7:
					System.out.println("Exiting program. Goodbye!");
					running = false;
					break;
				default:
					System.out.println("Invalid choice. Choose a menu option between 1-7.");
				}
			} else {
				System.out.println("Invalid input. Enter an integer.");
				kb.nextLine();
			}
		}
		kb.close();
	}

} // end of DeliveryDronesHangar


class Hangar {

	// Instance variables
	private ArrayList<Drone> drones = new ArrayList<>();

	// Instance Methods
	public void getCountByManufacturer(String manufacturerName) {
		int countByManufacturerName = 0;
		for (Drone drone : drones) {
			if (drone.getManufacturerName().equalsIgnoreCase(manufacturerName)) {
				countByManufacturerName++;
			}
		}
		System.out.println("Total drones for manufacturer " + manufacturerName + ": " + countByManufacturerName);
	}

	public void displayHangarInventory() {
		if (!drones.isEmpty()) {
			System.out.println("Current Hangar Inventory:");
			int droneNumber = 1;
			for (Drone drone : drones) {
				System.out.println("Drone #" + droneNumber);
				System.out.println("----------------------");
				System.out.println(drone);
				System.out.println("----------------------");
				droneNumber++;
			}
		} else {
			System.out.println("Hangar is currently empty.");
		}
	}

	public void searchDronesByManufacturerAndType(String droneType, String manufacturerName) {
		ArrayList<Drone> droneResults = new ArrayList<>();
		if (droneType.equalsIgnoreCase("P") || droneType.equalsIgnoreCase("Priority")) {
			for (Drone drone : drones) {
				if (drone instanceof PriorityDrone && drone.getManufacturerName().equalsIgnoreCase(manufacturerName)) {
					droneResults.add(drone);
				}
			}
		} else if (droneType.equalsIgnoreCase("S") || droneType.equalsIgnoreCase("Standard")) {
			for (Drone drone : drones) {
				if (drone instanceof StandardDrone && drone.getManufacturerName().equalsIgnoreCase(manufacturerName)) {
					droneResults.add(drone);
				}
			}
		} else {
			System.out.println("Invalid drone type. Enter Priority or Standard.");
			return;
		}
		if (droneResults.isEmpty()) {
			System.out.println("No drones found.");
		} else {
			int droneNumber = 1;
			System.out.println("Drone results: ");
			System.out.println("---------------------");
			for (Drone drone : droneResults) {
				System.out.println("Drone #" + droneNumber);
				System.out.println("--------------------");
				System.out.println(drone);
				droneNumber++;
			}
		}
	}

	public void generateReportSortedByManufacturingYear() {
		ArrayList<Drone> sortedDronesByManufacturingYear = new ArrayList<Drone>(drones);
		for (int pass = 0; pass < sortedDronesByManufacturingYear.size() - 1; pass++) {
			for (int i = 0; i < sortedDronesByManufacturingYear.size() - 1; i++) {
				Drone tempDrone;
				if (sortedDronesByManufacturingYear.get(i).getManufacturedYear() > sortedDronesByManufacturingYear
						.get(i + 1).getManufacturedYear()) {
					tempDrone = sortedDronesByManufacturingYear.get(i);
					sortedDronesByManufacturingYear.set(i, sortedDronesByManufacturingYear.get(i + 1));
					sortedDronesByManufacturingYear.set(i + 1, tempDrone);
				}
			}
		}
		System.out.println("Sorted inventory by Manufacturing Year:");
		for (Drone drone : sortedDronesByManufacturingYear) {
			System.out.println(drone);
		}
	}

	public void generateReportSortedByPayloadCapacity() {
		ArrayList<Drone> sortedDronesByPayloadCapacity = new ArrayList<Drone>(drones);
		for (int pass = 0; pass < sortedDronesByPayloadCapacity.size() - 1; pass++) {
			for (int i = 0; i < sortedDronesByPayloadCapacity.size() - 1; i++) {
				Drone tempDrone;
				if (sortedDronesByPayloadCapacity.get(i).getPayloadKg() > sortedDronesByPayloadCapacity.get(i + 1)
						.getPayloadKg()) {
					tempDrone = sortedDronesByPayloadCapacity.get(i);
					sortedDronesByPayloadCapacity.set(i, sortedDronesByPayloadCapacity.get(i + 1));
					sortedDronesByPayloadCapacity.set(i + 1, tempDrone);
				}
			}
		}
		System.out.println("Sorted inventory by Payload Capacity:");
		for (Drone drone : sortedDronesByPayloadCapacity) {
			System.out.println(drone);
		}
	}

	public void printMenu() {
		System.out.println("=== Drone Hangar Menu === ");
		System.out.println("1. Load Drones from CSV");
		System.out.println("2. Display Hangar Inventory");
		System.out.println("3. Search Drones (Manufacturer & Type)");
		System.out.println("4. View Inventory Sorted by Payload Capacity");
		System.out.println("5. View Inventory Sorted by Year");
		System.out.println("6. Count Drones by Manufacturer");
		System.out.println("7. Exit");
		System.out.println("Enter your choice (1-7):");
	}

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

				if (droneType.equalsIgnoreCase("P")) {
					drones.add(new PriorityDrone(manufacturerName, manufacturedYear, payloadKg));
				} else if (droneType.equalsIgnoreCase("S")) {
					drones.add(new StandardDrone(manufacturerName, manufacturedYear, payloadKg));
				} else {
					System.out.println("Skipping line " + lineNumber + ": invalid drone type. Use P or S.");
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
		}
	}

} // end of Hangar class

abstract class Drone {
	// Instance variables
	private String manufacturerName;
	private int manufacturedYear;
	private double payloadKg;

	// Constructor
	public Drone(String manufacturerName, int manufacturedYear, double payloadKg) {
		setManufacturerName(manufacturerName);
		this.manufacturedYear = manufacturedYear;
		this.payloadKg = payloadKg;
	}

	// Getters & Setters
	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public int getManufacturedYear() {
		return manufacturedYear;
	}

	public double getPayloadKg() {
		return payloadKg;
	}

	// toString
	@Override
	public abstract String toString();

} // end of abstract Drone superclass

class PriorityDrone extends Drone {

	// Constructors
	public PriorityDrone(String manufacturerName, int manufacturedYear, double payloadKg) {
		super(manufacturerName, manufacturedYear, payloadKg);
	}

	// toString
	@Override
	public String toString() {
		String toString = "Priority Drone - " + getManufacturerName() + " | Year: " + getManufacturedYear()
				+ " | Payload: " + getPayloadKg() + " kg";
		return toString;
	}

} // end of PriorityDrone class

class StandardDrone extends Drone {

	// Constructors
	public StandardDrone(String manufacturerName, int manufacturedYear, double payloadKg) {
		super(manufacturerName, manufacturedYear, payloadKg);
	}

	// toString
	@Override
	public String toString() {
		String toString = "Standard Drone - " + getManufacturerName() + " | Year: " + getManufacturedYear()
				+ " | Payload: " + getPayloadKg() + " kg";
		return toString;
	}

} // end of StandardDrone class
