import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The central engine of the chatbot. It owns the NLP pipeline, the list of
 * intents, the FAQ knowledge base, and the response generator. The GUI talks
 * only to this class, keeping the interface decoupled from the chatbot logic.
 *
 * Flow for every user message:
 *   User Input -> NLPProcessor (preprocess/tokenize) -> IntentClassifier
 *   -> KnowledgeBase search -> ResponseGenerator -> final reply string
 */
public class ChatbotEngine {

    private final NLPProcessor nlpProcessor;
    private final List<Intent> intents;
    private final IntentClassifier intentClassifier;
    private final KnowledgeBase knowledgeBase;
    private final ResponseGenerator responseGenerator;

    public ChatbotEngine() {
        this.nlpProcessor = new NLPProcessor();
        this.intents = buildIntents();
        this.intentClassifier = new IntentClassifier(intents, nlpProcessor);
        this.knowledgeBase = new KnowledgeBase(nlpProcessor);
        this.responseGenerator = new ResponseGenerator(knowledgeBase, intentClassifier);
    }

    /**
     * Main entry point used by the GUI: takes raw user text and returns the
     * bot's reply. Handles date/time intent specially since it needs live data.
     */
    public String getResponse(String userInput) {
        try {
            if (userInput == null || userInput.trim().isEmpty()) {
                return "Please type a message so I can help you.";
            }

            Intent quickCheckIntent = intentClassifier.classify(userInput);
            if (quickCheckIntent != null && quickCheckIntent.getName().equals("DATETIME")) {
                return buildDateTimeResponse();
            }

            return responseGenerator.generateResponse(userInput);
        } catch (Exception e) {
            // Defensive catch-all so the GUI never crashes because of unexpected input.
            return "Sorry, something went wrong while processing your message. Please try again.";
        }
    }

    private String buildDateTimeResponse() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");
        return "Today's date is " + today.format(dateFormat) + " and the current time is "
                + now.format(timeFormat) + ".";
    }

    /**
     * Builds and returns the full list of intents with their keywords and
     * sample responses. This acts as the chatbot's "training data" for the
     * rule-based classifier.
     */
    private List<Intent> buildIntents() {
        List<Intent> list = new ArrayList<>();

        Intent greeting = new Intent("GREETING");
        greeting.addKeywords("hello", "hi", "hey", "morning", "afternoon", "evening", "greetings", "yo");
        greeting.addResponses(
                "Hello! I'm your AI chatbot. How can I help you today?",
                "Hi there! What would you like to know?",
                "Hey! Feel free to ask me anything about AI, NLP, or Java."
        );
        list.add(greeting);

        Intent goodbye = new Intent("GOODBYE");
        goodbye.addKeywords("bye", "goodbye", "exit", "quit", "farewell", "later", "see you");
        goodbye.addResponses(
                "Goodbye! Have a great day!",
                "See you later! Feel free to come back anytime.",
                "Bye! It was nice chatting with you."
        );
        list.add(goodbye);

        Intent botIntro = new Intent("BOT_INTRO");
        botIntro.addKeywords("name", "who are you", "yourself", "about you", "introduce");
        botIntro.addResponses(
                "I'm an AI Chatbot built in Java using rule-based NLP techniques, created for an internship project.",
                "I'm your friendly Java-based chatbot assistant. I use NLP and intent detection to answer your questions."
        );
        list.add(botIntro);

        Intent help = new Intent("HELP");
        help.addKeywords("help", "assist", "support", "guide", "stuck");
        help.addResponses(
                "Sure! You can ask me about AI, Machine Learning, NLP, Java, programming, or this project. "
                        + "Just type your question naturally.",
                "I'm here to help! Try asking things like 'What is AI?' or 'What can you do?'"
        );
        list.add(help);

        Intent thanks = new Intent("THANKS");
        thanks.addKeywords("thanks", "thank you", "appreciate", "thx");
        thanks.addResponses(
                "You're welcome!",
                "Happy to help!",
                "Anytime! Let me know if you have more questions."
        );
        list.add(thanks);

        Intent howAreYou = new Intent("HOW_ARE_YOU");
        howAreYou.addKeywords("how are you", "how you doing", "how is it going", "whats up");
        howAreYou.addResponses(
                "I'm just a program, but I'm running smoothly and ready to help you!",
                "Doing great, thanks for asking! How can I assist you?"
        );
        list.add(howAreYou);

        Intent capabilities = new Intent("CAPABILITIES");
        capabilities.addKeywords("what can you do", "your capabilities", "your features", "functions");
        capabilities.addResponses(
                "I can answer frequently asked questions about AI, ML, NLP, and Java, and chat with you using "
                        + "rule-based intent detection."
        );
        list.add(capabilities);

        Intent javaIntent = new Intent("JAVA");
        javaIntent.addKeywords("java", "jvm", "jdk", "oop", "object oriented");
        javaIntent.addResponses(
                "Java is a widely-used, object-oriented programming language known for its portability "
                        + "('write once, run anywhere') and strong ecosystem.",
                "Java runs on the Java Virtual Machine (JVM), which makes compiled Java code platform-independent."
        );
        list.add(javaIntent);

        Intent programming = new Intent("PROGRAMMING");
        programming.addKeywords("programming", "coding", "code", "developer", "software", "language");
        programming.addResponses(
                "Programming is the process of writing instructions that a computer can execute to perform tasks. "
                        + "Popular languages include Java, Python, and C++."
        );
        list.add(programming);

        Intent ai = new Intent("AI");
        ai.addKeywords("artificial", "intelligence", "smart system", "ai");
        ai.addResponses(
                "Artificial Intelligence (AI) enables machines to mimic human-like reasoning, learning, and "
                        + "decision-making."
        );
        list.add(ai);

        Intent ml = new Intent("MACHINE_LEARNING");
        ml.addKeywords("machine learning", "training model", "dataset", "prediction", "algorithm");
        ml.addResponses(
                "Machine Learning allows computers to learn patterns from data and make predictions or decisions "
                        + "without being explicitly programmed for each case."
        );
        list.add(ml);

        Intent nlp = new Intent("NLP");
        nlp.addKeywords("nlp", "natural language", "text processing", "tokenization", "linguistics");
        nlp.addResponses(
                "Natural Language Processing (NLP) helps computers understand and generate human language. "
                        + "This chatbot uses basic NLP techniques like tokenization and keyword matching."
        );
        list.add(nlp);

        Intent internship = new Intent("INTERNSHIP");
        internship.addKeywords("internship", "project", "assignment", "evaluation", "report", "submission");
        internship.addResponses(
                "This chatbot was built as an internship project to demonstrate Java programming, OOP design, "
                        + "and rule-based NLP techniques in a real application."
        );
        list.add(internship);

        Intent study = new Intent("STUDY");
        study.addKeywords("study", "learn", "exam", "syllabus", "course", "tutorial", "resources");
        study.addResponses(
                "For studying programming and AI concepts, I'd recommend practicing small projects, reading "
                        + "documentation, and building things like this chatbot!"
        );
        list.add(study);

        Intent contact = new Intent("CONTACT");
        contact.addKeywords("contact", "email", "reach", "support team", "phone number");
        contact.addResponses(
                "This is a demo project, so live contact support isn't available, but you can reach out to your "
                        + "internship supervisor for further assistance."
        );
        list.add(contact);

        Intent dateTime = new Intent("DATETIME");
        dateTime.addKeywords("date", "time", "today", "current time", "what day");
        dateTime.addResponses("Let me check the current date and time for you.");
        list.add(dateTime);

        Intent unknown = new Intent("UNKNOWN");
        unknown.addResponses(
                "I'm sorry, I don't understand that yet. Try asking me about AI, NLP, Java, or programming.",
                "I'm not sure about that. Could you rephrase, or ask me about AI, Machine Learning, or Java?"
        );
        list.add(unknown);

        return list;
    }
}
