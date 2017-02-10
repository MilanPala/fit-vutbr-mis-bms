Program pro analýzu NMEA dat
- Autor: Milan Pála, xpalam00

Popis řešení
------------
Program projde všechny relevantní řádky (GPGGA a GPRMC) obsahující požadovaná data. Dočasné
maximální hodnoty se aktualizují při každé analýze řádku s daty. Program si tedy nepamatuje všechny
údaje z celého souboru, pouze ty požadované.

Pro uložení všech informací jsou využívány hierarchicky zanořené datové struktury reflektující
skutečnou hierarchii analyzovaných dat.