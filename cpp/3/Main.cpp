#include "Main.h"

using namespace std;

int main(int argc, char* argv[]) {
    vector<shared_ptr<Figura>> figury;

    for (int i = 1; i < argc;) {
        string typ = argv[i];
        try {
            if (typ == "o") {
                double promien = stod(argv[i + 1]);
                figury.push_back(make_shared<Kolo>(promien));
                i += 2;
            } else if (typ == "c") {
                try {
                    double bok1 = stod(argv[i + 1]);
                    double bok2 = stod(argv[i + 2]);
                    double bok3 = stod(argv[i + 3]);
                    double bok4 = stod(argv[i + 4]);
                    double kat = stod(argv[i + 5]);
                    
                    if (kat == 90) {
                        if (bok1 == bok2 && bok3 == bok4 || bok1 == bok3 && bok2 == bok4 || bok1 == bok4 && bok2 == bok3) {
                            figury.push_back(make_shared<Prostokat>(bok1, bok3));
                        } else {
                            throw invalid_argument("Boki muszą być równe.");
                        }
                    }
                    i += 6;
                } catch (invalid_argument&) {
                    double bok1 = stod(argv[i + 1]);
                    double kat = stod(argv[i + 2]);

                    if (kat == 90) {
                        figury.push_back(make_shared<Kwadrat>(bok1));
                    } else {
                        figury.push_back(make_shared<Romb>(bok1, kat));
                    }
                    i += 3;
                }
            } else if (typ == "p") {
                double bok = stod(argv[i + 1]);
                figury.push_back(make_shared<Pieciokat>(bok));
                i += 2;
            } else if (typ == "s") {
                double bok = stod(argv[i + 1]);
                figury.push_back(make_shared<Szesciokat>(bok));
                i += 2;
            } else {
                cout << "Nieznany typ figury." << endl;
                return 1;
            }
        } catch (exception& e) {
            cout << "Błąd: " << e.what() << endl;
            return 1;
        }
    }

    for (const auto& figura : figury) {
        cout << figura->nazwa() << " - Pole: " << figura->pole() << ", Obwód: " << figura->obwod() << endl;
    }

    return 0;
}