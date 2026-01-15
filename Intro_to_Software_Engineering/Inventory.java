/**
 * The Inventory class extends JFrame, providing a graphical user interface to display an inventory of items.
 * It categorizes items into food and gifts and updates their quantities in the display based on the inventory data provided.
 */
public class Inventory extends javax.swing.JFrame {

    private Item[] inventory; // Array of items representing the inventory.

    /**
     * Constructs an Inventory window with the specified array of items.
     * Initializes the GUI components and updates the display labels according to the inventory content.
     *
     * @param inventory Array of Item objects to be displayed in the inventory.
     */
    public Inventory(Item[] inventory) {
        this.inventory = inventory;
        initComponents();
        updateInventoryLabels();
    }

    /**
     * Updates the labels for each item in the inventory based on its current amount.
     * It categorizes items into food and gifts, and displays their quantities under the appropriate labels.
     * Food and gift items are differentiated by their category codes: 0 for food, and 1 for gifts.
     */
    private void updateInventoryLabels() {
        for (Item item : inventory) {
            int category = item.getCatogory();
            int type = item.getType();
            int amount = item.getAmmount();

            if (category == 0) { // Category 0: Food items
                switch (type) {
                    case 1: kibbleCount.setText(String.valueOf(amount)); break;
                    case 2: chickenCount.setText(String.valueOf(amount)); break;
                    case 3: feastCount.setText(String.valueOf(amount)); break;
                }
            } else if (category == 1) { // Category 1: Gift items
                switch (type) {
                    case 1: ballCount.setText(String.valueOf(amount)); break;
                    case 2: blanketCount.setText(String.valueOf(amount)); break;
                    case 3: collarCount.setText(String.valueOf(amount)); break;
                }
            }
        }
    }

    /**
     * Initializes the components of the Inventory window.
     * This method prepares the graphical user interface, setting up the layout, labels, panels, and buttons.
     * The detailed settings for the components such as fonts, sizes, and layout options are configured here.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Component initialization code goes here
  

        borderPanel = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        giftLabel = new javax.swing.JLabel();
        foodLabel = new javax.swing.JLabel();
        kibbleLabel = new javax.swing.JLabel();
        feastLabel = new javax.swing.JLabel();
        balnketLabel = new javax.swing.JLabel();
        blanketCount = new javax.swing.JLabel();
        ballCount = new javax.swing.JLabel();
        feastCount = new javax.swing.JLabel();
        ballLabel = new javax.swing.JLabel();
        kibbleCount = new javax.swing.JLabel();
        chickenLabel = new javax.swing.JLabel();
        chickenCount = new javax.swing.JLabel();
        collarLabel = new javax.swing.JLabel();
        collarCount = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(250, 500));
        setMinimumSize(new java.awt.Dimension(250, 500));
        setAlwaysOnTop(true);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(250, 500));
        setResizable(false);

        borderPanel.setBackground(new java.awt.Color(0, 0, 0));
        borderPanel.setMaximumSize(new java.awt.Dimension(250, 500));
        borderPanel.setMinimumSize(new java.awt.Dimension(250, 500));

        mainPanel.setBackground(new java.awt.Color(249, 241, 229));
        mainPanel.setMaximumSize(new java.awt.Dimension(238, 488));
        mainPanel.setMinimumSize(new java.awt.Dimension(238, 488));
        mainPanel.setPreferredSize(new java.awt.Dimension(238, 488));

        titleLabel.setFont(new java.awt.Font("Kristen ITC", 0, 36)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Inventory");
        titleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        giftLabel.setFont(new java.awt.Font("Kristen ITC", 0, 24)); // NOI18N
        giftLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        giftLabel.setText("Gifts");
        giftLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        foodLabel.setFont(new java.awt.Font("Kristen ITC", 0, 24)); // NOI18N
        foodLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        foodLabel.setText("Food");
        foodLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        kibbleLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        kibbleLabel.setText("Kibble");

        feastLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        feastLabel.setText("Deluxe Feast");

        balnketLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        balnketLabel.setText("Cozy Blanket");

        blanketCount.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        blanketCount.setText("00");

        ballCount.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        ballCount.setText("00");

        feastCount.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        feastCount.setText("00");

        ballLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        ballLabel.setText("Squeaky Toy");

        kibbleCount.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        kibbleCount.setText("00");

        chickenLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        chickenLabel.setText("Grilled Chicken");

        chickenCount.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        chickenCount.setText("00");

        collarLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        collarLabel.setText("Golden Collar");

        collarCount.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        collarCount.setText("00");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(giftLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(foodLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(backButton))
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addComponent(kibbleLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(kibbleCount))
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addComponent(feastLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(feastCount))
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addComponent(chickenLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(chickenCount))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(balnketLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(collarLabel)
                    .addComponent(ballLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(collarCount)
                    .addComponent(blanketCount)
                    .addComponent(ballCount))
                .addGap(21, 21, 21))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addGap(37, 37, 37)
                .addComponent(giftLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ballLabel)
                    .addComponent(ballCount))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(balnketLabel)
                    .addComponent(blanketCount))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(collarLabel)
                    .addComponent(collarCount))
                .addGap(20, 20, 20)
                .addComponent(foodLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kibbleLabel)
                    .addComponent(kibbleCount))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chickenLabel)
                    .addComponent(chickenCount))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(feastLabel)
                    .addComponent(feastCount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(backButton)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout borderPanelLayout = new javax.swing.GroupLayout(borderPanel);
        borderPanel.setLayout(borderPanelLayout);
        borderPanelLayout.setHorizontalGroup(
            borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        borderPanelLayout.setVerticalGroup(
            borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(borderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(borderPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(borderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
//---------------------------------------------------------------------------------------------------------
// interaction methods
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_backButtonActionPerformed
//---------------------------------------------------------------------------------------------------------
// Variables declaration
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JLabel ballCount;
    private javax.swing.JLabel ballLabel;
    private javax.swing.JLabel balnketLabel;
    private javax.swing.JLabel blanketCount;
    private javax.swing.JPanel borderPanel;
    private javax.swing.JLabel chickenCount;
    private javax.swing.JLabel chickenLabel;
    private javax.swing.JLabel collarCount;
    private javax.swing.JLabel collarLabel;
    private javax.swing.JLabel feastCount;
    private javax.swing.JLabel feastLabel;
    private javax.swing.JLabel foodLabel;
    private javax.swing.JLabel giftLabel;
    private javax.swing.JLabel kibbleCount;
    private javax.swing.JLabel kibbleLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
