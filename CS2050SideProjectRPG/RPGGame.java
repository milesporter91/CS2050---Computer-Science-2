import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class RPGGame {

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);

		kb.close();
	}

} // end of RPGGame class

class GameMaster {
	// instance variables
	private ArrayList<GameCharacter> characters = new ArrayList<>();
	private HashMap<String, GameCharacter> characterMap = new HashMap<String, GameCharacter>();
	private Queue<GameCharacter> initiativeQueue = new LinkedList<>();

	// instance methods

	public void printMenu() {

	} // end of printMenu()

	public void loadCharactersFromCSV(String fileName) {

	} // end of loadCharactersFromCSV()

	public void saveCharactersToCSV(String fileName) {

	} // end of saveCharactersToCSV()

	public void displayAllCharacters() {

	} // end of displayAllCharacters()

	public void displayCharacterById(String id) {

	} // end of displayCharacterById()

	public GameCharacter findCharacterById(String id) {
		return null;
	} // end of findCharacterById()

	public void addCharacter(GameCharacter character) {

	} // end of addCharacter()

	public void removeCharacterById(String id) {

	} // end of removeCharacterById()

	public void startWaveMode(String playerId) {

	} // end of startWaveMode()

	public ArrayList<GameCharacter> generateWave(GameCharacter player, int waveNumber) {
		return null;
	} // end of generateWave()

	public void buildInitiativeQueue(ArrayList<GameCharacter> combatants) {

	} // end of buildInitiativeQueue()

	public void processBattleTurn(GameCharacter current, GameCharacter target) {

	} // end of processBattleTurn()

	public BossEnemy generateBoss(int waveNumber, int playerLevel) {
		return null;
	} // end of generateBoss()

	public HordeEnemy generateHordeEnemy(int waveNumber, int playerLevel) {
		return null;
	} // end of generateHordeEnemy()

} // end of GameMaster class

abstract class GameCharacter {
	// instance variables
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

	// Constructors

	public GameCharacter(String name, String species, int level, String id) {
		this.id = id;
		this.name = name;
		this.species = species;
		this.level = level;
	}

	public GameCharacter(String name, String species, int level) {
		this.id = generateId();
		this.name = name;
		this.species = species;
		this.level = level;
	} // end of Constructors

	// instance methods

	public static void syncNextId(String id) {
		if (id != null && id.length() > 1 && id.startsWith("C")) {
			int loadedIdNumber = Integer.parseInt(id.substring(1));
			if (loadedIdNumber >= nextId) {
				nextId = loadedIdNumber + 1;
			}
		}
	}

	private String generateId() {
		String characterID = "C" + nextId;
		nextId++;
		return characterID;
	} // end of generateId()

	public abstract int calculateMaxHealthPoints();

	public abstract int calculateArmorClass();

	public abstract double calculateStartingXP();

	public abstract int calculateSpeedBonus();

	public void rollInitiative() {

	} // end of rollInitiative()

	public void resetForNewRun() {

	} // end of resetForNewRun()

	public void selfHeal() {

	} // end of selfHeal()

	public void takeDamage(int amount) {

	} // end of takeDamage()

	public boolean isAlive() {
		return true;
	} // end of isAlive()

	public void gainExperience(double amount) {

	} // end of gainExperience()

	public void checkLevelUp() {

	} // end of checkLevelUp()

	public abstract void attack(GameCharacter target);

	public abstract void useSpecialAbility(GameCharacter target);

	public abstract String getCharacterClassName();

	// Getters & Setters

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

	public int getMaxWaveReached() {
		return maxWaveReached;
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
	// Constructors

	public Fighter(String name, String species, int level, String id) {
		super(name, species, level, id);
		calculateArmorClass();
		calculateMaxHealthPoints();
		calculateSpeedBonus();
		calculateStartingXP();
	}

	public Fighter(String name, String species, int level) {
		super(name, species, level);
		// TODO Auto-generated constructor stub
	} // end of Constructors

	// instance methods

	public int calculateMaxHealthPoints() {
		return 0;
	} // end of calculateMaxHealthPoints()

	public int calculateArmorClass() {
		return 0;
	} // end of calculateArmorClass()

	public int calculateSpeedBonus() {
		return 0;
	} // end of calculateSpeedBonus()

	public double calculateStartingXP() {
		return 0.0;
	} // end of calculateStartingXP()

	public void attack(GameCharacter target) {

	} // end of attack()

	public void useSpecialAbility(GameCharacter target) {

	} // end of useSpecialAbility()

	public String getCharacterClassName() {
		return "";
	} // end of getCharacterClassName()

	@Override
	public String toString() {
		return "";
	} // end of toString()

} // end of Fighter class

class Wizard extends GameCharacter {
	// Constructors

	public Wizard(String name, String species, int level, String id) {
		super(name, species, level, id);
	}

	public Wizard(String name, String species, int level) {
		super(name, species, level);
		// TODO Auto-generated constructor stub
	} // end of Constructors

	// instance methods

	public int calculateMaxHealthPoints() {
		return 0;
	} // end of calculateMaxHealthPoints()

	public int calculateArmorClass() {
		return 0;
	} // end of calculateArmorClass()

	public int calculateSpeedBonus() {
		return 0;
	} // end of calculateSpeedBonus()

	public double calculateStartingXP() {
		return 0.0;
	} // end of calculateStartingXP()

	public void attack(GameCharacter target) {

	} // end of attack()

	public void useSpecialAbility(GameCharacter target) {

	} // end of useSpecialAbility()

	public String getCharacterClassName() {
		return "";
	} // end of getCharacterClassName()

	@Override
	public String toString() {
		return "";
	} // end of toString()

} // end of Wizard class

abstract class Enemy extends GameCharacter {
	// instance variables
	private int waveNumber;

	// Constructors

	public Enemy(String name, String species, int level, int waveNumber) {
		super(name, species, level);
		// TODO Auto-generated constructor stub
	} // end of Constructor

	public abstract void attack(GameCharacter target);

	public int getWaveNumber() {
		return this.waveNumber;
	}

	public abstract String getCharacterClassName();

} // end of abstract Enemy class

class HordeEnemy extends Enemy {
	// Constructors
	public HordeEnemy(String name, String species, int level, int waveNumber) {
		super(name, species, level, waveNumber);
		// TODO Auto-generated constructor stub
	} // end of Constructor

	// instance methods

	public int calculateMaxHealthPoints() {
		return 0;
	} // end of calculateMaxHealthPoints()

	public int calculateArmorClass() {
		return 0;
	} // end of calculateArmorClass()

	public int calculateSpeedBonus() {
		return 0;
	} // end of calculateSpeedBonus()

	public double calculateStartingXP() {
		return 0.0;
	} // end of calculateStartingXP()

	public void attack(GameCharacter target) {

	} // end of attack()

	public void useSpecialAbility(GameCharacter target) {

	} // end of useSpecialAbility()

	public String getCharacterClassName() {
		return "";
	} // end of getCharacterClassName()

	@Override
	public String toString() {
		return "";
	} // end of toString()

} // end of HordeEnemy class

class BossEnemy extends Enemy implements StrategicEnemy {
	// instance variables
	private String introLine;
	private String lowHealthLine;
	private String defeatLine;
	private double healThreshold;

	// Constructors
	public BossEnemy(String name, String species, int level, int waveNumber) {
		super(name, species, level, waveNumber);
		// TODO Auto-generated constructor stub
	} // end of Constructor

	// instance methods

	public int calculateMaxHealthPoints() {
		return 0;
	} // end of calculateMaxHealthPoints()

	public int calculateArmorClass() {
		return 0;
	} // end of calculateArmorClass()

	public int calculateSpeedBonus() {
		return 0;
	} // end of calculateSpeedBonus()

	public double calculateStartingXP() {
		return 0.0;
	} // end of calculateStartingXP()

	public String chooseAction(GameCharacter target) {
		return "";
	} // end of chooseAction()

	public void speakIntro() {

	} // end of speakIntro()

	public void speakLowHealth() {

	} // end of speakLowHealth()

	public void speakDefeat() {

	} // end of speakDefeat()

	public void attack(GameCharacter target) {

	} // end of attack()

	public void useSpecialAbility(GameCharacter target) {

	} // end of useSpecialAbility()

	public void selfHeal() {

	} // end of selfHeal()

	public String getCharacterClassName() {
		return "";
	} // end of getCharacterClassName()

	@Override
	public String toString() {
		return "";
	} // end of toString()

} // end of BossEnemy class

interface StrategicEnemy {
	public String chooseAction(GameCharacter target);
} // end of StrategicEnemy interface

class DiceRoller {
	public static int rollD20() {
		return 0;
	} // end of rollD20()

	public static int rollDie(int sides) {
		return 0;
	}

	public static int rollMultipleDice(int numberOfDice, int sides) {
		return 0;
	} // end of rollMultipleDice()

	public static int rollDamage(int numberOfDice, int sides, int bonus) {
		return 0;
	} // end of rollDamage()
}
