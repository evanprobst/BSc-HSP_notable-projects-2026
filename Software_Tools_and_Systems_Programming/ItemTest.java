import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;

public class ItemTest {

    private static final String TEST_FILE = "testfile.csv"; //file to load and save pet info to0
    private Pet pet;

    @Before
    public void setUp() throws IOException { // Before all, set up a pet wit hard codded inventory ammounts
        BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE));
        writer.write("Pet Type,Pet State,Pet Name,Pet Health,Pet Sleep,Pet Happiness,Pet Fullness,Score,Amount of Kibble item,Amount of Grilled Chicken item,Amount of Deluxe feast item,Amount of Squeaky toy item,Amount of Cozy Blanket item,Amount of Golden Collar item\n");
        writer.write("1,5,Pet,100,100,100,100,0,2,3,4,1,2,3\n");
        writer.write("parental");
        writer.close();

        pet = new Pet(TEST_FILE, 1);
    }

    @After
    public void tearDown() {
        new File(TEST_FILE).delete(); // Delete file when done
    }

    @Test
    public void testFoodItemKibble() { //test a kibble item on normal dog
        Item item = new Item(TEST_FILE, 1, 0, 1, pet); // FOOD, KIBBLE
        assertEquals(0, item.getCatogory());
        assertEquals(1, item.getType());
        assertEquals(2, item.getAmmount());
        assertEquals(10, item.getIncrease()); // 1 * 10 for dog
    }

    @Test
    public void testFoodItemFeastFish() throws IOException { //test a feast on normal fish
        BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE));
        writer.write("header\n");
        writer.write("3,5,Pet,100,100,100,100,0,1,1,5,0,0,0\n"); // fish with 5 feast
        writer.close();

        Pet fish = new Pet(TEST_FILE, 1);
        Item item = new Item(TEST_FILE, 1, 0, 3, fish); // FOOD, FEAST
        assertEquals(5, item.getAmmount());
        assertEquals(60, item.getIncrease()); // 3 * 20 for fish
    }

    @Test
    public void testGiftItemToyCat() throws IOException { // test a squeaky toy on cat
        BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE));
        writer.write("header\n");
        writer.write("2,5,Pet,100,100,100,100,0,0,0,0,3,0,0\n"); // cat with 3 toys
        writer.close();

        Pet cat = new Pet(TEST_FILE, 1);
        Item item = new Item(TEST_FILE, 1, 1, 1, cat); // GIFT, TOY
        assertEquals(3, item.getAmmount());
        assertEquals(10, item.getIncrease()); // 1 * 10 for cat
    }

    @Test
    public void testIsUsable() { // Test item usability in different states
        pet.setState(5); // Normal
        Item food = new Item(TEST_FILE, 1, 0, 1, pet);
        assertTrue(food.isUsable(pet));

        pet.setState(2); // Sleeping
        food = new Item(TEST_FILE, 1, 0, 1, pet);
        assertFalse(food.isUsable(pet));

        pet.setState(3); // Angry
        food = new Item(TEST_FILE, 1, 0, 1, pet);
        assertFalse(food.isUsable(pet));
    }


    @Test
    public void testSetAmount() { // Test setting item ammounts
        Item item = new Item(TEST_FILE, 1, 1, 1, pet);
        item.setAmmount(5);
        assertEquals(5, item.getAmmount());
    }
    public static void main(String[] args) throws IOException {
        ItemTest test = new ItemTest();

        // testFoodItemKibble
        test.setUp();
        test.testFoodItemKibble();
        test.tearDown();

        // testFoodItemFeastFish
        test.setUp();
        test.testFoodItemFeastFish();
        test.tearDown();

        // testGiftItemToyCat
        test.setUp();
        test.testGiftItemToyCat();
        test.tearDown();

        // testIsUsable
        test.setUp();
        test.testIsUsable();
        test.tearDown();

        // testSetAmount
        test.setUp();
        test.testSetAmount();
        test.tearDown();

        System.out.println("All automated Item tests passed.");
    }
}

