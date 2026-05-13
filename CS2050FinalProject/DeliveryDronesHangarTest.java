import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the Drone Delivery Hangar project classes.
 * 
 * This test class verifies the main behavior of the Hangar, Drone,
 * PriorityDrone, and StandardDrone classes. It checks normal cases and edge
 * cases for adding drones, searching, sorting, counting, HashMap lookup,
 * maintenance queue processing, CSV file loading, and drone formatting.
 */
class DeliveryDronesHangarTest {

	private Hangar hangar;

	/**
	 * Runs once before all test methods.
	 * 
	 * This setup method is currently unused but included as part of the standard
	 * JUnit test structure.
	 * 
	 * @throws Exception if setup fails
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// Runs once before all tests.
	}

	/**
	 * Runs before each test method.
	 * 
	 * Creates a fresh Hangar object so each test starts with an empty inventory,
	 * empty HashMap, and empty maintenance queue.
	 * 
	 * @throws Exception if setup fails
	 */
	@BeforeEach
	void setUp() throws Exception {
		// Creates a fresh Hangar before each test.
		hangar = new Hangar();
	}

	/**
	 * Runs after each test method.
	 * 
	 * Clears the Hangar reference and removes any temporary CSV files created by
	 * tests.
	 * 
	 * @throws Exception if cleanup fails
	 */
	@AfterEach
	void tearDown() throws Exception {
		// Clears the Hangar reference after each test.
		hangar = null;
		// Cleanup test CSV files if they exist.
		new File("test_drones.csv").delete();
		new File("test_invalid_drones.csv").delete();
		new File("test_empty_drones.csv").delete();
		new File("test_mixed_drones.csv").delete();
	}

	/**
	 * Runs once after all test methods.
	 * 
	 * This cleanup method is currently unused but included as part of the standard
	 * JUnit test structure.
	 * 
	 * @throws Exception if cleanup fails
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		// Runs once after all tests.
	}

	// ============================================================
	// Hangar class tests
	// ============================================================

	// ------------------------------------------------------------
	// Constructor / starting state
	// ------------------------------------------------------------

	/**
	 * Tests that a new Hangar starts with an empty inventory list.
	 */
	@Test
	void test_new_Hangar_starts_empty() {
		ArrayList<Drone> drones = hangar.getAllDrones();

		assertNotNull(drones);
		assertTrue(drones.isEmpty());
	}

	/**
	 * Tests that a new Hangar starts with an empty maintenance queue.
	 */
	@Test
	void test_new_Hangar_maintenance_queue_starts_empty() {
		ArrayList<Drone> queue = hangar.getMaintenanceQueueList();

		assertNotNull(queue);
		assertTrue(queue.isEmpty());
	}

	// ------------------------------------------------------------
	// addDrone()
	// ------------------------------------------------------------

	/**
	 * Tests that addDrone() adds one drone to the inventory with the expected
	 * field values.
	 */
	@Test
	void test_add_Drone_adds_drone_to_inventory() {
		Drone drone = new StandardDrone("D1000", "DJI", 2021, 12.5);

		hangar.addDrone(drone);

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertEquals(1, drones.size());
		assertEquals("D1000", drones.get(0).getDroneId());
		assertEquals("DJI", drones.get(0).getManufacturerName());
		assertEquals(2021, drones.get(0).getManufacturedYear());
		assertEquals(12.5, drones.get(0).getPayloadKg());
	}

	/**
	 * Tests that addDrone() can add multiple drones to the inventory in order.
	 */
	@Test
	void test_add_Drone_adds_multiple_drones_to_inventory() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "Skydio", 2024, 18.0));

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertEquals(2, drones.size());
		assertEquals("D1000", drones.get(0).getDroneId());
		assertEquals("D1001", drones.get(1).getDroneId());
	}

	// ------------------------------------------------------------
	// getAllDrones()
	// ------------------------------------------------------------

	/**
	 * Tests that getAllDrones() returns an empty list when the inventory has no
	 * drones.
	 */
	@Test
	void test_getAllDrones_returns_empty_list_when_no_drones_exist() {
		ArrayList<Drone> drones = hangar.getAllDrones();

		assertNotNull(drones);
		assertTrue(drones.isEmpty());
	}

	/**
	 * Tests that getAllDrones() returns all drones that have been added to the
	 * hangar.
	 */
	@Test
	void test_getAllDrones_returns_all_added_drones() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "Skydio", 2024, 18.0));

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertEquals(2, drones.size());
	}

	/**
	 * Tests that getAllDrones() returns a copy of the inventory rather than the
	 * original ArrayList.
	 */
	@Test
	void test_getAllDrones_returns_copy_not_original_inventory() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		ArrayList<Drone> drones = hangar.getAllDrones();
		drones.clear();

		ArrayList<Drone> dronesAfterClear = hangar.getAllDrones();

		assertEquals(1, dronesAfterClear.size());
	}

	// ------------------------------------------------------------
	// findDroneById()
	// ------------------------------------------------------------

	/**
	 * Tests that findDroneById() returns the correct drone when the ID exists.
	 */
	@Test
	void test_findDroneById_returns_correct_drone() {
		Drone drone = new PriorityDrone("D1001", "Skydio", 2024, 18.0);

		hangar.addDrone(drone);

		Drone result = hangar.findDroneById("D1001");

		assertNotNull(result);
		assertEquals("D1001", result.getDroneId());
		assertEquals("Skydio", result.getManufacturerName());
		assertTrue(result instanceof PriorityDrone);
	}

	/**
	 * Tests that findDroneById() returns null when the ID does not exist.
	 */
	@Test
	void test_findDroneById_returns_null_when_id_does_not_exist() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		Drone result = hangar.findDroneById("D9999");

		assertNull(result);
	}

	/**
	 * Tests that findDroneById() works when the user enters a lowercase ID.
	 */
	@Test
	void test_findDroneById_is_case_insensitive() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		Drone result = hangar.findDroneById("d1000");

		assertNotNull(result);
		assertEquals("D1000", result.getDroneId());
	}

	// ------------------------------------------------------------
	// searchDronesByManufacturerAndType()
	// ------------------------------------------------------------

	/**
	 * Tests that searchDronesByManufacturerAndType() returns a matching standard
	 * drone.
	 */
	@Test
	void test_searchDronesByManufacturerAndType_returns_matching_standard_drone() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "DJI", 2024, 18.0));
		hangar.addDrone(new StandardDrone("D1002", "Skydio", 2023, 14.0));

		ArrayList<Drone> results = hangar.searchDronesByManufacturerAndType("Standard", "DJI");

		assertEquals(1, results.size());
		assertEquals("D1000", results.get(0).getDroneId());
		assertTrue(results.get(0) instanceof StandardDrone);
	}

	/**
	 * Tests that searchDronesByManufacturerAndType() returns a matching priority
	 * drone.
	 */
	@Test
	void test_searchDronesByManufacturerAndType_returns_matching_priority_drone() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "DJI", 2024, 18.0));
		hangar.addDrone(new PriorityDrone("D1002", "Skydio", 2023, 14.0));

		ArrayList<Drone> results = hangar.searchDronesByManufacturerAndType("Priority", "DJI");

		assertEquals(1, results.size());
		assertEquals("D1001", results.get(0).getDroneId());
		assertTrue(results.get(0) instanceof PriorityDrone);
	}

	/**
	 * Tests that searchDronesByManufacturerAndType() accepts P as shorthand for
	 * Priority.
	 */
	@Test
	void test_searchDronesByManufacturerAndType_accepts_P_for_priority() {
		hangar.addDrone(new PriorityDrone("D1000", "Skydio", 2024, 18.0));

		ArrayList<Drone> results = hangar.searchDronesByManufacturerAndType("P", "Skydio");

		assertEquals(1, results.size());
		assertEquals("D1000", results.get(0).getDroneId());
	}

	/**
	 * Tests that searchDronesByManufacturerAndType() accepts S as shorthand for
	 * Standard.
	 */
	@Test
	void test_searchDronesByManufacturerAndType_accepts_S_for_standard() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		ArrayList<Drone> results = hangar.searchDronesByManufacturerAndType("S", "DJI");

		assertEquals(1, results.size());
		assertEquals("D1000", results.get(0).getDroneId());
	}

	/**
	 * Tests that searchDronesByManufacturerAndType() ignores capitalization for
	 * manufacturer and drone type input.
	 */
	@Test
	void test_searchDronesByManufacturerAndType_is_case_insensitive() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		ArrayList<Drone> results = hangar.searchDronesByManufacturerAndType("standard", "dji");

		assertEquals(1, results.size());
		assertEquals("D1000", results.get(0).getDroneId());
	}

	/**
	 * Tests that searchDronesByManufacturerAndType() returns an empty list when
	 * there are no matching drones.
	 */
	@Test
	void test_searchDronesByManufacturerAndType_returns_empty_list_when_no_matches() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		ArrayList<Drone> results = hangar.searchDronesByManufacturerAndType("Priority", "Skydio");

		assertNotNull(results);
		assertTrue(results.isEmpty());
	}

	/**
	 * Tests that searchDronesByManufacturerAndType() returns an empty list when
	 * the drone type is invalid.
	 */
	@Test
	void test_searchDronesByManufacturerAndType_returns_empty_list_for_invalid_type() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		ArrayList<Drone> results = hangar.searchDronesByManufacturerAndType("Heavy", "DJI");

		assertNotNull(results);
		assertTrue(results.isEmpty());
	}

	/**
	 * Tests that searchDronesByManufacturerAndType() returns an empty list when
	 * the inventory is empty.
	 */
	@Test
	void test_searchDronesByManufacturerAndType_returns_empty_list_when_inventory_empty() {
		ArrayList<Drone> results = hangar.searchDronesByManufacturerAndType("Standard", "DJI");

		assertNotNull(results);
		assertTrue(results.isEmpty());
	}

	// ------------------------------------------------------------
	// getDronesSortedByPayloadCapacity()
	// ------------------------------------------------------------

	/**
	 * Tests that getDronesSortedByPayloadCapacity() sorts drones from lowest
	 * payload capacity to highest payload capacity.
	 */
	@Test
	void test_getDronesSortedByPayloadCapacity_sorts_lowest_to_highest() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "Skydio", 2024, 18.0));
		hangar.addDrone(new StandardDrone("D1002", "Parrot", 2020, 8.0));

		ArrayList<Drone> sorted = hangar.getDronesSortedByPayloadCapacity();

		assertEquals("D1002", sorted.get(0).getDroneId());
		assertEquals("D1000", sorted.get(1).getDroneId());
		assertEquals("D1001", sorted.get(2).getDroneId());
	}

	/**
	 * Tests that getDronesSortedByPayloadCapacity() returns an empty list when the
	 * inventory is empty.
	 */
	@Test
	void test_getDronesSortedByPayloadCapacity_returns_empty_list_when_inventory_empty() {
		ArrayList<Drone> sorted = hangar.getDronesSortedByPayloadCapacity();

		assertNotNull(sorted);
		assertTrue(sorted.isEmpty());
	}

	/**
	 * Tests that getDronesSortedByPayloadCapacity() does not change the original
	 * inventory order.
	 */
	@Test
	void test_getDronesSortedByPayloadCapacity_does_not_change_original_inventory_order() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "Skydio", 2024, 18.0));
		hangar.addDrone(new StandardDrone("D1002", "Parrot", 2020, 8.0));

		hangar.getDronesSortedByPayloadCapacity();

		ArrayList<Drone> original = hangar.getAllDrones();

		assertEquals("D1000", original.get(0).getDroneId());
		assertEquals("D1001", original.get(1).getDroneId());
		assertEquals("D1002", original.get(2).getDroneId());
	}

	/**
	 * Tests that getDronesSortedByPayloadCapacity() can handle drones with equal
	 * payload capacities.
	 */
	@Test
	void test_getDronesSortedByPayloadCapacity_handles_equal_payloads() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "Skydio", 2024, 12.5));

		ArrayList<Drone> sorted = hangar.getDronesSortedByPayloadCapacity();

		assertEquals(2, sorted.size());
		assertEquals(12.5, sorted.get(0).getPayloadKg());
		assertEquals(12.5, sorted.get(1).getPayloadKg());
	}

	// ------------------------------------------------------------
	// getDronesSortedByManufacturingYear()
	// ------------------------------------------------------------

	/**
	 * Tests that getDronesSortedByManufacturingYear() sorts drones from oldest
	 * manufacturing year to newest manufacturing year.
	 */
	@Test
	void test_getDronesSortedByManufacturingYear_sorts_oldest_to_newest() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "Skydio", 2024, 18.0));
		hangar.addDrone(new StandardDrone("D1002", "Parrot", 2020, 8.0));

		ArrayList<Drone> sorted = hangar.getDronesSortedByManufacturingYear();

		assertEquals("D1002", sorted.get(0).getDroneId());
		assertEquals("D1000", sorted.get(1).getDroneId());
		assertEquals("D1001", sorted.get(2).getDroneId());
	}

	/**
	 * Tests that getDronesSortedByManufacturingYear() returns an empty list when
	 * the inventory is empty.
	 */
	@Test
	void test_getDronesSortedByManufacturingYear_returns_empty_list_when_inventory_empty() {
		ArrayList<Drone> sorted = hangar.getDronesSortedByManufacturingYear();

		assertNotNull(sorted);
		assertTrue(sorted.isEmpty());
	}

	/**
	 * Tests that getDronesSortedByManufacturingYear() does not change the original
	 * inventory order.
	 */
	@Test
	void test_getDronesSortedByManufacturingYear_does_not_change_original_inventory_order() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "Skydio", 2024, 18.0));
		hangar.addDrone(new StandardDrone("D1002", "Parrot", 2020, 8.0));

		hangar.getDronesSortedByManufacturingYear();

		ArrayList<Drone> original = hangar.getAllDrones();

		assertEquals("D1000", original.get(0).getDroneId());
		assertEquals("D1001", original.get(1).getDroneId());
		assertEquals("D1002", original.get(2).getDroneId());
	}

	/**
	 * Tests that getDronesSortedByManufacturingYear() can handle drones with equal
	 * manufacturing years.
	 */
	@Test
	void test_getDronesSortedByManufacturingYear_handles_equal_years() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "Skydio", 2021, 18.0));

		ArrayList<Drone> sorted = hangar.getDronesSortedByManufacturingYear();

		assertEquals(2, sorted.size());
		assertEquals(2021, sorted.get(0).getManufacturedYear());
		assertEquals(2021, sorted.get(1).getManufacturedYear());
	}

	// ------------------------------------------------------------
	// getCountByManufacturer()
	// ------------------------------------------------------------

	/**
	 * Tests that getCountByManufacturer() returns the correct number of drones for
	 * a manufacturer.
	 */
	@Test
	void test_getCountByManufacturer_returns_correct_count() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "DJI", 2024, 18.0));
		hangar.addDrone(new StandardDrone("D1002", "Skydio", 2023, 14.0));

		int count = hangar.getCountByManufacturer("DJI");

		assertEquals(2, count);
	}

	/**
	 * Tests that getCountByManufacturer() ignores capitalization when counting by
	 * manufacturer.
	 */
	@Test
	void test_getCountByManufacturer_is_case_insensitive() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "dji", 2024, 18.0));

		int count = hangar.getCountByManufacturer("DJI");

		assertEquals(2, count);
	}

	/**
	 * Tests that getCountByManufacturer() returns zero when no manufacturer
	 * matches.
	 */
	@Test
	void test_getCountByManufacturer_returns_zero_when_no_match() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		int count = hangar.getCountByManufacturer("Skydio");

		assertEquals(0, count);
	}

	/**
	 * Tests that getCountByManufacturer() returns zero when the inventory is
	 * empty.
	 */
	@Test
	void test_getCountByManufacturer_returns_zero_when_inventory_empty() {
		int count = hangar.getCountByManufacturer("DJI");

		assertEquals(0, count);
	}

	// ------------------------------------------------------------
	// addDroneToMaintenanceQueue()
	// ------------------------------------------------------------

	/**
	 * Tests that addDroneToMaintenanceQueue() returns true when the drone ID is
	 * valid.
	 */
	@Test
	void test_addDroneToMaintenanceQueue_returns_true_for_valid_id() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		boolean result = hangar.addDroneToMaintenanceQueue("D1000");

		assertTrue(result);
	}

	/**
	 * Tests that addDroneToMaintenanceQueue() returns false when the drone ID does
	 * not exist.
	 */
	@Test
	void test_addDroneToMaintenanceQueue_returns_false_for_invalid_id() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		boolean result = hangar.addDroneToMaintenanceQueue("D9999");

		assertFalse(result);
	}

	/**
	 * Tests that addDroneToMaintenanceQueue() accepts lowercase drone IDs.
	 */
	@Test
	void test_addDroneToMaintenanceQueue_is_case_insensitive() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		boolean result = hangar.addDroneToMaintenanceQueue("d1000");

		assertTrue(result);
	}

	// ------------------------------------------------------------
	// processNextMaintenanceDrone()
	// ------------------------------------------------------------

	/**
	 * Tests that processNextMaintenanceDrone() returns the first drone added to the
	 * maintenance queue.
	 */
	@Test
	void test_processNextMaintenanceDrone_processes_first_drone_added() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));

		hangar.addDroneToMaintenanceQueue("D1000");

		Drone result = hangar.processNextMaintenanceDrone();

		assertNotNull(result);
		assertEquals("D1000", result.getDroneId());
	}

	/**
	 * Tests that processNextMaintenanceDrone() processes drones in first-in,
	 * first-out order.
	 */
	@Test
	void test_processNextMaintenanceDrone_processes_in_first_in_first_out_order() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "Skydio", 2024, 18.0));

		hangar.addDroneToMaintenanceQueue("D1000");
		hangar.addDroneToMaintenanceQueue("D1001");

		Drone firstProcessed = hangar.processNextMaintenanceDrone();
		Drone secondProcessed = hangar.processNextMaintenanceDrone();

		assertEquals("D1000", firstProcessed.getDroneId());
		assertEquals("D1001", secondProcessed.getDroneId());
	}

	/**
	 * Tests that processNextMaintenanceDrone() returns null when the maintenance
	 * queue is empty.
	 */
	@Test
	void test_processNextMaintenanceDrone_returns_null_when_queue_empty() {
		Drone result = hangar.processNextMaintenanceDrone();

		assertNull(result);
	}

	// ------------------------------------------------------------
	// getMaintenanceQueueList()
	// ------------------------------------------------------------

	/**
	 * Tests that getMaintenanceQueueList() returns the drones currently waiting in
	 * the maintenance queue.
	 */
	@Test
	void test_getMaintenanceQueueList_returns_current_queue() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDrone(new PriorityDrone("D1001", "Skydio", 2024, 18.0));

		hangar.addDroneToMaintenanceQueue("D1000");
		hangar.addDroneToMaintenanceQueue("D1001");

		ArrayList<Drone> queue = hangar.getMaintenanceQueueList();

		assertEquals(2, queue.size());
		assertEquals("D1000", queue.get(0).getDroneId());
		assertEquals("D1001", queue.get(1).getDroneId());
	}

	/**
	 * Tests that getMaintenanceQueueList() returns an empty list when the queue is
	 * empty.
	 */
	@Test
	void test_getMaintenanceQueueList_returns_empty_list_when_queue_empty() {
		ArrayList<Drone> queue = hangar.getMaintenanceQueueList();

		assertNotNull(queue);
		assertTrue(queue.isEmpty());
	}

	/**
	 * Tests that getMaintenanceQueueList() returns a copy rather than the original
	 * queue.
	 */
	@Test
	void test_getMaintenanceQueueList_returns_copy_not_original_queue() {
		hangar.addDrone(new StandardDrone("D1000", "DJI", 2021, 12.5));
		hangar.addDroneToMaintenanceQueue("D1000");

		ArrayList<Drone> queue = hangar.getMaintenanceQueueList();
		queue.clear();

		ArrayList<Drone> queueAfterClear = hangar.getMaintenanceQueueList();

		assertEquals(1, queueAfterClear.size());
	}

	// ------------------------------------------------------------
	// readFromCSV()
	// ------------------------------------------------------------

	/**
	 * Tests that readFromCSV() loads valid standard and priority drone records.
	 * 
	 * @throws IOException if the test file cannot be created
	 */
	@Test
	void test_readFromCSV_loads_valid_drones() throws IOException {
		File testFile = new File("test_drones.csv");

		FileWriter writer = new FileWriter(testFile);
		writer.write("S,DJI,2021,12.5\n");
		writer.write("P,Skydio,2024,18.0\n");
		writer.close();

		hangar.readFromCSV("test_drones.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertEquals(2, drones.size());

		assertEquals("D1000", drones.get(0).getDroneId());
		assertEquals("DJI", drones.get(0).getManufacturerName());
		assertEquals(2021, drones.get(0).getManufacturedYear());
		assertEquals(12.5, drones.get(0).getPayloadKg());
		assertTrue(drones.get(0) instanceof StandardDrone);

		assertEquals("D1001", drones.get(1).getDroneId());
		assertEquals("Skydio", drones.get(1).getManufacturerName());
		assertEquals(2024, drones.get(1).getManufacturedYear());
		assertEquals(18.0, drones.get(1).getPayloadKg());
		assertTrue(drones.get(1) instanceof PriorityDrone);
	}

	/**
	 * Tests that readFromCSV() assigns unique drone IDs in the order valid records
	 * are loaded.
	 * 
	 * @throws IOException if the test file cannot be created
	 */
	@Test
	void test_readFromCSV_assigns_unique_ids_in_order() throws IOException {
		File testFile = new File("test_drones.csv");

		FileWriter writer = new FileWriter(testFile);
		writer.write("S,DJI,2021,12.5\n");
		writer.write("P,Skydio,2024,18.0\n");
		writer.write("S,Parrot,2020,8.0\n");
		writer.close();

		hangar.readFromCSV("test_drones.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertEquals("D1000", drones.get(0).getDroneId());
		assertEquals("D1001", drones.get(1).getDroneId());
		assertEquals("D1002", drones.get(2).getDroneId());
	}

	/**
	 * Tests that readFromCSV() skips a line with an invalid drone type.
	 * 
	 * @throws IOException if the test file cannot be created
	 */
	@Test
	void test_readFromCSV_skips_invalid_drone_type() throws IOException {
		File testFile = new File("test_invalid_drones.csv");

		FileWriter writer = new FileWriter(testFile);
		writer.write("X,DJI,2021,12.5\n");
		writer.close();

		hangar.readFromCSV("test_invalid_drones.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertTrue(drones.isEmpty());
	}

	/**
	 * Tests that readFromCSV() skips a line with a missing manufacturer.
	 * 
	 * @throws IOException if the test file cannot be created
	 */
	@Test
	void test_readFromCSV_skips_missing_manufacturer() throws IOException {
		File testFile = new File("test_invalid_drones.csv");

		FileWriter writer = new FileWriter(testFile);
		writer.write("S,,2021,12.5\n");
		writer.close();

		hangar.readFromCSV("test_invalid_drones.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertTrue(drones.isEmpty());
	}

	/**
	 * Tests that readFromCSV() skips a line with non-numeric year data.
	 * 
	 * @throws IOException if the test file cannot be created
	 */
	@Test
	void test_readFromCSV_skips_invalid_year_text() throws IOException {
		File testFile = new File("test_invalid_drones.csv");

		FileWriter writer = new FileWriter(testFile);
		writer.write("S,DJI,twenty,12.5\n");
		writer.close();

		hangar.readFromCSV("test_invalid_drones.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertTrue(drones.isEmpty());
	}

	/**
	 * Tests that readFromCSV() skips a line with non-numeric payload data.
	 * 
	 * @throws IOException if the test file cannot be created
	 */
	@Test
	void test_readFromCSV_skips_invalid_payload_text() throws IOException {
		File testFile = new File("test_invalid_drones.csv");

		FileWriter writer = new FileWriter(testFile);
		writer.write("S,DJI,2021,heavy\n");
		writer.close();

		hangar.readFromCSV("test_invalid_drones.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertTrue(drones.isEmpty());
	}

	/**
	 * Tests that readFromCSV() skips a drone with a manufacturing year before
	 * 1917.
	 * 
	 * @throws IOException if the test file cannot be created
	 */
	@Test
	void test_readFromCSV_skips_year_before_1917() throws IOException {
		File testFile = new File("test_invalid_drones.csv");

		FileWriter writer = new FileWriter(testFile);
		writer.write("S,DJI,1916,12.5\n");
		writer.close();

		hangar.readFromCSV("test_invalid_drones.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertTrue(drones.isEmpty());
	}

	/**
	 * Tests that readFromCSV() skips a drone with a negative payload value.
	 * 
	 * @throws IOException if the test file cannot be created
	 */
	@Test
	void test_readFromCSV_skips_negative_payload() throws IOException {
		File testFile = new File("test_invalid_drones.csv");

		FileWriter writer = new FileWriter(testFile);
		writer.write("S,DJI,2021,-12.5\n");
		writer.close();

		hangar.readFromCSV("test_invalid_drones.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertTrue(drones.isEmpty());
	}

	/**
	 * Tests that readFromCSV() skips a line with the wrong number of CSV fields.
	 * 
	 * @throws IOException if the test file cannot be created
	 */
	@Test
	void test_readFromCSV_skips_line_with_wrong_number_of_fields() throws IOException {
		File testFile = new File("test_invalid_drones.csv");

		FileWriter writer = new FileWriter(testFile);
		writer.write("S,DJI,2021\n");
		writer.close();

		hangar.readFromCSV("test_invalid_drones.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertTrue(drones.isEmpty());
	}

	/**
	 * Tests that readFromCSV() skips empty lines and still loads valid records.
	 * 
	 * @throws IOException if the test file cannot be created
	 */
	@Test
	void test_readFromCSV_skips_empty_lines() throws IOException {
		File testFile = new File("test_mixed_drones.csv");

		FileWriter writer = new FileWriter(testFile);
		writer.write("\n");
		writer.write("S,DJI,2021,12.5\n");
		writer.close();

		hangar.readFromCSV("test_mixed_drones.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertEquals(1, drones.size());
		assertEquals("DJI", drones.get(0).getManufacturerName());
	}

	/**
	 * Tests that readFromCSV() loads valid lines while skipping invalid lines from
	 * the same file.
	 * 
	 * @throws IOException if the test file cannot be created
	 */
	@Test
	void test_readFromCSV_loads_valid_lines_and_skips_invalid_lines() throws IOException {
		File testFile = new File("test_mixed_drones.csv");

		FileWriter writer = new FileWriter(testFile);
		writer.write("S,DJI,2021,12.5\n");
		writer.write("X,BadType,2021,10.0\n");
		writer.write("P,Skydio,2024,18.0\n");
		writer.write("S,BadPayload,2021,-5.0\n");
		writer.close();

		hangar.readFromCSV("test_mixed_drones.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertEquals(2, drones.size());
		assertEquals("DJI", drones.get(0).getManufacturerName());
		assertEquals("Skydio", drones.get(1).getManufacturerName());
	}

	/**
	 * Tests that readFromCSV() leaves the inventory empty when the file does not
	 * exist.
	 */
	@Test
	void test_readFromCSV_file_not_found_leaves_inventory_empty() {
		hangar.readFromCSV("file_does_not_exist.csv");

		ArrayList<Drone> drones = hangar.getAllDrones();

		assertNotNull(drones);
		assertTrue(drones.isEmpty());
	}

	// ============================================================
	// Drone superclass method tests through concrete subclasses
	// ============================================================

	/**
	 * Tests that getManufacturerName() returns the drone manufacturer.
	 */
	@Test
	void test_Drone_getManufacturerName_returns_manufacturer() {
		Drone drone = new StandardDrone("D1000", "DJI", 2021, 12.5);

		assertEquals("DJI", drone.getManufacturerName());
	}

	/**
	 * Tests that setManufacturerName() updates the drone manufacturer.
	 */
	@Test
	void test_Drone_setManufacturerName_updates_manufacturer() {
		Drone drone = new StandardDrone("D1000", "DJI", 2021, 12.5);

		drone.setManufacturerName("Skydio");

		assertEquals("Skydio", drone.getManufacturerName());
	}

	/**
	 * Tests that getManufacturedYear() returns the drone manufacturing year.
	 */
	@Test
	void test_Drone_getManufacturedYear_returns_year() {
		Drone drone = new StandardDrone("D1000", "DJI", 2021, 12.5);

		assertEquals(2021, drone.getManufacturedYear());
	}

	/**
	 * Tests that getPayloadKg() returns the drone payload capacity.
	 */
	@Test
	void test_Drone_getPayloadKg_returns_payload() {
		Drone drone = new StandardDrone("D1000", "DJI", 2021, 12.5);

		assertEquals(12.5, drone.getPayloadKg());
	}

	/**
	 * Tests that getDroneId() returns the drone ID.
	 */
	@Test
	void test_Drone_getDroneId_returns_id() {
		Drone drone = new StandardDrone("D1000", "DJI", 2021, 12.5);

		assertEquals("D1000", drone.getDroneId());
	}

	/**
	 * Tests that setDroneId() updates the drone ID.
	 */
	@Test
	void test_Drone_setDroneId_updates_id() {
		Drone drone = new StandardDrone("D1000", "DJI", 2021, 12.5);

		drone.setDroneId("D2000");

		assertEquals("D2000", drone.getDroneId());
	}

	// ============================================================
	// PriorityDrone class tests
	// ============================================================

	/**
	 * Tests that the PriorityDrone constructor sets all inherited field values.
	 */
	@Test
	void test_PriorityDrone_constructor_sets_all_fields() {
		PriorityDrone drone = new PriorityDrone("D1000", "Skydio", 2024, 18.0);

		assertEquals("D1000", drone.getDroneId());
		assertEquals("Skydio", drone.getManufacturerName());
		assertEquals(2024, drone.getManufacturedYear());
		assertEquals(18.0, drone.getPayloadKg());
	}

	/**
	 * Tests that PriorityDrone toString() returns the required display format.
	 */
	@Test
	void test_PriorityDrone_toString_returns_expected_format() {
		PriorityDrone drone = new PriorityDrone("D1000", "Skydio", 2024, 18.0);

		String expected = "D1000 | Priority Drone - Skydio | Year: 2024 | Payload: 18.0 kg";

		assertEquals(expected, drone.toString());
	}

	// ============================================================
	// StandardDrone class tests
	// ============================================================

	/**
	 * Tests that the StandardDrone constructor sets all inherited field values.
	 */
	@Test
	void test_StandardDrone_constructor_sets_all_fields() {
		StandardDrone drone = new StandardDrone("D1000", "DJI", 2021, 12.5);

		assertEquals("D1000", drone.getDroneId());
		assertEquals("DJI", drone.getManufacturerName());
		assertEquals(2021, drone.getManufacturedYear());
		assertEquals(12.5, drone.getPayloadKg());
	}

	/**
	 * Tests that StandardDrone toString() returns the required display format.
	 */
	@Test
	void test_StandardDrone_toString_returns_expected_format() {
		StandardDrone drone = new StandardDrone("D1000", "DJI", 2021, 12.5);

		String expected = "D1000 | Standard Drone - DJI | Year: 2021 | Payload: 12.5 kg";

		assertEquals(expected, drone.toString());
	}
}