#include <iostream>
#include <string>
#include <stdexcept>

#include "arabrzym.hpp"

using namespace std;

const string ArabRzym::liczby[13] = { "I","IV","V","IX","X","XL","L","XC","C","CD","D","CM","M" };
const int ArabRzym::wartosci[13] = { 1,4,5,9,10,40,50,90,100,400,500,900,1000 };

int ArabRzym::rzym2arab(string rzym) {
    int wynik = 0;
    int i = 0;
    int j = 12;

    int ile = 0;
    while (i < rzym.length()) {
        if (rzym.substr(i).find(liczby[j]) == 0) {
            wynik += wartosci[j];
            i += liczby[j].length();

            ile++;
            if (ile>3){
                throw ArabRzymException("Nieprawidłowy format liczby rzymskiej");
            }

            if (liczby[j].length() == 2) {
                j--;
            }

            if (j == 2 || j == 6 || j == 11){
                        j -= 2;
                    }
        } else {
            if (j<0){
                throw ArabRzymException("Nieprawidłowy format liczby rzymskiej");
            }
            j--;
        }
        }

    if (wynik < MIN || wynik > MAX) {
        throw ArabRzymException("Liczba spoza zakresu");
    }
    return wynik;
}

string ArabRzym::arab2rzym(int arab) {
    if (arab < MIN || arab > MAX) {
        throw ArabRzymException("Liczba spoza zakresu");
    }
    string wynik;
    int i = 12;
    while (arab > 0) {
        if (wartosci[i] <= arab) {
            wynik += liczby[i];
            arab -= wartosci[i];
        } else {
            i--;
        }
    }
    return wynik;
}