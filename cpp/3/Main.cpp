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
                int paramsCount = 0;
                while (i + paramsCount + 1 < argc &&
                       std::string("ocps").find(argv[i + paramsCount + 1]) == std::string::npos) {
                    paramsCount++;
                }

                if (paramsCount == 5) {
                    double bok1 = std::stod(argv[i + 1]);
                    double bok2 = std::stod(argv[i + 2]);
                    double bok3 = std::stod(argv[i + 3]);
                    double bok4 = std::stod(argv[i + 4]);
                    double kat = std::stod(argv[i + 5]);

                    if (kat == 90) {
                        if (bok1 <= 0 || bok2 <= 0 || bok3 <= 0 || bok4 <= 0 || kat <= 0) {
                            throw std::out_of_range("Boki i kąt muszą być większe od zera.");
                        }
                        if ((bok1 == bok3 && bok2 == bok4) || (bok1 == bok4 && bok2 == bok3)) {
                            figury.push_back(make_shared<Prostokat>(bok1, bok2));
                        } else {
                            throw std::out_of_range("Boki muszą być równe.");
                        }
                    }
                    i += 6;
                } else if (paramsCount == 2) {
                    double bok1 = std::stod(argv[i + 1]);
                    double kat = std::stod(argv[i + 2]);

                    if (bok1 <= 0 || kat <= 0) {
                        throw std::out_of_range("Boki i kąt muszą być większe od zera.");
                    }
                    if (kat == 90) {
                        figury.push_back(make_shared<Kwadrat>(bok1));
                    } else {
                        figury.push_back(make_shared<Romb>(bok1, kat));
                    }
                    i += 3;
                } else {
                    throw std::invalid_argument("Niepoprawna liczba argumentów dla czworokąta.");
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