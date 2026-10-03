import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles the basic Natural Language Processing (NLP) steps used by the chatbot:
 *   1. Lowercasing
 *   2. Punctuation removal
 *   3. Extra-space removal
 *   4. Tokenization
 *   5. Stop-word removal (for cleaner keyword extraction)
 *
 * This is a lightweight, rule-based NLP pipeline (no external NLP libraries),
 * which keeps the project self-contained and easy to run offline.
 */
public class NLPProcessor {

    // Common English stop-words that carry little meaning for intent detection.
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "is", "are", "am", "was", "were", "be", "been", "being",
            "do", "does", "did", "will", "would", "shall", "should", "can", "could",
            "may", "might", "must", "i", "you", "he", "she", "it", "we", "they",
            "me", "him", "her", "us", "them", "my", "your", "his", "its", "our", "their",
            "to", "of", "in", "on", "at", "for", "with", "about", "against", "between",
            "into", "through", "during", "before", "after", "above", "below", "from",
            "up", "down", "out", "off", "over", "under", "again", "further", "then",
            "once", "and", "or", "but", "if", "so", "that", "this", "these", "those",
            "please", "just"
    ));

    /**
     * Cleans raw user input: lowercases, removes punctuation, and trims extra spaces.
     */
    public String preprocess(String rawText) {
        if (rawText == null) {
            return "";
        }
        String lower = rawText.toLowerCase().trim();
        // Remove punctuation and any character that is not a letter, digit, or space.
        String noPunctuation = lower.replaceAll("[^a-z0-9\\s]", " ");
        // Collapse multiple spaces into a single space.
        return noPunctuation.replaceAll("\\s+", " ").trim();
    }

    /**
     * Splits cleaned text into individual word tokens.
     */
    public List<String> tokenize(String cleanedText) {
        List<String> tokens = new ArrayList<>();
        if (cleanedText == null || cleanedText.isEmpty()) {
            return tokens;
        }
        String[] parts = cleanedText.split(" ");
        for (String part : parts) {
            if (!part.isBlank()) {
                tokens.add(part);
            }
        }
        return tokens;
    }

    /**
     * Removes stop-words from a token list, leaving the meaningful "keyword" tokens.
     */
    public List<String> extractKeywords(List<String> tokens) {
        List<String> keywords = new ArrayList<>();
        for (String token : tokens) {
            if (!STOP_WORDS.contains(token)) {
                keywords.add(token);
            }
        }
        // If everything was a stop-word (e.g. "who are you"), fall back to all tokens
        // so short questions like "who are you" still carry signal.
        return keywords.isEmpty() ? tokens : keywords;
    }

    /**
     * Full pipeline convenience method: raw text -> keyword tokens.
     */
    public List<String> processToKeywords(String rawText) {
        String cleaned = preprocess(rawText);
        List<String> tokens = tokenize(cleaned);
        return extractKeywords(tokens);
    }
}
