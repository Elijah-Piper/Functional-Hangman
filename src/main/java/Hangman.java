import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Hangman {
    private String word;
    private int attempts; // Available guesses remaining; decremented until loss condition
    private HashSet<Character> guessedSet;

    // CONSTRUCTORS
    public Hangman() {
        // Non-custom instantiation
        word = selectWord(1, 20);
        attempts = 6;
        guessedSet = new HashSet<>();
    }

    public Hangman(int minLength, int maxLength) {
        // Custom instantiation; allows for parameterization of word length
        word = selectWord(minLength, maxLength);
        attempts = 6;
        guessedSet = new HashSet<>();
    }

    // GETTERS AND SETTERS
    public void setWord(String w) {
        this.word = w;
    }
    public String getWord() {
        return word;
    }
    public void setAttempts(int a) {
        this.attempts = a;
    }
    public int getAttempts() {
        return attempts;
    }
    public boolean addToGuessedSet(char c) {
        // Case-insensitive; converts capital to lower-case
        // Returns true if character is not already in guessedSet, else false
        if (guessedSet.contains(c)) {
            return false;
        }
        this.guessedSet.add(("" + c).toLowerCase().charAt(0));
        return true;
    }
    public HashSet<Character> getGuessedSet() {
        return new HashSet<Character>(guessedSet);
    }

    public static String selectWord(int minLength, int maxLength) {
        if (maxLength < minLength) return "";

        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/wordrepo.txt"));
            Collections.shuffle(lines); // Shuffles order of word repository to introduce randomness

            // Filters out word that don't match the desired word based on word length and content
            Pattern wordPat = Pattern.compile("[0-9- ]"); // For excluding words with numbers, hyphen, or spaces
            return lines.stream().filter(
                    line -> line.length() >= minLength && line.length() <= maxLength && !wordPat.matcher(line).find())
                    .findFirst().orElse("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {

    }
}