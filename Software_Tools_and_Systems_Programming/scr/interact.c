/*Assignment 5
Program that manages reminders for each day of The current month
Interaction file
eprobst2
251369388
CS2211 */
#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include "interact.h"
#include "reminder.h"
#include "linked_list.h"
//--------------------------------------------------------------------------
int readReminder(char *str) { // Get the reminder from user
    int day; // Store day of reminder
    printf("Enter day and reminder (0 to quit): "); // Input
    scanf("%d", &day); // Input for day
    fgets(str, MAX_STR_LEN, stdin); // Input for reminder
    if (day == 0) return 0; // If its zero, no reminder so send up 0 as day
    if (day < 1 || day > month.month_days) { // If invalid day
        printf("Invalid day:: The day must be >= 1 and <= %d.\n", month.month_days);
        return -1; // Send up a basis for invalid days
    }
    return day; // Otherwise return the day
}
//--------------------------------------------------------------------------
void writeToFile(const char *filename) { // Write reminders to txt file
    FILE *file = fopen(filename, "w"); // Open file for writing
    if (!file) { // Case no file
        printf("No reminders file\n");
        return;
    }
    for (int day = 1; day <= month.month_days; day++) { // Loop over all days
        Node *current = month.reminders[day - 1]; // Sart at first node
        while (current) { // Loop over list
            fprintf(file, "%d %s\n", day, current->reminder); // Print the day and the reminder
            current = current->next; // Itterate list
        }
    }
    fclose(file); // Close the file
}
//--------------------------------------------------------------------------
void readFromFile(const char *filename) { // Read reminders from txt file
    FILE *file = fopen(filename, "r"); // Open file for reading
    if (!file) { // Case no file
        printf("No reminders file\n");
        return;
    }
    int day; // Store day to be read
    char reminder[MAX_STR_LEN]; // Store reminder to be read
    while (fscanf(file, "%d ", &day) == 1) { // Read the days, stops at no more ints
        if (fgets(reminder, MAX_STR_LEN, file)) { // Read the reminder
            insertToCalendar(day, reminder); // Add the reminder into the calandar
        }
    }
    fclose(file); // Close the file
}
//--------------------------------------------------------------------------
void handleSignal(int sig) { // Signal handler
    printf("\nProgram Terminated..\nWriting to file...\n");
    writeToFile("reminders.txt"); // Save reminders before exiting
    freeList(&month.reminders[0]); // Release list from memory
    exit(0);
}
//--------------------------------------------------------------------------
int main(int argc, char *argv[]) {
    signal(SIGINT, handleSignal);  // Handle Ctrl+C signal
    signal(SIGTERM, handleSignal); // Handle termination signal
    signal(SIGSEGV, handleSignal); // Handle segmentation signal
    initializeMonth(); // Get the month information
    readFromFile("reminders.txt");// Load reminders from file
//---------------------------------
    if (argc < 2) { // Case not enough arguments
        printf("Usage: ./gcal [view | add <day> <reminder > | remove <day> <index> | today]\n");
    }
//---------------------------------    
    else if (strcmp(argv[1], "add") == 0 && argc >= 4) { // Case add
        int day = atoi(argv[2]); // Get day as int
        char reminder[MAX_STR_LEN] = "";  // Initialize the reminder string
        for (int i = 3; i < argc; i++) { //Loop over words in reminder
            strncat(reminder, argv[i], MAX_STR_LEN - strlen(reminder) - 1);  // Concatenate all arguments starting from argv[3]
            if (i < argc - 1) {
                strncat(reminder, " ", MAX_STR_LEN - strlen(reminder) - 1);  // Add space between words (except last one)
            }
        }
        strncat(reminder, "\n", MAX_STR_LEN - strlen(reminder) - 1); // Add neww line to end
        if (day < 1 || day > month.month_days) { //Bad day input
            printf("Invalid day. Must be a number between 1 and %d.\n", month.month_days);
        } else { 
            insertToCalendar(day, reminder); // Add reminder to calandar
            printCalendar(); // Print calandar
        }
    } 
//---------------------------------
    else if (strcmp(argv[1], "remove") == 0 && argc == 4) { // Case remove
        int day = atoi(argv[2]); // get day as int
        int index = atoi(argv[3]); // Get index of reminder as int
        if (day < 1 || day > month.month_days) { // Bad day input
            printf("Invalid day. Must be a number between 1 and %d.\n", month.month_days);
        } else {
            removeReminder(day, index); // Remove the reminder
            printCalendar(); // Print calandar
        }
    }
//--------------------------------- 
    else if (strcmp(argv[1], "view") == 0) { // Case view
        printCalendar(); // Print calandar
    }
//--------------------------------- 
    else if (strcmp(argv[1], "today") == 0){ // Case today
        printCurrReminders(); // Print todays reminders
    }
//--------------------------------- 
    else { // Other arguments
        printf("Invalid arguments.\nUsage: ./gcal [view | add <day> <reminder > | remove <day> <index> | today]\n");
    }
//---------------------------------
    writeToFile("reminders.txt"); // Save reminders to file
    freeList(&month.reminders[0]); // Release list from memory
    return 0; // End
}
