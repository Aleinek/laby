#ifndef KWADRAT_HPP
#define KWADRAT_HPP

#include "Czworokat.hpp"

class Kwadrat : public Czworokat {
public:
    Kwadrat(double bok) : Czworokat(bok, bok, bok, bok, 90) {}

    double obwod() const override {
        return 4 * bok1;
    }

    double pole() const override {
        return bok1 * bok1;
    }

    std::string nazwa() const override {
        return "Kwadrat";
    }
};

#endif // KWADRAT_HPP