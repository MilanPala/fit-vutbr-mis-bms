#include "parser.h"

char buf[1024] = "";
const char delimiter[] = ",";

typedef enum { NIC, GPRMC, GPGGA } TOKENY;
TOKENY token;

typedef struct { double souradnice; char polokoule; } SOURADNICE;
typedef struct { SOURADNICE sirka; SOURADNICE delka; } POLOHA;
typedef struct { POLOHA poloha; double vyska; } NIVELACE;
typedef struct { POLOHA poloha; double rychlost; } RYCHLOST;
typedef struct { POLOHA nejsevernejsi; POLOHA nejjiznejsi; POLOHA nejzapadnejsi; POLOHA nejvychodnejsi; NIVELACE nejvyssi; NIVELACE nejnizsi; RYCHLOST nejrychlejsi; } MAXIMA;

char cislice[] = "0123456789";

double preved_sirku( SOURADNICE souradnice )
{
	if( souradnice.polokoule == 'N' ) return 9000.0+souradnice.souradnice;
	else return 9000.0-souradnice.souradnice;
}

double preved_delku( SOURADNICE souradnice )
{
	if( souradnice.polokoule == 'W' ) return 18000.0+souradnice.souradnice;
	else return 18000.0-souradnice.souradnice;
}

void stupne(double hodnota)
{
	int stupne = (int)hodnota/100;
	int minuty = (int)hodnota-stupne*100;
	double sekundy = (hodnota-(double)stupne*100.00-(double)minuty)*60.00;
	strcpy(buf, "");
	sprintf(buf, "%02i° %02i' %02.2f\"", stupne, minuty, sekundy);
}

int parser(FILE *file)
{
	char *tokens;
	int set = 0;
	unsigned int poradi = 0;
	MAXIMA maxima;

	POLOHA aktualni;
	NIVELACE aktualni_nivelace;
	RYCHLOST aktualni_rychlost;

	maxima.nejsevernejsi.sirka.souradnice = 9000.0;
	maxima.nejsevernejsi.delka.souradnice = 0.0;
	maxima.nejsevernejsi.sirka.polokoule = 'S';
	maxima.nejsevernejsi.delka.polokoule = 'E';

	maxima.nejjiznejsi.sirka.souradnice = 9000.0;
	maxima.nejjiznejsi.delka.souradnice = 0.0;
	maxima.nejjiznejsi.sirka.polokoule = 'N';
	maxima.nejjiznejsi.delka.polokoule = 'E';

	maxima.nejzapadnejsi.sirka.souradnice = 0.0;
	maxima.nejzapadnejsi.delka.souradnice = 18000.0;
	maxima.nejzapadnejsi.sirka.polokoule = 'N';
	maxima.nejzapadnejsi.delka.polokoule = 'E';

	maxima.nejvychodnejsi.sirka.souradnice = 0.0;
	maxima.nejvychodnejsi.delka.souradnice = 18000.0;
	maxima.nejvychodnejsi.sirka.polokoule = 'N';
	maxima.nejvychodnejsi.delka.polokoule = 'W';

	maxima.nejnizsi.poloha.delka.polokoule = 'W';
	maxima.nejnizsi.poloha.delka.souradnice = 0.0;
	maxima.nejnizsi.poloha.sirka.polokoule = 'N';
	maxima.nejnizsi.poloha.sirka.souradnice = 0.0;
	maxima.nejnizsi.vyska = 0.0;

	maxima.nejvyssi.poloha.delka.polokoule = 'W';
	maxima.nejvyssi.poloha.delka.souradnice = 0.0;
	maxima.nejvyssi.poloha.sirka.polokoule = 'N';
	maxima.nejvyssi.poloha.sirka.souradnice = 0.0;
	maxima.nejvyssi.vyska = 0.0;

	maxima.nejrychlejsi.poloha.delka.polokoule = 'W';
	maxima.nejrychlejsi.poloha.delka.souradnice = 0.0;
	maxima.nejrychlejsi.poloha.sirka.polokoule = 'N';
	maxima.nejrychlejsi.poloha.sirka.souradnice = 0.0;
	maxima.nejrychlejsi.rychlost = 0.0;


	while( fgets(buf, 1024, file) != NULL )
	{
		tokens = strtok(buf, delimiter);
		poradi = 0;
		set = 0;

		while( tokens != NULL )
		{
			if( set == 0 )
			{
				set = 1;
				if(strcmp(tokens, "$GPRMC") == 0) token = GPRMC;
				else if(strcmp(tokens, "$GPGGA") == 0) token = GPGGA;
				else set = 0;
			}
			if( set == 1)
			{
				if( token == GPRMC )
				{
					if( poradi == 3 )
					{
						aktualni.sirka.souradnice = atof(tokens);

						tokens = strtok( NULL, delimiter );
						poradi++;
						aktualni.sirka.polokoule = tokens[0];

						tokens = strtok( NULL, delimiter );
						poradi++;
						aktualni.delka.souradnice = strtod(tokens, NULL);

						tokens = strtok( NULL, delimiter );
						poradi++;
						aktualni.delka.polokoule = tokens[0];

						tokens = strtok( NULL, delimiter );
						poradi++;
						aktualni_rychlost.rychlost = strtod(tokens, NULL);

						if( aktualni_rychlost.rychlost > maxima.nejrychlejsi.rychlost )
						{
							maxima.nejrychlejsi.rychlost = aktualni_rychlost.rychlost;
							maxima.nejrychlejsi.poloha = aktualni;
						}
					}
				}
				if( token == GPGGA )
				{
					if( poradi == 2 )
					{
						if( strspn(tokens, cislice) < 4 ) { set = 0; continue; }
						aktualni.sirka.souradnice = strtod(tokens, NULL);

						tokens = strtok( NULL, delimiter );
						poradi++;
						aktualni.sirka.polokoule = tokens[0];

						tokens = strtok( NULL, delimiter );
						poradi++;
                              if( strspn(tokens, cislice) < 5 ) { set = 0; continue; }
                              aktualni.delka.souradnice = strtod(tokens, NULL);

						tokens = strtok( NULL, delimiter );
						poradi++;
						aktualni.delka.polokoule = tokens[0];

						if( preved_sirku(aktualni.sirka) > preved_sirku(maxima.nejsevernejsi.sirka) )
						{
							maxima.nejsevernejsi.sirka = aktualni.sirka;
							maxima.nejsevernejsi.delka = aktualni.delka;
						}

						if( preved_sirku(aktualni.sirka) < preved_sirku(maxima.nejjiznejsi.sirka) )
						{
							maxima.nejjiznejsi.sirka = aktualni.sirka;
							maxima.nejjiznejsi.delka = aktualni.delka;
						}

						if( preved_delku(aktualni.delka) > preved_delku(maxima.nejzapadnejsi.delka) )
						{
							maxima.nejzapadnejsi.sirka = aktualni.sirka;
							maxima.nejzapadnejsi.delka = aktualni.delka;
						}

						if( preved_delku(aktualni.delka) < preved_delku(maxima.nejvychodnejsi.delka) )
						{
							maxima.nejvychodnejsi.sirka = aktualni.sirka;
							maxima.nejvychodnejsi.delka = aktualni.delka;
						}
					}
					if( poradi == 9 )
					{
						aktualni_nivelace.vyska = atof(tokens);
						aktualni_nivelace.poloha = aktualni;

						if( aktualni_nivelace.vyska > maxima.nejvyssi.vyska )
						{
							maxima.nejvyssi = aktualni_nivelace;
						}

						if( aktualni_nivelace.vyska < maxima.nejnizsi.vyska )
						{
							maxima.nejnizsi = aktualni_nivelace;
						}
					}
				}
			}
			tokens = strtok( NULL, delimiter );
			poradi++;
		}
		token = NIC;
	}

     stupne(maxima.nejsevernejsi.sirka.souradnice);
	printf("| Nejsevernejsi misto  | %s %c ", buf, maxima.nejsevernejsi.sirka.polokoule);
	stupne(maxima.nejsevernejsi.delka.souradnice);
     printf("| %s %c\n", buf, maxima.nejsevernejsi.delka.polokoule);

     stupne(maxima.nejjiznejsi.sirka.souradnice);
	printf("| Nejjiznejsi misto    | %s %c ", buf, maxima.nejjiznejsi.sirka.polokoule);
	stupne(maxima.nejjiznejsi.delka.souradnice);
     printf("| %s %c\n", buf, maxima.nejjiznejsi.delka.polokoule);

     stupne(maxima.nejzapadnejsi.sirka.souradnice);
	printf("| Nejzapadnejsi misto  | %s %c ", buf, maxima.nejzapadnejsi.sirka.polokoule);
	stupne(maxima.nejzapadnejsi.delka.souradnice);
     printf("| %s %c\n", buf, maxima.nejzapadnejsi.delka.polokoule);

     stupne(maxima.nejvychodnejsi.sirka.souradnice);
	printf("| Nejvychodnejsi misto | %s %c ", buf, maxima.nejvychodnejsi.sirka.polokoule);
	stupne(maxima.nejvychodnejsi.delka.souradnice);
     printf("| %s %c\n", buf, maxima.nejvychodnejsi.delka.polokoule);

     stupne(maxima.nejvyssi.poloha.sirka.souradnice);
	printf("| Nejvyssi misto       | %s %c ", buf, maxima.nejvyssi.poloha.sirka.polokoule);
	stupne(maxima.nejvyssi.poloha.delka.souradnice);
     printf("| %s %c %.2f m\n", buf, maxima.nejvyssi.poloha.delka.polokoule, maxima.nejvyssi.vyska);

     stupne(maxima.nejnizsi.poloha.sirka.souradnice);
	printf("| Nejnizsi misto       | %s %c ", buf, maxima.nejnizsi.poloha.sirka.polokoule);
	stupne(maxima.nejnizsi.poloha.delka.souradnice);
     printf("| %s %c %.2f m\n", buf, maxima.nejnizsi.poloha.delka.polokoule, maxima.nejnizsi.vyska);

     stupne(maxima.nejrychlejsi.poloha.sirka.souradnice);
	printf("| Nejrychlejsi misto   | %s %c ", buf, maxima.nejrychlejsi.poloha.sirka.polokoule);
	stupne(maxima.nejrychlejsi.poloha.delka.souradnice);
     printf("| %s %c %.2f km/h\n", buf, maxima.nejrychlejsi.poloha.delka.polokoule, maxima.nejrychlejsi.rychlost*1.852);

	return 0;
}
