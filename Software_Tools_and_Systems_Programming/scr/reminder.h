#ifndef REMINDER_H
#define REMINDER_H
#include <stdio.h>
#include <stdbool.h>
#include "linked_list.h"
#define MAX_STR_LEN 100

typedef struct Month { // Structure for a month
    int month_days; // Days in a month
    int start_day; // Starting day of month
    int month_idx; // Month in number form
    int curr_day; // Current day
    Node *reminders[31]; // Array for all days
} Month;
extern Month month; // Make decalration visible to other functions

// Function prototypes
void initializeMonth(); // Function to use time.h to get the current month info in real time
void insertToCalendar(int day, const char *value); // Function to instert a node into its coresponding linked list
void printCalendar(); // Function to output the calandar and the reminders for the month
void removeReminder(int day, int index); // Function to remove a reminder
void printCurrReminders(); // Function to print reminder for current day
#endif
