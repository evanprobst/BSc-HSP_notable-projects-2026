Welcome to Perfect Pet Pals - the ideal game for children looking to learn key responsibility skills in an enjoyable way!

As the user/manager of this application, here is some of the key information you will need to know. 

A short description of your software and what it does: 
Our game, Perfect Pet Pals, aims to provide children, aged 7 and up, with an engaging, interactive, and enjoyable experience that enhances their responsibility skills. This will do so by offering users the opportunity to select one or more pets which it will take care of by taking actions like feeding it, taking it to the veterinarian's office, playing with the pet, and giving it gifts. Users will encounter a simple game layout that will be easy for even young children to become accustomed to. Our game will set ourselves apart by including music to also stimulate the player’s auditory senses, and further immerse them in this game. 

A list of the required libraries and third party tools required to run or build your software (include version numbers):
There are no additional third-party tools required to run or build our software. All that you would require is Java, the latest version of which you can download at https://www.oracle.com/ca-en/java/technologies/downloads/, and access to a terminal to compile and run the executable file.  

A detailed step by step guide for building your software (compiling it from source code). This should include details on how to obtain and install any third party libraries.
Within the zip folder you currently find yourself in, you should see multiple .java files, for each of the classes we created. Using the Windows File Explorer program, extract the .zip file, and locate these .java classes in the extracted folder, then follow these steps.
1. Right-click on the folder, and click "Open in Terminal". Depending on your version of Windows, you may need to click "Show more options", then "Open in Terminal"
2. For each .java file, enter the command, 'javac {classname}.java', where classname is the text preceding the .java.
3. Once each class has been successfully compiled, run the program by entering the command 'java TitleScreenUI.java'


A detailed step by step guide on how to run your already built (compiled) software.
Within the zip folder you currently find yourself in, you should see a program entitled "group55.jar". This is an executable file that, when run, will launch our prgoram. Using the Windows File Explorer program, extract the .zip file and locate the "group55.jar" file in the extracted folder, then follow these steps.
1. Right-click on the folder, and click "Open in Terminal". Depending on your version of Windows, you may need to click "Show more options", then "Open in Terminal"
2. Enter the command: 'java -jar group55.jar'
3. Open and enjoy our game! 

A user guide, that explains how to use your software.

1. The program will open on the Title Screen, where you can click anywhere to begin. 
2. There will be four buttons on the main menu, the Play Game, Tutorial, Parental Controls, or Exit button. 
3. Start off by visiting the Tutorial and getting a sense of the graphics, visuals, and commands relevant to our game. 
4. Next, you can try your hand at the Parental Controls screen, and experiment by editing the restricted start and time frames, and whether or not you want to restrict the timing of your game. To enter this screen, enter the pin found in the next section of this file.
5. Once you are happy with your restrictions, begin playing the game! When you first open the program, no pets will be pre-loaded onto the game, so you can create a new pet, choosing from a dog, cat, or fish, and name it! Once you are on the game screen, you will notice that your pets vitals, found in the top left of your screen, begin going down. To boost their vitals, use the following commands. 

Feed: To increase fullness
Exercise: To increase health (while decreasing sleep and fullness)
Play: To increase happiness
Gift: To increase happiness
Bed: To increase sleep - your pet will stay in this state until sleep stays at 100.

Note: Certain commands will not be available in certain states. 

6. Once you are happy with your session, click the pause button in the top right to 'Save and Exit', then you will be brought back to the main menu screen. From there, you can click 'Exit' to safely exit the game.

If your software uses accounts, a password, or pin you must include any account username/password, pin, etc. required to use your software.

Parental Controls pin: 2212

You must also include details on how to access your parental controls and steps to build/install it if it is a separate program:
To access parental controls, go to the Main Menu and click 'Parental Controls'.

Anything else that would be helpful for the TA marking your project to know.

File Formats: 
Once the game .zip folder is downloaded, we ensure that the file format is such that our source code can easefully parse and utilize the information stored, to enable the storing and loading of game files. To maintain this, ensure the following format for the "savefile.csv" is not altered.

Line 1: Header information, containing the columns related to the pet information
Line 2: Pet 1 information (reference the following column format)
Line 3: Pet 2 information
Line 4: Pet 3 information
Line 5: Parental Controls information, storing Start Time, End Time, Total Play Time, Number of Sessions, Time of Last Open, Whether or not the game is restricted (0 for no, 1 for yes)

Pet information lines can either be filled with pet information, or can contain only -1, -1 indicating that this save slot is avalible to create a new pet (as seen the the below example Line 4)

Example file
Line 1: Pet Type,Pet State,Pet Name,Pet Health,Pet Sleep,Pet Happiness,Pet Fullness,Score,Amount of Kibble item,Amount of Grilled Chicken item,Amount of Deluxe feast item,Amount of Squeaky toy item,Amount of Cozy Blanket item,Amount of Golden Collar item
Line 2: 1,5,Evan,150,96,92,84,0,20,16,8,22,16,20
Line 3: 2,5,peter,100,100,100,100,0,11,5,1,11,5,1
Line 4: -1
Line 5: 07:45:00,11:00:00,1,5,23:49:11.854564100,0


To differentiate the pets, we included different rates of vital decline, which can be found on the choosing a pet screen, and different starting healths. 