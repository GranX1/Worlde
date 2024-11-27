package Wordle;

import java.util.ArrayList;
import java.util.List;

public class WordleGame {
    final String targetWord;
    private final int maxAttempts;
    private int currentAttempt;
    private final List<String> attempts;

    public WordleGame(String targetWord, int maxAttempts) {
        this.targetWord = targetWord;
        this.maxAttempts = maxAttempts;
        this.currentAttempt = 0;
        this.attempts = new ArrayList<>();
    }

    public String checkGuess(String guess) {
        if (guess.length() != targetWord.length()) {
            return "La palabra debe tener " + targetWord.length() + " letras.";
        }

        currentAttempt++;
        attempts.add(guess);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < guess.length(); i++) {
            char guessChar = guess.charAt(i);
            if (guessChar == targetWord.charAt(i)) {
                result.append(guessChar).append(" (✔) ");
            } else if (targetWord.indexOf(guessChar) != -1) {
                result.append(guessChar).append(" (⚠) ");
            } else {
                result.append(guessChar).append(" (✘) ");
            }
        }

        return result.toString();
    }

    public boolean isGameOver() {
        return currentAttempt >= maxAttempts;
    }

    public boolean hasWon(String guess) {
        return guess.equals(targetWord);
    }

    public List<String> getAttempts() {
        return attempts;
    }

    // Agregar este método para obtener el número de intentos realizados
    public int getAttemptCount() {
        return currentAttempt;
    }
}
