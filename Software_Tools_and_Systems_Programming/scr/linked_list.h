#ifndef LINKED_LIST_H
#define LINKED_LIST_H
#include <stdio.h>
#define MAX_STR_LEN 100

typedef struct Node { // Structure for a node in a linked list
    char reminder[MAX_STR_LEN]; // Reminder stored in it
    struct Node *next; // Next node
} Node;

// Function prototypes
void addNode(Node **head, const char *value); // Function to add a node to the linked list
void freeList(Node **head); // Function to free all nodes in the linked list from memory
#endif
