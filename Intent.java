import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single conversational intent (e.g. GREETING, GOODBYE).
 * Each intent has a name, a list of keywords/phrases used for matching,
 * and a list of possible responses the bot can give when this intent is detected.
 */
public class Intent {

    private final String name;
    private final List<String> keywords;
    private final List<String> responses;

    public Intent(String name) {
        this.name = name;
        this.keywords = new ArrayList<>();
        this.responses = new ArrayList<>();
    }

    public void addKeyword(String keyword) {
        keywords.add(keyword.toLowerCase());
    }

    public void addKeywords(String... words) {
        for (String w : words) {
            addKeyword(w);
        }
    }

    public void addResponse(String response) {
        responses.add(response);
    }

    public void addResponses(String... texts) {
        for (String t : texts) {
            addResponse(t);
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<String> getResponses() {
        return responses;
    }

    /**
     * Returns a random response belonging to this intent.
     * Using multiple possible responses makes the bot feel less repetitive.
     */
    public String getRandomResponse() {
        if (responses.isEmpty()) {
            return "I don't have a response configured for this yet.";
        }
        int index = (int) (Math.random() * responses.size());
        return responses.get(index);
    }
}
