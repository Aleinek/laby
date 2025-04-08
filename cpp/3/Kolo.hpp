#ifndef KOLO_HPP
#define KOLO_HPP

#include "Figura.hpp"

class Kolo : public Figura {
private:
    double promien;

public:
    Kolo(double promien) : promien(promien) {}

    double obwod() const override {
        return 2 * M_PI * promien;
    }

    double pole() const override {
        return M_PI * promien * promien;
    }

    std::string nazwa() const override {
        return "Koło";
    }
};

#endif // KOLO_HPP