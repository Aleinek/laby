#include "BinaryTree.hpp"
#include <iostream>
#include <limits>

int main() {
    std::cout << "Wybierz typ drzewa (INTEGER, DOUBLE, STRING): ";
    std::string typStr;
    std::getline(std::cin, typStr);
    int typ = 0;
    if (typStr == "INTEGER") typ = 1;
    else if (typStr == "DOUBLE") typ = 2;
    else if (typStr == "STRING") typ = 3;
    else {
        std::cout << "Nieprawidłowy wybór typu.\n";
        return 1;
    }

    if (typ == 1) {
        BinaryTree<int> tree;
        while (true) {
            std::cout << "\nWybierz operację (insert, delete, search, draw, exit): ";
            std::string command;
            std::getline(std::cin, command);
            if (command == "insert") {
                std::cout << "Podaj liczbę: "; int v; std::cin >> v; std::cin.ignore(); tree.insert(v);
                std::cout << tree.draw();
            } else if (command == "delete") {
                std::cout << "Podaj liczbę: "; int v; std::cin >> v; std::cin.ignore(); tree.remove(v);
                std::cout << tree.draw();
            } else if (command == "search") {
                std::cout << "Podaj liczbę: "; int v; std::cin >> v; std::cin.ignore();
                std::cout << (tree.search(v) ? "Znaleziono\n" : "Nie znaleziono\n");
            } else if (command == "draw") {
                std::cout << tree.draw();
            } else if (command == "exit") break;
            else std::cout << "Nieprawidłowa komenda.\n";
        }
    } else if (typ == 2) {
        BinaryTree<double> tree;
        while (true) {
            std::cout << "\nWybierz operację (insert, delete, search, draw, exit): ";
            std::string command;
            std::getline(std::cin, command);
            if (command == "insert") {
                std::cout << "Podaj liczbę: "; double v; std::cin >> v; std::cin.ignore(); tree.insert(v);
                std::cout << tree.draw();
            } else if (command == "delete") {
                std::cout << "Podaj liczbę: "; double v; std::cin >> v; std::cin.ignore(); tree.remove(v);
                std::cout << tree.draw();
            } else if (command == "search") {
                std::cout << "Podaj liczbę: "; double v; std::cin >> v; std::cin.ignore();
                std::cout << (tree.search(v) ? "Znaleziono\n" : "Nie znaleziono\n");
            } else if (command == "draw") {
                std::cout << tree.draw();
            } else if (command == "exit") break;
            else std::cout << "Nieprawidłowa komenda.\n";
        }
    } else if (typ == 3) {
        BinaryTree<std::string> tree;
        while (true) {
            std::cout << "\nWybierz operację (insert, delete, search, draw, exit): ";
            std::string command;
            std::getline(std::cin, command);
            if (command == "insert") {
                std::cout << "Podaj napis: "; std::string v; std::cin >> v; std::cin.ignore(); tree.insert(v);
                std::cout << tree.draw();
            } else if (command == "delete") {
                std::cout << "Podaj napis: "; std::string v; std::cin >> v; std::cin.ignore(); tree.remove(v);
                std::cout << tree.draw();
            } else if (command == "search") {
                std::cout << "Podaj napis: "; std::string v; std::cin >> v; std::cin.ignore();
                std::cout << (tree.search(v) ? "Znaleziono\n" : "Nie znaleziono\n");
            } else if (command == "draw") {
                std::cout << tree.draw();
            } else if (command == "exit") break;
            else std::cout << "Nieprawidłowa komenda.\n";
        }
    }
    return 0;
}