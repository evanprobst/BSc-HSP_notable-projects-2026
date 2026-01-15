#ifndef INTERACT_H
#define INTERACT_H
#include <stdio.h>

// Function prototypes
int readReminder(char *str); // Function to get reminder input
void writeToFile(const char *filename); // Function to write to external file upon exit
void readFromFile(const char *filename); // Function to read from external file upon entry
void handleSignal(int sig); // Function to handel termination signals
#endif
