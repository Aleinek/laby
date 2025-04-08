#ifndef SZESCIOKAT_HPP
#define SZESCIOKAT_HPP

#include "Figura.hpp"

class Szesciokat : public Figura {
private:
    double bok;

public:
    Szesciokat(double bok) : bok(bok) {}

    double obwod() const override {
        return 6 * bok;
    }

    double pole() const override {
        return (3 * std::sqrt(3) * bok * bok) / 2;
    }

    std::string nazwa() const override {
        return "Sześciokąt foremny";
    }
};

#endif // SZESCIOKAT_HPP