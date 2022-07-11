import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Hangman {
    private String word;
    private int attempts; // Available guesses remaining; decremented until loss condition
    private HashSet<Character> guessedSet;
    private String playerName;
    private int score;

    // CONSTRUCTORS
    public Hangman() {
        // Non-custom instantiation
        word = selectWord(1, 20);
        attempts = 6;
        guessedSet = new HashSet<>();
        playerName = userInput("What is your name?\n", Pattern.compile(".*"), "New lines not allowed.\n");
        score = 0;
    }

    public Hangman(int minLength, int maxLength) {
        // Custom instantiation; allows for parameterization of word length
        word = selectWord(minLength, maxLength);
        attempts = 6;
        guessedSet = new HashSet<>();
        playerName = userInput("What is your name?\n", Pattern.compile(".*"), "New lines not allowed.\n");
        score = 0;
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
    public void setPlayerName(String name) {
        this.playerName = name;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setScore(int s) {
        this.score = s;
    }
    public int getScore() {
        return score;
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

    public String generateGallows() {
        // Reads gallows ascii art from 1 of 7 'gallow' text files, returning properly formatted string
        String gallows = "";
        try {
            gallows = Files.readAllLines(Paths.get("src/main/resources/gallow%d.txt".formatted(attempts))).stream()
                    .reduce("", (acc, line) -> acc.concat(line + "\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gallows;
    }

    public String generateMissed() {
        String output = "Guessed letters: ";
        for (char c: guessedSet.stream().sorted().toList()) {
            output = output.length() == 17 ? output.concat("" + c) : output.concat(", " + c);
        }
        return output;
    }

    public String generateHiddenWord() {
        String hiddenWord = "";
        for (char c: word.toCharArray()) {
            if (guessedSet.contains(c)) {
                hiddenWord = hiddenWord.concat("" + c);
            } else {
                hiddenWord = hiddenWord.concat("_");
            }
        }
        return hiddenWord;
    }

    public static boolean userInputYesOrNo(String question) {
        // Asks the user a yes or no question, returning boolean depending on answer, using recursion for input validation
        Scanner sc = new Scanner(System.in);

        String suffix = "(y or n)\n";
        System.out.println(question + suffix);
        try {
            String response = sc.nextLine().toLowerCase();
            if (response.equals("y")) return true;
            else if (response.equals("n")) return false;
            else {
                System.out.println("Invalid input. Please enter " + suffix);
                return userInputYesOrNo(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // This statement should never be reached
        return false;
    }

    public static String userInput(String question, Pattern validInputs, String invalidInputSuffix) {
        // Asks user a question using recursion for input validation
        Scanner sc = new Scanner(System.in);

        System.out.println(question);
        try {
            String response = sc.nextLine().toLowerCase().strip();
            if (validInputs.matcher(response).matches()) return response;
            else {
                System.out.println("Invalid input. Please enter " + invalidInputSuffix);
                userInput(question, validInputs, invalidInputSuffix);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // This statement should never be reached
        return "";
    }

    public static void main(String[] args) {

    }
}