import java.util.*;

public class MapGrouper {

    public static Map<Integer, List<String>> groupByLength(String[] words) {
        Map<Integer, List<String>> wordMap = new HashMap<>();
        for (String word : words) {
            int length = word.length();
            wordMap.computeIfAbsent(length, k -> new ArrayList<>()).add(word);
        }
        return wordMap;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите слова через запятую: ");
        String input = scanner.nextLine();
        String[] words = input.split(",\\s*");

        Map<Integer, List<String>> groupedWords = groupByLength(words);
        System.out.println("Группировка слов по длине: " + groupedWords);
    }
}
