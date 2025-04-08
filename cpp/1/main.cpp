#include <iostream>
#include "trojkat.hpp"

using namespace std;

int main(int argc, char* argv[]){
    if (argc > 1){
        try{
            if (stoi(argv[1]) < 0){
                cout << "Podano liczbe ujemna" << endl;
                return 1;
            }
            WierszTrojkataPascala Pascal(stoi(argv[1]));
            for(int i = 2; i < argc; i++){
                try{
                    int n = stoi(argv[i]);
                    if(n >= 0 && n <= stoi(argv[1])){
                        cout << argv[i] << " - " << Pascal.triangle[n] << endl;
                    }else{
                        cout << argv[i] << " - Podano liczbe spoza zakresu" << endl;
                    }
                }
                catch(invalid_argument){
                    cout << argv[i] << " - Podany argument nie jest liczba calkowita" << endl;
                }
            }  
        }
        catch(invalid_argument){
            cout << " - Podany argument nie jest liczba calkowita" << endl;
        }
        return 0;
    }else{
        cout << "Nie podano argumentow" << endl;
        return 1;
    }

    
}