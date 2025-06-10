#ifndef BINARY_TREE_HPP
#define BINARY_TREE_HPP

#include <iostream>
#include <string>
#include <queue>

template <typename T>
class BinaryTree {
private:
    struct Node {
        T data;
        Node* left;
        Node* right;
        
        Node(const T& value) : data(value), left(nullptr), right(nullptr) {}
    };
    
    Node* root;
    
    // Helper function for recursive deletion
    void deleteTree(Node* node) {
        if (node) {
            deleteTree(node->left);
            deleteTree(node->right);
            delete node;
        }
    }
    
    // Helper function for recursive insertion
    Node* insertRecursive(Node* node, const T& value) {
        if (!node) {
            return new Node(value);
        }
        
        if (value < node->data) {
            node->left = insertRecursive(node->left, value);
        } else if (value > node->data) {
            node->right = insertRecursive(node->right, value);
        }
        
        return node;
    }
    
    // Helper function for recursive search
    Node* searchRecursive(Node* node, const T& value) const {
        if (!node || node->data == value) {
            return node;
        }
        
        if (value < node->data) {
            return searchRecursive(node->left, value);
        }
        return searchRecursive(node->right, value);
    }
    
    // Helper function for finding minimum value node
    Node* findMin(Node* node) {
        while (node->left) {
            node = node->left;
        }
        return node;
    }
    
    // Helper function for recursive deletion
    Node* deleteRecursive(Node* node, const T& value) {
        if (!node) return nullptr;
        
        if (value < node->data) {
            node->left = deleteRecursive(node->left, value);
        } else if (value > node->data) {
            node->right = deleteRecursive(node->right, value);
        } else {
            // Node with only one child or no child
            if (!node->left) {
                Node* temp = node->right;
                delete node;
                return temp;
            } else if (!node->right) {
                Node* temp = node->left;
                delete node;
                return temp;
            }
            
            // Node with two children
            Node* temp = findMin(node->right);
            node->data = temp->data;
            node->right = deleteRecursive(node->right, temp->data);
        }
        return node;
    }

public:
    BinaryTree() : root(nullptr) {}
    
    ~BinaryTree() {
        deleteTree(root);
    }
    
    // Insert a value into the tree
    void insert(const T& value) {
        root = insertRecursive(root, value);
    }
    
    // Search for a value in the tree
    bool search(const T& value) const {
        return searchRecursive(root, value) != nullptr;
    }
    
    // Delete a value from the tree
    void remove(const T& value) {
        root = deleteRecursive(root, value);
    }

    // Draw the tree structure as a string (like in the Java example)
    std::string draw() const {
        std::string sb;
        drawRecursive(root, "", true, sb);
        return sb;
    }

private:
    // Helper function for drawing the tree as a string
    void drawRecursive(Node* node, const std::string& prefix, bool isTail, std::string& sb) const {
        if (!node) return;
        if (node->right) {
            drawRecursive(node->right, prefix + (isTail ? "│   " : "    "), false, sb);
        }
        sb += prefix;
        sb += (isTail ? "└── " : "┌── ");
        sb += toString(node->data);
        sb += "\n";
        if (node->left) {
            drawRecursive(node->left, prefix + (isTail ? "    " : "│   "), true, sb);
        }
    }
    // Helper to convert node data to string
    template<typename U>
    std::string toString(const U& value) const {
        using std::to_string;
        return to_string(value);
    }
    // Specialization for std::string
    std::string toString(const std::string& value) const {
        return value;
    }
};

#endif