import java.awt.Color; //test
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.time.*;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.PrintWriter;

/**
 * Parental Controls screen where users can restrict app usage, see statistics, and revive pets.
 * 
 * @version 1.0
 * @author group55
 */
public class ParentalControls extends javax.swing.JFrame {
    /** The file in which parental controls and statistics are saved. Must be in the same directory as this class.*/
    private static String filename = "savefile.csv";
    /** The hour at which the player is allowed to begin playing the game.*/
    private static String parentalStartTime;
    /** The hour by which the player must cease playing the game.*/
    private static String parentalEndTime;

    // ---------------------------------------------------------------------------------------------------------

    /**
     * 
     * Calculates and displays the player's statistics.
     * 
     */
    private void showStats() {
        try {
            String current = readFromFile(4); // line 5 (zero-based index)
            if (current == null) {
                throw new IOException("Save file is missing line 5 (parental controls/stats).");
            }
            System.out.println(current);
            String[] vals = current.split(",");
            String newString = "";
            String totalTime;
            String avgSessions;
            String numSessions;
            int sessions;
            int time;
            totalTime = vals[2];
            numSessions = vals[3];
            sessions = Integer.parseInt(numSessions);
            time = Integer.parseInt(totalTime);
            System.out.println(String.valueOf(sessions) + "," + String.valueOf(time));
            int avgSession = 0;
            // avoid divide by 0 error
            if (sessions != 0)
                avgSession = time / sessions;

            totalPlaytimeLabel.setText("Total Playtime:    " + totalTime + " minutes");
            avgSessionLabel.setText("Average Session:     " + String.valueOf(avgSession) + " minutes");
            if (isRestricted())
                restrictButton.setSelected(true);
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Error with save file, ensure save is in in correct format");
            JOptionPane.showMessageDialog(this, "Error with save file, ensure all saves are in the correct format");
            System.exit(0);
        }

    }

      /**
     * Retrieves saved information and reflects whether or not restrictions are enabled, and if so, the current restricted hours.
     * 
     * @see isRestricted
     */
    private void initializeRestrictions(){
        // Select restricted button if needed
        if (isRestricted()) restrictButton.setSelected(true);
        // read restrictionsfrom file
        try {
            File saveFile = new File(filename);
            BufferedReader fileReader = new BufferedReader(new FileReader(saveFile));
            String currLine;
            for (int i = 0; i < 4; i++) {
                currLine = fileReader.readLine();
            }
            String parentControlLine = fileReader.readLine();
            String[] controls = parentControlLine.split(","); 
            parentalStartTime = controls[0];
            parentalEndTime = controls[1];
            LocalTime startTime = LocalTime.parse(parentalStartTime);
            LocalTime endTime = LocalTime.parse(parentalEndTime);
            int startHourInt = startTime.getHour();
            int startMinuteInt = startTime.getMinute();
            int endHourInt = endTime.getHour();
            int endMinuteInt = endTime.getMinute();
            if (startHourInt >= 12){
                startAMPM.setSelectedItem("PM");
                startHourInt = startHourInt -12;
            }
            if (endHourInt >= 12){
                endAMPM.setSelectedItem("PM");
                endHourInt = endHourInt -12;
            }
            startHour.setSelectedItem(String.valueOf(startHourInt));
            startMin.setSelectedItem(String.valueOf(startMinuteInt));
            endHour.setSelectedItem(String.valueOf(endHourInt));
            endMin.setSelectedItem(String.valueOf(endMinuteInt));
           
            fileReader.close();
        } catch (FileNotFoundException e){
            System.out.println("Error: File not found");
        } catch (IOException e) {
            System.out.println("Error");
        }

    }

    /**
     * ParentalControls constructor. Creates a new ParentalControls object, or screen.
     * 
     * <br>Initializes the on-screen components, and calls the {@link showStats} and {@link initializeRestrictions} functions, to calculate and display user statistics and restrictions. </br> 
     */
    public ParentalControls() {
        initComponents();
        showStats();
        initializeRestrictions();
    }

    // ---------------------------------------------------------------------------------------------------------
    // Swing components
    @SuppressWarnings("unchecked")
    /**
     * Initializes components to show on screen. 
     * Citation: Generated using the NetBeans IDE design tool.
     */
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        userLimLabel = new javax.swing.JLabel();
        revPetLabel = new javax.swing.JLabel();
        statLabel = new javax.swing.JLabel();
        resPlayHoursLabel = new javax.swing.JLabel();
        setStartTimeLabel = new javax.swing.JLabel();
        totalPlaytimeLabel = new javax.swing.JLabel();
        avgSessionLabel = new javax.swing.JLabel();
        totalPlaytime = new javax.swing.JLabel();
        avgSession = new javax.swing.JLabel();
        resetStatsButton = new javax.swing.JButton();
        petOneLabel = new javax.swing.JLabel();
        petTwoLabel = new javax.swing.JLabel();
        petThreeLabel = new javax.swing.JLabel();
        petOneRevive = new javax.swing.JButton();
        petTwoRevive = new javax.swing.JButton();
        petThreeRevive = new javax.swing.JButton();
        restrictButton = new javax.swing.JToggleButton();
        setAllowedTimes1 = new javax.swing.JLabel();
        startHour = new javax.swing.JComboBox<>();
        startAMPM = new javax.swing.JComboBox<>();
        startMin = new javax.swing.JComboBox<>();
        endHour = new javax.swing.JComboBox<>();
        endAMPM = new javax.swing.JComboBox<>();
        endMin = new javax.swing.JComboBox<>();
        saveButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Perfect Pet Pals - Parental Controls");
        jPanel1.setBackground(new java.awt.Color(249, 241, 229));
        jPanel1.setMaximumSize(new java.awt.Dimension(800, 800));
        jPanel1.setMinimumSize(new java.awt.Dimension(800, 800));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 800));

        title.setFont(new java.awt.Font("Kristen ITC", 1, 48)); // NOI18N
        title.setText("Parental Controls Menu");

        userLimLabel.setFont(new java.awt.Font("Kristen ITC", 1, 24)); // NOI18N
        userLimLabel.setText("User Limitations");

        revPetLabel.setFont(new java.awt.Font("Kristen ITC", 1, 24)); // NOI18N
        revPetLabel.setText("Revive Pet");

        statLabel.setFont(new java.awt.Font("Kristen ITC", 1, 24)); // NOI18N
        statLabel.setText("Statistics");

        resPlayHoursLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        resPlayHoursLabel.setText("Restrict Play Hours: ");

        setStartTimeLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        setStartTimeLabel.setText("Set Start Time:");

        totalPlaytimeLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        totalPlaytimeLabel.setText("Total Playtime:    10");

        avgSessionLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        avgSessionLabel.setText("Average Session:     9");

        resetStatsButton.setFont(new java.awt.Font("Kristen ITC", 1, 18)); // NOI18N
        resetStatsButton.setText("Reset Stats");
        resetStatsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetStatsButtonActionPerformed(evt);
            }
        });
        resetFileButton = new javax.swing.JButton();
        resetFileButton.setFont(new java.awt.Font("Kristen ITC", 1, 18)); 
        resetFileButton.setText("Reset Save File");
        resetFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFileButtonActionPerformed(evt);
            }
        });


        // Revive Section Under

        ArrayList<String> petNames = getPetNamesFromCSV("savefile.csv");

        petOneLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N

        petTwoLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N

        petThreeLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N

        petOneRevive.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        petOneRevive.setText("Revive");
        petOneRevive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                petOneReviveActionPerformed(evt);
            }
        });

        petTwoRevive.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        petTwoRevive.setText("Revive");
        petTwoRevive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                petTwoReviveActionPerformed(evt);
            }
        });

        petThreeRevive.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        petThreeRevive.setText("Revive");
        petThreeRevive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                petThreeReviveActionPerformed(evt);
            }
        });

        restrictButton.setBackground(new java.awt.Color(0, 204, 0));
        restrictButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        restrictButton.setForeground(new java.awt.Color(255, 255, 255));
        restrictButton.setText("Unrestricted");
        restrictButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                restrictButtonStateChanged(evt);
            }
        });
        restrictButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        restrictButton.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
            }
        });

        setAllowedTimes1.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        setAllowedTimes1.setText("Set End Time:");

        startHour.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        startHour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startHourActionPerformed(evt);
            }
        });

        startAMPM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AM", "PM" }));

        startMin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "15", "30", "45", }));

        endHour.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        endAMPM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AM", "PM" }));

        endMin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "15", "30", "45", " " }));

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        backButton.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(60, 60, 60)
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                jPanel1Layout.createSequentialGroup()
                                                                        .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(avgSessionLabel)
                                                                                .addComponent(totalPlaytimeLabel)
                                                                                // .addComponent(avgSession)
                                                                                // .addComponent(totalPlaytime)
                                                                                .addComponent(resetStatsButton)
                                                                                .addComponent(resetFileButton)

                                                                                .addComponent(revPetLabel)
                                                                                .addComponent(statLabel)
                                                                                .addComponent(setAllowedTimes1))
                                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(setStartTimeLabel)
                                                                        .addComponent(resPlayHoursLabel))
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addGap(94, 94, 94)
                                                                                .addComponent(saveButton))
                                                                        .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(
                                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                        jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(endHour,
                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                        50,
                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(endMin,
                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                        47,
                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(endAMPM,
                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                        57,
                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel1Layout
                                                                                        .createSequentialGroup()
                                                                                        .addGap(5, 5, 5)
                                                                                        .addComponent(restrictButton,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                161,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel1Layout
                                                                                        .createSequentialGroup()
                                                                                        .addGap(2, 2, 2)
                                                                                        .addComponent(startHour,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                48,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addPreferredGap(
                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                        .addComponent(startMin,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                47,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addPreferredGap(
                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                        .addComponent(startAMPM,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                57,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                jPanel1Layout.createSequentialGroup()
                                                                        .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(petOneLabel)
                                                                                .addGroup(jPanel1Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                        .addComponent(petTwoLabel)
                                                                                        .addComponent(petThreeLabel))
                                                                                .addComponent(userLimLabel))
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(petOneRevive,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        88, Short.MAX_VALUE)
                                                                                .addComponent(petTwoRevive,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE)
                                                                                .addComponent(petThreeRevive,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE)))))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(title)
                                                .addGap(23, 23, 23)))
                                .addGap(123, 123, 123))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(backButton)
                                .addGap(375, 375, 375)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(title)
                                .addGap(42, 42, 42)
                                .addComponent(userLimLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(resPlayHoursLabel)
                                        .addComponent(restrictButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(setStartTimeLabel)
                                                .addGap(18, 18, 18)
                                                .addComponent(setAllowedTimes1))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(startMin, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(startHour, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(startAMPM, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(endMin, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(endAMPM, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(endHour, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(4, 4, 4)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(saveButton)
                                .addGap(5, 5, 5)
                                .addComponent(statLabel)
                                .addGap(18, 18, 18)
                                .addComponent(avgSessionLabel)
                                .addGap(18, 18, 18)
                                .addComponent(totalPlaytimeLabel)
                                .addGap(18, 18, 18)
                                .addComponent(resetStatsButton)
                                .addComponent(resetFileButton)

                                
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(revPetLabel)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(petOneLabel)
                                        .addComponent(petOneRevive, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(44, 44, 44)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(petTwoLabel)
                                        .addComponent(petTwoRevive, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(petThreeLabel)
                                        .addComponent(petThreeRevive, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(backButton)
                                .addContainerGap()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));

        /** display pet-specific information on screen */

        /** Pet One */
        if (petNames.size() > 0 && !petNames.get(0).isEmpty()) {
            petOneLabel.setText(petNames.get(0));
            petOneLabel.setVisible(true);
            petOneRevive.setVisible(true);
        } else {
            petOneLabel.setText("No pets are created!");
            ;
            petOneRevive.setVisible(false);
        }

        /** Pet Two */
        if (petNames.size() > 1 && !petNames.get(1).isEmpty()) {
            petTwoLabel.setText(petNames.get(1));
            petTwoLabel.setVisible(true);
            petTwoRevive.setVisible(true);
        } else {
            petTwoLabel.setVisible(false);
            petTwoRevive.setVisible(false);
        }

        /** Pet Three */
        if (petNames.size() > 2 && !petNames.get(2).isEmpty()) {
            petThreeLabel.setText(petNames.get(2));
            petThreeLabel.setVisible(true);
            petThreeRevive.setVisible(true);
        } else {
            petThreeLabel.setVisible(false);
            petThreeRevive.setVisible(false);
        }

        pack();
    }

    /**
     * Retrieves pet names from the save file to display in the revive pet section.
     * @param filePath where the save file is stored.
     * @return an ArrayList of Strings containing the pet names.
     */
    private ArrayList<String> getPetNamesFromCSV(String filePath) {
        ArrayList<String> petNames = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null && petNames.size() < 3) {
                lineCount++;
                // skip the first row containing header information
                if (lineCount == 1)
                    continue;
                // don't parse if line starts with -1 (pet doesn't exist) 
                if (line.trim().equals("-1"))
                    break; // stop parsing when reached -1 (-1 = pet does't exist)

                String[] columns = line.split(",");
                if (columns.length >= 3 && !columns[2].trim().isEmpty()) {
                    petNames.add(columns[2].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return petNames;
    }

    /** 
     * Retrieves a pet's state from a file.
     * @param filePath the location of the save file.
     * @param petIndex the pet to be checked.
     */
    private int getPetStateFromCSV(String filePath, int petIndex) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int currentLine = 0;
            int petLineCount = 0;

            while ((line = reader.readLine()) != null) {
                currentLine++;

                // skip header line
                if (currentLine == 1)
                    continue;
                // don't parse if pet doesn't exist
                if (line.trim().equals("-1"))
                    break; 
                if (petLineCount == petIndex) {
                    String[] columns = line.split(",");
                    return Integer.parseInt(columns[1].trim()); // Column 1 = pet state
                }

                petLineCount++;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return -1; // invalid state
    }

    /**
     * Returns whether or not the parent has enabled restrictions for the player's play time. 
     * 
     * Reads from the save file using the {@link readFromFile} method.
     * @return true if the parent has enabled restrictions, and false if not. 
     */
    public static boolean isRestricted() {
        String[] vals = ParentalControls.readFromFile(4).split(",");
        if (Integer.parseInt(vals[5]) == 1)
            return true;
        return false;
    }


    /**
     * Method stub generated by NetBeans IDE GUI builder, logic built by team.
     * 
     * Resets the statistics of the player when the Reset Stats button is pressed.
     * 
     * @param evt the information passed when the button is pressed.
     */
    private void resetStatsButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_petThreeReviveActionPerformed
        /** the updated line that will be stored in the file after the statistics return to 0*/
        String hold = "";
        /** an ArrayList storing the current lines in the file */ 
        ArrayList<String> lines = new ArrayList<String>();
        try {
            
            File saveFile = new File("savefile.csv");
            /** store the current content of the files */
            BufferedReader fileReader = new BufferedReader(new FileReader(saveFile));
            String currLine;
            for (int i = 0; i < 4; i++) {
                if ((currLine = fileReader.readLine()) != null) {
                    lines.add(currLine);
                }
            }
            /** take in the relevant Parental Controls line  */
            String[] vals = fileReader.readLine().split(",");
            fileReader.close();
            vals[2] = "0";
            vals[3] = "0";
            /** create the new parental controls line */
            for (int i = 0; i < vals.length; i++) {
                hold = hold + vals[i];
                if (i != vals.length - 1)
                    hold = hold + ",";
            }
            PrintWriter fileWriter = new PrintWriter(new FileWriter("savefile.csv", false));
            /** write the new lines back to the file */
            for (int i = 0; i < 4; i++) {
                System.out.println(lines.get(i));
                fileWriter.println(lines.get(i));
            }
            fileWriter.println(hold);
            fileWriter.close();

        } catch (IOException e) {
            System.out.println("error");
        }
        /** change labels on screen */
        totalPlaytimeLabel.setText("Total Playtime:    0 minutes");
        avgSessionLabel.setText("Average Session:    0 minutes");
    }

    /**
     * Reads the current line stored in the save file.
     * 
     * Uses the class attribute saveFile.
     * @param lineNum the line to be read. 0 means the first line of the file.
     * @return String representation of the line.
     */
    public static String readFromFile(int lineNum) {
        String currLine;
        File saveFile = new File(filename);
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(saveFile));
            /** read lines until you reach the intended line. */
            for (int i = 0; i < lineNum; i++) {
                currLine = fileReader.readLine();
            }
            /** read and return the next line */
            currLine = fileReader.readLine();
            fileReader.close();
            return currLine;
            /** check if there is an error in the save file */
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Error with save file, ensure exists and all lines are in the correct format, See README for file requierments");
            System.exit(0);
            return null;
        }
    }

    /**
     * Writes a string to the parental controls section of the save file.
     * 
     * Uses the class attribute saveFile.
     * @param line the string to be written.
     */
    private void writeToFile(String line) {
        /** first, read and save the existing information */ 
        String currLine;
        ArrayList<String> lines = new ArrayList<String>();
        File saveFile = new File(filename);
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(saveFile));
           /** read and add each line to the array list */
            for (int i = 0; i < 4; i++) {
                if ((currLine = fileReader.readLine()) != null) {
                    lines.add(currLine);
                }
            }
            /** check if there is an issue in the save file */
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Error with save file, ensure exists and all lines are in the correct format, See README for file requierments");
            System.exit(0);
        }
        try {
            /** Rewrite each line, as well as the new line, into the file */
            PrintWriter fileWriter = new PrintWriter(new FileWriter("savefile.csv", false));
            for (int i = 0; i < 4; i++) {
                System.out.println(lines.get(i));
                fileWriter.println(lines.get(i));
            }
            fileWriter.println(line);
            fileWriter.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error with save file, ensure exists and all lines are in the correct format, See README for file requierments");
            System.exit(0);
        }
    }

    /** 
     * Helper method to revive a specific pet. 
     * @param lineNum number to be changed in the save file
     * @param petIndex index of the pet that will be revived
     * 
     */
    private void revivePet(int lineNum, int petIndex) {
        /** Retrieves the pet state  */
        int petState = getPetStateFromCSV("savefile.csv", petIndex); // column 1 = Pet State
        String current = ParentalControls.readFromFile(lineNum);
        String[] vals = current.split(",");
        String newString = "";
        ArrayList<String> petNames = getPetNamesFromCSV("savefile.csv");
        String petName = petNames.get(petIndex);

        /** Check if pet is dead */
        if (petState != 1) {
            JOptionPane.showMessageDialog(this, "Cannot revive, " + petName + " is not dead!");
            return;
        }

        /** Change state and happiness, fullness, and sleep to 100 */
        vals[1] = "5";
        vals[4] = "100";
        vals[5] = "100";
        vals[6] = "100";
        /** Change health based on pet type */
        if (Integer.parseInt(vals[0]) == 1) {
            vals[3] = "150";
        }
        if (Integer.parseInt(vals[0]) == 2) {
            vals[3] = "100";
        }
        if (Integer.parseInt(vals[0]) == 3) {
            vals[3] = "75";
        }
        for (int i = 0; i < vals.length; i++) {
            newString = newString + vals[i];
            if (i != vals.length - 1)
                newString = newString + ",";
        }
        writeToFile(newString, lineNum);
        JOptionPane.showMessageDialog(this, petName + " is now revived!");
    }

    /** 
     * Helper method to write to the save file. 
     * @param line string to be written
     * @param lineNum the line number in the save file to be changed.
     * 
     */
    private void writeToFile(String line, int lineNum) {
        /** first, read and save the existing information */
        String currLine;
        ArrayList<String> lines = new ArrayList<String>();
        File saveFile = new File(filename);
        try {
            /** Save current information */
            BufferedReader fileReader = new BufferedReader(new FileReader(saveFile));
            for (int i = 0; i < 5; i++) {
                if ((currLine = fileReader.readLine()) != null) {
                    lines.add(currLine);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error with save file, ensure exists and all lines are in the correct format, See README for file requierments");
            System.exit(0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error with save file, ensure exists and all lines are in the correct format, See README for file requierments");
            System.exit(0);
        }
        try {
            
            PrintWriter fileWriter = new PrintWriter(new FileWriter("savefile.csv", false));
            /** Write up until the line to be changed */
            for (int i = 0; i < lineNum; i++) {
                System.out.println(lines.get(i));
                fileWriter.println(lines.get(i));
            }
            /** Add new line */
            fileWriter.println(line);
            /** Write the following lines */
            for (int i = lineNum + 1; i < 5; i++) {
                System.out.println(lines.get(i));
                fileWriter.println(lines.get(i));
            }
            fileWriter.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error with save file, ensure exists and all lines are in the correct format, See README for file requierments");
            System.exit(0);
        }
    }

    /** 
     * Revives pet one. 
     * @see revivePet
     */
    private void petOneReviveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_petOneReviveActionPerformed
        // TODO add your handling code here:
        revivePet(1, 0);
    }// GEN-LAST:event_petOneReviveActionPerformed
    // -------------------------------------------------------------------------

    /** 
     * Revives pet two. 
     * @see revivePet
     */
    private void petTwoReviveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_petTwoReviveActionPerformed
        // TODO add your handling code here:
        revivePet(2, 1);
    }// GEN-LAST:event_petTwoReviveActionPerformed
    // -------------------------------------------------------------------------

    /** 
     * Revives pet three. 
     * @see revivePet
     */
    private void petThreeReviveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_petThreeReviveActionPerformed
        // TODO add your handling code here:
        revivePet(3, 2);
    }// GEN-LAST:event_petThreeReviveActionPerformed
    // -------------------------------------------------------------------------

    /** 
     * Helper method to intialize the starting and ending times for the time restrictions, in the Parental Controls class
     */
    private void getAllowedTimeFrame() {
        /** Take all of the starting and endingtime information frpm the user selection, converted to String*/
        String startHourString = startHour.getSelectedItem().toString();
        String startMinString = startMin.getSelectedItem().toString();
        String endHourString = endHour.getSelectedItem().toString();
        String endMinString = endMin.getSelectedItem().toString();

        /** Convert the strings to integer */
        int startHourInt = Integer.parseInt(startHourString);
        int endHourInt = Integer.parseInt(endHourString);

        /** check if hour is a single digit number, add 0 if so as the format "0x" is needed to parse into a LocalTime object */

        if (startHourInt < 10) {
            startHourString = "0" + String.valueOf(startHourInt);
        }
        if (endHourInt < 10) {
            endHourString = "0" + String.valueOf(endHourInt);
        }

        /** convert into military time */
        if (startAMPM.getSelectedItem().toString().equals("PM") && !startHourString.equals("12")) {
            startHourInt += 12;
            startHourString = String.valueOf(startHourInt);
        }
        if (endAMPM.getSelectedItem().toString().equals("PM") && !startHourString.equals("12")) {
            endHourInt += 12;
            endHourString = String.valueOf(endHourInt);
        }
        /** Create a string with the starting and ending hour to store in the class */
        String startTime = startHourString + ":" + startMinString + ":00";
        String endTime = endHourString + ":" + endMinString + ":00";
        ParentalControls.parentalStartTime = startTime;
        ParentalControls.parentalEndTime = endTime;
        LocalTime startTimeLT = LocalTime.parse(startTime);
        LocalTime endTimeLT = LocalTime.parse(endTime);

    }// GEN-LAST:event_restrictButtonStateChanged

    // -------------------------------------------------------------------------
    /** 
     * Action performed on save. 
     * Saves the user's current timing selection into the class attributes, and updates the save file.
     * Citation: NetBeans IDE GUI builder generated the method stub for this.
     */
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
        /** String representations of the starting and ending hours and minutes */
        getAllowedTimeFrame();
        /** Read the current parental controls line, and store into a String array with the different values */
        String current = readFromFile(4);
        String[] vals = current.split(",");
        String newString = "";
        vals[0] = ParentalControls.parentalStartTime;
        vals[1] = ParentalControls.parentalEndTime;
        if (restrictButton.isSelected()) {
            vals[5] = "1";
        } else {
            vals[5] = "0";
        }
        /** Store the new values into a string */
        for (int i = 0; i < vals.length; i++) {
            newString = newString + vals[i];
            if (i != vals.length - 1)
                newString = newString + ",";
        }
        /** Write the new string into the parental controls line of the save file */
        writeToFile(newString);

    }

    // ---------------------------------------------------------------------------------------------------------

    // ---------------------------------------------------------------------------------------------------------
    //
    /** 
     * Update the color of the resticted button based on whether or not it is selected.
     */
    private void restrictButtonStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_restrictButtonStateChanged
        // TODO add your handling code here:
        if (restrictButton.isSelected()) {
            restrictButton.setText("Restricted");
            restrictButton.setBackground(new Color(176, 39, 39));
        } else {
            restrictButton.setText("Unrestricted");
            restrictButton.setBackground(new Color(50, 168, 82));
        }

    }// GEN-LAST:event_saveButtonActionPerformed
    /**
     * Completely resets the savefile.csv to the default template.
     */
    private void resetFileButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset the entire save file?\nThis will erase all pets and stats!", "Confirm Reset", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (PrintWriter writer = new PrintWriter(new FileWriter("savefile.csv", false))) {
            writer.println("Pet Type,Pet State,Pet Name,Pet Health,Pet Sleep,Pet Happiness,Pet Fullness,Score,Amount of Kibble item,Amount of Grilled Chicken item,Amount of Deluxe feast item,Amount of Squeaky toy item,Amount of Cozy Blanket item,Amount of Golden Collar item");
            writer.println("-1");
            writer.println("-1");
            writer.println("-1");
            writer.println("00:00:00,23:45:00,0,0,00:00:00.0,0");
            JOptionPane.showMessageDialog(this, "Save file has been reset to default values.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: Could not reset the save file.");
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------

    private void startHourActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_startHourActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_startHourActionPerformed
    // -------------------------------------------------------------------------

    /** 
     * Returns to Main Menu
     */
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        MainMenu menuScreen = new MainMenu();
        menuScreen.setLocationRelativeTo(ParentalControls.this);
        menuScreen.setVisible(true);
        this.dispose();
    }// GEN-LAST:event_backButtonActionPerformed
    // ---------------------------------------------------------------------------------------------------------
    // Variables declaration
    /** Code below is generated by NetBeans GUI Builder */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avgSessionLabel;
    private javax.swing.JLabel avgSession;
    private javax.swing.JLabel totalPlaytime;
    private javax.swing.JButton backButton;
    private javax.swing.JComboBox<String> endAMPM;
    private javax.swing.JComboBox<String> endHour;
    private javax.swing.JComboBox<String> endMin;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel petOneLabel;
    private javax.swing.JButton petOneRevive;
    private javax.swing.JLabel petThreeLabel;
    private javax.swing.JButton petThreeRevive;
    private javax.swing.JLabel petTwoLabel;
    private javax.swing.JButton petTwoRevive;
    private javax.swing.JLabel resPlayHoursLabel;
    private javax.swing.JButton resetStatsButton;
    private javax.swing.JToggleButton restrictButton;
    private javax.swing.JLabel revPetLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel setAllowedTimes1;
    private javax.swing.JLabel setStartTimeLabel;
    private javax.swing.JComboBox<String> startAMPM;
    private javax.swing.JComboBox<String> startHour;
    private javax.swing.JComboBox<String> startMin;
    private javax.swing.JLabel statLabel;
    private javax.swing.JLabel title;
    private javax.swing.JLabel totalPlaytimeLabel;
    private javax.swing.JLabel userLimLabel;
    private javax.swing.JButton resetFileButton;

    // End of variables declaration//GEN-END:variables
}
