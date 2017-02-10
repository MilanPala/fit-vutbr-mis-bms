/* 
 * File:   main.c
 * Author: xpalam00
 *
 * Created on 6. říjen 2010, 9:52
 */

#include <stdio.h>
#include <stdlib.h>

#include "parser.h"

/*
 * 
 */
int main(int argc, char** argv)
{
    if( argc != 2 )
    {
        fputs("Je nutne zadat nazev souboru.\n", stderr);
        return EXIT_FAILURE;
    }

    FILE *file_in;

    file_in = fopen(argv[1], "r");
    if( !file_in )
    {
        fputs("Nepodarilo se otevrit vstupni soubor s daty.\n", stderr);
        return EXIT_FAILURE;
    }

    parser(file_in);

    fclose(file_in);

    return (EXIT_SUCCESS);
}

