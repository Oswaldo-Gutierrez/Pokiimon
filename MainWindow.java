import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class MainWindow extends JFrame {
    private List<Character> characters;
    private int currentPage = 0;
    private JPanel charactersPanel;
    private JButton prevButton, nextButton;

    public MainWindow(List<Character> characters) {
        this.characters = characters;
        setTitle("Enciclopedia Pokemon");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        charactersPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        add(charactersPanel, BorderLayout.CENTER);

        JPanel navPanel = new JPanel();
        prevButton = new JButton("Anterior");
        nextButton = new JButton("Siguiente");
        navPanel.add(prevButton);
        navPanel.add(nextButton);
        add(navPanel, BorderLayout.SOUTH);

        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updateCharacters();
            }
        });
        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * 4 < characters.size()) {
                currentPage++;
                updateCharacters();
            }
        });

        updateCharacters();
    }

    private void updateCharacters() {
        charactersPanel.removeAll();
        int start = currentPage * 4;
        int end = Math.min(start + 4, characters.size());
        for (int i = start; i < end; i++) {
            Character c = characters.get(i);
            JPanel panel = new JPanel(new BorderLayout());
            JLabel nameLabel = new JLabel(c.getName(), SwingConstants.CENTER);
            try {
                ImageIcon icon = new ImageIcon(new java.net.URL(c.getImageUrl()));
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(img));
                panel.add(imgLabel, BorderLayout.CENTER);
            } catch (Exception ex) {
                panel.add(new JLabel("Sin imagen", SwingConstants.CENTER), BorderLayout.CENTER);
            }
            panel.add(nameLabel, BorderLayout.SOUTH);
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            int idx = i;
            panel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    new CharacterDetailWindow(characters.get(idx));
                }
            });
            charactersPanel.add(panel);
        }
        charactersPanel.revalidate();
        charactersPanel.repaint();
    }
}
