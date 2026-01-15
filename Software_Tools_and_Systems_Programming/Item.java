
/** File reading imports */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * Item ojetcs store the ammount of 1 of 6 items
 * <br><br>
 *
 * <b>Example Use:</b>
 * <pre>
 * {@code 
 *      Pet p = new Pet("savefile.csv", 1)
 *      Item i = new Item("savefile.csv", 1, 0, 1, p);
 *      System.out.println(Item.getType());
 * }
 * </pre>
 * 
 * <b>Example Output:</b>1<br>
 * 
 * @version 1.0.0
 * @author Group 55
 */
public class Item {
    /** Catagory of item, either gift or food */
    private int catogory;
    /** Type of item, 3 types in each catagory */
    private int type;
    /** Ammount of item */
    private int ammount;
    /** Ammount item increases a vital */
    private int increase;
    /** Item catagorys and types, stored as ints */
    private static final int FOOD = 0;
    private static final int GIFT = 1;

    private static final int KIBBLE = 1;
    private static final int CHICKEN = 2;
    private static final int FEAST = 3;
    private static final int TOY = 1;
    private static final int BLANKET = 2;
    private static final int COLLAR = 3;
    /**
     * Item constructor. Creates a new Item object.
     * 
     * @param filename   The save file containing item to be loaded
     * @param lineNumber Line number of item to be loaded
     * @param catagory The catagory of the item, gift or food
     * @param type The type of the item, 1 of 3 for each catagory
     * @param pet The pet that the item is to be used on
     * 
     * @see Pet.java
     * @see calcIncrease()
     */
    public Item(String filename, int lineNum, int catagory, int type, Pet pet) {
        this.catogory = catagory;
        this.type = type;
        /** Extract information from savefile */
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine();
    
            for (int i = 1; i < lineNum; i++) {
                br.readLine(); /** skip lines until the target one */
            }
    
            String line = br.readLine(); /** read the actual pet line */
    
            if (line != null) {
                String[] items = line.split(",");
                int index = -1;
                /** determine the ammount of this item there is based on file */
                if (catagory == FOOD) {
                    if (type == KIBBLE) index = 8;
                    else if (type == CHICKEN) index = 9;
                    else if (type == FEAST) index = 10;
                } else if (catagory == GIFT) {
                    if (type == TOY) index = 11;
                    else if (type == BLANKET) index = 12;
                    else if (type == COLLAR) index = 13;
                }
                /** store the ammount of item */
                if (index != -1 && index < items.length) {
                    this.ammount = Integer.parseInt(items[index]);
                } else {
                    this.ammount = 0;
                }
            }
    
        } catch (IOException e) {
            e.printStackTrace();
            this.ammount = 0;
        }
        this.calcIncrease(pet);
    }
    //** getter */
    public int getCatogory() {
        return this.catogory;
    }
    //** getter */
    public int getAmmount() {
        return this.ammount;
    }
    //** getter */
    public int getIncrease() {
        return this.increase;
    }
    //** getter */
    public int getType() {
        return this.type;
    }
    /**
     * Determine if the item is usable based on the pet state.
     * 
     * @param pet The pet that the item is to be used on
     * @see Pet.java
     * @return True if is usable, false otherwise
     */
    public boolean isUsable(Pet pet){
        /** Items are never usable when sleping or dead */
        if (pet.getState() == 2 || pet.getState() == 1){
            return false;
        /** Only Gifts usable when angry */
        } else if(pet.getState()==3 && this.getCatogory()==FOOD){ //angry and item is food
            return false;
        } else{
            return true; 
        }
    }
    /** Setter */
    public void setAmmount(int ammount){
        this.ammount = ammount;
    }
    /**
     * Determine how much the item increase vitals.
     * 
     * @param pet The pet that the item is to be used on
     * @see Pet.java
     */
    public void calcIncrease(Pet pet){
        /** Based on pet type, base rate is item type * 10 */
        switch (pet.getPetType()) {
            case 1: /** Dog */
                if (this.catogory == FOOD){
                    this.increase = this.getType()*10;
                } else{
                    this.increase = this.getType()*20; /** For dogs, happiness increases more */
                }
                break;
            case 2: /**Cat */
                if (this.catogory == FOOD){
                    this.increase = this.getType()*10;
                } else{
                    this.increase = this.getType()*10;
                }
                break;
            case 3: /** Fish */
                if (this.catogory == FOOD){
                    this.increase = this.getType()*20; /* For fish fullness incease more */
                } else{
                    this.increase = this.getType()*10;
                }
                break;
            default:
            this.increase = 0;
                break;
        }
    }
}

