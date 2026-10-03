import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores a simple Frequently Asked Questions (FAQ) database as question -> answer
 * pairs. This is separate from the Intent system so that specific factual
 * questions (e.g. "What is AI?") can be answered directly, in addition to the
 * broader intent-based conversation handling.
 *
 * The data is easy to extend: just add more calls to addFAQ(...) in buildDefaultData().
 */
public class KnowledgeBase {

    // question keywords (lowercase) -> answer
    private final Map<String, String> faqEntries;
    private final NLPProcessor nlpProcessor;

    public KnowledgeBase(NLPProcessor nlpProcessor) {
        this.nlpProcessor = nlpProcessor;
        this.faqEntries = new LinkedHashMap<>();
        buildDefaultData();
    }

    private void addFAQ(String questionKeywords, String answer) {
        faqEntries.put(questionKeywords.toLowerCase(), answer);
    }

    private void buildDefaultData() {
        addFAQ("what is ai artificial intelligence",
                "Artificial Intelligence (AI) is the branch of computer science that focuses on building "
                        + "systems capable of performing tasks that normally require human intelligence, "
                        + "such as understanding language, recognizing patterns, and making decisions.");

        addFAQ("what is machine learning ml",
                "Machine Learning is a subset of AI where computers learn patterns from data and improve "
                        + "their performance on a task without being explicitly programmed for every rule.");

        addFAQ("what is nlp natural language processing",
                "Natural Language Processing (NLP) is a field of AI that helps computers understand, "
                        + "interpret, and respond to human language, such as text or speech.");

        addFAQ("what is java",
                "Java is a popular, object-oriented, platform-independent programming language used to "
                        + "build everything from desktop apps to large-scale enterprise systems.");

        addFAQ("what is a chatbot",
                "A chatbot is a software application designed to simulate human conversation through text "
                        + "or voice, often used to answer questions or assist users automatically.");

        addFAQ("what can you do capabilities features",
                "I can chat with you, answer frequently asked questions about AI, Machine Learning, NLP, "
                        + "and Java, and help explain basic programming and internship-related concepts.");

        addFAQ("how does this chatbot work",
                "I work by cleaning and analyzing your message using basic NLP techniques, detecting your "
                        + "intent using a rule-based keyword scoring system, then searching my knowledge base "
                        + "or intent responses to generate the most relevant reply.");

        addFAQ("who developed made created you built you",
                "I was developed as part of an internship project to demonstrate a Java-based, rule-based "
                        + "NLP chatbot that works completely offline without any paid AI API.");

        addFAQ("what is your purpose goal objective",
                "My purpose is to demonstrate how NLP techniques and rule-based intent classification can be "
                        + "combined to build an interactive chatbot, as part of an internship project.");
    }

    /**
     * Attempts to find the best matching FAQ answer for the user's message using
     * keyword overlap scoring. Returns null if no FAQ entry scores above zero.
     */
    public String findAnswer(String rawUserInput) {
        List<String> userKeywords = nlpProcessor.processToKeywords(rawUserInput);
        if (userKeywords.isEmpty()) {
            return null;
        }

        String bestAnswer = null;
        int bestScore = 0;

        for (Map.Entry<String, String> entry : faqEntries.entrySet()) {
            List<String> faqKeywords = nlpProcessor.processToKeywords(entry.getKey());
            int score = overlapScore(userKeywords, faqKeywords);
            if (score > bestScore) {
                bestScore = score;
                bestAnswer = entry.getValue();
            }
        }

        // Require at least 2 overlapping keywords to avoid false-positive matches.
        return bestScore >= 2 ? bestAnswer : null;
    }

    private int overlapScore(List<String> userKeywords, List<String> faqKeywords) {
        int score = 0;
        for (String userWord : userKeywords) {
            if (faqKeywords.contains(userWord)) {
                score++;
            }
        }
        return score;
    }

    public List<String> getAllQuestionsSummary() {
        return new ArrayList<>(faqEntries.keySet());
    }
}
