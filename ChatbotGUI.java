import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Swing-based graphical user interface for the AI Chatbot.
 * Renders a modern, messenger-style chat window with rounded chat bubbles,
 * avatars, a gradient header, and pill-shaped buttons/input field.
 *
 * All chatbot "thinking" is delegated to ChatbotEngine, keeping this class
 * focused purely on presentation.
 */
public class ChatbotGUI extends JFrame {

    // ---- Modern color palette ----
    private static final Color BACKGROUND_COLOR = new Color(241, 244, 249);
    private static final Color HEADER_COLOR_TOP = new Color(99, 102, 241);
    private static final Color HEADER_COLOR_BOTTOM = new Color(79, 70, 229);
    private static final Color USER_BUBBLE_COLOR = new Color(99, 102, 241);
    private static final Color USER_TEXT_COLOR = Color.WHITE;
    private static final Color BOT_BUBBLE_COLOR = Color.WHITE;
    private static final Color BOT_TEXT_COLOR = new Color(31, 41, 55);
    private static final Color TIMESTAMP_COLOR = new Color(156, 163, 175);
    private static final Color SEND_BUTTON_COLOR = new Color(99, 102, 241);
    private static final Color CLEAR_BUTTON_COLOR = Color.WHITE;
    private static final Color CLEAR_BUTTON_TEXT = new Color(75, 85, 99);
    private static final Color EXIT_BUTTON_COLOR = new Color(239, 68, 68);
    private static final Color INPUT_BORDER_COLOR = new Color(224, 227, 235);
    private static final Color BOT_AVATAR_COLOR = new Color(99, 102, 241);
    private static final Color USER_AVATAR_COLOR = new Color(16, 185, 129);

    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 19);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font AVATAR_FONT = new Font("Segoe UI", Font.BOLD, 14);

    private final ChatbotEngine chatbotEngine;
    private JPanel messagesPanel;
    private JScrollPane scrollPane;
    private JTextField inputField;
    private JButton sendButton;

    public ChatbotGUI() {
        this.chatbotEngine = new ChatbotEngine();
        initializeWindow();
        buildHeader();
        buildChatArea();
        buildBottomSection();
        showWelcomeMessage();
    }

    private void initializeWindow() {
        setTitle("AI Chatbot - Internship Project");
        setSize(640, 760);
        setMinimumSize(new Dimension(420, 520));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(0, 0));
    }

    // ---------------------------------------------------------------
    // HEADER
    // ---------------------------------------------------------------

    private void buildHeader() {
        JPanel headerPanel = new GradientPanel(HEADER_COLOR_TOP, HEADER_COLOR_BOTTOM);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 78));
        headerPanel.setBorder(new EmptyBorder(12, 22, 12, 22));

        JPanel textStack = new JPanel();
        textStack.setOpaque(false);
        textStack.setLayout(new BoxLayout(textStack, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("AI Chatbot Assistant");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("● Online — Rule-Based NLP Engine");
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(new Color(224, 231, 255));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textStack.add(titleLabel);
        textStack.add(Box.createVerticalStrut(3));
        textStack.add(subtitleLabel);

        JLabel avatarLabel = createAvatarLabel("AI", new Color(255, 255, 255, 60), Color.WHITE, 46);

        JPanel leftGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        leftGroup.setOpaque(false);
        leftGroup.add(avatarLabel);
        leftGroup.add(textStack);

        headerPanel.add(leftGroup, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
    }

    // ---------------------------------------------------------------
    // CHAT AREA
    // ---------------------------------------------------------------

    private void buildChatArea() {
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(BACKGROUND_COLOR);
        messagesPanel.setBorder(new EmptyBorder(16, 14, 16, 14));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BACKGROUND_COLOR);
        wrapper.add(messagesPanel, BorderLayout.NORTH);

        scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    // ---------------------------------------------------------------
    // BOTTOM SECTION (input row + control row)
    // ---------------------------------------------------------------

    private void buildBottomSection() {
        JPanel bottomWrapper = new JPanel();
        bottomWrapper.setLayout(new BoxLayout(bottomWrapper, BoxLayout.Y_AXIS));
        bottomWrapper.setBackground(BACKGROUND_COLOR);
        bottomWrapper.setBorder(new EmptyBorder(8, 16, 16, 16));

        // ---- Input row ----
        JPanel inputRow = new JPanel(new BorderLayout(10, 0));
        inputRow.setOpaque(false);
        inputRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedPanel inputFieldHolder = new RoundedPanel(24, Color.WHITE, INPUT_BORDER_COLOR);
        inputFieldHolder.setLayout(new BorderLayout());

        inputField = new JTextField();
        inputField.setFont(MAIN_FONT);
        inputField.setOpaque(false);
        inputField.setBorder(new EmptyBorder(12, 18, 12, 18));
        inputField.addActionListener(this::onSendMessage);
        inputFieldHolder.add(inputField, BorderLayout.CENTER);

        sendButton = createPillButton("Send", SEND_BUTTON_COLOR, Color.WHITE, 90);
        sendButton.addActionListener(this::onSendMessage);

        inputRow.add(inputFieldHolder, BorderLayout.CENTER);
        inputRow.add(sendButton, BorderLayout.EAST);
        inputRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        // ---- Control row ----
        JPanel controlRow = new JPanel(new GridLayout(1, 2, 10, 0));
        controlRow.setOpaque(false);
        controlRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlRow.setBorder(new EmptyBorder(10, 0, 0, 0));
        controlRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton clearButton = createOutlineButton("Clear Chat", CLEAR_BUTTON_TEXT);
        clearButton.addActionListener(this::onClearChat);

        JButton exitButton = createPillButton("Exit", EXIT_BUTTON_COLOR, Color.WHITE, 0);
        exitButton.addActionListener(this::onExit);

        controlRow.add(clearButton);
        controlRow.add(exitButton);

        bottomWrapper.add(inputRow);
        bottomWrapper.add(controlRow);

        add(bottomWrapper, BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------
    // Styled component factories
    // ---------------------------------------------------------------

    private JButton createPillButton(String text, Color backgroundColor, Color textColor, int preferredWidth) {
        RoundedButton button = new RoundedButton(text, backgroundColor, textColor);
        if (preferredWidth > 0) {
            button.setPreferredSize(new Dimension(preferredWidth, 46));
        } else {
            button.setPreferredSize(new Dimension(button.getPreferredSize().width, 42));
        }
        return button;
    }

    private JButton createOutlineButton(String text, Color textColor) {
        RoundedButton button = new RoundedButton(text, Color.WHITE, textColor);
        button.setBorderColor(INPUT_BORDER_COLOR);
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 42));
        return button;
    }

    private JLabel createAvatarLabel(String initials, Color bg, Color fg, int size) {
        JLabel label = new JLabel(initials, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setOpaque(false);
        label.setBackground(bg);
        label.setForeground(fg);
        label.setFont(AVATAR_FONT);
        label.setPreferredSize(new Dimension(size, size));
        label.setMinimumSize(new Dimension(size, size));
        label.setMaximumSize(new Dimension(size, size));
        return label;
    }

    // ---------------------------------------------------------------
    // Chat logic / message rendering
    // ---------------------------------------------------------------

    private void showWelcomeMessage() {
        appendMessage("Hello! I'm your AI chatbot. How can I help you today?", false);
    }

    private void onSendMessage(ActionEvent event) {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) {
            return;
        }
        appendMessage(userText, true);
        inputField.setText("");

        String botReply = chatbotEngine.getResponse(userText);
        appendMessage(botReply, false);
    }

    private void onClearChat(ActionEvent event) {
        messagesPanel.removeAll();
        messagesPanel.revalidate();
        messagesPanel.repaint();
        showWelcomeMessage();
    }

    private void onExit(ActionEvent event) {
        int choice = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to exit?", "Exit Chatbot",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Adds one chat bubble (user or bot) to the message list and auto-scrolls
     * to the bottom so the newest message is always visible.
     */
    private void appendMessage(String message, boolean isUser) {
        MessageBubble bubble = new MessageBubble(message, isUser);
        JPanel row = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel avatar = isUser
                ? createAvatarLabel("You", USER_AVATAR_COLOR, Color.WHITE, 34)
                : createAvatarLabel("AI", BOT_AVATAR_COLOR, Color.WHITE, 34);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 10));

        JPanel group = new JPanel();
        group.setOpaque(false);
        group.setLayout(new BoxLayout(group, BoxLayout.X_AXIS));
        if (isUser) {
            group.add(bubble);
            group.add(Box.createHorizontalStrut(8));
            group.add(avatar);
        } else {
            group.add(avatar);
            group.add(Box.createHorizontalStrut(8));
            group.add(bubble);
        }

        row.add(group);
        row.setBorder(new EmptyBorder(6, 6, 6, 6));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));

        messagesPanel.add(row);
        messagesPanel.revalidate();
        messagesPanel.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }

    // ---------------------------------------------------------------
    // Custom painted components
    // ---------------------------------------------------------------

    /** A JPanel that paints a smooth vertical gradient background. */
    private static class GradientPanel extends JPanel {
        private final Color top;
        private final Color bottom;

        GradientPanel(Color top, Color bottom) {
            this.top = top;
            this.bottom = bottom;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** A JPanel with fully rounded corners and an optional border. */
    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color fillColor;
        private final Color borderColor;

        RoundedPanel(int radius, Color fillColor, Color borderColor) {
            this.radius = radius;
            this.fillColor = fillColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fillColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** A JButton painted as a smooth, rounded "pill" shape with a hover effect. */
    private static class RoundedButton extends JButton {
        private final Color baseColor;
        private Color borderColor = null;
        private boolean hovering = false;

        RoundedButton(String text, Color baseColor, Color textColor) {
            super(text);
            this.baseColor = baseColor;
            setForeground(textColor);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 20, 10, 20));
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    hovering = true;
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    hovering = false;
                    repaint();
                }
            });
        }

        void setBorderColor(Color color) {
            this.borderColor = color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fill = hovering ? baseColor.darker() : baseColor;
            if (baseColor.equals(Color.WHITE)) {
                fill = hovering ? new Color(245, 246, 250) : Color.WHITE;
            }
            g2.setColor(fill);
            int arc = getHeight();
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, arc, arc));
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, arc, arc));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** A chat bubble that wraps text, rounds its corners, and aligns per sender. */
    private class MessageBubble extends JPanel {
        private static final int MAX_TEXT_WIDTH = 320;

        private final Color bubbleColor;
        private final boolean isUser;

        MessageBubble(String message, boolean isUser) {
            this.bubbleColor = isUser ? USER_BUBBLE_COLOR : BOT_BUBBLE_COLOR;
            this.isUser = isUser;

            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(10, 14, 8, 14));

            Color textColor = isUser ? USER_TEXT_COLOR : BOT_TEXT_COLOR;

            JTextArea textArea = new JTextArea(message);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setOpaque(false);
            textArea.setFont(MAIN_FONT);
            textArea.setForeground(textColor);
            textArea.setBorder(null);
            textArea.setFocusable(false);
            textArea.setHighlighter(null);

            // Use column-based sizing (the standard, reliable way to make a
            // JTextArea wrap at a predictable width inside a BoxLayout) instead
            // of manually measuring pixel widths, which can mismatch the real
            // layout pass and clip text.
            FontMetrics metrics = textArea.getFontMetrics(MAIN_FONT);
            int averageCharWidth = Math.max(1, metrics.charWidth('n'));
            int maxColumns = MAX_TEXT_WIDTH / averageCharWidth;
            int neededColumns = (int) Math.ceil((double) metrics.stringWidth(message) / averageCharWidth);
            textArea.setColumns(Math.max(1, Math.min(neededColumns, maxColumns)));

            // Columns fix a reliable pixel width; now force the text area to
            // that exact width so it can compute the correct wrapped height
            // (a JTextArea only knows its wrapped line count once its width
            // is actually set, not just requested via columns).
            int fixedWidth = textArea.getPreferredSize().width;
            textArea.setSize(new Dimension(fixedWidth, Short.MAX_VALUE));
            Dimension wrappedSize = textArea.getPreferredSize();
            Dimension finalSize = new Dimension(fixedWidth, wrappedSize.height);
            textArea.setPreferredSize(finalSize);
            textArea.setMinimumSize(finalSize);
            textArea.setMaximumSize(finalSize);
            textArea.setAlignmentX(isUser ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

            JLabel timeLabel = new JLabel(new SimpleDateFormat("hh:mm a").format(new Date()));
            timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            timeLabel.setForeground(isUser ? new Color(224, 231, 255) : TIMESTAMP_COLOR);
            timeLabel.setBorder(new EmptyBorder(4, 0, 0, 0));
            timeLabel.setAlignmentX(isUser ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

            JPanel textStack = new JPanel();
            textStack.setOpaque(false);
            textStack.setLayout(new BoxLayout(textStack, BoxLayout.Y_AXIS));
            textStack.add(textArea);
            textStack.add(timeLabel);

            add(textStack, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bubbleColor);
            int arc = 20;
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, arc, arc));
            if (!isUser) {
                g2.setColor(new Color(229, 231, 235));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, arc, arc));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
