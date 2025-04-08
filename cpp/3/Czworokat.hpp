#ifndef CZWOROKAT_HPP
#define CZWOROKAT_HPP

#include "Figura.hpp"

class Czworokat : public Figura {
protected:
    double bok1, bok2, bok3, bok4, kat;

public:
    Czworokat(double bok1, double bok2, double bok3, double bok4, double kat)
        : bok1(bok1), bok2(bok2), bok3(bok3), bok4(bok4), kat(kat) {}
};

#endif // CZWOROKAT_HPP