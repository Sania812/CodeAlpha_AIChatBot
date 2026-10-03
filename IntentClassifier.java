import java.util.ArrayList;
import java.util.List;

/**
 * A rule-based "classification" engine that scores every known Intent against
 * the user's message and picks the best match. This plays the role that a
 * trained machine-learning classifier would play in a full AI system, but it
 * works entirely offline using weighted keyword matching and simple text
 * similarity - no external model or API is required.
 *
 * Scoring approach:
 *   - Each keyword of an intent that appears in the user's tokens adds 1 point.
 *   - An exact whole-phrase match (keyword contains a space, e.g. "good morning")
 *     found inside the original cleaned sentence adds a bonus of 2 points.
 *   - The intent with the highest score wins, provided the score passes a
 *     minimum confidence threshold; otherwise the UNKNOWN/fallback intent is used.
 */
public class IntentClassifier {

    private static final int MIN_CONFIDENCE_SCORE = 1;

    private final List<Intent> intents;
    private final NLPProcessor nlpProcessor;

    public IntentClassifier(List<Intent> intents, NLPProcessor nlpProcessor) {
        this.intents = intents;
        this.nlpProcessor = nlpProcessor;
    }

    /**
     * Detects the most likely intent for the given raw user input.
     */
    public Intent classify(String rawUserInput) {
        String cleanedSentence = nlpProcessor.preprocess(rawUserInput);
        List<String> keywordTokens = nlpProcessor.processToKeywords(rawUserInput);

        Intent bestIntent = null;
        int bestScore = 0;

        for (Intent intent : intents) {
            int score = scoreIntent(intent, keywordTokens, cleanedSentence);
            if (score > bestScore) {
                bestScore = score;
                bestIntent = intent;
            }
        }

        if (bestIntent == null || bestScore < MIN_CONFIDENCE_SCORE) {
            return findIntentByName("UNKNOWN");
        }
        return bestIntent;
    }

    /**
     * Computes a weighted match score between one intent and the user's message.
     */
    private int scoreIntent(Intent intent, List<String> keywordTokens, String cleanedSentence) {
        int score = 0;
        for (String keyword : intent.getKeywords()) {
            if (keyword.contains(" ")) {
                // Multi-word phrase keyword (e.g. "good morning", "who are you")
                if (cleanedSentence.contains(keyword)) {
                    score += 2;
                }
            } else {
                // Single-word keyword: check token-by-token for an exact match.
                for (String token : keywordTokens) {
                    if (token.equals(keyword)) {
                        score += 1;
                    } else if (similar(token, keyword)) {
                        // Basic similarity match catches minor typos/variants.
                        score += 1;
                    }
                }
            }
        }
        return score;
    }

    /**
     * Very small similarity check: treats two words as similar if one contains
     * the other (handles simple plurals/variants like "program" vs "programs").
     */
    private boolean similar(String tokenA, String tokenB) {
        if (tokenA.length() < 4 || tokenB.length() < 4) {
            return false;
        }
        return tokenA.contains(tokenB) || tokenB.contains(tokenA);
    }

    private Intent findIntentByName(String name) {
        for (Intent intent : intents) {
            if (intent.getName().equalsIgnoreCase(name)) {
                return intent;
            }
        }
        return null;
    }
}
