#ifndef ROMB_HPP
#define ROMB_HPP

#include "Czworokat.hpp"

class Romb : public Czworokat {
public:
    Romb(double bok, double kat) : Czworokat(bok, bok, bok, bok, kat) {}

    double obwod() const override {
        return 4 * bok1;
    }

    double pole() const override {
        return bok1 * bok1 * std::sin(kat * M_PI / 180);
    }

    std::string nazwa() const override {
        return "Romb";
    }
};

#endif // ROMB_HPP