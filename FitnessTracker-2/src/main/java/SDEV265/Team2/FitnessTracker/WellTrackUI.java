package SDEV265.Team2.FitnessTracker;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

// IMAGE CACHE & UTILITIES

class ImageCache {
    private static final HashMap<String, Image> cache = new HashMap<>();
    
    public static Image getImage(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        
        if (cache.containsKey(path)) {
            return cache.get(path);
        }
        
        try {
            ImageIcon icon = new ImageIcon(UIComponents.class.getResource(path));
            Image img = icon.getImage();
            if (img.getWidth(null) > 0) { // Valid image
                cache.put(path, img);
                return img;
            }
        } catch (Exception e) {
            System.err.println("Failed to load image: " + path + " - " + e.getMessage());
        }
        return null;
    }
    
    public static void clearCache() {
        cache.clear();
    }
}

// CONSTANTS

class WellTrackConstants {
    public static final Color PURPLE = new Color(124, 58, 237);
    public static final Color BLUE = new Color(59, 130, 246);
    public static final Color PINK = new Color(236, 72, 153);
    public static final Color BG_BLACK = new Color(0, 0, 0);
    public static final Color BG_WHITE = new Color(255, 255, 255);
    public static final Color CARD_BG = new Color(248, 248, 250);
    public static final Color TEXT_LIGHT = new Color(240, 240, 245);
    public static final Color TEXT_DARK = new Color(26, 26, 26);
    public static final Color TEXT_DIM = new Color(107, 114, 128);
}

// COMPONENTS

class UIComponents {
    
    //Sets the background image for the given panel with proper caching and scaling

    public static JPanel backgroundImagePanel(String imgPath) {
        final Image bgImage = ImageCache.getImage(imgPath);
        
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    int imgW = bgImage.getWidth(null);
                    int imgH = bgImage.getHeight(null);
                    int panelW = getWidth();
                    int panelH = getHeight();
                    
                    if (imgW > 0 && imgH > 0) {
                        double imgAspect = (double) imgW / imgH;
                        double panelAspect = (double) panelW / panelH;
                        
                        int drawW, drawH, drawX, drawY;
                        
                        if (imgAspect > panelAspect) {
                            drawH = panelH;
                            drawW = (int) (drawH * imgAspect);
                            drawX = (panelW - drawW) / 2;
                            drawY = 0;
                        } else {
                            drawW = panelW;
                            drawH = (int) (drawW / imgAspect);
                            drawX = 0;
                            drawY = (panelH - drawH) / 2;
                        }
                        
                        g2d.drawImage(bgImage, drawX, drawY, drawW, drawH, this);
                        
                        // Add dark overlay for better readability
                        g2d.setColor(new Color(0, 0, 0, 80));
                        g2d.fillRect(0, 0, panelW, panelH);
                    }
                } else {
                    g.setColor(WellTrackConstants.BG_BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
    }


    // Creates a panel with a gradient background from black to white.

    public static JPanel gradientPanel() {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setPaint(new GradientPaint(0, 0, WellTrackConstants.BG_BLACK, 0, getHeight(), WellTrackConstants.BG_WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }


    // Creates a header panel with title and subtitle in a rounded black background.

    public static JPanel header(String title, String subtitle) {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(WellTrackConstants.BG_BLACK);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        header.setBorder(new EmptyBorder(20, 35, 20, 35));

        JLabel t = new JLabel(title);
        t.setFont(font(28, true));
        t.setForeground(Color.WHITE);

        JLabel s = new JLabel("  •  " + subtitle);
        s.setFont(font(16));
        s.setForeground(new Color(200, 200, 210));

        header.add(t);
        header.add(s);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(25, 35, 0, 35));
        wrapper.add(header);
        return wrapper;
    }

    // Creates a custom styled button with black background and white text.

    public static JButton button(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(WellTrackConstants.BG_BLACK);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    ((getHeight() - fm.getHeight()) / 2) + fm.getAscent());
            }
        };
        btn.setFont(font(16, true));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(170, 45));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }


    // Creates a rounded card panel with semi-transparent white background.

    public static JPanel roundCard() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
    }


    // Creates a styled label with dark text color.

    public static JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font(16));
        lbl.setForeground(WellTrackConstants.TEXT_DARK);
        return lbl;
    }

    // Creates a custom styled text field with rounded corners and transparent background.

    public static JTextField textField() {
        JTextField field = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        field.setFont(font(16));
        field.setPreferredSize(new Dimension(340, 40));
        field.setOpaque(false);
        field.setForeground(WellTrackConstants.TEXT_DARK);
        field.setCaretColor(WellTrackConstants.TEXT_DARK);
        field.setBorder(new CompoundBorder(new LineBorder(new Color(209, 213, 219, 180), 2, true), new EmptyBorder(6, 12, 6, 12)));
        return field;
    }


    // Creates a custom styled combo box with rounded corners and transparent background.

    public static JComboBox<String> combo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        cb.setFont(font(16));
        cb.setPreferredSize(new Dimension(340, 40));
        cb.setOpaque(false);
        cb.setForeground(WellTrackConstants.TEXT_DARK);
        return cb;
    }

    // Creates a Font object with Montserrat font family, plain style.

    public static Font font(int size) { 
        return font(size, false); 
    }
    
    public static Font font(int size, boolean bold) {
        return new Font("Montserrat", bold ? Font.BOLD : Font.PLAIN, size);
    }
    
    // Creates the footer panel with brand name and tagline.
    
    public static JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(WellTrackConstants.BG_BLACK);
        footer.setBorder(new EmptyBorder(8, 30, 8, 30));
        
        JPanel leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftSection.setOpaque(false);
        
        JLabel brand = new JLabel("WellTrack");
        brand.setFont(font(11, true));
        brand.setForeground(Color.WHITE);
        
        JLabel tagline = new JLabel("© 2026 • Your wellness, your way");
        tagline.setFont(font(9));
        tagline.setForeground(new Color(140, 140, 150));
        
        leftSection.add(brand);
        leftSection.add(new JLabel("  "));
        leftSection.add(tagline);
        
        footer.add(leftSection, BorderLayout.WEST);
        
        return footer;
    }
}

// PANEL FACTORY

class PanelFactory {
    private final JLabel summaryLabel;
    private final Runnable updateSummaryCallback;
    private final DatabaseManager dbManager;
    

    // Constructs a PanelFactory with references to summary label, update callback, and database manager.

    public PanelFactory(JLabel summaryLabel, Runnable updateSummaryCallback, DatabaseManager dbManager) {
        this.summaryLabel = summaryLabel;
        this.updateSummaryCallback = updateSummaryCallback;
        this.dbManager = dbManager;
    }
    
    // Creates the home panel with welcome message, background image, and progress summary.

    public JPanel createHomePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WellTrackConstants.BG_BLACK);
        
        // Top section with background image
        JPanel topSection = UIComponents.backgroundImagePanel("/images/home.jpg");
        topSection.setPreferredSize(new Dimension(1600, 700));
        topSection.setOpaque(true);
        
        JPanel hero = new JPanel();
        hero.setOpaque(false);
        hero.setLayout(new FlowLayout(FlowLayout.CENTER));
        hero.setBorder(new EmptyBorder(100, 60, 40, 60));

        JPanel titleBox = new JPanel();
        titleBox.setBackground(new Color(0, 0, 0, 200));
        titleBox.setBorder(new CompoundBorder(
            new LineBorder(Color.BLACK, 3, true),
            new EmptyBorder(35, 60, 35, 60)
        ));
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome to WellTrack!");
        title.setFont(UIComponents.font(52, true));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Track your wellness and eating habits with WellTrack");
        subtitle.setFont(UIComponents.font(20, true));
        subtitle.setForeground(Color.WHITE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle2 = new JLabel("Built to manage all parts of your routine, not just your workout");
        subtitle2.setFont(UIComponents.font(17, true));
        subtitle2.setForeground(new Color(220, 220, 230));
        subtitle2.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleBox.add(title);
        titleBox.add(Box.createRigidArea(new Dimension(0, 15)));
        titleBox.add(subtitle);
        titleBox.add(Box.createRigidArea(new Dimension(0, 10)));
        titleBox.add(subtitle2);

        hero.add(titleBox);
        topSection.add(hero, BorderLayout.CENTER);

        // Creates stats and footer section with solid black background

        JPanel bottomSection = new JPanel(new BorderLayout());
        bottomSection.setBackground(WellTrackConstants.BG_BLACK);
        bottomSection.setOpaque(true);

        JPanel statsWrap = new JPanel(new BorderLayout());
        statsWrap.setBackground(WellTrackConstants.BG_BLACK);
        statsWrap.setOpaque(true);
        statsWrap.setBorder(new EmptyBorder(80, 60, 80, 60));

        JPanel card = UIComponents.roundCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new CompoundBorder(new RoundBorder(WellTrackConstants.PURPLE, 2), new EmptyBorder(15, 25, 15, 25)));

        JLabel statsTitle = new JLabel("Your Progress Today");
        statsTitle.setFont(UIComponents.font(16, true));
        statsTitle.setForeground(WellTrackConstants.PURPLE);
        statsTitle.setHorizontalAlignment(SwingConstants.LEFT);

        summaryLabel.setFont(UIComponents.font(13));
        summaryLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        card.add(statsTitle, BorderLayout.NORTH);
        card.add(summaryLabel, BorderLayout.CENTER);
        statsWrap.add(card, BorderLayout.CENTER);
        bottomSection.add(statsWrap, BorderLayout.CENTER);

        JPanel footerSection = new JPanel(new BorderLayout());
        footerSection.setBackground(WellTrackConstants.BG_BLACK);
        footerSection.setOpaque(true);
        footerSection.setPreferredSize(new Dimension(1600, 150));
        footerSection.setBorder(new EmptyBorder(60, 0, 0, 0));
        footerSection.add(UIComponents.createFooter(), BorderLayout.CENTER);
        bottomSection.add(footerSection, BorderLayout.SOUTH);

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(bottomSection, BorderLayout.CENTER);
        
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scroll);
        
        return mainPanel;
    }

    // Creates a form panel with background image, header, and input form based on provided labels and types.

    public JPanel createFormPanel(String title, String desc, String[] labels, String[] types, FormAction action) {
        String imgPath = "";
        if (title.contains("Workout")) imgPath = "/images/workout.jpg";
        else if (title.contains("Nutrition")) imgPath = "/images/nutrition.jpg";
        else if (title.contains("Goals")) imgPath = "/images/goals.jpg";
        else if (title.contains("Sleep")) imgPath = "/images/sleep.jpg";

        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(WellTrackConstants.BG_BLACK);
        
        JPanel bgImageSection = UIComponents.backgroundImagePanel(imgPath);
        bgImageSection.setPreferredSize(new Dimension(1600, 900));
        bgImageSection.setMinimumSize(new Dimension(1600, 900));
        bgImageSection.setMaximumSize(new Dimension(5000, 900));
        bgImageSection.setLayout(new BorderLayout());
        bgImageSection.add(UIComponents.header(title, desc), BorderLayout.NORTH);

        JPanel inputSection = new JPanel();
        inputSection.setLayout(new BorderLayout());
        inputSection.setBackground(WellTrackConstants.BG_BLACK);
        inputSection.setOpaque(true);
        
        JLabel debugTitle = new JLabel("INPUT FORM SECTION");
        debugTitle.setFont(UIComponents.font(24, true));
        debugTitle.setForeground(Color.WHITE);
        debugTitle.setHorizontalAlignment(SwingConstants.CENTER);
        debugTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        JPanel formContainer = new JPanel();
        formContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        formContainer.setBackground(WellTrackConstants.BG_BLACK);
        formContainer.setOpaque(true);
        formContainer.setBorder(new EmptyBorder(80, 60, 80, 60));

        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setOpaque(true);
        form.setBorder(new CompoundBorder(
            new LineBorder(WellTrackConstants.PURPLE, 3),
            new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JComponent[] fields = new JComponent[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(UIComponents.font(14, true));
            lbl.setForeground(WellTrackConstants.TEXT_DARK);
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            form.add(lbl, gbc);

            if (types[i].startsWith("combo:")) {
                String[] opts = types[i].substring(6).split(",");
                JComboBox<String> combo = new JComboBox<>(opts);
                combo.setFont(UIComponents.font(14, false));
                fields[i] = combo;
                gbc.gridx = 1; gbc.weightx = 1;
                form.add(combo, gbc);
            } else {
                JTextField field = new JTextField(30);
                field.setFont(UIComponents.font(14, false));
                fields[i] = field;
                gbc.gridx = 1; gbc.weightx = 1;
                form.add(field, gbc);
            }
        }

        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton btn = UIComponents.button("Send to DB");
        form.add(btn, gbc);
        btn.addActionListener(e -> {
            action.execute(fields);
            clearFields(fields);
        });

        formContainer.add(form);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(WellTrackConstants.BG_BLACK);
        contentWrapper.add(debugTitle, BorderLayout.NORTH);
        contentWrapper.add(formContainer, BorderLayout.CENTER);
        
        inputSection.add(contentWrapper, BorderLayout.CENTER);

        JPanel footerSection = new JPanel(new BorderLayout());
        footerSection.setBackground(WellTrackConstants.BG_BLACK);
        footerSection.setOpaque(true);
        footerSection.setPreferredSize(new Dimension(1600, 150));
        footerSection.setBorder(new EmptyBorder(60, 0, 0, 0));
        footerSection.add(UIComponents.createFooter(), BorderLayout.CENTER);

        container.add(bgImageSection);
        container.add(inputSection);
        container.add(footerSection);
        
        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scroll);
        
        return mainPanel;
    }

    // Creates a simple input panel with background image, header, and a single input field with button.

    public JPanel createSimplePanel(String title, String fieldLabel, ValueAction action) {
        String imgPath = "";
        if (title.contains("Water")) imgPath = "/images/hydration.jpg";

        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(WellTrackConstants.BG_BLACK);
        
        JPanel bgImageSection = UIComponents.backgroundImagePanel(imgPath);
        bgImageSection.setPreferredSize(new Dimension(1600, 900));
        bgImageSection.setMinimumSize(new Dimension(1600, 900));
        bgImageSection.setMaximumSize(new Dimension(5000, 900));
        bgImageSection.setLayout(new BorderLayout());
        bgImageSection.add(UIComponents.header(title, "Track your progress"), BorderLayout.NORTH);

        JPanel inputSection = new JPanel();
        inputSection.setLayout(new BorderLayout());
        inputSection.setBackground(WellTrackConstants.BG_BLACK);
        inputSection.setOpaque(true);
        
        JLabel debugTitle = new JLabel("INPUT FORM SECTION");
        debugTitle.setFont(UIComponents.font(24, true));
        debugTitle.setForeground(Color.WHITE);
        debugTitle.setHorizontalAlignment(SwingConstants.CENTER);
        debugTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        JPanel content = new JPanel(new FlowLayout(FlowLayout.CENTER));
        content.setBackground(WellTrackConstants.BG_BLACK);
        content.setOpaque(true);
        content.setBorder(new EmptyBorder(80, 60, 80, 60));

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setOpaque(true);
        card.setBorder(new CompoundBorder(
            new LineBorder(WellTrackConstants.BLUE, 3),
            new EmptyBorder(30, 40, 30, 40)
        ));
        card.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));

        JLabel lbl = new JLabel(fieldLabel);
        lbl.setFont(UIComponents.font(16, true));
        lbl.setForeground(WellTrackConstants.TEXT_DARK);
        
        JTextField field = new JTextField(15);
        field.setFont(UIComponents.font(14));
        field.setPreferredSize(new Dimension(200, 35));
        
        JButton btn = UIComponents.button("Log");
        btn.addActionListener(e -> {
            try {
                action.execute(field.getText());
                field.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input!", "WellTrack", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        card.add(lbl);
        card.add(field);
        card.add(btn);
        content.add(card);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(WellTrackConstants.BG_BLACK);
        contentWrapper.add(debugTitle, BorderLayout.NORTH);
        contentWrapper.add(content, BorderLayout.CENTER);
        
        inputSection.add(contentWrapper, BorderLayout.CENTER);

        JPanel footerSection = new JPanel(new BorderLayout());
        footerSection.setBackground(WellTrackConstants.BG_BLACK);
        footerSection.setOpaque(true);
        footerSection.setPreferredSize(new Dimension(1600, 150));
        footerSection.setBorder(new EmptyBorder(60, 0, 0, 0));
        footerSection.add(UIComponents.createFooter(), BorderLayout.CENTER);

        container.add(bgImageSection);
        container.add(inputSection);
        container.add(footerSection);
        
        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scroll);
        
        return mainPanel;
    }




    // Creates the notes panel with category selection and note management interface.

    public JPanel createNotesPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(WellTrackConstants.BG_BLACK);
        
        // Hero section with image and header
        JPanel bgImageSection = UIComponents.backgroundImagePanel("/images/notes.jpg");
        bgImageSection.setPreferredSize(new Dimension(1600, 900));
        bgImageSection.setMinimumSize(new Dimension(1600, 900));
        bgImageSection.setMaximumSize(new Dimension(5000, 900));
        bgImageSection.setLayout(new BorderLayout());
        bgImageSection.add(UIComponents.header("Notes", "Track your thoughts and reflections"), BorderLayout.NORTH);
        
        // Category selection section

        JPanel categorySection = new JPanel(new BorderLayout());
        categorySection.setBackground(WellTrackConstants.BG_BLACK);
        categorySection.setOpaque(true);
        categorySection.setPreferredSize(new Dimension(1600, 700));
        
        JLabel categoryTitle = new JLabel("Select a Category", SwingConstants.CENTER);
        categoryTitle.setFont(UIComponents.font(36, true));
        categoryTitle.setForeground(WellTrackConstants.TEXT_LIGHT);
        categoryTitle.setBorder(new EmptyBorder(60, 0, 40, 0));
        
        JPanel categoriesGrid = new JPanel(new GridLayout(2, 4, 30, 30));
        categoriesGrid.setBackground(WellTrackConstants.BG_BLACK);
        categoriesGrid.setBorder(new EmptyBorder(0, 80, 100, 80));
        
        String[] categories = {"Nutrition", "Workout", "Sleep", "Hydration", "Weight", "Mood", "Goals", "General"};
        Color[] colors = {
            WellTrackConstants.PURPLE,
            new Color(236, 72, 153), // Pink
            WellTrackConstants.BLUE,
            new Color(59, 130, 246), // Blue
            new Color(16, 185, 129), // Green
            new Color(245, 158, 11), // Amber
            WellTrackConstants.PURPLE,
            new Color(107, 114, 128) // Gray
        };
        
        JPanel formSection = new JPanel(new BorderLayout());
        formSection.setBackground(WellTrackConstants.BG_BLACK);
        formSection.setOpaque(true);
        formSection.setVisible(false);
        
        for (int i = 0; i < categories.length; i++) {
            final String category = categories[i];
            final Color color = colors[i];
            
            JPanel categoryCard = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(248, 248, 250));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }
            };
            categoryCard.setLayout(new BorderLayout());
            categoryCard.setBorder(new CompoundBorder(
                new RoundBorder(color, 3),
                new EmptyBorder(40, 30, 40, 30)
            ));
            categoryCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel catLabel = new JLabel(category, SwingConstants.CENTER);
            catLabel.setFont(UIComponents.font(22, true));
            catLabel.setForeground(color);
            
            categoryCard.add(catLabel, BorderLayout.CENTER);
            
            categoryCard.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showNotesForm(formSection, category, color, categorySection);
                }
            });
            
            categoriesGrid.add(categoryCard);
        }
        
        categorySection.add(categoryTitle, BorderLayout.NORTH);
        categorySection.add(categoriesGrid, BorderLayout.CENTER);
        
        // Footer

        JPanel footerSection = new JPanel(new BorderLayout());
        footerSection.setBackground(WellTrackConstants.BG_BLACK);
        footerSection.setOpaque(true);
        footerSection.setPreferredSize(new Dimension(1600, 150));
        footerSection.setBorder(new EmptyBorder(60, 0, 0, 0));
        footerSection.add(UIComponents.createFooter(), BorderLayout.CENTER);

        container.add(bgImageSection);
        container.add(categorySection);
        container.add(formSection);
        container.add(footerSection);
        
        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scroll);
        
        return mainPanel;
    }
    
    // Displays the notes form for a specific category, showing saved notes and input form.

    private void showNotesForm(JPanel formSection, String category, Color borderColor, JPanel categorySection) {
        formSection.removeAll();
        formSection.setVisible(true);
        formSection.setMinimumSize(new Dimension(1600, 700));
        formSection.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WellTrackConstants.BG_BLACK);
        headerPanel.setBorder(new EmptyBorder(60, 80, 20, 80));
        
        JButton backButton = new JButton("← Back to Categories") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(60, 60, 60));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    ((getHeight() - fm.getHeight()) / 2) + fm.getAscent());
            }
        };
        backButton.setFont(UIComponents.font(14, true));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            formSection.setVisible(false);
            categorySection.setVisible(true);
        });
        
        JLabel formTitle = new JLabel(category + " Notes", SwingConstants.CENTER);
        formTitle.setFont(UIComponents.font(32, true));
        formTitle.setForeground(WellTrackConstants.TEXT_LIGHT);
        
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(backButton);
        
        headerPanel.add(buttonWrapper, BorderLayout.WEST);
        headerPanel.add(formTitle, BorderLayout.CENTER);
        
        // Main content area with stored notes and form
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(WellTrackConstants.BG_BLACK);
        
        // Stored Notes Section

        JPanel storedNotesSection = new JPanel(new BorderLayout());
        storedNotesSection.setBackground(WellTrackConstants.BG_BLACK);
        storedNotesSection.setBorder(new EmptyBorder(20, 80, 30, 80));
        
        JLabel storedTitle = new JLabel("Saved " + category + " Notes", SwingConstants.LEFT);
        storedTitle.setFont(UIComponents.font(20, true));
        storedTitle.setForeground(WellTrackConstants.TEXT_LIGHT);
        storedTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JPanel notesListPanel = new JPanel();
        notesListPanel.setLayout(new BoxLayout(notesListPanel, BoxLayout.Y_AXIS));
        notesListPanel.setBackground(WellTrackConstants.BG_BLACK);
        
        java.util.List<NotesStorage.Note> savedNotes = NotesStorage.getNotesByCategory(category);
        
        if (savedNotes.isEmpty()) {
            JLabel emptyMsg = new JLabel("No notes saved yet. Add your first note below!");
            emptyMsg.setFont(UIComponents.font(14));
            emptyMsg.setForeground(new Color(150, 150, 160));
            emptyMsg.setBorder(new EmptyBorder(10, 0, 10, 0));
            notesListPanel.add(emptyMsg);
        } else {
            for (NotesStorage.Note note : savedNotes) {
                JPanel noteCard = new JPanel(new BorderLayout(15, 10));
                noteCard.setBackground(WellTrackConstants.CARD_BG);
                noteCard.setBorder(new CompoundBorder(
                    new LineBorder(borderColor, 2),
                    new EmptyBorder(15, 20, 15, 20)
                ));
                noteCard.setMaximumSize(new Dimension(1400, 200));
                
                JPanel noteContent = new JPanel();
                noteContent.setLayout(new BoxLayout(noteContent, BoxLayout.Y_AXIS));
                noteContent.setOpaque(false);
                
                JLabel titleLabel = new JLabel(note.title);
                titleLabel.setFont(UIComponents.font(16, true));
                titleLabel.setForeground(borderColor);
                titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel dateLabel = new JLabel(note.date);
                dateLabel.setFont(UIComponents.font(12));
                dateLabel.setForeground(WellTrackConstants.TEXT_DIM);
                dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JTextArea contentArea = new JTextArea(note.content);
                contentArea.setFont(UIComponents.font(13));
                contentArea.setForeground(WellTrackConstants.TEXT_DARK);
                contentArea.setLineWrap(true);
                contentArea.setWrapStyleWord(true);
                contentArea.setEditable(false);
                contentArea.setOpaque(false);
                contentArea.setBorder(new EmptyBorder(8, 0, 0, 0));
                contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                noteContent.add(titleLabel);
                noteContent.add(Box.createRigidArea(new Dimension(0, 5)));
                noteContent.add(dateLabel);
                noteContent.add(contentArea);
                
                noteCard.add(noteContent, BorderLayout.CENTER);
                noteCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                notesListPanel.add(noteCard);
                notesListPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        }
        
        storedNotesSection.add(storedTitle, BorderLayout.NORTH);
        storedNotesSection.add(notesListPanel, BorderLayout.CENTER);
        
        // Input Form Section

        JPanel formContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formContainer.setBackground(WellTrackConstants.BG_BLACK);
        formContainer.setBorder(new EmptyBorder(30, 60, 80, 60));
        
        JPanel form = new JPanel();
        form.setBackground(WellTrackConstants.CARD_BG);
        form.setLayout(new GridBagLayout());
        form.setBorder(new CompoundBorder(
            new LineBorder(borderColor, 3),
            new EmptyBorder(40, 50, 40, 50)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 12, 12, 12);
        
        String[] labels = {"Date:", "Title:", "Notes:"};
        JComponent[] fields = new JComponent[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(UIComponents.font(15, true));
            lbl.setForeground(WellTrackConstants.TEXT_DARK);
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            form.add(lbl, gbc);
            
            if (i == 2) { // Notes field
                JTextArea area = new JTextArea(8, 40);
                area.setFont(UIComponents.font(14, false));
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                area.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
                JScrollPane scroll = new JScrollPane(area);
                scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                fields[i] = area;
                gbc.gridx = 1; gbc.weightx = 1;
                form.add(scroll, gbc);
            } else {
                JTextField field = new JTextField(35);
                field.setFont(UIComponents.font(14, false));
                field.setPreferredSize(new Dimension(400, 40));
                fields[i] = field;
                gbc.gridx = 1; gbc.weightx = 1;
                form.add(field, gbc);
            }
        }
        
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 12, 12, 12);
        JButton saveBtn = UIComponents.button("Save " + category + " Note");
        form.add(saveBtn, gbc);
        
        saveBtn.addActionListener(e -> {
            String date = ((JTextField)fields[0]).getText();
            String title = ((JTextField)fields[1]).getText();
            String notes = ((JTextArea)fields[2]).getText();
            
            if (!date.isEmpty() && !title.isEmpty() && !notes.isEmpty()) {
                NotesStorage.addNote(category, date, title, notes);
                clearFields(fields);
                JOptionPane.showMessageDialog(null, category + " note saved!", "WellTrack", JOptionPane.INFORMATION_MESSAGE);
                showNotesForm(formSection, category, borderColor, categorySection);
            } else {
                JOptionPane.showMessageDialog(null, "Please fill all fields!", "WellTrack", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        formContainer.add(form);
        
        mainContent.add(storedNotesSection);
        mainContent.add(formContainer);
        
        formSection.add(headerPanel, BorderLayout.NORTH);
        formSection.add(mainContent, BorderLayout.CENTER);
        
        formSection.revalidate();
        formSection.repaint();
        categorySection.setVisible(false);
    }

    // Creates an information panel with a title and message in a card layout.

    public JPanel createInfoPanel(String title, String msg) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(WellTrackConstants.BG_BLACK);
        
        JPanel bgImageSection = UIComponents.backgroundImagePanel("/images/home.jpg");
        bgImageSection.setPreferredSize(new Dimension(1600, 900));
        bgImageSection.setMinimumSize(new Dimension(1600, 900));
        bgImageSection.setMaximumSize(new Dimension(5000, 900));
        
        JPanel contentSection = new JPanel(new BorderLayout());
        contentSection.setBackground(WellTrackConstants.BG_BLACK);
        contentSection.setOpaque(true);
        
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(WellTrackConstants.BG_BLACK);
        wrapper.setOpaque(true);
        wrapper.setBorder(new EmptyBorder(80, 60, 80, 60));

        JPanel card = UIComponents.roundCard();
        card.setPreferredSize(new Dimension(500, 220));
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setFont(UIComponents.font(32, true));
        t.setForeground(WellTrackConstants.PURPLE);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel m = new JLabel("<html><div style='text-align:center;'>" + msg + "</div></html>");
        m.setFont(UIComponents.font(14));
        m.setForeground(WellTrackConstants.TEXT_DIM);
        m.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(t);
        card.add(m);
        wrapper.add(card);
        contentSection.add(wrapper, BorderLayout.CENTER);
        
        JPanel footerSection = new JPanel(new BorderLayout());
        footerSection.setBackground(WellTrackConstants.BG_BLACK);
        footerSection.setOpaque(true);
        footerSection.setPreferredSize(new Dimension(1600, 150));
        footerSection.setBorder(new EmptyBorder(60, 0, 0, 0));
        footerSection.add(UIComponents.createFooter(), BorderLayout.CENTER);
        
        container.add(bgImageSection);
        container.add(contentSection);
        container.add(footerSection);
        
        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scroll);
        
        return mainPanel;
    }

    // Creates the about panel with information about the WellTrack application.

    public JPanel createAboutPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel panel = UIComponents.backgroundImagePanel("/images/group.jpg");
        
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(80, 60, 100, 60));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 255, 255, 220));
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 255, 255, 150), 3, true),
            new EmptyBorder(30, 40, 30, 40)
        ));
        card.setPreferredSize(new Dimension(900, 850));

        String html = "<html><div style='font-family:Montserrat,sans-serif;padding:10px;'>" +
            "<h2 style='color:#7C3AED;margin-top:0;font-size:28px;'>About WellTrack</h2>" +
            "<p style='color:#6B7280;font-size:14px;line-height:2.0;'>WellTrack is a simple, privacy-focused wellness tracking application " +
            "built for people who want to take control of their health without sacrificing their data. Most fitness apps are bloated, " +
            "locked behind paywalls, or require you to hand over your personal data. We wanted to create something different—" +
            "a minimalistic, powerful tool that respects your privacy and puts you in control of your wellness journey.</p>" +
            
            "<h3 style='color:#10B981;margin-top:25px;font-size:20px;'>What Makes Us Different</h3>" +
            "<p style='color:#6B7280;font-size:14px;line-height:2.0;'><strong>All-in-One Tracking:</strong> Monitor workouts, nutrition, hydration, sleep, and goals in one place.<br>" +
            "<strong>Privacy First:</strong> Your data stays on your device. No cloud storage, no tracking, no ads.<br>" +
            "<strong>Completely Free:</strong> No subscriptions, no hidden fees, no premium tiers. Everything is free, forever.<br>" +
            "<strong>Simple & Beautiful:</strong> Clean design that's easy to use and pleasant to look at.</p>" +
            
            "<h3 style='color:#7C3AED;margin-top:25px;font-size:20px;'>Meet the Team</h3>" +
            "<p style='color:#6B7280;font-size:15px;'>Developed with passion by: <strong style='color:#7C3AED;'>Abdallah, Bithiah, Emily, Ryan, Kiley</strong></p>" +
            "<p style='color:#6B7280;font-size:14px;'>We're a group of students who care about health, technology, and making tools that actually help people.</p>" +
            
            "<div style='background:#F8F8FA;padding:20px;border-radius:12px;margin-top:20px;border:2px solid #7C3AED;'>" +
            "<p style='color:#7C3AED;font-size:16px;margin:0;'><strong>Your Privacy Matters</strong></p>" +
            "<p style='color:#6B7280;margin-top:8px;margin-bottom:0;font-size:14px;'>All your data is stored locally on your device. " +
            "We don't have servers, we don't collect analytics, and we never see your information. Your wellness journey is yours alone.</p>" +
            "</div>" +
            
            "<div style='text-align:center;margin-top:25px;'>" +
            "<p style='color:#6B7280;font-size:13px;margin:0;'>© 2026 WellTrack • Made with love for wellness enthusiasts everywhere</p>" +
            "</div>" +
            "</div></html>";

        JLabel text = new JLabel(html);
        text.setVerticalAlignment(SwingConstants.TOP);
        card.add(text, BorderLayout.CENTER);

        wrapper.add(card);
        panel.add(wrapper, BorderLayout.CENTER);
        panel.add(UIComponents.createFooter(), BorderLayout.SOUTH);
        
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scroll);
        
        return mainPanel;
    }

    // Creates the goals panel with form to add goals and display of current goals.

    public JPanel createGoalsPanel(GoalSaveAction saveAction, MessageAction messageAction) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(WellTrackConstants.BG_BLACK);
        
        JPanel bgImageSection = UIComponents.backgroundImagePanel("/images/goals.jpg");
        bgImageSection.setPreferredSize(new Dimension(1600, 900));
        bgImageSection.setMinimumSize(new Dimension(1600, 900));
        bgImageSection.setMaximumSize(new Dimension(5000, 900));
        bgImageSection.setLayout(new BorderLayout());
        bgImageSection.add(UIComponents.header("Goals", "Achieve your targets"), BorderLayout.NORTH);

        // Form section
        JPanel formSection = new JPanel();
        formSection.setLayout(new BorderLayout());
        formSection.setBackground(WellTrackConstants.BG_BLACK);
        formSection.setOpaque(true);
        
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        formContainer.setBackground(WellTrackConstants.BG_BLACK);
        formContainer.setOpaque(true);
        formContainer.setBorder(new EmptyBorder(40, 60, 40, 60));

        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setOpaque(true);
        form.setBorder(new CompoundBorder(
            new LineBorder(WellTrackConstants.PURPLE, 3),
            new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        String[] labels = {"Goal Name:", "Goal Type:", "Target Value:", "Current Value:", "Start Date:", "End Date:", "Status:"};
        String[] types = {"text", "combo:Weight,Strength,Endurance,Flexibility", "text", "text", "date", "date", "combo:Active,Completed,Paused"};
        JComponent[] fields = new JComponent[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(UIComponents.font(14, true));
            lbl.setForeground(WellTrackConstants.TEXT_DARK);
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            form.add(lbl, gbc);

            if (types[i].startsWith("combo:")) {
                String[] opts = types[i].substring(6).split(",");
                JComboBox<String> combo = new JComboBox<>(opts);
                combo.setFont(UIComponents.font(14, false));
                fields[i] = combo;
                gbc.gridx = 1; gbc.weightx = 1;
                form.add(combo, gbc);
            } else {
                JTextField field = new JTextField(30);
                field.setFont(UIComponents.font(14, false));
                fields[i] = field;
                gbc.gridx = 1; gbc.weightx = 1;
                form.add(field, gbc);
            }
        }

        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton btn = UIComponents.button("Add Goal");
        form.add(btn, gbc);
        JComponent[] finalFields = fields;
        btn.addActionListener(e -> {
            String goalName = ((JTextField)finalFields[0]).getText();
            String goalType = (String)((JComboBox<?>)finalFields[1]).getSelectedItem();
            String targetValue = ((JTextField)finalFields[2]).getText();
            String currentValue = ((JTextField)finalFields[3]).getText();
            String startDate = ((JTextField)finalFields[4]).getText();
            String endDate = ((JTextField)finalFields[5]).getText();
            String status = (String)((JComboBox<?>)finalFields[6]).getSelectedItem();
            
            saveAction.execute(goalName, goalType, targetValue, currentValue, startDate, endDate, status);
            clearFields(finalFields);
            messageAction.show("Goal added: " + goalName);
        });

        formContainer.add(form);
        formSection.add(formContainer, BorderLayout.CENTER);

        // Current goals section
        JPanel goalsSection = new JPanel(new BorderLayout());
        goalsSection.setBackground(WellTrackConstants.BG_BLACK);
        goalsSection.setOpaque(true);
        goalsSection.setBorder(new EmptyBorder(40, 60, 80, 60));

        JLabel goalsTitle = new JLabel("Current Goals", SwingConstants.LEFT);
        goalsTitle.setFont(UIComponents.font(20, true));
        goalsTitle.setForeground(WellTrackConstants.TEXT_LIGHT);
        goalsTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel goalsListPanel = new JPanel();
        goalsListPanel.setLayout(new BoxLayout(goalsListPanel, BoxLayout.Y_AXIS));
        goalsListPanel.setBackground(WellTrackConstants.BG_BLACK);

        List<Map<String, String>> goals = new ArrayList<>();
        try {
            goals = dbManager.getAllGoals();
        } catch (Exception ex) {
            System.err.println("Error loading goals: " + ex.getMessage());
        }

        if (goals.isEmpty()) {
            JLabel emptyMsg = new JLabel("No goals set yet. Add your first goal above!");
            emptyMsg.setFont(UIComponents.font(14));
            emptyMsg.setForeground(new Color(150, 150, 160));
            emptyMsg.setBorder(new EmptyBorder(10, 0, 10, 0));
            goalsListPanel.add(emptyMsg);
        } else {
            for (Map<String, String> goal : goals) {
                JPanel goalCard = new JPanel(new BorderLayout(15, 10));
                goalCard.setBackground(WellTrackConstants.CARD_BG);
                goalCard.setBorder(new CompoundBorder(
                    new LineBorder(WellTrackConstants.PURPLE, 2),
                    new EmptyBorder(15, 20, 15, 20)
                ));
                
                JPanel goalContent = new JPanel();
                goalContent.setLayout(new BoxLayout(goalContent, BoxLayout.Y_AXIS));
                goalContent.setOpaque(false);
                
                JLabel nameLabel = new JLabel(goal.get("goal_name"));
                nameLabel.setFont(UIComponents.font(16, true));
                nameLabel.setForeground(WellTrackConstants.PURPLE);
                nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel typeLabel = new JLabel("Type: " + goal.get("goal_type"));
                typeLabel.setFont(UIComponents.font(12));
                typeLabel.setForeground(WellTrackConstants.TEXT_DIM);
                typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel progressLabel = new JLabel("Progress: " + goal.get("current_value") + " / " + goal.get("target_value"));
                progressLabel.setFont(UIComponents.font(12));
                progressLabel.setForeground(WellTrackConstants.TEXT_DIM);
                progressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel datesLabel = new JLabel("From " + goal.get("start_date") + " to " + goal.get("end_date") + " - " + goal.get("status"));
                datesLabel.setFont(UIComponents.font(12));
                datesLabel.setForeground(WellTrackConstants.TEXT_DIM);
                datesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                goalContent.add(nameLabel);
                goalContent.add(Box.createRigidArea(new Dimension(0, 5)));
                goalContent.add(typeLabel);
                goalContent.add(progressLabel);
                goalContent.add(datesLabel);
                
                goalCard.add(goalContent, BorderLayout.CENTER);
                goalCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                goalsListPanel.add(goalCard);
                goalsListPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        }

        goalsSection.add(goalsTitle, BorderLayout.NORTH);
        JScrollPane goalsScroll = new JScrollPane(goalsListPanel);
        goalsScroll.setBorder(null);
        goalsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        goalsSection.add(goalsScroll, BorderLayout.CENTER);

        JPanel footerSection = new JPanel(new BorderLayout());
        footerSection.setBackground(WellTrackConstants.BG_BLACK);
        footerSection.setOpaque(true);
        footerSection.setPreferredSize(new Dimension(1600, 150));
        footerSection.setBorder(new EmptyBorder(60, 0, 0, 0));
        footerSection.add(UIComponents.createFooter(), BorderLayout.CENTER);

        container.add(bgImageSection);
        container.add(formSection);
        container.add(goalsSection);
        container.add(footerSection);
        
        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scroll);
        
        return mainPanel;
    }

    private void clearFields(JComponent[] fields) {
        for (JComponent f : fields) {
            if (f instanceof JTextField jTextField) jTextField.setText("");
            else if (f instanceof JTextArea jTextArea) jTextArea.setText("");
        }
    }

    public Runnable getUpdateSummaryCallback() {
        return updateSummaryCallback;
    }

    // Functional interface for form submission actions

    @FunctionalInterface 
    public interface FormAction { 
        void execute(JComponent[] fields); 
    }
    
    // Functional interface for simple value input actions

    @FunctionalInterface 
    public interface ValueAction { 
        void execute(String value); 
    }
    
    // Functional interface for goal save actions
    
    @FunctionalInterface
    public interface GoalSaveAction {
        void execute(String goalName, String goalType, String targetValue, String currentValue, String startDate, String endDate, String status);
    }
    
    // Functional interface for message display actions
    
    @FunctionalInterface
    public interface MessageAction {
        void show(String msg);
    }
    
    static class RoundBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;

        RoundBorder(Color color, int thickness) {
            this.color = color;
            this.thickness = thickness;
        }

        // Paints a rounded border around the component.

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRoundRect(x + thickness/2, y + thickness/2, width - thickness, height - thickness, 20, 20);
        }

        // Returns the insets for the border.

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
    }
}

// MAIN CLASS

public class WellTrackUI {
    private final JLabel summaryLabel;
    private final PanelFactory panelFactory;
    // database manager handles persistence
    private DatabaseManager dbManager;


    // Main function for the WellTrack application.

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WellTrackUI());
    }


    // Constructs the WellTrackUI, initializes the database, and sets up the main application window.

    public WellTrackUI() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) { e.printStackTrace(); }

        // initialize database manager first so summaryHTML() can fetch data
        dbManager = new DatabaseManager();
        dbManager.initializeDatabase();
        
        summaryLabel = new JLabel(summaryHTML(), SwingConstants.LEFT);
        
        panelFactory = new PanelFactory(summaryLabel, this::updateSummary, dbManager);

        JFrame frame = new JFrame("WellTrack");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 1000);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIComponents.font(14, true));
        tabs.setBackground(WellTrackConstants.BG_WHITE);
        tabs.setForeground(Color.BLACK);
        UIManager.put("TabbedPane.selected", WellTrackConstants.BG_BLACK);
        UIManager.put("TabbedPane.selectedForeground", Color.WHITE);

        tabs.addTab("Home", panelFactory.createHomePanel());
        
        tabs.addTab("Workouts", panelFactory.createFormPanel("Workout Tracker", "Log your fitness activities",
            new String[]{"Type:", "Date:", "Duration (min):", "Intensity:", "Scheduled:"},
            new String[]{"combo:Cardio,Strength,Flexibility,Sports", "date", "text", "combo:Low,Moderate,High,Extreme", "combo:Yes,No"},
            fields -> {
                String type = (String)((JComboBox<?>)fields[0]).getSelectedItem();
                String date = ((JTextField)fields[1]).getText();
                String duration = ((JTextField)fields[2]).getText();
                String intensity = (String)((JComboBox<?>)fields[3]).getSelectedItem();
                String scheduled = (String)((JComboBox<?>)fields[4]).getSelectedItem();

                // Saves to database and updates summary

                dbManager.saveUserWorkout(type, date, duration, intensity, scheduled);
                updateSummary();
                showMsg("Workout logged: " + type);
            }));
            
        tabs.addTab("Hydration", panelFactory.createSimplePanel("Water Tracker", "Cups:", val -> {
            int cups = Integer.parseInt(val);

            // Saves to database and updates summary

            dbManager.saveUserHydration(LocalDate.now().toString(), cups);
            updateSummary();
            showMsg("Water logged!");
        }));
        
        tabs.addTab("Sleep", panelFactory.createFormPanel("Sleep Tracker", "Track your rest",
            new String[]{"Date:", "Hours:", "Quality:"},
            new String[]{"date", "text", "combo:Excellent,Good,Fair,Poor"},
            fields -> {
                String date = ((JTextField)fields[0]).getText();
                String hours = ((JTextField)fields[1]).getText();
                String quality = (String)((JComboBox<?>)fields[2]).getSelectedItem();
                
                // Saves to database and updates summary

                dbManager.saveUserSleep(date, hours, quality);
                updateSummary();
                showMsg("Sleep logged: " + hours + " hours");
            }));
        
        tabs.addTab("Nutrition", panelFactory.createFormPanel("Nutrition Tracker", "Monitor your meals",
            new String[]{"Date:", "Meal Type:", "Food Name:", "Calories:", "Protein (g):", "Carbs (g):", "Fat (g):", "Serving Size:"},
            new String[]{"date", "combo:Breakfast,Lunch,Dinner,Snack", "text", "text", "text", "text", "text", "text"},
            fields -> {
                String date = ((JTextField)fields[0]).getText();
                String mealType = (String)((JComboBox<?>)fields[1]).getSelectedItem();
                String foodName = ((JTextField)fields[2]).getText();
                String calories = ((JTextField)fields[3]).getText();
                String protein = ((JTextField)fields[4]).getText();
                String carbs = ((JTextField)fields[5]).getText();
                String fat = ((JTextField)fields[6]).getText();
                String serving = ((JTextField)fields[7]).getText();
                
                // Saves to database and updates summary

                dbManager.saveUserMeal(date, mealType, foodName, calories, protein, carbs, fat, serving);
                updateSummary();
                showMsg("Meal logged: " + foodName);
            }));
            
        tabs.addTab("Goals", panelFactory.createGoalsPanel(
            (goalName, goalType, targetValue, currentValue, startDate, endDate, status) -> {
                dbManager.saveUserGoal(goalName, goalType, targetValue, currentValue, startDate, endDate, status);
                updateSummary();
            },
            msg -> showMsg(msg)
        ));
            

        tabs.addTab("Notes", panelFactory.createNotesPanel());
        tabs.addTab("About", panelFactory.createAboutPanel());

        frame.add(tabs);
        frame.setVisible(true);
    }

    // Updates the summary label with the current progress information.

    private void updateSummary() { 
        summaryLabel.setText(summaryHTML()); 
    }

    // Generates the HTML-formatted summary text showing user's progress across different categories.
    // Data is retrieved from the database in real-time.

    private String summaryHTML() {
        // Fetch data from database
        int workoutCount = dbManager.getWorkoutCount();
        int mealCount = dbManager.getMealCount();
        int sleepCount = dbManager.getSleepCount();
        int totalSleepHours = dbManager.getTotalSleepHours();
        int totalHydrationCups = dbManager.getTotalCups();
        int goalCount = dbManager.getGoalsCount();
        
        // Get recent entries
        Map<String, String> recentWorkout = dbManager.getRecentWorkout();
        Map<String, String> recentMeal = dbManager.getRecentMeal();
        Map<String, String> recentSleep = dbManager.getRecentSleep();
        Map<String, String> recentGoal = dbManager.getRecentGoal();
        
        String workoutInfo = workoutCount > 0 ? 
            (recentWorkout.containsKey("workout_type") ? 
                " <span style='color:#6B7280;font-size:12px;'>(Latest: " + recentWorkout.get("workout_type") + " - " + recentWorkout.get("workout_date") + ")</span>" 
                : "") 
            : "";
        
        String sleepInfo = sleepCount > 0 && recentSleep.containsKey("sleep_date") ?
            " <span style='color:#6B7280;font-size:12px;'>(Latest entry: " + recentSleep.get("sleep_date") + ")</span>"
            : "";
        
        String goalInfo = goalCount > 0 && recentGoal.containsKey("goal_name") ?
            " <span style='color:#6B7280;font-size:12px;'>(\"" + recentGoal.get("goal_name") + "\" started " + recentGoal.get("start_date") + ")</span>"
            : "";
        
        String mealInfo = mealCount > 0 && recentMeal.containsKey("food_name") ?
            recentMeal.get("food_name") + " (" + recentMeal.get("meal_type") + ")"
            : "No meals logged yet";
        
        String mealDateInfo = mealCount > 0 && recentMeal.containsKey("meal_date") ?
            " <span style='color:#6B7280;font-size:12px;'>on " + recentMeal.get("meal_date") + "</span>"
            : "";
        
        return "<html><div style='line-height:1.8;'>" +
            "<p style='font-size:14px;color:#7C3AED;margin:0 0 12px 0;font-weight:bold;'>Your Progress Summary</p>" +
            "<p style='font-size:13px;color:#1a1a1a;margin:6px 0;'><strong style='color:#7C3AED;'>Workouts:</strong> " + workoutCount + " logged" + workoutInfo + "</p>" +
            "<p style='font-size:13px;color:#1a1a1a;margin:6px 0;'><strong style='color:#3B82F6;'>Hydration:</strong> " + totalHydrationCups + " cups logged</p>" +
            "<p style='font-size:13px;color:#1a1a1a;margin:6px 0;'><strong style='color:#EC4899;'>Sleep:</strong> " + totalSleepHours + " total hours" + sleepInfo + "</p>" +
            "<p style='font-size:13px;color:#1a1a1a;margin:6px 0;'><strong style='color:#7C3AED;'>Goals:</strong> " + goalCount + " created" + goalInfo + "</p>" +
            "<p style='font-size:13px;color:#1a1a1a;margin:6px 0;'><strong style='color:#10B981;'>Nutrition:</strong> " + mealInfo + mealDateInfo + "</p>" +
            "</div></html>";
    }
    
    // Displays a message to the user.

    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(null, msg, "WellTrack", JOptionPane.INFORMATION_MESSAGE);
    }
}