/*Assignment 5
Program that manages reminders for each day of The current month
Linked list file
eprobst2
251369388
CS2211 */
#include <stdio.h>
#include "linked_list.h"
#include <stdlib.h>
#include <string.h>
//--------------------------------------------------------------------------
void addNode(Node **head, const char *value) { // Add a node to the linked list
    Node *newNode = malloc(sizeof(Node)); // Allocate memory for new node
    strcpy(newNode->reminder, value); // Copy the reminder into the new node
    newNode->next = NULL; // Set the new node's next to NULL (as it will be the last node)
    if (*head == NULL) { // If the list is empty
        *head = newNode; // Set the new node as the head of the list
    } else {
        Node *current = *head; // Start at the head
        while (current->next != NULL) { // Traverse to the last node
            current = current->next;
        }
        current->next = newNode; // Set the next of the last node to the new node
    }
}
//--------------------------------------------------------------------------
void freeList(Node **head) { // Free list from memory
    Node *current = *head; // Set current to head
    while (current) { // While nodes exist
        Node *temp = current; // Store node temporarily
        current = current->next; // Set node to next node
        free(temp); // Free the node
    }
    *head = NULL; // Free head
}

