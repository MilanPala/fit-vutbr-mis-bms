CFLAGS=-std=c99 -g
BIN=bms1
CC=gcc
RM=rm -f

SOUBORY=main.c parser.c parser.h

ALL: $(SOUBORY)
	$(CC) $(CFLAGS) -o $(BIN) $(SOUBORY)

clean:
	$(RM) *.o $(BIN)
