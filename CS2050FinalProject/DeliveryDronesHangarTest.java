import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryDronesHangarTest {
	
	private Hangar hangar;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		hangar = new Hangar();
	}

	@AfterEach
	void tearDown() throws Exception {
		hangar = null;
	}

	@Test
	void test_new_Hangar_starts_empty() {
		ArrayList<Drone> drones = hangar.getAllDrones();
		
		assertNotNull(drones);
		assertTrue(drones.isEmpty());
	}
	
	@Test
	void test_add_Drone_adds_drone_to_inventory() {
		Drone drone = new StandardDrone("D1000", "DJI", 2021, 12.5);
		
		hangar.addDrone(drone);
		
		ArrayList<Drone> drones = hangar.getAllDrones();
		
		assertEquals(1, drones.size());
		assertEquals("D1000", drones.get(0).getDroneId());
		assertEquals("DJI", drones.get(0).getManufacturerName());
	}

}
