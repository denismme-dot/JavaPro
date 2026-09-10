package dz2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.HashMap;

public class main {

    public static OptionalInt findThirdLargest(List<Integer> numbers) {
        if (numbers == null || numbers.size() < 3) {
            return OptionalInt.empty();
        }

        return numbers.stream()
                //.distinct()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .mapToInt(Integer::intValue)
                .findFirst();
    }

    public static OptionalInt findThirdLargestUnique(List<Integer> numbers) {
        if (numbers == null || numbers.size() < 3) {
            return OptionalInt.empty();
        }

        return numbers.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .mapToInt(Integer::intValue)
                .findFirst();
    }

    public static List<String> getTop3OldestEngineers(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return List.of();
        }

        return employees.stream()
                .filter(e -> "Engineer".equals(e.getPosition()))
                .sorted(Comparator.comparingInt(Employee::getAge).reversed())
                .limit(3)
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    public static double getAverageAgeOfEngineers(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return 0.0;
        }
        return employees.stream()
                .filter(e -> "Engineer".equals(e.getPosition()))
                .mapToInt(Employee::getAge)
                .average()
                .orElse(0.0);
    }

    public static Optional<String> findLongestString(List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return Optional.empty();
        }
        return strings.stream()
                .max(Comparator.comparingInt(String::length));
    }

    public static HashMap<String, Long> countWords(String text) {
        if (text == null || text.isEmpty()) {
            return new HashMap<>();
        }

        return Arrays.stream(text.toLowerCase().split("\\s+"))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toMap(
                        word -> word,
                        word -> 1L,
                        Long::sum,
                        HashMap::new
                ));
    }

    public static List<String> sortByLengthThenAlphabetically(List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return List.of();
        }
        return strings.stream()
                .sorted(Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toList());
    }

    public static Optional<String> findLongestWordInArrays(String[] wordSets) {
        if (wordSets == null || wordSets.length == 0) {
            return Optional.empty();
        }
        return Arrays.stream(wordSets)
                .filter(s -> s != null && !s.isEmpty())
                .flatMap(s -> Arrays.stream(s.split("\\s+")))
                .filter(word -> !word.isEmpty())
                .max(Comparator.comparingInt(String::length));
    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(5, 2, 10, 9, 4, 3, 10, 1, 13);

        System.out.println("\n=== Case 1 ===");
        findThirdLargest(numbers)
                .ifPresentOrElse(
                        result -> System.out.println("Third largest number: " + result + " for list " + numbers.toString()),
                        () -> System.out.println("List is too small or empty")
                );
        System.out.println("\n=== Case 2 ===");
        findThirdLargestUnique(numbers)
                .ifPresentOrElse(
                        result -> System.out.println("Third largest unique number: " + result + " for list " + numbers.toString()),
                        () -> System.out.println("List is too small or empty")
                );

        System.out.println("\n=== Employee list is ===");
        List<Employee> employees = Employee.createEmployeeList();
        employees.forEach(System.out::println);

        System.out.println("\n=== Case 3 - Top 3 oldest engineers names ===");
        List<String> topEngineers = getTop3OldestEngineers(employees);
        topEngineers.forEach(name -> System.out.println("- " + name));

        double avgAge = getAverageAgeOfEngineers(employees);
        System.out.printf("\n=== Case 4 - Average age of engineers: %.2f ===", avgAge);

        System.out.println("\n=== Case 5 - Longest string ===");
        List<String> strings = Arrays.asList(
                "cat","elephant","dog","hippopotamus","bird","crocodile","ant","butterfly","fox","rhinoceros"
        );
        System.out.println("Strings: " + String.join(", ", strings));
        findLongestString(strings)
                .ifPresentOrElse(
                        longest -> System.out.println("Longest string: " + longest + " (length: " + longest.length() + ")"),
                        () -> System.out.println("List is empty or null")
                );

        String sentence = "the quick brown fox jumps over the lazy dog the fox runs fast and the dog sleeps";
        System.out.println("\n=== Case 6 - Hashmap ===\nInput string is: '"+sentence+"'");
        HashMap<String, Long> wordCounts = countWords(sentence);
        System.out.println("Word counts:");
        wordCounts.forEach((word, count) -> System.out.println("- " + word + ": " + count));

        List<String> myWords = Arrays.asList(
                "apple","banana","cherry","date","elderberry","fig","grape","honeydew","kiwi","lemon"
        );
        System.out.println("\n=== Case 7 - List of words by length ===");
        List<String> sortedWords = sortByLengthThenAlphabetically(myWords);
        System.out.println("Sorted:   " + String.join(", ", sortedWords));

        String[] wordSets = {
                "the quick brown fox jumps",
                "fat cat sat at met",
                "and eat a fat rat",
                "birds fly high in sky",
                "fish swim in the sea",
                "sun shines bright today",
                "yellow moon glows at night"
        };
        System.out.println("\n=== Case 8 - Largest word from sentences ===\nwordSet is: ");
        System.out.println(String.join(", ", wordSets));
        findLongestWordInArrays(wordSets)
                .ifPresentOrElse(
                        longest -> System.out.println("Longest word: " + longest + " (length: " + longest.length() + ")"),
                        () -> System.out.println("No words found")
                );
        System.out.println("\n---------- Happy end! -------------");
    }
}
