
/**
 * Update code and add comments explaining concepts 
 */

import java.util.ArrayList;
import java.util.Iterator;

public class PetAdoptionCenter {
	public static void main(String[] args) {
		// ============================================
		// Part 1: Setup an ArrayList of interface type
		// ============================================
		ArrayList<Pet> pets = new ArrayList<>();

		// Add some example pets
		pets.add(new Bulldog("Bear"));
		pets.add(new Cat("Mittens"));

		// Task 1:
		// Add at least one more Pet type class (example: Parrot)
		// Then add at least one object of that type to the list.
		// pets.add(new Parrot("Rio"));

		pets.add(new Monkey("Bart"));
		// ============================================
		// Part 2: Explain Polymorphic behavior with the interface
		// Each class has its own version of beFriendly() and play(), due to
		// polymorphism and abstract method implementation
		// They all implement the same methods, but the output differs based on the
		// subclass calling the method
		// ============================================
		System.out.println("--- Meet Our Pets ---");
		for (int i = 0; i < pets.size(); i++) {
			Pet currentPet = pets.get(i);
			currentPet.beFriendly();
			currentPet.play();
		}

		// ============================================
		// Part 3: Shared behavior through the interface
		// ============================================
		System.out.println("\n--- Snack Time ---");
		feedAll(pets);

		// ============================================
		// Part 4: ArrayList operations
		// ============================================
		System.out.println("\n--- Adoption Updates ---");
		System.out.println("Total pets before adoption: " + pets.size());

		if (pets.size() > 0) {
			Pet adopted = pets.remove(0);
			System.out.println("Adopted out: " + adopted);
		}

		System.out.println("Total pets after adoption: " + pets.size());

		// ============================================
		// Part 5: Search
		// ============================================
		// Call findByName and print the result
		Pet foundPet = findByName(pets, "Bart");
		System.out.println("Found pet: " + foundPet);

		// ============================================
		// Part 6: Remove by name
		// ============================================
		// Call removeByName and print the result
		String nameToRemove = "Bart";
		Boolean removed = removeByName(pets, nameToRemove);
		System.out.println(nameToRemove + " removed: " + removed);
		// ============================================
		// Part 7: Favorites list
		// ============================================
		// Build a list of pets whose names start with 'M'
		// Print the favorites list
	}

	private static void feedAll(ArrayList<Pet> pets) {
		for (int i = 0; i < pets.size(); i++) {
			pets.get(i).eat();
		}
	}

	public static Pet findByName(ArrayList<Pet> pets, String nameToFind) {
		Pet foundPet = null;
		for (Pet pet : pets) {
			if (pet.getName().equalsIgnoreCase(nameToFind)) {
				foundPet = pet;
			}
		}
		return foundPet;
	}

	public static boolean removeByName(ArrayList<Pet> pets, String nameToRemove) {
		boolean petRemoved = false;
		petRemoved = pets.remove(findByName(pets, nameToRemove));
		return petRemoved;
	}

	public static ArrayList<Pet> buildFavoritesStartingWith(ArrayList<Pet> pets, char letter) {
		// TODO:
		// Create a new ArrayList<Pet>
		// Add pets whose names start with the given letter
		// Comparison should be case-insensitive
		return null;
	}
}

// ============================================
// Abstract superclass
// ============================================
abstract class Animal {
	private String name;

	public Animal(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

// ============================================
// Interface
// ============================================
interface Pet {
	void beFriendly();

	void play();

	void eat();

	String getName();
}

// ============================================
// Concrete classes
// ============================================
class Bulldog extends Animal implements Pet {
	public Bulldog(String name) {
		super(name);
	}

	@Override
	public void beFriendly() {
		System.out.println(getName() + " wags tail and leans on your leg.");
	}

	@Override
	public void play() {
		System.out.println(getName() + " plays tug-of-war.");
	}

	@Override
	public void eat() {
		System.out.println(getName() + " munches crunchy kibble.");
	}

	@Override
	public String toString() {
		return "Bulldog(" + getName() + ")";
	}
} // end of BullDog class

class Cat extends Animal implements Pet {
	public Cat(String name) {
		super(name);
	}

	@Override
	public void beFriendly() {
		System.out.println(getName() + " purrs and headbutts your hand.");
	}

	@Override
	public void play() {
		System.out.println(getName() + " chases a laser pointer.");
	}

	@Override
	public void eat() {
		System.out.println(getName() + " nibbles salmon pate.");
	}

	@Override
	public String toString() {
		return "Cat(" + getName() + ")";
	}
} // end of Cat class

// Task:
// Add one more class such as Parrot that extends Animal and implements Pet.
class Monkey extends Animal implements Pet {

	public Monkey(String name) {
		super(name);
	}

	@Override
	public void beFriendly() {
		System.out.println(getName() + " laughs and hugs you.");
	}

	@Override
	public void play() {
		System.out.println(getName() + " swings from the bars of the enclosure.");
	}

	@Override
	public void eat() {
		System.out.println(getName() + " devours a banana.");
	}

	@Override
	public String toString() {
		return "Monkey(" + getName() + ")";
	}
} // end of Monkey class