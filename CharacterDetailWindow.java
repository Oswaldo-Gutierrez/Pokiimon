import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class CharacterDetailWindow extends JFrame {
    public CharacterDetailWindow(Character character) {
        setTitle(character.getName());
        setSize(420, 520);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        // Header: image + title
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(character.getImageUrl()));
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            header.add(imgLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel noImg = new JLabel("Sin imagen", SwingConstants.CENTER);
            header.add(noImg, BorderLayout.CENTER);
        }

        JLabel title = new JLabel(character.getName(), SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(new EmptyBorder(8, 0, 0, 0));
        header.add(title, BorderLayout.SOUTH);

        content.add(header, BorderLayout.NORTH);

        // Description area with border and custom font
        JTextArea descArea = new JTextArea(character.getDescription());
        descArea.setFont(new Font("Serif", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setOpaque(true);
        descArea.setBackground(new Color(250, 250, 250));

        JScrollPane scroll = new JScrollPane(descArea);
        Border outside = new LineBorder(Color.LIGHT_GRAY, 1, true);
        Border inside = new EmptyBorder(8, 8, 8, 8);
        scroll.setBorder(new CompoundBorder(outside, inside));

        content.add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }
}
