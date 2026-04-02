import java.util.ArrayList;
import java.util.Scanner;

public class DeliveryDronesHangar {

	public static void main(String[] args) {
		Hangar hangar = new Hangar();
	}

} // end of DeliveryDronesHangar

class Hangar {

	// Instance variables
	ArrayList<Drone> drones;

	// Instance Methods
	public int getCountByManufacturer(String manufacturerName) {
		int countByManufacturerName = 0;
		for (Drone drone : drones) {
			if (drone.getManufacturerName().equalsIgnoreCase(manufacturerName)) {
				countByManufacturerName++;
			}
		}
		return countByManufacturerName;
	}

	public void displayHangarInventory() {
		for (Drone drone : drones) {
			System.out.println(drone);
		}
	}

	public ArrayList<Drone> searchDronesByManufacturerAndType(String droneType, String manufacturerName) {
		ArrayList<Drone> droneResults = new ArrayList<>();
		if (droneType.equalsIgnoreCase("P")) {
			for (Drone drone : drones) {
				if (drone instanceof PriorityDrone && drone.getManufacturerName().equalsIgnoreCase(manufacturerName)) {
					droneResults.add(drone);
				}
			}
		} else if (droneType.equalsIgnoreCase("S")) {
			for (Drone drone : drones) {
				if (drone instanceof StandardDrone && drone.getManufacturerName().equalsIgnoreCase(manufacturerName)) {
					droneResults.add(drone);
				}
			}
		}
		return droneResults;
	}

	public void generateReportSortedByManufacturingYear(ArrayList<Drone> drones) {
		/*
		 * Code for method
		 */
	}

	public void generateReportSortedByPayloadCapacity(ArrayList<Drone> drones) {
		/*
		 * Code for method
		 */
	}

	public void printMenu() {
		/*
		 * Code for method
		 */
	}

	private void readFromCSV(String fileName, Scanner scanner) {
		Drone drone;
		String droneType;
		String manufacturerName;
		Integer manufacturedYear;
		double payloadKg;
		/*
		 * Code for method
		 * 
		 */
		if (droneType.equalsIgnoreCase("PriorityDrone")) {
			drones.add(drone = new PriorityDrone(manufacturerName, manufacturedYear, payloadKg));

		} else if (droneType.equalsIgnoreCase("StandardDrone")) {
			drones.add(drone = new StandardDrone(manufacturerName, manufacturedYear, payloadKg));

		}
		scanner.close();
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
		String toString = "";
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
		String toString = "";
		return toString;
	}

} // end of StandardDrone class
