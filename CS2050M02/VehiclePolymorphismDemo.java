/**
 * Explore, debug to understand the concepts
 * Add comments explaining
 * Polymorphism with the Arraylist collection 
 */

import java.util.ArrayList;

public class VehiclePolymorphismDemo
{

	public static void main(String[] args)
	{
		ArrayList<Vehicle> fleet = new ArrayList<>();

		fleet.add(new ElectricCar("Hyundai Ioniq5", 60));
		fleet.add(new GasCar("Honda Civic", 50));
		fleet.add(new HybridCar("Toyota Prius", 55));

		System.out.println("=== Fleet Demonstration ===\n");

		for (Vehicle car : fleet)
		{

			System.out.println("Vehicle: " + car.getBrand());

			car.accelerate(5);
			car.refuel();

			System.out.println();
		}
	}
}

abstract class Vehicle
{

	private String brand;
	private int speed;

	public Vehicle(String brand, int speed)
	{
		this.brand = brand;
		this.speed = speed;
	}

	public String getBrand()
	{
		return brand;
	}

	public int getSpeed()
	{
		return speed;
	}

	public void accelerate(int increase)
	{
		speed += increase;
		System.out.println(brand + " accelerates to " + speed + " mph.");
	}

	public abstract void refuel();
}

interface Electric
{
	void chargeBattery();
}

interface GasPowered
{
	void refuelGas();
}

class ElectricCar extends Vehicle implements Electric
{

	public ElectricCar(String brand, int speed)
	{
		super(brand, speed);
	}

	@Override
	public void chargeBattery()
	{
		System.out.println("Charging battery for " + getBrand());
	}

	@Override
	public void refuel()
	{
		chargeBattery();
	}
}

class GasCar extends Vehicle implements GasPowered
{

	public GasCar(String brand, int speed)
	{
		super(brand, speed);
	}

	@Override
	public void refuelGas()
	{
		System.out.println("Refueling gas tank for " + getBrand());
	}

	@Override
	public void refuel()
	{
		refuelGas();
	}
}

class HybridCar extends Vehicle implements Electric, GasPowered
{

	public HybridCar(String brand, int speed)
	{
		super(brand, speed);
	}

	@Override
	public void chargeBattery()
	{
		System.out.println("Charging battery for " + getBrand());
	}

	@Override
	public void refuelGas()
	{
		System.out.println("Refueling gas tank for " + getBrand());
	}

	@Override
	public void refuel()
	{

		System.out.println(getBrand() + " can use both charging and gas.");

		chargeBattery();
		refuelGas();
	}
}