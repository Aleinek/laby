#ifndef PIECIOKAT_HPP
#define PIECIOKAT_HPP

#include "Figura.hpp"

class Pieciokat : public Figura {
private:
    double bok;

public:
    Pieciokat(double bok) : bok(bok) {}

    double obwod() const override {
        return 5 * bok;
    }

    double pole() const override {
        return (5 * bok * bok) / (4 * std::tan(M_PI / 5));
    }

    std::string nazwa() const override {
        return "Pięciokąt foremny";
    }
};

#endif // PIECIOKAT_HPP