import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashMapa<K, V> implements  Iterable<HashMapa.Node<K, V>> {
    private Node<K, V>[] buckets;
    private int size, capacity;
    private static final float LOAD_FACTOR = 0.75f;

    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    public HashMapa() {
        this.capacity = 16;
        this.buckets = new Node[capacity];
        this.size = 0;
    }

    public V put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);

        if (buckets[index] == null) {
            buckets[index] = newNode;
        } else {
            Node<K, V> current = buckets[index];
            while (current != null) {
                if (current.key.equals(key)) {
                    V oldValue = current.value;
                    current.value = value;
                    return oldValue;
                }
                if (current.next == null) break;
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
        return null;
    }

    public V get(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public void clear() {
        buckets = new Node[capacity];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V remove(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];
        Node<K, V> previous = null;

        while (current  != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public void resize() {
        capacity *= 2;
        Node<K, V>[] newBuckets = new Node[capacity];

        for(Node<K, V> head : buckets) {
            Node<K, V> current = head;
            while (current != null) {
                int index = Math.abs(current.key.hashCode()) % capacity;
                Node<K, V> newNode = new Node<>(current.key, current.value);
                if(newBuckets[index] == null) {
                    newBuckets[index] = newNode;
                } else {
                    Node<K, V> temp = newBuckets[index];
                    while (temp.next != null) {
                        temp = temp.next;
                    }
                    temp.next = newNode;
                }
                current = current.next;
            }
        }
        buckets = newBuckets;
    }

    @Override
    public Iterator<Node<K, V>> iterator() {
        return new Iterator<Node<K, V>>() {
            private int currentBucket = 0;
            private Node<K, V> currentNode = null;

            @Override
            public boolean hasNext() {
                if (currentNode != null && currentNode.next != null) {
                    return true; // Если есть следующий узел в текущем бакете
                }
                // Перейти к следующему бакету
                while (currentBucket < buckets.length) {
                    if (buckets[currentBucket] != null) {
                        return true; // Если следующий бакет не пуст
                    }
                    currentBucket++;
                }
                return false; // Нет больше узлов
            }

            @Override
            public Node<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (currentNode == null || currentNode.next == null) {
                    currentNode = buckets[currentBucket++];
                    while (currentNode == null && currentBucket < buckets.length) {
                        currentNode = buckets[currentBucket++];
                    }
                } else {
                    currentNode = currentNode.next;
                }
                return currentNode; // Возвращаем текущий узел
            }
        };
    }

    public static void main(String[] args) {
        HashMapa<String, Integer> hashTable = new HashMapa<>();

        hashTable.put("Один", 1);
        hashTable.put("Два", 2);
        hashTable.put("Три", 3);

        System.out.println("\nРазмер таблицы: " + hashTable.size()); // 3
        System.out.println("\nПолучим индекс 'Два': " + hashTable.get("Два")); // 2
        System.out.println("\nВ таблице есть 'Три'?: " + hashTable.containsKey("Три")); // true
        System.out.println("\nУдаляем 'Один'. \nВозвращаемый индекс удаленного элемента: " + hashTable.remove("Один")); // 1
        System.out.println("\nРазмер таблицы после удаления элемента 'Один': " + hashTable.size()); // 2

        System.out.println("\nПосмотрим индексы значений в таблице:");
        for (Node<String, Integer> node : hashTable) {
            System.out.println(node.key + ": " + node.value);
        }

        hashTable.clear();
        System.out.println("\n[Очищаем таблицу]");
        System.out.println("Пуста ли таблица? - " + hashTable.isEmpty());
        System.out.println("Размер таблицы после ее очистки: " + hashTable.size()); // 0
    }
}

