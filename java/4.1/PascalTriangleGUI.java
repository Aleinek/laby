import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PascalTriangleGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PascalTriangleFrame().setVisible(true));
    }
}

class PascalTriangleFrame extends JFrame {
    private JTextField sizeInputField;
    private JTextPane triangleDisplayArea;

    public PascalTriangleFrame() {
        setTitle("Trójkąt Pascala");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1500);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        JLabel inputLabel = new JLabel("Podaj rozmiar trójkąta: ");
        sizeInputField = new JTextField(10);
        JButton generateButton = new JButton("Generuj");
        generateButton.addActionListener(new GenerateButtonListener());

        inputPanel.add(inputLabel);
        inputPanel.add(sizeInputField);
        inputPanel.add(generateButton);
        add(inputPanel, BorderLayout.NORTH);

        triangleDisplayArea = new JTextPane();

        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_CENTER);

        triangleDisplayArea.setEditable(false);
        triangleDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        triangleDisplayArea.setBackground(new Color(240, 248, 255)); // Light blue background
        triangleDisplayArea.setForeground(new Color(0, 51, 102)); // Dark blue text
        triangleDisplayArea.setParagraphAttributes(attributes, true);
        triangleDisplayArea.setText("Wprowadź rozmiar trójkąta i naciśnij 'Generuj'.");

        JPanel trianglePanel = new JPanel();
        trianglePanel.setLayout(new BorderLayout());
        trianglePanel.add(triangleDisplayArea);

        JScrollPane scrollPane = new JScrollPane(trianglePanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private class GenerateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String inputText = sizeInputField.getText();
                int size = Integer.parseInt(inputText);

                if (size < 0 || size > 30) {
                    throw new NumberFormatException("Rozmiar musi być liczbą dodatnią. Mniejsza od 30.");
                }

                String triangle = generatePascalTriangle(size);
                triangleDisplayArea.setText(triangle);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PascalTriangleFrame.this,
                        ex.getMessage(),
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PascalTriangleFrame.this,
                        "Wystąpił nieoczekiwany błąd: " + ex.getMessage(),
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String generatePascalTriangle(int size) {
        StringBuilder triangle = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int number = 1;
            for (int j = 0; j <= i; j++) {
                triangle.append(String.format("%4d", number));
                triangle.append(" ");
                number = number * (i - j) / (j + 1);
            }
            triangle.append("\n");
        }
        return triangle.toString();
    }
}