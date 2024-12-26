import java.util.*;

class Game {
    private Player player;
    private World world;
    private Scanner scanner;
    
    public Game() {
        this.player = new Player("Hero");
        this.world = new World();
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        System.out.println("Welcome to the Text-Based Adventure Game!");
        System.out.println("Type 'help' for a list of commands.");
        
        while (true) {
            System.out.println("\nWhat do you want to do?");
            String command = scanner.nextLine().toLowerCase().trim();
            
            if (command.equals("quit")) 
            {
                System.out.println("Thanks for playing!");
                break;
            } 
            else if (command.equals("help")) 
            {
                showHelp();
            } 
            else if (command.equals("look")) 
            {
                world.describeCurrentLocation();
            } 
            else if (command.equals("move")) 
            {
                movePlayer();
            } 
            else if (command.startsWith("attack")) 
            {
                attack(command);
            } 
            else if (command.equals("inventory")) 
            {
                player.showInventory();
            } 
            else if (command.equals("pickup")) 
            {
                pickupItem();
            } 
            else if (command.equals("use")) 
            {
                useItem();
            } 
            else if (command.equals("quests")) 
            {
                player.showQuests();
            } 
            else if (command.equals("status")) 
            {
                player.showStatus();
            } 
            else
            {
                System.out.println("Unknown command.");
            }
        }
    }

    private void showHelp() {
        System.out.println("Commands:");
        System.out.println("  look - Look around the current location.");
        System.out.println("  move - Move to a new location.");
        System.out.println("  attack <monster> - Attack a monster.");
        System.out.println("  inventory - Check your inventory.");
        System.out.println("  pickup - Pick up an item.");
        System.out.println("  use <item> - Use an item.");
        System.out.println("  quests - View active quests.");
        System.out.println("  status - View your character's stats.");
        System.out.println("  quit - Exit the game.");
    }

    private void movePlayer() {
        System.out.println("Where do you want to move? (north, south, east, west)");
        String direction = scanner.nextLine().toLowerCase().trim();
        
        if (world.movePlayer(player, direction)) {
            System.out.println("You moved " + direction + ".");
        } 
        else
        {
            System.out.println("You can't move in that direction.");
        }
    }

    private void attack(String command) 
    {
        String[] parts = command.split(" ");
        if (parts.length < 2) 
        {
            System.out.println("Attack who?");
            return;
        }
        
        String monsterName = parts[1];
        Monster monster = world.getMonsterAtLocation(monsterName);
        
        if (monster != null) 
        {
            player.attack(monster);
        } 
        else 
        {
            System.out.println("No such monster here.");
        }
    }

    private void pickupItem() {
        Item item = world.getItemAtLocation();
        if (item != null) {
            player.addItem(item);
            System.out.println("You picked up a " + item.getName() + ".");
        } else {
            System.out.println("No items to pick up here.");
        }
    }

    private void useItem() {
        System.out.println("Which item would you like to use?");
        String itemName = scanner.nextLine().toLowerCase().trim();
        
        Item item = player.getItem(itemName);
        if (item != null) 
        {
            player.useItem(item);
        } 
        else 
        {
            System.out.println("You don't have that item.");
        }
    }
}

class Player {
    private String name;
    private int health;
    private int attackPower;
    private int defense;
    private Inventory inventory;
    private List<Quest> quests;
    
    public Player(String name) {
        this.name = name;
        this.health = 100;
        this.attackPower = 10;
        this.defense = 5;
        this.inventory = new Inventory();
        this.quests = new ArrayList<>();
        
        // Adding a sample quest
        quests.add(new Quest("Find the lost sword", QuestStatus.ACTIVE));
    }

    public void attack(Monster monster) 
    {
        int damage = Math.max(0, attackPower - monster.getDefense());
        monster.takeDamage(damage);
        System.out.println(name + " attacks " + monster.getName() + " for " + damage + " damage.");
    }

    public void showInventory() 
    {
        inventory.showItems();
    }

    public void takeDamage(int damage) 
    {
        health -= Math.max(0, damage - defense);
        System.out.println(name + " takes " + damage + " damage.");
        if (health <= 0) 
        {
            System.out.println(name + " has died.");
        }
    }

    public void addItem(Item item) 
    {
        inventory.addItem(item);
    }

    public void useItem(Item item) 
    {
        System.out.println("Using item: " + item.getName());
        if (item.getName().equals("Health Potion")) 
        {
            health += 20;
            System.out.println("You gained 20 health.");
        }
        inventory.removeItem(item);
    }

    public void showQuests() {
        if (quests.isEmpty()) 
        {
            System.out.println("No active quests.");
        } 
        else 
        {
            for (Quest quest : quests) 
            {
                System.out.println(quest.getName() + " - Status: " + quest.getStatus());
            }
        }
    }

    public void showStatus() {
        System.out.println("Player: " + name);
        System.out.println("Health: " + health);
        System.out.println("Attack Power: " + attackPower);
        System.out.println("Defense: " + defense);
    }

    public String getName() 
    {
        return name;
    }

    public Item getItem(String itemName) 
    {
        return inventory.getItem(itemName);
    }
}

class Monster {
    private String name;
    private int health;
    private int attackPower;
    private int defense;
    
    public Monster(String name, int health, int attackPower, int defense) 
    {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
        this.defense = defense;
    }

    public String getName() 
    {
        return name;
    }

    public void takeDamage(int damage) 
    {
        health -= damage;
        if (health <= 0) 
        {
            System.out.println(name + " has been defeated.");
        }
    }

    public int getHealth() 
    {
        return health;
    }

    public int getAttackPower() 
    {
        return attackPower;
    }

    public int getDefense() 
    {
        return defense;
    }
}

class Item {
    private String name;
    private String description;
    
    public Item(String name, String description) 
    {
        this.name = name;
        this.description = description;
    }
    
    public String getName() 
    {
        return name;
    }
    
    public String getDescription() 
    {
        return description;
    }
}

class Quest {
    private String name;
    private QuestStatus status;
    
    public Quest(String name, QuestStatus status) 
    {
        this.name = name;
        this.status = status;
    }

    public String getName() 
    {
        return name;
    }

    public QuestStatus getStatus() 
    {
        return status;
    }

    public void complete() 
    {
        status = QuestStatus.COMPLETED;
        System.out.println(name + " has been completed!");
    }
}

enum QuestStatus {
    ACTIVE, COMPLETED;
}

class World {
    private Location currentLocation;
    
    public World() {
        this.currentLocation = new Location("Start Room", "A small room with a wooden door.");
        setupWorld();
    }

    private void setupWorld() {
        Location northRoom = new Location("North Room", "A cold, dark room with a flickering light.");
        Location southRoom = new Location("South Room", "A room with a large treasure chest in the corner.");
        Location eastRoom = new Location("East Room", "A bright, sunlit room with a beautiful view.");
        
        currentLocation.setAdjacentLocation("north", northRoom);
        northRoom.setAdjacentLocation("south", currentLocation);
        
        currentLocation.setAdjacentLocation("south", southRoom);
        southRoom.setAdjacentLocation("north", currentLocation);
        
        currentLocation.setAdjacentLocation("east", eastRoom);
        eastRoom.setAdjacentLocation("west", currentLocation);
        
        Monster goblin = new Monster("Goblin", 30, 5, 2);
        currentLocation.addMonster(goblin);
        
        Item sword = new Item("Sword", "A sharp sword that looks useful.");
        currentLocation.addItem(sword);

        Item healthPotion = new Item("Health Potion", "A potion that restores 20 health.");
        northRoom.addItem(healthPotion);
    }

    public void describeCurrentLocation() {
        System.out.println("You are in the " + currentLocation.getName());
        System.out.println(currentLocation.getDescription());
        if (currentLocation.hasMonsters()) 
        {
            System.out.println("There are monsters here!");
        }
        if (currentLocation.hasItems()) 
        {
            System.out.println("There is an item here to pick up.");
        }
    }

    public boolean movePlayer(Player player, String direction) 
    {
        Location nextLocation = currentLocation.getAdjacentLocation(direction);
        if (nextLocation != null) {
            currentLocation = nextLocation;
            return true;
        }
        return false;
    }

    public Monster getMonsterAtLocation(String monsterName) {
        return currentLocation.getMonster(monsterName);
    }

    public Item getItemAtLocation() {
        return currentLocation.getItem();
    }
}

class Location {
    private String name;
    private String description;
    private Map<String, Location> adjacentLocations;
    private Map<String, Monster> monsters;
    private Item item;
    
    public Location(String name, String description) {
        this.name = name;
        this.description = description;
        this.adjacentLocations = new HashMap<>();
        this.monsters = new HashMap<>();
    }

    public String getName() 
    {
        return name;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setAdjacentLocation(String direction, Location location) 
    {
        adjacentLocations.put(direction, location);
    }

    public Location getAdjacentLocation(String direction) 
    {
        return adjacentLocations.get(direction);
    }

    public void addMonster(Monster monster) 
    {
        monsters.put(monster.getName(), monster);
    }

    public Monster getMonster(String monsterName) 
    {
        return monsters.get(monsterName);
    }

    public boolean hasMonsters()
    {
        return !monsters.isEmpty();
    }

    public void addItem(Item item) 
    {
        this.item = item;
    }

    public boolean hasItems() 
    {
        return item != null;
    }

    public Item getItem() {
        Item tempItem = item;
        item = null;
        return tempItem;
    }
}

class Inventory {
    private List<Item> items;

    public Inventory() 
    {
        items = new ArrayList<>();
    }

    public void showItems() 
    {
        if (items.isEmpty()) 
        {
            System.out.println("Your inventory is empty.");
        } 
        else 
        {
            System.out.println("Inventory: ");
            for (Item item : items) 
            {
                System.out.println("- " + item.getName());
            }
        }
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item getItem(String itemName) 
    {
        for (Item item : items) 
        {
            if (item.getName().equalsIgnoreCase(itemName)) 
            {
                return item;
            }
        }
        return null;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}

public class Main 
{
    public static void main(String[] args) 
    {
        Game game = new Game();
        game.start();
    }
}