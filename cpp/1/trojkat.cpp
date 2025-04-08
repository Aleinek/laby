#include "trojkat.hpp"

WierszTrojkataPascala::WierszTrojkataPascala(int n) {
    triangle = new int[n + 1];
    triangle[0] = 1;

    for (int i = 1; i <= n; i++) {
        for (int j = i; j > 0; j--) {
            triangle[j] += triangle[j - 1];
        }
    }
}

WierszTrojkataPascala::~WierszTrojkataPascala() {
    delete[] triangle;
}