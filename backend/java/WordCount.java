import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCount {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java WordCount <input_file> <output_file>");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args[1];

        try {
            countWords(inputFile, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void countWords(String inputFile, String outputFile) throws IOException {
        // Regular expression to match words as defined
        Pattern wordPattern = Pattern.compile("[A-Za-z0-9-]+");

        // Use a HashMap to count word frequencies
        Map<String, Integer> wordCounts = new HashMap<>();

        // Read the input file
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Convert the line to lower case and find words
                Matcher matcher = wordPattern.matcher(line.toLowerCase());
                while (matcher.find()) {
                    String word = matcher.group();
                    wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                }
            }
        }

        // Create a list from the wordCounts map for sorting
        List<Map.Entry<String, Integer>> sortedWordCounts = new ArrayList<>(wordCounts.entrySet());

        // Sort by frequency, and then alphabetically by word
        sortedWordCounts.sort((e1, e2) -> {
            int countComparison = e2.getValue().compareTo(e1.getValue());
            return countComparison != 0 ? countComparison : e1.getKey().compareTo(e2.getKey());
        });

        // Write the result to the output file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Map.Entry<String, Integer> entry : sortedWordCounts) {
                writer.write(entry.getValue() + " : " + entry.getKey());
                writer.newLine();
            }
        }
    }
}