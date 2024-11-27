package Wordle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordLoader {
    private List<String> words;

    public WordLoader(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        words = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            words.add(line.trim().toLowerCase()); // Convertimos todo a minúsculas
        }
    }


    public String getRandomWord() {
        if (words.isEmpty()) {
            throw new IllegalStateException("No words loaded from the file.");
        }
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }
}
