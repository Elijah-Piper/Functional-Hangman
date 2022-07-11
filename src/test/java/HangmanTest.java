import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HangmanTest {

    // Hangman.selectWord tests
    @ParameterizedTest
    @CsvSource({"7,6", "0,1", "40,60"})
    void selectWord_ReturnsEmptyStringsForInvalidSizes(int minValue, int maxValue) {
        // If no valid word is found, selectWord should return an empty string
        assertEquals("", Hangman.selectWord(minValue, maxValue));
    }
    @ParameterizedTest
    @CsvSource({"0,30", "5,5", "7,30"})
    void selectWord_ReturnsWordForValidSizes(int minValue, int maxValue) {
        // Assuming there is a valid word found, a string of length greater than 0 should be returned
        assertTrue(Hangman.selectWord(minValue, maxValue).length() > 0);
    }
}
