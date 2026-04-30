package solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class RPGGameSolution {

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		GameMaster gm = new GameMaster(kb);
		boolean running = true;

		while (running) {
			gm.printMenu();

			if (!kb.hasNextInt()) {
				System.out.println("Invalid input. Enter an integer.");
				kb.nextLine();
				continue;
			}

			int choice = kb.nextInt();
			kb.nextLine();

			switch (choice) {
			case 1:
				System.out.print("Enter CSV file name to load: ");
				String loadFile = kb.nextLine();
				gm.loadCharactersFromCSV(loadFile);
				break;
			case 2:
				gm.displayAllCharacters();
				break;
			case 3:
				System.out.print("Enter character ID: ");
				String id = kb.nextLine();
				gm.displayCharacterById(id);
				break;
			case 4:
				gm.createCharacter();
				break;
			case 5:
				System.out.print("Enter player character ID for wave mode: ");
				String playerId = kb.nextLine();
				gm.startWaveMode(playerId);
				break;
			case 6:
				System.out.print("Enter CSV file name to save: ");
				String saveFile = kb.nextLine();
				gm.saveCharactersToCSV(saveFile);
				break;
			case 7:
				System.out.print("Enter character ID to remove: ");
				String removeId = kb.nextLine();
				gm.removeCharacterById(removeId);
				break;
			case 8:
				System.out.println("Exiting program. Goodbye!");
				running = false;
				break;
			default:
				System.out.println("Invalid choice. Choose a menu option between 1-8.");
			}
		}

		kb.close();
	}

} // end of RPGGame class

class GameMaster {
	private ArrayList<GameCharacter> characters = new ArrayList<>();
	private HashMap<String, GameCharacter> characterMap = new HashMap<>();
	private Queue<GameCharacter> initiativeQueue = new LinkedList<>();
	private Scanner kb;

	public GameMaster(Scanner kb) {
		this.kb = kb;
	}

	public void printMenu() {
		System.out.println("\n=== RPG Menu ===");
		System.out.println("1. Load Characters from CSV");
		System.out.println("2. Display All Characters");
		System.out.println("3. Display Character by ID");
		System.out.println("4. Create Character");
		System.out.println("5. Start Wave Mode");
		System.out.println("6. Save Characters to CSV");
		System.out.println("7. Remove Character by ID");
		System.out.println("8. Exit");
		System.out.print("Enter your choice (1-8): ");
	}

	public void createCharacter() {
		System.out.print("Enter name: ");
		String name = kb.nextLine().trim();

		System.out.print("Enter species: ");
		String species = kb.nextLine().trim();

		System.out.print("Enter class (Fighter/Wizard): ");
		String classType = kb.nextLine().trim();

		System.out.print("Enter level: ");
		if (!kb.hasNextInt()) {
			System.out.println("Invalid level.");
			kb.nextLine();
			return;
		}
		int level = kb.nextInt();
		kb.nextLine();

		if (name.isEmpty() || species.isEmpty() || level < 1) {
			System.out.println("Invalid character data.");
			return;
		}

		GameCharacter character;
		if (classType.equalsIgnoreCase("Fighter")) {
			character = new Fighter(name, species, level);
		} else if (classType.equalsIgnoreCase("Wizard")) {
			character = new Wizard(name, species, level);
		} else {
			System.out.println("Invalid class. Use Fighter or Wizard.");
			return;
		}

		addCharacter(character);
		System.out.println("Created character: " + character);
	}

	public void loadCharactersFromCSV(String fileName) {
		int lineNumber = 0;
		int loadedCount = 0;

		try (Scanner fileScanner = new Scanner(new File(fileName))) {
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine().trim();
				lineNumber++;

				if (line.isEmpty()) {
					continue;
				}

				String[] data = line.split(",");
				if (data.length != 7) {
					System.out.println("Skipping line " + lineNumber + ": incorrect number of fields.");
					continue;
				}

				String id = data[0].trim();
				String name = data[1].trim();
				String species = data[2].trim();
				String classType = data[3].trim();
				String levelString = data[4].trim();
				String xpString = data[5].trim();
				String maxWaveString = data[6].trim();

				if (id.isEmpty() || name.isEmpty() || species.isEmpty() || classType.isEmpty()) {
					System.out.println("Skipping line " + lineNumber + ": missing required field.");
					continue;
				}

				int level;
				double xp;
				int maxWave;

				try {
					level = Integer.parseInt(levelString);
					xp = Double.parseDouble(xpString);
					maxWave = Integer.parseInt(maxWaveString);
				} catch (NumberFormatException e) {
					System.out.println("Skipping line " + lineNumber + ": invalid numeric value.");
					continue;
				}

				if (level < 1 || xp < 0 || maxWave < 0) {
					System.out.println("Skipping line " + lineNumber + ": invalid level/xp/maxWave.");
					continue;
				}

				GameCharacter character = null;

				if (classType.equalsIgnoreCase("Fighter")) {
					character = new Fighter(name, species, level, id);
				} else if (classType.equalsIgnoreCase("Wizard")) {
					character = new Wizard(name, species, level, id);
				} else {
					System.out.println("Skipping line " + lineNumber + ": invalid class type.");
					continue;
				}

				character.setExperiencePoints(xp);
				character.setMaxWaveReached(maxWave);
				GameCharacter.syncNextId(id);
				addCharacter(character);
				loadedCount++;
			}

			System.out.println("Loaded " + loadedCount + " characters from " + fileName + ".");

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
		}
	}

	public void saveCharactersToCSV(String fileName) {
		try (PrintWriter writer = new PrintWriter(fileName)) {
			for (GameCharacter character : characters) {
				writer.println(character.toCSV());
			}
			System.out.println("Characters saved to " + fileName + ".");
		} catch (FileNotFoundException e) {
			System.out.println("Could not save file: " + fileName);
		}
	}

	public void displayAllCharacters() {
		if (characters.isEmpty()) {
			System.out.println("No characters available.");
			return;
		}

		System.out.println("\n=== Character Roster ===");
		for (GameCharacter character : characters) {
			System.out.println(character);
		}
	}

	public void displayCharacterById(String id) {
		GameCharacter character = findCharacterById(id);
		if (character == null) {
			System.out.println("Character not found.");
		} else {
			System.out.println(character.detailedString());
		}
	}

	public GameCharacter findCharacterById(String id) {
		return characterMap.get(id);
	}

	public void addCharacter(GameCharacter character) {
		if (character == null) {
			return;
		}

		if (characterMap.containsKey(character.getId())) {
			System.out.println("Character with ID " + character.getId() + " already exists.");
			return;
		}

		characters.add(character);
		characterMap.put(character.getId(), character);
	}

	public void removeCharacterById(String id) {
		GameCharacter character = findCharacterById(id);
		if (character == null) {
			System.out.println("Character not found.");
			return;
		}

		characters.remove(character);
		characterMap.remove(id);
		System.out.println("Removed character " + id + ".");
	}

	public void startWaveMode(String playerId) {
		GameCharacter player = findCharacterById(playerId);

		if (player == null) {
			System.out.println("Character not found.");
			return;
		}

		if (player instanceof Enemy) {
			System.out.println("Only player characters can enter wave mode.");
			return;
		}

		player.resetForNewRun();
		int waveNumber = 1;
		double runXP = 0.0;
		boolean continueRun = true;

		System.out.println("\n=== Wave Mode Started ===");
		System.out.println("Player: " + player);

		while (continueRun && player.isAlive()) {
			System.out.println("\n--- Wave " + waveNumber + " ---");
			ArrayList<GameCharacter> enemies = generateWave(player, waveNumber);

			if (!runBattle(player, enemies, waveNumber)) {
				break;
			}

			player.setMaxWaveReached(Math.max(player.getMaxWaveReached(), waveNumber));

			double waveReward = calculateWaveReward(waveNumber);
			runXP += waveReward;
			System.out.println("Wave cleared! Earned " + waveReward + " run XP.");
			System.out.println("Run XP banked if you cash out now: " + runXP);

			System.out.print("Continue to next wave? (Y/N): ");
			String answer = kb.nextLine().trim();

			if (!answer.equalsIgnoreCase("Y")) {
				player.gainExperience(runXP);
				System.out.println("You cashed out and kept " + runXP + " XP.");
				player.checkLevelUp();
				continueRun = false;
			} else {
				waveNumber++;
			}
		}

		if (!player.isAlive()) {
			double partialXP = runXP / 2.0;
			player.gainExperience(partialXP);
			System.out.println("\nYou died. You keep half your run XP: " + partialXP);
			player.checkLevelUp();
		}

		player.resetForNewRun();
		System.out.println("Wave mode ended.");
	}

	private boolean runBattle(GameCharacter player, ArrayList<GameCharacter> enemies, int waveNumber) {
		ArrayList<GameCharacter> combatants = new ArrayList<>();
		combatants.add(player);
		combatants.addAll(enemies);

		if (!enemies.isEmpty() && enemies.get(0) instanceof BossEnemy) {
			((BossEnemy) enemies.get(0)).speakIntro();
		}

		buildInitiativeQueue(combatants);

		while (player.isAlive() && hasLivingEnemies(enemies)) {
			GameCharacter current = initiativeQueue.poll();

			if (current == null) {
				break;
			}

			if (!current.isAlive()) {
				continue;
			}

			GameCharacter target = chooseTarget(current, player, enemies);
			if (target == null) {
				break;
			}

			processBattleTurn(current, target);

			if (current.isAlive()) {
				initiativeQueue.offer(current);
			}
		}

		if (!player.isAlive()) {
			System.out.println("Your character was defeated on wave " + waveNumber + ".");
			return false;
		}

		System.out.println("You defeated all enemies in wave " + waveNumber + "!");
		return true;
	}

	private boolean hasLivingEnemies(ArrayList<GameCharacter> enemies) {
		for (GameCharacter enemy : enemies) {
			if (enemy.isAlive()) {
				return true;
			}
		}
		return false;
	}

	private GameCharacter chooseTarget(GameCharacter current, GameCharacter player, ArrayList<GameCharacter> enemies) {
		if (current instanceof Enemy) {
			return player.isAlive() ? player : null;
		}

		for (GameCharacter enemy : enemies) {
			if (enemy.isAlive()) {
				return enemy;
			}
		}
		return null;
	}

	public ArrayList<GameCharacter> generateWave(GameCharacter player, int waveNumber) {
		ArrayList<GameCharacter> waveEnemies = new ArrayList<>();

		if (waveNumber % 5 == 0) {
			waveEnemies.add(generateBoss(waveNumber, player.getLevel()));
		} else {
			int enemyCount = Math.min(1 + ((waveNumber - 1) / 2), 4);
			for (int i = 0; i < enemyCount; i++) {
				waveEnemies.add(generateHordeEnemy(waveNumber, player.getLevel()));
			}
		}

		System.out.println("Enemies this wave:");
		for (GameCharacter enemy : waveEnemies) {
			System.out.println(enemy);
		}

		return waveEnemies;
	}

	public void buildInitiativeQueue(ArrayList<GameCharacter> combatants) {
		initiativeQueue.clear();

		for (GameCharacter combatant : combatants) {
			combatant.rollInitiative();
		}

		combatants.sort(Comparator.comparingInt(GameCharacter::getInitiativeTotal).reversed());

		System.out.println("\nInitiative Order:");
		for (GameCharacter combatant : combatants) {
			System.out.println(combatant.getName() + " rolled " + combatant.getInitiativeRoll() + " + "
					+ combatant.getSpeedBonus() + " = " + combatant.getInitiativeTotal());
			initiativeQueue.offer(combatant);
		}
	}

	public void processBattleTurn(GameCharacter current, GameCharacter target) {
		System.out.println("\n" + current.getName() + "'s turn.");

		if (current instanceof BossEnemy) {
			BossEnemy boss = (BossEnemy) current;
			String action = boss.chooseAction(target);

			if (action.equalsIgnoreCase("HEAL")) {
				boss.selfHeal();
			} else if (action.equalsIgnoreCase("SPECIAL")) {
				boss.useSpecialAbility(target);
			} else {
				boss.attack(target);
			}
			return;
		}

		if (current instanceof Enemy) {
			current.attack(target);
			return;
		}

		boolean validChoice = false;
		while (!validChoice) {
			System.out.println("1. Basic Attack");
			System.out.println("2. Special Ability");
			System.out.println("3. Self Heal");
			System.out.print("Choose action: ");

			if (!kb.hasNextInt()) {
				System.out.println("Invalid input.");
				kb.nextLine();
				continue;
			}

			int choice = kb.nextInt();
			kb.nextLine();

			switch (choice) {
			case 1:
				current.attack(target);
				validChoice = true;
				break;
			case 2:
				current.useSpecialAbility(target);
				validChoice = true;
				break;
			case 3:
				current.selfHeal();
				validChoice = true;
				break;
			default:
				System.out.println("Invalid choice.");
			}
		}
	}

	public BossEnemy generateBoss(int waveNumber, int playerLevel) {
		int bossLevel = Math.max(1, playerLevel + (waveNumber / 3));
		String[] bossNames = { "Dread Knight", "Void Magus", "Blood Tyrant" };
		String bossName = bossNames[ThreadLocalRandom.current().nextInt(bossNames.length)];
		return new BossEnemy(bossName, "Boss", bossLevel, waveNumber);
	}

	public HordeEnemy generateHordeEnemy(int waveNumber, int playerLevel) {
		int enemyLevel = Math.max(1, playerLevel + ((waveNumber - 1) / 3));
		String[] names = { "Goblin", "Skeleton", "Bandit", "Wolf", "Cultist" };
		String enemyName = names[ThreadLocalRandom.current().nextInt(names.length)];
		return new HordeEnemy(enemyName, "Horde", enemyLevel, waveNumber);
	}

	private double calculateWaveReward(int waveNumber) {
		return 50.0 + (waveNumber * 20.0);
	}

} // end of GameMaster class

abstract class GameCharacter {
	private static int nextId = 1000;

	private String id;
	private String name;
	private String species;
	private int level;
	private int maxHealthPoints;
	private int currentHealthPoints;
	private int armorClass;
	private int speedBonus;
	private double experiencePoints;
	private int maxWaveReached;
	private int initiativeRoll;
	private int initiativeTotal;
	private int healUsesRemaining;

	public GameCharacter(String name, String species, int level, String id) {
		this.id = id;
		this.name = name;
		this.species = species;
		this.level = Math.max(1, level);
		GameCharacter.syncNextId(id);
	}

	public GameCharacter(String name, String species, int level) {
		this.id = generateId();
		this.name = name;
		this.species = species;
		this.level = Math.max(1, level);
	}

	protected void initializeDerivedStats() {
		this.maxHealthPoints = calculateMaxHealthPoints();
		this.currentHealthPoints = this.maxHealthPoints;
		this.armorClass = calculateArmorClass();
		this.speedBonus = calculateSpeedBonus();
		this.experiencePoints = calculateStartingXP();
		this.maxWaveReached = 0;
		this.initiativeRoll = 0;
		this.initiativeTotal = 0;
		this.healUsesRemaining = 0;
	}

	public static void syncNextId(String id) {
		if (id != null && id.length() > 1 && id.startsWith("C")) {
			try {
				int loadedIdNumber = Integer.parseInt(id.substring(1));
				if (loadedIdNumber >= nextId) {
					nextId = loadedIdNumber + 1;
				}
			} catch (NumberFormatException ignored) {
			}
		}
	}

	private String generateId() {
		String characterID = "C" + nextId;
		nextId++;
		return characterID;
	}

	public abstract int calculateMaxHealthPoints();

	public abstract int calculateArmorClass();

	public abstract double calculateStartingXP();

	public abstract int calculateSpeedBonus();

	public void rollInitiative() {
		this.initiativeRoll = DiceRoller.rollD20();
		this.initiativeTotal = this.initiativeRoll + this.speedBonus;
	}

	public void resetForNewRun() {
		this.currentHealthPoints = this.maxHealthPoints;
		this.healUsesRemaining = 2;
		this.initiativeRoll = 0;
		this.initiativeTotal = 0;
	}

	public void selfHeal() {
		if (healUsesRemaining <= 0) {
			System.out.println(name + " has no heals remaining.");
			return;
		}

		int healAmount = DiceRoller.rollDamage(1, 8, Math.max(1, level / 2));
		currentHealthPoints = Math.min(maxHealthPoints, currentHealthPoints + healAmount);
		healUsesRemaining--;
		System.out.println(
				name + " heals for " + healAmount + " HP. Current HP: " + currentHealthPoints + "/" + maxHealthPoints);
	}

	public void takeDamage(int amount) {
		if (amount < 0) {
			amount = 0;
		}
		currentHealthPoints -= amount;
		if (currentHealthPoints < 0) {
			currentHealthPoints = 0;
		}
		System.out.println(
				name + " takes " + amount + " damage. Current HP: " + currentHealthPoints + "/" + maxHealthPoints);
	}

	public boolean isAlive() {
		return currentHealthPoints > 0;
	}

	public void gainExperience(double amount) {
		if (amount > 0) {
			this.experiencePoints += amount;
		}
	}

	public void checkLevelUp() {
		int oldLevel = this.level;
		while (experiencePoints >= getRequiredXPForLevel(level + 1)) {
			level++;
		}

		if (level > oldLevel) {
			int oldMax = maxHealthPoints;
			maxHealthPoints = calculateMaxHealthPoints();
			armorClass = calculateArmorClass();
			speedBonus = calculateSpeedBonus();
			currentHealthPoints += (maxHealthPoints - oldMax);
			if (currentHealthPoints > maxHealthPoints) {
				currentHealthPoints = maxHealthPoints;
			}
			System.out.println(name + " leveled up! New level: " + level);
		}
	}

	private double getRequiredXPForLevel(int targetLevel) {
		return (targetLevel - 1) * 100.0;
	}

	protected boolean attemptHit(GameCharacter target, int attackBonus, String attackName) {
		int roll = DiceRoller.rollD20();
		int total = roll + attackBonus;
		System.out.println(name + " uses " + attackName + ". Attack roll: " + roll + " + " + attackBonus + " = " + total
				+ " vs AC " + target.getArmorClass());
		return total >= target.getArmorClass();
	}

	public abstract void attack(GameCharacter target);

	public abstract void useSpecialAbility(GameCharacter target);

	public abstract String getCharacterClassName();

	public String toCSV() {
		return id + "," + name + "," + species + "," + getCharacterClassName() + "," + level + "," + experiencePoints
				+ "," + maxWaveReached;
	}

	public String detailedString() {
		return id + " | " + getCharacterClassName() + " | Name: " + name + " | Species: " + species + " | Level: "
				+ level + " | HP: " + currentHealthPoints + "/" + maxHealthPoints + " | AC: " + armorClass
				+ " | Speed Bonus: " + speedBonus + " | XP: " + experiencePoints + " | Max Wave: " + maxWaveReached
				+ " | Heals Remaining: " + healUsesRemaining;
	}

	public int getMaxHealthPoints() {
		return maxHealthPoints;
	}

	public void setMaxHealthPoints(int maxHealthPoints) {
		this.maxHealthPoints = maxHealthPoints;
	}

	public int getCurrentHealthPoints() {
		return currentHealthPoints;
	}

	public void setCurrentHealthPoints(int currentHealthPoints) {
		this.currentHealthPoints = currentHealthPoints;
	}

	public static int getNextId() {
		return nextId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSpecies() {
		return species;
	}

	public int getLevel() {
		return level;
	}

	public int getArmorClass() {
		return armorClass;
	}

	public int getSpeedBonus() {
		return speedBonus;
	}

	public double getExperiencePoints() {
		return experiencePoints;
	}

	public void setExperiencePoints(double experiencePoints) {
		this.experiencePoints = Math.max(0.0, experiencePoints);
	}

	public int getMaxWaveReached() {
		return maxWaveReached;
	}

	public void setMaxWaveReached(int maxWaveReached) {
		this.maxWaveReached = Math.max(0, maxWaveReached);
	}

	public int getInitiativeRoll() {
		return initiativeRoll;
	}

	public int getInitiativeTotal() {
		return initiativeTotal;
	}

	public int getHealUsesRemaining() {
		return healUsesRemaining;
	}

	@Override
	public abstract String toString();

} // end of abstract GameCharacter class

class Fighter extends GameCharacter {

	public Fighter(String name, String species, int level, String id) {
		super(name, species, level, id);
		initializeDerivedStats();
	}

	public Fighter(String name, String species, int level) {
		super(name, species, level);
		initializeDerivedStats();
	}

	@Override
	public int calculateMaxHealthPoints() {
		return 25 + (getLevel() * 8);
	}

	@Override
	public int calculateArmorClass() {
		return 14 + (getLevel() / 3);
	}

	@Override
	public int calculateSpeedBonus() {
		return 2 + (getLevel() / 4);
	}

	@Override
	public double calculateStartingXP() {
		return (getLevel() - 1) * 100.0;
	}

	@Override
	public void attack(GameCharacter target) {
		int attackBonus = 4 + (getLevel() / 2);
		if (attemptHit(target, attackBonus, "Sword Slash")) {
			int damage = DiceRoller.rollDamage(1, 8, 3 + (getLevel() / 2));
			target.takeDamage(damage);
		} else {
			System.out.println(getName() + " misses.");
		}
	}

	@Override
	public void useSpecialAbility(GameCharacter target) {
		int attackBonus = 3 + (getLevel() / 2);
		if (attemptHit(target, attackBonus, "Power Strike")) {
			int damage = DiceRoller.rollDamage(2, 6, 4 + (getLevel() / 2));
			target.takeDamage(damage);
		} else {
			System.out.println(getName() + "'s Power Strike misses.");
		}
	}

	@Override
	public String getCharacterClassName() {
		return "Fighter";
	}

	@Override
	public String toString() {
		return getId() + " | Fighter | " + getName() + " | Lvl " + getLevel() + " | HP " + getCurrentHealthPoints()
				+ "/" + getMaxHealthPoints() + " | AC " + getArmorClass() + " | XP " + getExperiencePoints()
				+ " | Max Wave " + getMaxWaveReached();
	}

} // end of Fighter class

class Wizard extends GameCharacter {

	public Wizard(String name, String species, int level, String id) {
		super(name, species, level, id);
		initializeDerivedStats();
	}

	public Wizard(String name, String species, int level) {
		super(name, species, level);
		initializeDerivedStats();
	}

	@Override
	public int calculateMaxHealthPoints() {
		return 16 + (getLevel() * 5);
	}

	@Override
	public int calculateArmorClass() {
		return 11 + (getLevel() / 4);
	}

	@Override
	public int calculateSpeedBonus() {
		return 4 + (getLevel() / 3);
	}

	@Override
	public double calculateStartingXP() {
		return (getLevel() - 1) * 100.0;
	}

	@Override
	public void attack(GameCharacter target) {
		int attackBonus = 4 + (getLevel() / 2);
		if (attemptHit(target, attackBonus, "Arcane Bolt")) {
			int damage = DiceRoller.rollDamage(1, 6, 2 + (getLevel() / 2));
			target.takeDamage(damage);
		} else {
			System.out.println(getName() + "'s Arcane Bolt misses.");
		}
	}

	@Override
	public void useSpecialAbility(GameCharacter target) {
		int attackBonus = 5 + (getLevel() / 2);
		if (attemptHit(target, attackBonus, "Fireball")) {
			int damage = DiceRoller.rollDamage(2, 8, 2 + (getLevel() / 2));
			target.takeDamage(damage);
		} else {
			System.out.println(getName() + "'s Fireball misses.");
		}
	}

	@Override
	public String getCharacterClassName() {
		return "Wizard";
	}

	@Override
	public String toString() {
		return getId() + " | Wizard | " + getName() + " | Lvl " + getLevel() + " | HP " + getCurrentHealthPoints() + "/"
				+ getMaxHealthPoints() + " | AC " + getArmorClass() + " | XP " + getExperiencePoints() + " | Max Wave "
				+ getMaxWaveReached();
	}

} // end of Wizard class

abstract class Enemy extends GameCharacter {
	private int waveNumber;

	public Enemy(String name, String species, int level, int waveNumber) {
		super(name, species, level);
		this.waveNumber = waveNumber;
	}

	public int getWaveNumber() {
		return waveNumber;
	}

	@Override
	public abstract void attack(GameCharacter target);

	@Override
	public abstract String getCharacterClassName();

} // end of abstract Enemy class

class HordeEnemy extends Enemy {

	public HordeEnemy(String name, String species, int level, int waveNumber) {
		super(name, species, level, waveNumber);
		initializeDerivedStats();
	}

	@Override
	public int calculateMaxHealthPoints() {
		return 10 + (getLevel() * 4) + getWaveNumber();
	}

	@Override
	public int calculateArmorClass() {
		return 10 + (getWaveNumber() / 3);
	}

	@Override
	public int calculateSpeedBonus() {
		return 1 + (getWaveNumber() / 4);
	}

	@Override
	public double calculateStartingXP() {
		return 0.0;
	}

	@Override
	public void attack(GameCharacter target) {
		int attackBonus = 2 + (getLevel() / 2);
		if (attemptHit(target, attackBonus, "Claw Swipe")) {
			int damage = DiceRoller.rollDamage(1, 6, 1 + (getWaveNumber() / 3));
			target.takeDamage(damage);
		} else {
			System.out.println(getName() + " misses.");
		}
	}

	@Override
	public void useSpecialAbility(GameCharacter target) {
		attack(target);
	}

	@Override
	public String getCharacterClassName() {
		return "HordeEnemy";
	}

	@Override
	public String toString() {
		return getId() + " | Horde Enemy | " + getName() + " | Wave " + getWaveNumber() + " | Lvl " + getLevel()
				+ " | HP " + getCurrentHealthPoints() + "/" + getMaxHealthPoints() + " | AC " + getArmorClass();
	}

} // end of HordeEnemy class

class BossEnemy extends Enemy implements StrategicEnemy {
	private String introLine;
	private String lowHealthLine;
	private String defeatLine;
	private double healThreshold;

	public BossEnemy(String name, String species, int level, int waveNumber) {
		super(name, species, level, waveNumber);
		this.healThreshold = 0.35;
		this.introLine = "You have carved through my minions, but your run ends here.";
		this.lowHealthLine = "I will not fall so easily!";
		this.defeatLine = "Impossible...";
		initializeDerivedStats();
	}

	@Override
	public int calculateMaxHealthPoints() {
		return 40 + (getLevel() * 8) + (getWaveNumber() * 2);
	}

	@Override
	public int calculateArmorClass() {
		return 13 + (getLevel() / 3);
	}

	@Override
	public int calculateSpeedBonus() {
		return 3 + (getLevel() / 3);
	}

	@Override
	public double calculateStartingXP() {
		return 0.0;
	}

	@Override
	public String chooseAction(GameCharacter target) {
		double healthRatio = (double) getCurrentHealthPoints() / getMaxHealthPoints();

		if (healthRatio <= healThreshold && getHealUsesRemaining() > 0) {
			if (ThreadLocalRandom.current().nextInt(100) < 60) {
				return "HEAL";
			}
		}

		if (ThreadLocalRandom.current().nextInt(100) < 40) {
			return "SPECIAL";
		}

		return "ATTACK";
	}

	public void speakIntro() {
		System.out.println(getName() + ": \"" + introLine + "\"");
	}

	public void speakLowHealth() {
		System.out.println(getName() + ": \"" + lowHealthLine + "\"");
	}

	public void speakDefeat() {
		System.out.println(getName() + ": \"" + defeatLine + "\"");
	}

	@Override
	public void attack(GameCharacter target) {
		int attackBonus = 4 + (getLevel() / 2);
		if (attemptHit(target, attackBonus, "Boss Strike")) {
			int damage = DiceRoller.rollDamage(2, 6, 2 + (getWaveNumber() / 2));
			target.takeDamage(damage);
			if (!target.isAlive()) {
				speakDefeat();
			}
		} else {
			System.out.println(getName() + " misses.");
		}
	}

	@Override
	public void useSpecialAbility(GameCharacter target) {
		if ((double) getCurrentHealthPoints() / getMaxHealthPoints() <= healThreshold) {
			speakLowHealth();
		}

		int attackBonus = 5 + (getLevel() / 2);
		if (attemptHit(target, attackBonus, "Dark Burst")) {
			int damage = DiceRoller.rollDamage(2, 8, 3 + (getWaveNumber() / 2));
			target.takeDamage(damage);
			if (!target.isAlive()) {
				speakDefeat();
			}
		} else {
			System.out.println(getName() + "'s Dark Burst misses.");
		}
	}

	@Override
	public void selfHeal() {
		System.out.println(getName() + " draws on dark power!");
		super.selfHeal();
	}

	@Override
	public String getCharacterClassName() {
		return "BossEnemy";
	}

	@Override
	public String toString() {
		return getId() + " | Boss Enemy | " + getName() + " | Wave " + getWaveNumber() + " | Lvl " + getLevel()
				+ " | HP " + getCurrentHealthPoints() + "/" + getMaxHealthPoints() + " | AC " + getArmorClass();
	}

} // end of BossEnemy class

interface StrategicEnemy {
	String chooseAction(GameCharacter target);
} // end of StrategicEnemy interface

class DiceRoller {
	public static int rollD20() {
		return ThreadLocalRandom.current().nextInt(1, 21);
	}

	public static int rollDie(int sides) {
		return ThreadLocalRandom.current().nextInt(1, sides + 1);
	}

	public static int rollMultipleDice(int numberOfDice, int sides) {
		int total = 0;
		for (int i = 0; i < numberOfDice; i++) {
			total += rollDie(sides);
		}
		return total;
	}

	public static int rollDamage(int numberOfDice, int sides, int bonus) {
		return rollMultipleDice(numberOfDice, sides) + bonus;
	}
}
