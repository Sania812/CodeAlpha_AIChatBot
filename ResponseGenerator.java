/**
 * Generates the final text response to show the user, combining results from
 * the KnowledgeBase (specific FAQ answers) and the IntentClassifier (general
 * conversational intents).
 *
 * Priority order:
 *   1. If the FAQ knowledge base has a confident answer, use it.
 *   2. Otherwise, fall back to the detected intent's response.
 *   3. If nothing matches, the UNKNOWN intent provides a graceful fallback message.
 */
public class ResponseGenerator {

    private final KnowledgeBase knowledgeBase;
    private final IntentClassifier intentClassifier;

    public ResponseGenerator(KnowledgeBase knowledgeBase, IntentClassifier intentClassifier) {
        this.knowledgeBase = knowledgeBase;
        this.intentClassifier = intentClassifier;
    }

    public String generateResponse(String rawUserInput) {
        if (rawUserInput == null || rawUserInput.trim().isEmpty()) {
            return "It looks like you didn't type anything. Could you please ask a question?";
        }

        // Step 1: Try the FAQ knowledge base first for specific factual questions.
        String faqAnswer = knowledgeBase.findAnswer(rawUserInput);
        if (faqAnswer != null) {
            return faqAnswer;
        }

        // Step 2: Fall back to general intent-based conversation handling.
        Intent detectedIntent = intentClassifier.classify(rawUserInput);
        if (detectedIntent != null) {
            return detectedIntent.getRandomResponse();
        }

        // Step 3: Should rarely happen since UNKNOWN intent always exists.
        return "I'm sorry, I don't understand that yet. Try asking me about AI, NLP, Java, or programming.";
    }
}
