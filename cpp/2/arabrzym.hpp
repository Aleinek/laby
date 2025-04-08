#ifndef ARABRZYM_HPP
#define ARABRZYM_HPP

#include <iostream>
#include <string>
#include <stdexcept>

using namespace std;

class ArabRzym {
    private:
        static const int MIN = 1;
        static const int MAX = 3999;
    
        static const string liczby[13];
        static const int wartosci[13];
    
    public:
        static int rzym2arab(string rzym); 
    
        static string arab2rzym(int arab);
    };

class ArabRzymException : public runtime_error{
    public:
        ArabRzymException(string message) : runtime_error(message) {}
};

#endif // ARABRZYM_HPP

    