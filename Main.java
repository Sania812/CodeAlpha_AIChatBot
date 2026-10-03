import javax.swing.SwingUtilities;

/**
 * Entry point of the AI Chatbot application.
 * Launches the Swing GUI on the Event Dispatch Thread, as required for
 * thread-safe Swing applications.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatbotGUI chatbotGUI = new ChatbotGUI();
            chatbotGUI.setVisible(true);
        });
    }
}
