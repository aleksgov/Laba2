import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T> implements Iterable<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
        }
    }

    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Введенный индекс выходит за рамки - " + index);
        }
        Node<T> newNode = new Node<>(element);
        if(index == 0) {
            if (head == null) {
                head = tail = newNode;
            } else {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            }
        } else if (index == size) {
            add(element);
        } else {
            Node<T> current = getNode(index);
            newNode.next = current;
            newNode.prev = current.prev;
            current.prev.next = newNode;
            current.prev = newNode;
        }
        size++;
    }

    public T get(int index) {
        Node<T> node = getNode(index);
        return node.data;
    }

    public T remove(int index) {
        Node<T> current = getNode(index);
        if(current.prev != null) {
            current.prev.next = current.next;
        } else {
            head = current.next;
        }
        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            tail = current.prev;
        }
        size--;
        return current.data;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Введенный индекс выходит за рамки - " + index);
        }
        Node<T> current;
        if(index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    public static void main(String[] args) {
        DoubleLinkedList<String> list = new DoubleLinkedList<>();

        // Добавление элементов
        list.add("Яблоко");
        list.add("Банан");
        list.add("Арбуз");
        System.out.println("После добавления элементов: ");
        list.forEach(System.out::println);

        // Вставка по индексу
        list.add(1, "Черника");
        System.out.println("\nПосле добавления Черники в индекс 1: ");
        list.forEach(System.out::println);

        // Получение элемента
        System.out.println("\nЭлемент в нулевом индексе: " + list.get(0));
        System.out.println("Элемент в первом индексе: " + list.get(1));
        System.out.println("Элемент во втором индексе: " + list.get(2));
        System.out.println("Элемент в третьем индексе: " + list.get(3));

        // Удаление элемента
        System.out.println("\nУдаления элемента на индексе 1: " + list.remove(1));
        System.out.println("После удаления: ");
        list.forEach(System.out::println);

        // Размер и пустота списка
        System.out.println("\nРазмер списка: " + list.size());
        System.out.println("Список пуст? " + list.isEmpty());
    }
}


