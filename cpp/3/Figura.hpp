#ifndef FIGURA_HPP
#define FIGURA_HPP

#include <string>
#include <cmath>

class Figura {
public:
    virtual double obwod() const = 0;
    virtual double pole() const = 0;
    virtual std::string nazwa() const = 0;
    virtual ~Figura() = default;
};

#endif // FIGURA_HPP