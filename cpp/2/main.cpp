#include <iostream>
#include <string>
#include <stdexcept>

#include "arabrzym.hpp"

using namespace std;

int main(int argc, char* argv[]) {
    for (int i = 1; i < argc; i++) {
        try {
            try {
                int arab = stoi(argv[i]);
                cout << ArabRzym::arab2rzym(arab) << endl;
            } catch (const invalid_argument& error) {
                cout << ArabRzym::rzym2arab(argv[i]) << endl;
            }
        } catch (const ArabRzymException& error) {
            cout << error.what() << endl;
        }
    }
    return 0;
}