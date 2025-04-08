#ifndef PROSTOKAT_HPP
#define PROSTOKAT_HPP

#include "Czworokat.hpp"

class Prostokat : public Czworokat {
public:
    Prostokat(double bok1, double bok2) : Czworokat(bok1, bok2, bok1, bok2, 90) {}

    double obwod() const override {
        return 2 * (bok1 + bok2);
    }

    double pole() const override {
        return bok1 * bok2;
    }

    std::string nazwa() const override {
        return "Prostokąt";
    }
};

#endif // PROSTOKAT_HPP