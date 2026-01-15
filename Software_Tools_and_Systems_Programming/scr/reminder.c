/*Assignment 5
Program that manages reminders for each day of The current month
Calendar file
eprobst2
251369388
CS2211 */
#include <stdio.h>
#include <stdlib.h>
#include "reminder.h"
#include <time.h>

Month month; //crate stucture for current month
//--------------------------------------------------------------------------
void initializeMonth(void) { // Initalize month (given in instructions)
    time_t now = time(NULL);
    struct tm *t = localtime(&now);
    month.month_idx = t->tm_mon;

    month.curr_day = t->tm_mday; // Set current day (only thing added to og algorithim)

    t->tm_mday = 1;
    mktime(t);
    month.start_day = t->tm_wday;

    month.month_days = t->tm_mday;
    while (t->tm_mon == month.month_idx){
        month.month_days = t->tm_mday;
        t->tm_mday++;
        mktime(t);
    }
}
//--------------------------------------------------------------------------
void insertToCalendar(int day, const char *value) { // Insert a reminder
    if (day < 1 || day > month.month_days) { // If bad input, return nothing
        return;
    }
    addNode(&month.reminders[day - 1], value); // Otherwise add node to calandar
}
//--------------------------------------------------------------------------
void printCalendar() { // Print the callendar
    const char *weekdays[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; // Store days of the week
    const char *months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"}; // Store months

    printf(" Sun Mon Tue Wed Thu Fri Sat\n");
    for (int i = 0; i < month.start_day; i++) { // Print blanks for days before the start
        printf("    ");
    }

    for (int day = 1; day <= month.month_days; day++) { // Loop over all days
        if (month.reminders[day - 1]) { // If the day has reminders
            printf("(%02d)", day); // Day with reminders
        } else {
            printf(" %02d ", day); // Day without reminders
        }
        if ((day + month.start_day) % 7 == 0) { // Newline every 7 days
            printf("\n");
        }
    }
    printf("\n%s Reminders:\n", months[month.month_idx]); // Reminders title
    for (int day = 1; day <= month.month_days; day++) { // Loop over all days
        Node *current = month.reminders[day - 1]; // Set current to node to the list of the day were on
        int weekdayIndex = (month.start_day + (day - 1)) % 7; // Get the day of the week
        if (current) { // If there are reminders for this day
            printf("%s %02d:: ", weekdays[weekdayIndex], day); // Day title
            int count = 1; // Check for first one
            while (current) { // Iterate through the linked list
                if(count != 1) printf("         "); // Dont print space for first one
                printf("(%d) %s", count, current->reminder); // Print the reminder
                current = current->next; // Itterate linked list
                count++; // Itterate count
            }
        }
    }
    printf("____________________________________\n");
}
//--------------------------------------------------------------------------
void printCurrReminders() { // Print todays reminders
    Node *current = month.reminders[month.curr_day - 1]; // Point to head of list for day
    if (current) { // Check if there are any reminders for the current day
        const char *weekdays[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; // Store weekdays
        const char *months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"}; // Store months
        int weekdayIndex = (month.start_day + (month.curr_day - 1)) % 7;  // Calculate the weekday index
        printf("Reminders for %s %d:\n", months[month.month_idx], month.curr_day);
        printf("%s %02d:: ", weekdays[weekdayIndex], month.curr_day); // Day title
        int count = 1;
        while (current) { // Iterate through the linked list
                if(count != 1) printf("         "); // Dont print space for first one
                printf("(%02d) %s", count, current->reminder); // Print the reminder
                current = current->next; // Itterate linked list
                count++; // Itterate count
            }
    } else {
        printf("No reminders for today.\n"); // Case empty list
    }
}
//--------------------------------------------------------------------------
void removeReminder(int day, int index) { // Remove a reminder
    if (day < 1 || day > month.month_days || !month.reminders[day - 1]) return; // Case bad day input or no reminders on day   
    Node *current = month.reminders[day - 1]; // Point to head of list for day
    Node *prev = NULL; // Previous node
    for (int i = 1; i < index; i++) { // Loop until index to remove
        if (!current) return; // Invalid index
        prev = current; // previous as current
        current = current->next; // Set current to next node
    }
    if (prev) { // If a previous node exists (not head) 
        prev->next = current->next; // Set nodes previous to its next (skip over it)
        free(current); // Free the removed node
    }
    else {  // If node is head
        month.reminders[day - 1] = current->next; // Set had as next node (skip over it)
        free(current); // Free the removed node
    }
}

