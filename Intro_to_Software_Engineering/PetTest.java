import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
public class PetTest {

    private static final String TEST_FILE = "testfile.csv";
    private Pet pet;

    @Before
    public void setUp() throws IOException { // Before all, set up a pet wit hard codded Pets
        BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE));
        writer.write("Pet Type,Pet State,Pet Name,Pet Health,Pet Sleep,Pet Happiness,Pet Fullness,Score,Amount of Kibble item,Amount of Grilled Chicken item,Amount of Deluxe feast item,Amount of Squeaky toy item,Amount of Cozy Blanket item,Amount of Golden Collar item\n");
        writer.write("1,5,Pet1,100,100,100,100,0,1,2,3,4,5,6\n"); // Pet 1
        writer.write("2,5,Pet2,90,80,70,60,10,6,5,4,3,2,1\n");     // Pet 2
        writer.write("3,5,Pet3,75,60,65,70,20,0,0,0,0,0,0\n");    // Pet 3
        writer.write("parental");
        writer.close();

        pet = new Pet(TEST_FILE, 1); // Load pet 1
    }

    @After
    public void tearDown() { // Delete file when done
        new File(TEST_FILE).delete();
    }

    @Test
    public void testConstructor() { // Test the costruction of a loaded pet
        assertEquals(1, pet.getPetType());
        assertEquals(5, pet.getState());
        assertEquals("Pet1", pet.getName());
        assertEquals(100, pet.getHealth());
        assertEquals(100, pet.getSleep());
        assertEquals(100, pet.getHappiness());
        assertEquals(100, pet.getFullness());
        assertEquals(0, pet.getScore());
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, pet.getInventoryAmounts());
    }

    @Test
    public void testSettersAndGetters() { // Test pet getters and setters
        pet = new Pet(TEST_FILE, 1);

        pet.setName("Pet");
        assertEquals("Pet", pet.getName());

        pet.setHealth(90);
        assertEquals(90, pet.getHealth());

        pet.setHappiness(80);
        assertEquals(80, pet.getHappiness());

        pet.setSleep(70);
        assertEquals(70, pet.getSleep());

        pet.setFullness(60);
        assertEquals(60, pet.getFullness());

        pet.setScore(42);
        assertEquals(42, pet.getScore());

        int[] inventory = {9, 8, 7, 6, 5, 4};
        pet.setInventoryAmounts(inventory);
        assertArrayEquals(inventory, pet.getInventoryAmounts());
    }

    @Test 
    public void testMaxHealth() { // Test pet type max healths
        Pet dog = new Pet(TEST_FILE, 1); // petType = 1 Dog
        assertEquals(150, dog.getMaxHealth());

        Pet cat = new Pet(TEST_FILE, 2); // petType = 2 Cat
        assertEquals(100, cat.getMaxHealth());

        Pet fish = new Pet(TEST_FILE, 3); // petType = 3 Fish
        assertEquals(75, fish.getMaxHealth());
    }

    @Test
    public void testRead() { // Test reading a pet from file
        Pet loadedPet = new Pet(TEST_FILE, 3); // Read from same line
        assertEquals(75, loadedPet.getHealth());
        assertEquals(70, loadedPet.getFullness());
        assertEquals(60, loadedPet.getSleep());
        assertEquals(65, loadedPet.getHappiness());
        assertEquals(20, loadedPet.getScore());
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0}, loadedPet.getInventoryAmounts());
    }

    public static void main(String[] args) throws IOException {
        PetTest test = new PetTest();
        // testConstructor
        test.setUp();
        test.testConstructor();
        test.tearDown();
        // testSettersAndGetters
        test.setUp();
        test.testSettersAndGetters();
        test.tearDown();
        // testMaxHealth
        test.setUp();
        test.testMaxHealth();
        test.tearDown();
        // testRead
        test.setUp();
        test.testRead();
        test.tearDown();

        System.out.println("All automated Pet tests passed.");
    }
}
