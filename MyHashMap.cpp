#include <iostream>
#include <vector>
using namespace std;

struct Node {
    int key;
    int value;
    Node* next;

    Node(int k, int v) : key(k), value(v), next(nullptr) {}
};

class MyHashMap {
private:
    vector<Node*> buckets;
    int capacity;
    int num_elements;
    const double LOAD_FACTOR_THRESHOLD = 0.7;

    int hash(int key) {
        return key % capacity;
    }

    int get_next_capacity(int current_capacity) {
        return current_capacity * 2;
    }

    void resize() {
        int old_capacity = capacity;
        capacity = get_next_capacity(old_capacity);
        vector<Node*> new_buckets(capacity, nullptr);

        for (int i = 0; i < old_capacity; ++i) {
            Node* current = buckets[i];
            while (current != nullptr) {
                Node* next_node = current->next;
                int new_bucket_idx = hash(current->key);
                current->next = new_buckets[new_bucket_idx];
                new_buckets[new_bucket_idx] = current;
                current = next_node;
            }
        }

        buckets.clear();
        buckets = move(new_buckets);
    }

public:
    MyHashMap() {
        capacity = 10000;
        num_elements = 0;
        buckets.resize(capacity, nullptr);
    }

    void put(int key, int value) {
        int bucket_idx = hash(key);
        Node* current = buckets[bucket_idx];

        while (current != nullptr) {
            if (current->key == key) {
                current->value = value;
                return;
            }
            current = current->next;
        }

        Node* new_node = new Node(key, value);
        new_node->next = buckets[bucket_idx];
        buckets[bucket_idx] = new_node;
        num_elements++;

        if ((double)num_elements / capacity > LOAD_FACTOR_THRESHOLD) {
            resize();
        }
    }

    int get(int key) {
        int bucket_idx = hash(key);
        Node* current = buckets[bucket_idx];

        while (current != nullptr) {
            if (current->key == key) {
                return current->value;
            }
            current = current->next;
        }
        return -1;
    }

    void remove(int key) {
        int bucket_idx = hash(key);
        Node* current = buckets[bucket_idx];
        Node* prev = nullptr;

        while (current != nullptr) {
            if (current->key == key) {
                if (prev == nullptr) {
                    buckets[bucket_idx] = current->next;
                } else {
                    prev->next = current->next;
                }
                delete current;
                num_elements--;
                return;
            }
            prev = current;
            current = current->next;
        }
    }

    ~MyHashMap() {
        for (int i = 0; i < capacity; ++i) {
            Node* current = buckets[i];
            while (current != nullptr) {
                Node* next_node = current->next;
                delete current;
                current = next_node;
            }
        }
    }
};

int main() {
    cout << "Initializing MyHashMap..." << endl;
    MyHashMap obj;

    cout << "\n--- Operations ---" << endl;

    cout << "put(1, 10)" << endl;
    obj.put(1, 10);

    cout << "put(2, 20)" << endl;
    obj.put(2, 20);

    cout << "get(1): " << obj.get(1) << endl;
    cout << "get(3): " << obj.get(3) << endl;

    cout << "put(2, 30)" << endl;
    obj.put(2, 30);

    cout << "get(2): " << obj.get(2) << endl;

    cout << "remove(2)" << endl;
    obj.remove(2);

    cout << "get(2): " << obj.get(2) << endl;

    cout << "\n--- End of Operations ---" << endl;

    return 0;
}





