gcal: interact.o linked_list.o reminder.o 
	gcc -g -o gcal interact.o linked_list.o reminder.o

interact.o: interact.c interact.h reminder.h linked_list.h
	gcc -c -g interact.c

linked_list.o: linked_list.c linked_list.h
	gcc -c -g linked_list.c
	
reminder.o: reminder.c reminder.h
	gcc -c -g reminder.c