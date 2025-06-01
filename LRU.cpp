#include <iostream>
#include <unordered_map>
using namespace std;

struct Node {
    int key;
    int value;
    Node* prev;
    Node* next;

    Node(int k, int v) : key(k), value(v), prev(nullptr), next(nullptr) {}
};

class LRUCache {
private:
    unordered_map<int, Node*> cache_map;
    int capacity;
    int current_size;
    Node* head;
    Node* tail;

    void add_node(Node* node) {
        node->next = head->next;
        node->prev = head;
        head->next->prev = node;
        head->next = node;
    }

    void remove_node(Node* node) {
        node->prev->next = node->next;
        node->next->prev = node->prev;
    }

    void move_to_front(Node* node) {
        remove_node(node);
        add_node(node);
    }

public:
    LRUCache(int capacity) {
        this->capacity = capacity;
        current_size = 0;
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head->next = tail;
        tail->prev = head;
    }

    int get(int key) {
        if (cache_map.find(key) == cache_map.end()) {
            return -1;
        }
        Node* node = cache_map[key];
        move_to_front(node);
        return node->value;
    }

    void put(int key, int value) {
        if (cache_map.find(key) != cache_map.end()) {
            Node* node = cache_map[key];
            node->value = value;
            move_to_front(node);
        } else {
            if (current_size == capacity) {
                Node* lru_node = tail->prev;
                cache_map.erase(lru_node->key);
                remove_node(lru_node);
                delete lru_node;
                current_size--;
            }
            Node* new_node = new Node(key, value);
            add_node(new_node);
            cache_map[key] = new_node;
            current_size++;
        }
    }

    ~LRUCache() {
        Node* current = head->next;
        while (current != tail) {
            Node* next = current->next;
            delete current;
            current = next;
        }
        delete head;
        delete tail;
    }
};

int main() {
    cout << "Initializing LRUCache with capacity 2..." << endl;
    LRUCache lru(2);

    cout << "\n--- Operations ---" << endl;

    cout << "put(1, 1)" << endl;
    lru.put(1, 1);

    cout << "put(2, 2)" << endl;
    lru.put(2, 2);

    cout << "get(1): " << lru.get(1) << endl;

    cout << "put(3, 3)" << endl;
    lru.put(3, 3);

    cout << "get(2): " << lru.get(2) << endl;

    cout << "put(4, 4)" << endl;
    lru.put(4, 4);

    cout << "get(1): " << lru.get(1) << endl;
    cout << "get(3): " << lru.get(3) << endl;
    cout << "get(4): " << lru.get(4) << endl;

    cout << "\n--- End of Operations ---" << endl;

    return 0;
}
