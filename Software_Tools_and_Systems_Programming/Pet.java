import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 * The Pet class represents the state and behavior of a virtual pet.
 * It includes properties such as type, health, happiness, and other attributes that can be saved to and read from a file.
 */
public class Pet {

    private int petType;
    /** rates of decline */
    private double happinessRate; 
    private double fullnessRate;
    private double sleepRate;
    /** Pet statisitcs */
    private String name;
    private int happiness;
    private int fullness;
    private int health; 
    private int sleep; 
    private int state; /**  1 - Dead, 2 - Sleeping, 3 - Angry, 4 - Hungry, 5 - Normal */
    private int[] inventoryAmounts = new int[6]; /** 0-2 food, 3-5 gifts */

    private int score;
    /**
     * Reads a specified line from a file and returns it as an array of strings split by commas.
     * Handles errors such as file not found or bad file format.
     *
     * @param filename the path to the file to read
     * @param lineNum the line number to read from the file
     * @return an array of strings containing the data from the specified line or null if an error occurs
     */     
    private String[] readFromFile(String filename, int lineNum) {
        String currLine;
        try {
            File saveFile = new File(filename); 
            BufferedReader fileReader = new BufferedReader(new FileReader(saveFile));
            for (int i = 0; i < lineNum; i++) {
                currLine = fileReader.readLine();
            }
            currLine = fileReader.readLine();
            String [] returnVals = currLine.split(",");
            fileReader.close();
            return returnVals;
        } catch (FileNotFoundException e){
            System.out.println("Error: File not found");
            return null;
        } catch (IOException e) {
            System.out.println("Error");
            return null;
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Error: file format");
            return null;
        }
    }
    /**
     * Constructs a Pet object by reading its state from a file.
     * Throws IllegalArgumentException if the file is incorrectly formatted.
     *
     * @param filename the file from which to read the pet's state
     * @param lineNum the line number to read from the file
     */
    public Pet(String filename, int lineNum) {
        String[] values = readFromFile(filename, lineNum);
        try {
            if (values == null || values.length != 14) {
                throw new IllegalArgumentException();
            }
            this.petType = Integer.parseInt(values[0]);
            this.state = Integer.parseInt(values[1]);
            this.name = values[2];
            this.health = Integer.parseInt(values[3]);
            this.sleep = Integer.parseInt(values[4]);
            this.happiness = Integer.parseInt(values[5]);
            this.fullness = Integer.parseInt(values[6]);
            this.score = Integer.parseInt(values[7]);
            for (int i = 0; i < 6; i++) {
                this.inventoryAmounts[i] = Integer.parseInt(values[8 + i]);
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("Error with save file, ensure exists and all lines are in the correct format, See README for file requierments");
            System.exit(0);
            
        }
    }
    /** Getters and Setters */
    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSleep() {
        return sleep;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public int getPetType() {
        return petType;
    }

    public double getHappinessRate() {
        return happinessRate;
    }

    public double getFullnessRate() {
        return fullnessRate;
    }

    public double getSleepRate() {
        return sleepRate;
    }

    public int getFullness() {
        return fullness;
    }

    public void setFullness(int fullness) {
        this.fullness = fullness;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public  void setHappinessRate(double happinessRate) {
        this.happinessRate = happinessRate;
    }

    public void setFullnessRate(double fullnessRate) {
        this.fullnessRate = fullnessRate;
    }

    public void setSleepRate(double sleepRate) {
        this.sleepRate = sleepRate;
    }

    public int getState() {
        return state;
    }
    /**
     * Returns the maximum health value depending on the pet type.
     * Different pet types have different health capacities.
     *
     * @return the maximum health value
     */
    public int getMaxHealth() {
        switch (getPetType()) {
            case 1:
                return 150; /** Dog */
            case 2:
                return 100; /** Cat */
            case 3:
                return 75; /** Fish */
            default:
                return 100;
        }
    }
    public void setState(int state) {
        this.state = state;
    }
    public void setInventoryAmounts(int[] amounts) {
        this.inventoryAmounts = amounts;
    }
    
    public int[] getInventoryAmounts() {
        return this.inventoryAmounts;
    }
     /**
     * Formats the pet's data into a comma-separated string for file saving.
     * Includes all attributes of the pet necessary for reconstruction.
     *
     * @param p the Pet object to format
     * @return a string representation of the pet's data
     */
    private static String fileFormat(Pet p) {
        StringBuilder sb = new StringBuilder();
        sb.append(p.getPetType()).append(",")
        .append(p.getState()).append(",")
        .append(p.getName()).append(",")
        .append(p.getHealth()).append(",")
        .append(p.getSleep()).append(",")
        .append(p.getHappiness()).append(",")
        .append(p.getFullness()).append(",")
        .append(p.getScore());

        for (int amount : p.getInventoryAmounts()) { 
            sb.append(",").append(amount);
        }

        return sb.toString();
    }

    /**
     * Writes the pet's current state to a specified line in a file.
     * If the pet's attributes exceed their logical bounds, they are adjusted before saving.
     *
     * @param filename the file to write to
     * @param lineNum the line number to overwrite or append to
     * @param p the Pet object whose data is to be written
     */
    public static void writeToFile(String filename, int lineNum, Pet p){
        /** first, read and save the existing information */
        String currLine;
        String addLine = "";
        if (p.getSleep() < 0) p.setSleep(0);
        if (p.getFullness() < 0) p.setFullness(0);
        if (p.getFullness() > 100 ) p.setFullness(100);
        if (p.getHappiness() > 100) p.setHappiness(100);
        if (p.getHealth() > p.getMaxHealth() ) p.setHealth(p.getMaxHealth());
        if (p.getHappiness() > 100) p.setHappiness(100);
        addLine = fileFormat(p);
        ArrayList<String> lines = new ArrayList<String>();
        File saveFile = new File(filename);
        
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(saveFile));
            for (int i = 0; i < 5; i++) {
                if ((currLine = fileReader.readLine()) != null){
                    lines.add(currLine);
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e){
            System.out.println("Error: File not found");
        } catch (IOException e) {
            System.out.println("Error");
        }
        try {    
            PrintWriter fileWriter = new PrintWriter(new FileWriter("savefile.csv", false));
            for (int i = 0; i < lineNum; i++) {
                fileWriter.println(lines.get(i));
            }
            fileWriter.println(addLine);
            for (int i = lineNum+1; i < 5; i++) {
                fileWriter.println(lines.get(i));
            }
            fileWriter.close();
            
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}
