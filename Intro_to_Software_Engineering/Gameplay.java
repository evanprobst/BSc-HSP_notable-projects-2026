
/** Imports for timers and durations */
import java.time.Instant;
import javax.swing.Timer;
import java.time.Duration;

/** Imports for UI events */
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Imports for data storage */
import java.util.ArrayList;
import java.util.List;

/** Import for randomization */
import java.util.Random;
/**
 * Gameplay Objects open a gameplay UI screen based on a loaded Pet from a csv file
 * <br><br>
 *
 * <b>Example Use:</b>
 * <pre>
 * {@code 
 *      Gampelay g = new Gameplay("savefile.csv", 1);
 *      g.setVisible(true);
 * }
 * </pre>
 * 
 * <b>Example Output:</b>A gameplay screen with the first pet in a given csv file stored <br>
 * 
 * @version 1.0.0
 * @author Group 55
 */
public class Gameplay extends javax.swing.JFrame {

    /** Last time play command was used */
    private Instant lastPlayTime = Instant.EPOCH;
    /** Durration of play command cooldown */
    Duration playCooldown = Duration.ofSeconds(10);
    /** Last time vet command was used */
    private Instant lastVetTime = Instant.EPOCH;
    /** Vet command cooldown */
    Duration vetCooldown = Duration.ofSeconds(30);
    /** Pet sprite image being diplayed */
    private String image;
    /** Current pet in gameplay */
    public Pet p; // These are public casue pause needs them
    /** Line number the of current pet in savefile */
    public int lineNum;
    /** Inventory of items for current pet */
    public Item[] inventory;
    /** Timer to flash vitals (used when below 25%) */
    private Timer flashVitalsTimer;
    /** Whnetehr or not vitals are flashing */
    private boolean flashOn = false;
    /** Timer to give player items over a set time */
    private Timer rewardTimer;

    /**
     * Gameplay constructor. Creates a new Gameplay object.
     * 
     * @param filename   The save file containing pet to be loaded
     * @param lineNumber Line number of pet to be loaded
     * 
     * @see readFromFile
     * @see setStateImage
     * @see loadInventory
     */
    public Gameplay(String filename, int lineNumber) {
        this.lineNum = lineNumber; /** Init line for pet save */
        this.p = readFromFile("savefile.csv", lineNumber); /** Create a pet object by reading form file */
        loadInventory("savefile.csv", lineNumber); /** Load the desired pet's inventory */
        setStateImage(p.getPetType(), p.getState()); /** Set image of pet */

        this.setResizable(false); /** Call UI element methods */
        initComponents(); 
        initKeyShortcuts();
        initiateScreenVitals();
        decayTimer.start();
        flipTimer.start();

        vitalLabelTimer(); /** Start UI timers */
        initRewardTimer();
    }

    /**
     * Initalizes the The pets array of items for inventory
     * 
     * @param filename The save file containing inventory to be loaded
     * @param lineNumber Line number of inventroy to be loaded
     * @see Item.java
     */
    private void loadInventory(String filename, int lineNumber) {
        inventory = new Item[] {
                new Item(filename, lineNumber, 0, 1, p), /** Kibble */
                new Item(filename, lineNumber, 0, 2, p), /** Chicken */
                new Item(filename, lineNumber, 0, 3, p), /** Feast */
                new Item(filename, lineNumber, 1, 1, p), /** Toy */
                new Item(filename, lineNumber, 1, 2, p), /** Blanket */
                new Item(filename, lineNumber, 1, 3, p) /** Collar */
        };
    }
    /**
     * Gives a item of each type every 20 seconds
     * 
     * @see Item.java
     */
    private void initRewardTimer() {
        rewardTimer = new Timer(20000, new ActionListener() {
            Random random = new Random(); /** Create a single Random instance to use for this timer. */

            @Override
            public void actionPerformed(ActionEvent e) {
                int foodIndex = random.nextInt(3); /** Randomly choose a food item index (0, 1, or 2) */
                int giftIndex = 3 + random.nextInt(3); /** Randomly choose a gift item index (3, 4, or 5) */
                inventory[foodIndex].setAmmount(inventory[foodIndex].getAmmount() + 1); /** Increase the amount of the chosen food and gift items by one */
                inventory[giftIndex].setAmmount(inventory[giftIndex].getAmmount() + 1);
            }
        });
        rewardTimer.start();
    }
    /**
     * Flip the pet sprite periodically
     * 
     * @see Pet.java
     */
    private void flipPetImage() {
        int state = p.getState();
        if (state == 3 || state == 4 || state == 5) { /** Only flip if state is normal (5), hungry (4), or angry (3) */
            if (image.contains("L.png")) { /** If left image, flip to right */
                image = image.replace("L.png", "R.png");
            } else if (image.contains("R.png")) { /** If right image, flip to left */
                image = image.replace("R.png", "L.png");
            }
            petSprite.setIcon(new javax.swing.ImageIcon(getClass().getResource(image))); /** Update the petSprite icon with the new image */
        }
    }
    /**
     * Timer for flipping pet images
     * 
     * @see Pet.java
     * @see flipPetImage
     */
    Timer flipTimer = new Timer(1000, new ActionListener() { /** Flip every second */
        @Override
        public void actionPerformed(ActionEvent e) {
            flipPetImage();
        }
    });
    /** Store the decay rates for vitals (easier to modify) */
    private final int BASE_HAPPINESS_DECAY = 1; 
    private final int BASE_FULLNESS_DECAY = 1;
    private final int BASE_SLEEP_DECAY = 1;
    private final int SLEEP_RECOVERY_RATE = 10;
    private final int HUNGRY_HEALTH_PENALTY = 1;
    private final int HUNGRY_HAPPINESS_PENALTY = 1;
    private final int SLEEP_HEALTH_PENALTY = 25;
    /**
     * Updates the pet's vital statistics such as sleep, health, fullness, and happiness.
     * Handles automatic state changes based on thresholds (e.g., hungry, angry, sleeping, dead).
     * 
     * @see Pet.java
     */
    private void updateVitalsAndState() {
        /** If pet is in sleeping state, recover sleep, otherwise, decrease it. */
        if (p.getState() == 2) { /** Sleep */

            /** Calculate rate modifiers based on pet modifiers, cat gains more from sleep*/
            int petRateModify = 0;
            if (p.getPetType() == 2) { /** Sleep */
                petRateModify = SLEEP_RECOVERY_RATE * 2;
            } else {
                petRateModify = SLEEP_RECOVERY_RATE;
            }
            p.setSleep(Math.min(100, p.getSleep() + petRateModify)); /** Decrease slee[] */

            /** Return pet to other state when sleep is recovered*/
            if (p.getSleep() >= 100) {
                p.setState(5);
                setStateImage(p.getPetType(), p.getState());
                JOptionPane.showMessageDialog(this, "Your pet has woken up");
                switch (p.getState()) {
                    case 1:
                        MusicPlayer.stopMusic();
                        MusicPlayer.playMusic("sounds/normal_state_music.wav");
                        break;
                    case 2:
                        MusicPlayer.stopMusic();
                        MusicPlayer.playMusic("sounds/sleep_state_music.wav");
                        break;
                    case 3:
                        MusicPlayer.stopMusic();
                        MusicPlayer.playMusic("sounds/hungry_state_music.wav");
                        break;
                    case 4:
                        MusicPlayer.stopMusic();
                        MusicPlayer.playMusic("sounds/angry_state_music.wav");
                        break;
                    case 5:
                        MusicPlayer.stopMusic();
                        MusicPlayer.playMusic("sounds/normal_state_music.wav");
                        break;
                }
            }
            /* If not sleeping, decay sleep */
        } else {
            /** Calculate rate modifiers based on pet modifiers, cat gains more from sleep*/
            int petRateModify = 0;
            if (p.getPetType() == 3) {
                petRateModify = BASE_SLEEP_DECAY * 2;
            } else {
                petRateModify = BASE_SLEEP_DECAY;
            }
            p.setSleep(Math.max(0, p.getSleep() - petRateModify)); /** Decrement sleep*/
        }

        /** Decrease happiness and fullness at their base rates * the modifiers for dog and cat.*/

        /** Calculate fullness decay modifier. */
        int fullnessRate = (p.getPetType() == 1) ? BASE_FULLNESS_DECAY * 2 : BASE_FULLNESS_DECAY;
        p.setFullness(Math.max(0, p.getFullness() - fullnessRate));

        /** Calculate happiness decay modifier. */
        int happinessRate = (p.getPetType() == 2) ? BASE_HAPPINESS_DECAY * 2 : BASE_HAPPINESS_DECAY;
        p.setHappiness(Math.max(0, p.getHappiness() - happinessRate));
        /** Calculate fullness decay modifier. */

        /**  If fullness is zero, pet enters the hungry state with extra penalties. */
        if (p.getFullness() == 0 && p.getState() > 4) {
            p.setState(4); // Hungry state
            setStateImage(p.getPetType(), p.getState());
            MusicPlayer.playMusic("sounds/hungry_state_music.wav");
            p.setScore(p.getScore() - 5);
        }
        /** If the pet is in the hungry state, continuously decay health and happiness. */
        if (p.getState() == 4) {
            p.setHealth(Math.max(0, p.getHealth() - HUNGRY_HEALTH_PENALTY));
            p.setHappiness(Math.max(0, p.getHappiness() - HUNGRY_HAPPINESS_PENALTY));
        }

        /** If sleep reaches zero while not already sleeping, force the pet to sleep and apply a health penalty. */
        if (p.getSleep() <= 0 && p.getState() != 2) {
            p.setHealth(Math.max(0, p.getHealth() - SLEEP_HEALTH_PENALTY));
            p.setState(2);
            setStateImage(p.getPetType(), p.getState());
            petSprite.setIcon(new javax.swing.ImageIcon(getClass().getResource(image)));
            JOptionPane.showMessageDialog(this, "Your pet has fallen asleep.");
            MusicPlayer.playMusic("sounds/sleep_state_music.wav");
        }

        /** If happiness reaches zero (and pet isn’t already dead or sleeping), pet becomes angry. */
        if (p.getHappiness() == 0 && p.getState() != 1 && p.getState() != 2 && p.getState() != 3) {
            p.setState(3);
            setStateImage(p.getPetType(), p.getState());
            MusicPlayer.playMusic("sounds/angry_state_music.wav");
            p.setScore(p.getScore() - 5);
        }

        /** If health reaches zero, the pet dies. */
        if (p.getHealth() == 0) {
            p.setState(1);
            setStateImage(p.getPetType(), p.getState());
            petSprite.setIcon(new javax.swing.ImageIcon(getClass().getResource(image)));

            /** Stop timers so no further actions occur. */
            decayTimer.stop();
            flipTimer.stop();
            JOptionPane.showMessageDialog(this, "Your pet has died.");
            MusicPlayer.stopMusic();
            p.setScore(0);
        }
    }
    /**
     * Checks the pet's current state and updates it based on its vitals.
     * Switch pet out of negative states if conditions are met.
     * Called when increasing actions occur
     * @see Pet.java
     */
    private void checkAndUpdatePetState() {
        /**  If health is zero, pet remains Dead (state 1) */
        if (p.getHealth() <= 0) {
            p.setState(1);
            setStateImage(p.getPetType(), p.getState());
            petSprite.setIcon(new javax.swing.ImageIcon(getClass().getResource(image)));
            return;
        }

        /** If pet is sleeping and fully rested, wake it up (back to Normal, state 5) */
        if (p.getState() == 2 && p.getSleep() >= 100) {
            p.setState(5);
            setStateImage(p.getPetType(), p.getState());
            JOptionPane.showMessageDialog(this, "Your pet has woken up.");
            MusicPlayer.playMusic("sounds/normal_state_music.wav");
            return;
        }

        /** If pet is angry, only allow exit when happiness is at least 50% of maximum */
        if (p.getState() == 3 && p.getHappiness() >= 50) {
            p.setState(5);
            setStateImage(p.getPetType(), p.getState());
            JOptionPane.showMessageDialog(this, "Your pet is no longer angry.");
            MusicPlayer.playMusic("sounds/normal_state_music.wav");
            return;
        }

        /** If pet is hungry and fullness is now above 0, it exits the hungry state */
        if (p.getState() == 4 && p.getFullness() > 0) {
            p.setState(5);
            setStateImage(p.getPetType(), p.getState());
            JOptionPane.showMessageDialog(this, "Your pet is no longer hungry.");
            MusicPlayer.playMusic("sounds/normal_state_music.wav");
            return;
        }
    }
    /**
     * Timer to call a vital update and check every 5 seconds
     * 
     * @see actionPerformed
     */
    Timer decayTimer = new Timer(5000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateVitalsAndState();
        }
    });
    /**
     * Check if a command canbe excecuted based on state
     * 
     * @param commandName the name of the command for output
     * @return True if command is allowed, otherwise false
     * @see Pet.java
     */
    private boolean checkCommandAllowed(String commandName) {
        int state = p.getState();
        if (state == 1 || state == 2) {  /** Dead or sleeping */
            JOptionPane.showMessageDialog(this, "No commands available while your pet is " + stateToText(p) + ".");
            return false;
        } /** Angry */
        if (state == 3 && !(commandName.equals("gift") || commandName.equals("play"))) {
            JOptionPane.showMessageDialog(this, "Your pet is angry! Only 'Gift' and 'Play' commands are allowed.");
            return false;
        }
        return true;
    }
    /**
     * timer to flash labels for vitals
     *  
     * @see updateVitalColors
     * @see initiateScreenVitals
     */
    private void vitalLabelTimer() {
        flashVitalsTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flashOn = !flashOn;
                updateVitalColors();
                initiateScreenVitals();
            }
        });
        flashVitalsTimer.start();
    }
    /**
     * Chnage the vital labels colour
     * 
     * @see vitalLabelTimer
     */
    private void updateVitalColors() {
        java.awt.Color flashColour = flashOn ? java.awt.Color.RED : java.awt.Color.BLACK; /* set colour based on flash state */

        health.setForeground(p.getHealth() <= p.getMaxHealth() * 0.25 ? flashColour : java.awt.Color.BLACK); /* Set the labels colours */
        sleep.setForeground(p.getSleep() <= 25 ? flashColour : java.awt.Color.BLACK);
        happiness.setForeground(p.getHappiness() <= 25 ? flashColour : java.awt.Color.BLACK);
        fullness.setForeground(p.getFullness() <= 25 ? flashColour : java.awt.Color.BLACK);
    }
    /**
     * When unpasued, restart timers
     * 
     * @see decayTimer
     * @see flipTimer
     */
    public void resumeGame() {
        decayTimer.start();
        flipTimer.start();
    }
    /**
     * Display the correct pet vitals to the screen
     * 
     * @see Pet.java
     */
    private void initiateScreenVitals() {
        /** Esnure vitals do not exced bounds */
        int maxHealth = p.getMaxHealth();
        if (this.p.getHealth() > maxHealth)
            p.setHealth(maxHealth);
        if (this.p.getHealth() < 0)
            p.setHealth(0);

        if (this.p.getHappiness() > 100)
            p.setHappiness(100);
        if (this.p.getHappiness() < 0)
            p.setHappiness(0);

        if (this.p.getSleep() > 100)
            p.setSleep(100);
        if (this.p.getSleep() < 0)
            p.setSleep(0);

        if (this.p.getFullness() > 100)
            p.setFullness(100);
        if (this.p.getFullness() < 0)
            p.setFullness(0);
        if (this.p.getScore() < 0)
            p.setScore(0);
        if (this.p.getScore() > 99)
            p.setScore(99);
            /** Set the vital label text */
        health.setText(String.valueOf(this.p.getHealth()));
        sleep.setText(String.valueOf(this.p.getSleep()));
        fullness.setText(String.valueOf(this.p.getFullness()));
        happiness.setText(String.valueOf(this.p.getHappiness()));
        score.setText(stateToText(this.p));
        scoreLabel.setText("Score:" + String.valueOf(this.p.getScore()));
        updateVitalColors();

    }
    /**
     * Initalize pet
     * 
     * @param filename
     * @param lineNum
     * @return p, a loaded pet
     * @see Pet.java
     */
    private Pet readFromFile(String filename, int lineNum) {
        Pet p = new Pet(filename, lineNum);
        return p;
    }
    /**
     * Convert pet state into word
     * 
     * @param p 
     * @return String pet state
     * @see Pet.java
     */
    private String stateToText(Pet p) {
        switch (p.getState()) {
            case 1:
                return "Dead";
            case 2:
                return "Sleeping";
            case 3:
                return "Angry";
            case 4:
                return "Hungry";
            case 5:
                return "Normal";
            default:
                return "Normal";
        }
    }
    /**
     * set the pet sprite image based on state
     * 
     * @param type Pet Type
     * @param state Pet state
     * @see Pet.java
     */
    private void setStateImage(int type, int state) {
        String imgStr = "/img/cat/catDead.png";
        /** Switch pet image based on its Type and State */
        switch (type) {
            case 2:
                switch (state) {
                    case 1:
                        imgStr = "/img/cat/catDead.png";
                        // dead image
                        break;
                    case 2:
                        // sleeping image
                        imgStr = "/img/cat/catSleep.png";
                        break;
                    case 3:
                        // angry image
                        imgStr = "/img/cat/catAngryL.png";
                        break;
                    case 4:
                        // hungry image
                        imgStr = "/img/cat/catHungryL.png";
                        break;
                    case 5:
                        // normal image
                        imgStr = "/img/cat/catNormalL.png";
                        break;
                }
                break;
            case 1:
                switch (state) {
                    case 1:
                        // dead image
                        imgStr = "/img/dog/dogDead.png";
                        break;
                    case 2:
                        // sleeping image
                        imgStr = "/img/dog/dogSleep.png";
                        break;
                    case 3:
                        // angry image
                        imgStr = "/img/dog/dogAngryL.png";
                        break;
                    case 4:
                        // hungry image
                        imgStr = "/img/dog/dogHungryL.png";
                        break;
                    case 5:
                        // normal image
                        imgStr = "/img/dog/dogNormalL.png";
                        break;
                }
                break;
            case 3:
                switch (state) {
                    case 1:
                        // dead image
                        imgStr = "/img/fish/fishDead.png";
                        break;
                    case 2:
                        // sleeping image
                        imgStr = "/img/fish/fishSleep.png";
                        break;
                    case 3:
                        // angry image
                        imgStr = "/img/fish/fishAngryL.png";
                        break;
                    case 4:
                        // hungry image
                        imgStr = "/img/fish/fishHungryL.png";
                        break;
                    case 5:
                        // normal image
                        imgStr = "/img/fish/fishNormalL.png";
                        break;
                }
                break;

        }
        this.image = imgStr;
    }
    /**
     * Deterime if button is usable based on pet state
     * 
     * @param actionName
     * @param lastUsed
     * @param cooldown
     * @return True if action cooldown done, otherwise false 
     */
    private boolean isCooldownReady(String actionName, Instant lastUsed, Duration cooldown) {
        Instant now = Instant.now();
        /** Cooldown over */
        if (Duration.between(lastUsed, now).compareTo(cooldown) >= 0) {
            return true;
        /** Cooldown not over */
        } else {
            long secondsLeft = cooldown.minus(Duration.between(lastUsed, now)).getSeconds();
            JOptionPane.showMessageDialog(this, actionName + " cooldown! Try again in " + secondsLeft + " seconds.");
            return false;
        }
    }
    /**
     * Keyboard shortcuts corresponding to gameplay buttons
     * 
     * @param e event
     */
    private void initKeyShortcuts() {
        javax.swing.InputMap inputMap = background.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        javax.swing.ActionMap actionMap = background.getActionMap();

        // ESC to pause the game, click corresponding button */
        inputMap.put(javax.swing.KeyStroke.getKeyStroke("ESCAPE"), "pauseGame");
        actionMap.put("pauseGame", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseButton.doClick();
            }
        });

        /** F to feed, click corresponding button */
        inputMap.put(javax.swing.KeyStroke.getKeyStroke('f'), "feedPet");
        actionMap.put("feedPet", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                feedButton.doClick();
            }
        });

        /** G to gift, click corresponding button */
        inputMap.put(javax.swing.KeyStroke.getKeyStroke('g'), "giftPet");
        actionMap.put("giftPet", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                giftButton.doClick();
            }
        });

        /** P to play, click corresponding button */
        inputMap.put(javax.swing.KeyStroke.getKeyStroke('p'), "playWithPet");
        actionMap.put("playWithPet", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButton.doClick();
            }
        });

        /** E to exercise, click corresponding button */
        inputMap.put(javax.swing.KeyStroke.getKeyStroke('e'), "exercisePet");
        actionMap.put("exercisePet", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exerciseButton.doClick();
            }
        });

        /** B for bed, click corresponding button */
        inputMap.put(javax.swing.KeyStroke.getKeyStroke('b'), "putPetToBed");
        actionMap.put("putPetToBed", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bedButton.doClick();
            }
        });

        /** V for vet, click corresponding button */
        inputMap.put(javax.swing.KeyStroke.getKeyStroke('v'), "takeToVet");
        actionMap.put("takeToVet", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vetButton.doClick();
            }
        });

        /** I for inventory, click corresponding button */
        inputMap.put(javax.swing.KeyStroke.getKeyStroke('i'), "openInventory");
        actionMap.put("openInventory", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inventoryButton.doClick();
            }
        });

        /** M for mute, pause music */
        inputMap.put(javax.swing.KeyStroke.getKeyStroke('m'), "mute");
        actionMap.put("mute", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!MusicPlayer.isPasued()) { /** Pasue if not already, otherwise play */
                    MusicPlayer.pauseMusic();
                } else {
                    MusicPlayer.resumeMusic();
                }

            }
        });
    }
    /**
     * Java swing components generated by NetBeans GUI builder
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        background = new javax.swing.JPanel();
        petSprite = new javax.swing.JLabel();
        playButton = new javax.swing.JButton();
        feedButton = new javax.swing.JButton();
        giftButton = new javax.swing.JButton();
        exerciseButton = new javax.swing.JButton();
        bedButton = new javax.swing.JButton();
        vetButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        inventoryButton = new javax.swing.JButton();
        scoreLabel = new javax.swing.JLabel();
        score = new javax.swing.JLabel();
        scoreFrame = new javax.swing.JLabel();
        health = new javax.swing.JLabel();
        sleep = new javax.swing.JLabel();
        happiness = new javax.swing.JLabel();
        fullness = new javax.swing.JLabel();
        healthLabel = new javax.swing.JLabel();
        sleepLabel = new javax.swing.JLabel();
        happinessLabel = new javax.swing.JLabel();
        fullnessLabel = new javax.swing.JLabel();
        whiteboardImage = new javax.swing.JLabel();
        roomBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Perfect Pet Pals");
        setMinimumSize(new java.awt.Dimension(800, 800));
        setMaximumSize(new java.awt.Dimension(800, 800));
        background.setBackground(new java.awt.Color(249, 241, 229));
        background.setAutoscrolls(true);
        background.setName(""); // NOI18N
        background.setPreferredSize(new java.awt.Dimension(800, 800));
        background.setLayout(null);

        petSprite.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        petSprite.setIcon(new javax.swing.ImageIcon(getClass().getResource(this.image))); // NOI18N
        petSprite.setToolTipText("");
        petSprite.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        petSprite.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        background.add(petSprite);
        petSprite.setBounds(330, 470, 200, 200);

        playButton.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        playButton.setText("Play");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        switch (p.getState()) { /** Play music on init based on state */
            case 1:
                MusicPlayer.playMusic("sounds/normal_state_music.wav");
                break;
            case 2:
                MusicPlayer.playMusic("sounds/sleep_state_music.wav");
                break;
            case 3:
                MusicPlayer.playMusic("sounds/hungry_state_music.wav");
                break;
            case 4:
                MusicPlayer.playMusic("sounds/angry_state_music.wav");
                break;
            case 5:
                MusicPlayer.playMusic("sounds/normal_state_music.wav");
                break;
        }

        background.add(playButton);
        playButton.setBounds(20, 725, 105, 50);

        feedButton.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        feedButton.setText("Feed");
        feedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feedButtonActionPerformed(evt);
            }
        });
        background.add(feedButton);
        feedButton.setBounds(135, 725, 105, 50);

        giftButton.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        giftButton.setText("Gift");
        giftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                giftButtonActionPerformed(evt);
            }
        });
        background.add(giftButton);
        giftButton.setBounds(250, 725, 105, 50);

        exerciseButton.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        exerciseButton.setText("Exercise");
        exerciseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exerciseButtonActionPerformed(evt);
            }
        });
        background.add(exerciseButton);
        exerciseButton.setBounds(365, 725, 105, 50);

        bedButton.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        bedButton.setText("Bed");
        bedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bedButtonActionPerformed(evt);
            }
        });
        background.add(bedButton);
        bedButton.setBounds(480, 725, 105, 50);

        vetButton.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        vetButton.setText("Vet");
        vetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vetButtonActionPerformed(evt);
            }
        });
        background.add(vetButton);
        vetButton.setBounds(595, 725, 105, 50);

        pauseButton.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        pauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pauseButton.png"))); // NOI18N
        pauseButton.setToolTipText("");
        pauseButton.setContentAreaFilled(false);
        pauseButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });
        background.add(pauseButton);
        pauseButton.setBounds(730, 10, 60, 55);

        inventoryButton.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        inventoryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/dogbackpack.png"))); // NOI18N
        inventoryButton.setToolTipText("");
        inventoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventoryButtonActionPerformed(evt);
            }
        });
        background.add(inventoryButton);
        inventoryButton.setBounds(730, 725, 50, 50);

        scoreLabel.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        scoreLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        scoreLabel.setText("Score 0");
        background.add(scoreLabel);
        scoreLabel.setBounds(455, 73, 130, 25);

        score.setFont(new java.awt.Font("Comic Sans MS", 1, 20)); // NOI18N
        score.setForeground(new java.awt.Color(81, 141, 103));
        score.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        score.setText("Mood");
        background.add(score);
        score.setBounds(465, 103, 110, 25);

        scoreFrame.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        scoreFrame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/scoreFrame.png"))); // NOI18N
        background.add(scoreFrame);
        scoreFrame.setBounds(440, -10, 160, 170);

        health.setFont(new java.awt.Font("Comic Sans MS", 1, 16)); // NOI18N
        health.setText("~~");
        background.add(health);
        health.setBounds(170, 40, 30, 25);

        sleep.setFont(new java.awt.Font("Comic Sans MS", 1, 16)); // NOI18N
        sleep.setText("~~");
        background.add(sleep);
        sleep.setBounds(170, 65, 30, 25);

        happiness.setFont(new java.awt.Font("Comic Sans MS", 1, 16)); // NOI18N
        happiness.setText("~~");
        background.add(happiness);
        happiness.setBounds(170, 90, 30, 25);

        fullness.setFont(new java.awt.Font("Comic Sans MS", 1, 16)); // NOI18N
        fullness.setText("~~");
        background.add(fullness);
        fullness.setBounds(170, 115, 30, 25);

        healthLabel.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        healthLabel.setText("Health:");
        background.add(healthLabel);
        healthLabel.setBounds(60, 40, 75, 25);

        sleepLabel.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        sleepLabel.setText("Sleep:");
        background.add(sleepLabel);
        sleepLabel.setBounds(60, 65, 60, 25);

        happinessLabel.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        happinessLabel.setText("Happiness: ");
        background.add(happinessLabel);
        happinessLabel.setBounds(60, 90, 105, 25);

        fullnessLabel.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        fullnessLabel.setText("Fullness:");
        background.add(fullnessLabel);
        fullnessLabel.setBounds(60, 115, 80, 25);

        whiteboardImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/whiteboard.png"))); // NOI18N
        background.add(whiteboardImage);
        whiteboardImage.setBounds(25, 9, 240, 165);

        roomBackground.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        roomBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/room.jpg"))); // NOI18N
        background.add(roomBackground);
        roomBackground.setBounds(0, 0, 800, 800);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));

        pack();
    }
    /**
     * Button click for inventory
     * 
     * @param e event clicked
     * @see Inventory.java
     * @see Item.java
     */
    private void inventoryButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_inventoryButtonActionPerformed
        Inventory inventory = new Inventory(this.inventory);
        /** Get the location of the main gameplay window */
        int gameplayX = this.getX();
        int gameplayY = this.getY();

        /** Set the location of the inventory window to the right of gameplay window */
        inventory.setLocation(gameplayX + 550, gameplayY + 200);
        inventory.setVisible(true);
    }// GEN-LAST:event_inventoryButtonActionPerformed
    /**
     * Button click for vet command
     * 
     * @param e event clicked
     * @see Pet.java
     * @see checkCommandAllowed
     * @see isCooldownReady
     * @see initiateScreenVitals
     * @see checkAndUpdatePetState
     */
    private void vetButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!checkCommandAllowed("vet"))
            return; /** Disallow if pet state does not allow vet visits. */
        if (isCooldownReady("Vet", lastVetTime, vetCooldown)) { /** if cooldown has passed */
            lastVetTime = Instant.now(); /** Reset cooldown */
            /** change pet health */
            int health = p.getHealth();
            health = Math.min(p.getMaxHealth(), health + 20);
            p.setHealth(health);
            initiateScreenVitals();
            JOptionPane.showMessageDialog(this, "Your pet went to the Vet, gained 20 health.");
            checkAndUpdatePetState(); /**  After healing, update state if necessary. */
            p.setScore(p.getScore() - 5); /** Vet subtracts score */
        }
    }

    // -------------------------------------------------------------------------
    /**
     * Button clicked for Bed command
     *  
     * @param e event clicked
     * @see Pet.java
     * @see checkCommandAllowed
     * @see checkAndUpdatePetState
     */
    private void bedButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!checkCommandAllowed("bed")) /** Disallow if pet state does not allow bed. */
            return;
        /** update pet to sleep*/
        p.setState(2);
        setStateImage(p.getPetType(), p.getState());
        petSprite.setIcon(new javax.swing.ImageIcon(getClass().getResource(image)));
        JOptionPane.showMessageDialog(this, "Your pet is now going to bed.");
        MusicPlayer.playMusic("sounds/sleep_state_music.wav");
        checkAndUpdatePetState();
    }
    /**
     * Button clcik for exercise command
     * 
     * @param e event clicked
     * @see Pet.java
     * @see checkCommandAllowed
     * @see checkAndUpdatePetState
     */
    private void exerciseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!checkCommandAllowed("exercise"))
            return; /** Disallow if pet’s state prevents exercise. */
        int sleep = p.getSleep();
        int health = p.getHealth();
        int fullness = p.getFullness();
        /** Alter vitals */
        sleep = Math.max(0, sleep - 5);
        health = Math.min(p.getMaxHealth(), health + 10);
        fullness = Math.max(0, fullness - 5);
        /** update pet */
        p.setSleep(sleep);
        p.setHealth(health);
        p.setFullness(fullness);
        initiateScreenVitals();
        JOptionPane.showMessageDialog(this, "Your pet exercised!");
        checkAndUpdatePetState();
    }
    /**
     * Button click for gift command
     * 
     * @param e event clicked
     * @see Item.java
     * @see checkCommandAllowed
     * @see checkAndUpdatePetState
     */
    private void giftButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!checkCommandAllowed("gift"))
            return; /** Disallow if pet’s state prevents gifting. */

        /** Get list of gifts */
        List<Item> availableGifts = new ArrayList<>();
        for (int i = 3; i <= 5; i++) {
            Item giftItem = inventory[i];
            if (giftItem.getAmmount() > 0 && giftItem.isUsable(p)) {
                availableGifts.add(giftItem);
            }
        } /** Case no gifts */
        if (availableGifts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No usable gift items available!");
            return;
        }

        /**  Create an array of option strings for the selection of gifts. */
        String[] options = new String[availableGifts.size()];
        for (int i = 0; i < availableGifts.size(); i++) {
            /** Show the gift name along with its available amount. */
            options[i] = getGiftName(availableGifts.get(i).getType()) + " (x" + availableGifts.get(i).getAmmount()+ ")";
        }

        /** Prompt the user to choose a gift item. */
        String chosen = (String) JOptionPane.showInputDialog(null, "Choose a gift item to use:", "Gift Pet", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (chosen == null) {
            return;
        }

        /** Find and use the selected gift item. */
        for (Item giftItem : availableGifts) {
            String option = getGiftName(giftItem.getType()) + " (x" + giftItem.getAmmount() + ")";
            /** When found */
            if (option.equals(chosen)) {
                /** Detrmine pet increase ammounts */
                int increase = giftItem.getIncrease();
                int newHappiness = p.getHappiness() + increase;
                p.setHappiness(Math.min(100, newHappiness));
                giftItem.setAmmount(giftItem.getAmmount() - 1);
                initiateScreenVitals();
                /** Check if pet should exit Angry state. */
                checkAndUpdatePetState();
                p.setScore(p.getScore() + 1);
                JOptionPane.showMessageDialog(this, "You gave your pet a " + getGiftName(giftItem.getType()) + "! Happiness increased by " + increase + ".");
                return;
            }
        }
    }

    // -------------------------------------------------------------------------
    /**
     * Button click for feed command
     * 
     * @param e event clicked
     * @see Item.java
     * @see checkCommandAllowed
     * @see checkAndUpdatePetState
     */
    private void feedButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!checkCommandAllowed("feed"))
            return; /** Disallow if pet’s state prevents feeding. */

        /** Get list of food */
        List<Item> availableFoods = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Item foodItem = inventory[i];
            if (foodItem.getAmmount() > 0 && foodItem.isUsable(p)) {
                availableFoods.add(foodItem);
            }
        } /** Case no food */
        if (availableFoods.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No usable food items available!");
            return;
        }

       /**  Create an array of option strings for the selection of food. */
        String[] options = new String[availableFoods.size()];
        for (int i = 0; i < availableFoods.size(); i++) {
            /** Show the foods name along with its available amount. */
            options[i] = getFoodName(availableFoods.get(i).getType()) + " (x" + availableFoods.get(i).getAmmount() + ")";
        }

        /** Prompt the user to choose a food item. */
        String chosen = (String) JOptionPane.showInputDialog(null,"Choose a food item to use:","Feed Pet", JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
        if (chosen == null) {
            return;
        }
         /** Find and use the selected food item. */
        for (Item foodItem : availableFoods) {
            String option = getFoodName(foodItem.getType()) + " (x" + foodItem.getAmmount() + ")";
            /** When found */
            if (option.equals(chosen)) {
                /** Detrmine pet increase ammounts */
                int increase = foodItem.getIncrease();
                int newFullness = p.getFullness() + increase;
                p.setFullness(Math.min(100, newFullness));
                foodItem.setAmmount(foodItem.getAmmount() - 1);
                initiateScreenVitals();
                /** Check if pet should exit hungry state. */
                checkAndUpdatePetState();
                p.setScore(p.getScore() + 1);
                JOptionPane.showMessageDialog(this, "You fed your pet " + getFoodName(foodItem.getType())
                        + "! Fullness increased by " + increase + ".");
                return;
            }
        }
    }
    /**
     * Get the name of a food based on type
     * 
     * @param type
     * @return String food type to name
     * @see Item.java
     */
    private String getFoodName(int type) {
        switch (type) {
            case 1:
                return "Kibble";
            case 2:
                return "Grilled Chicken";
            case 3:
                return "Deluxe Feast";
            default:
                return "Unknown Food";
        }
    }
    /**
     * Get the name of a gift based on type
     * 
     * @param type
     * @return String gift type to name
     * @see Item.java
     */
    private String getGiftName(int type) {
        switch (type) {
            case 1:
                return "Squeaky Toy";
            case 2:
                return "Cozy Blanket";
            case 3:
                return "Golden Collar";
            default:
                return "Unknown Gift";
        }
    }

    // -------------------------------------------------------------------------
    /**
     * Play Button command clicked
     * 
     * @param e event
     * @see Pet.java
     * @see checkCommandAllowed
     * @see isCooldownReady
     * @see checkAndUpdatePetState
     */
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!checkCommandAllowed("play"))
            return; /** Disallow if pet’s state prevents play. */
            /** Confirm cooldon is over, set last time used to now if so */
        if (isCooldownReady("Play", lastPlayTime, playCooldown)) {
            lastPlayTime = Instant.now();
            /** Update pet */
            p.setHappiness(Math.min(100, p.getHappiness() + 10));
            initiateScreenVitals();
            JOptionPane.showMessageDialog(this, "Your pet played!");
            p.setScore(p.getScore() + 1);
            checkAndUpdatePetState();
        }
    }
    /**
     * Pasue game command clicked
     * 
     * @param e event
     * @see PasueScreenUI.java
     */
    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pauseButtonActionPerformed
        /** Open pasue screen */
        PasueScreenUI parentLogin = new PasueScreenUI(Gameplay.this);
        parentLogin.setLocationRelativeTo(Gameplay.this);
        parentLogin.setVisible(true);
        /* Pasuse all timers and music */
        decayTimer.stop();
        flipTimer.stop();
        MusicPlayer.pauseMusic();
    }// GEN-LAST:event_pauseButtonActionPerformed

    /**
     * SWing elements variables, generated by netBeans GUI builder
     */
     // Variables declaration
     // Variables declaration - do not modify//GEN-BEGIN:variables

    private javax.swing.JPanel background;
    private javax.swing.JButton bedButton;
    private javax.swing.JButton exerciseButton;
    private javax.swing.JButton feedButton;
    private javax.swing.JLabel fullness;
    private javax.swing.JLabel fullnessLabel;
    private javax.swing.JButton giftButton;
    private javax.swing.JLabel happiness;
    private javax.swing.JLabel happinessLabel;
    private javax.swing.JLabel health;
    private javax.swing.JLabel healthLabel;
    private javax.swing.JButton inventoryButton;
    private javax.swing.JButton pauseButton;
    private javax.swing.JLabel petSprite;
    private javax.swing.JButton playButton;
    private javax.swing.JLabel roomBackground;
    private javax.swing.JLabel score;
    private javax.swing.JLabel scoreFrame;
    private javax.swing.JLabel scoreLabel;
    private javax.swing.JLabel sleep;
    private javax.swing.JLabel sleepLabel;
    private javax.swing.JButton vetButton;
    private javax.swing.JLabel whiteboardImage;
    // End of variables declaration//GEN-END:variables
}
