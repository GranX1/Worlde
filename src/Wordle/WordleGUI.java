package Wordle;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class WordleGUI extends JFrame {
    private WordleGame wordleGame;
    private final JPanel gridPanel; // Panel de cuadriculas

    public WordleGUI() {
        // Configurar LookAndFeel para garantizar compatibilidad entre plataformas
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        setTitle("Wordle - Java Edition");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Cargar palabras desde el archivo
        try {
            WordLoader wordLoader = new WordLoader(getClass().getResourceAsStream("/Wordle/palabras.txt"));
            String targetWord = wordLoader.getRandomWord();
            wordleGame = new WordleGame(targetWord, 6); // 6 intentos máximos
            System.out.println("Palabra seleccionada (debug): " + targetWord);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las palabras: " + e.getMessage());
            System.exit(1);
        }

        // Componentes de la interfaz
        JLabel titleLabel = new JLabel("¡Adivina la palabra!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JTextField inputField = new JTextField();
        JButton submitButton = new JButton("Probar");
        JTextPane attemptsPane = new JTextPane();
        attemptsPane.setEditable(false);

        // Panel para mostrar las cuadriculas de cada intento
        gridPanel = new JPanel(new GridLayout(6, 5, 5, 5)); // 6 intentos y 5 letras
        for (int i = 0; i < 30; i++) {
            JButton cell = new JButton();
            cell.setFont(new Font("Times", Font.PLAIN, 20));
            cell.setEnabled(false); // No es necesario que los botones sean interactivos
            gridPanel.add(cell);
        }

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);

        // Agregar componentes a la ventana
        add(titleLabel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Acción del botón
        submitButton.addActionListener(e -> {
            String guess = inputField.getText().trim().toLowerCase();
            if (guess.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa una palabra.");
                return;
            }

            String feedback = wordleGame.checkGuess(guess);
            updateGridWithFeedback(guess, feedback);

            if (wordleGame.hasWon(guess)) {
                JOptionPane.showMessageDialog(this, "¡Felicidades! Adivinaste la palabra.");
                inputField.setEnabled(false);
                submitButton.setEnabled(false);
            } else if (wordleGame.isGameOver()) {
                JOptionPane.showMessageDialog(this, "¡Se acabaron los intentos! La palabra era: " + wordleGame.targetWord);
                inputField.setEnabled(false);
                submitButton.setEnabled(false);
            }

            inputField.setText(""); // Limpiar campo de entrada
        });
    }

    private void updateGridWithFeedback(String guess, String feedback) {
        // Asegurarse de que se esté trabajando en el hilo de la interfaz de usuario
        SwingUtilities.invokeLater(() -> {
            String[] tokens = feedback.split(" ");
            int attemptCount = wordleGame.getAttemptCount();  // Obtener el intento actual antes de procesar

            for (int i = 0; i < guess.length(); i++) {
                // Calcular la celda correspondiente usando la fila actual
                JButton cell = (JButton) gridPanel.getComponent(i + (attemptCount - 1) * 5);
                char letter = guess.charAt(i);

                // Cambiar el color del texto según la retroalimentación
                if (tokens[i].contains("(✔)")) {
                    cell.setForeground(Color.GREEN);  // Color verde para la letra correcta
                } else if (tokens[i].contains("(⚠)")) {
                    cell.setForeground(Color.ORANGE); // Color naranja para la letra incorrecta pero presente
                } else {
                    cell.setForeground(Color.GRAY);   // Color gris para la letra incorrecta
                }

                // Mostrar la letra en la celda
                cell.setText(String.valueOf(letter));

                // Forzar actualización de la celda
                cell.revalidate();
                cell.repaint();
            }

            // Forzar la actualización del panel principal
            gridPanel.repaint();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WordleGUI().setVisible(true));
    }
}
