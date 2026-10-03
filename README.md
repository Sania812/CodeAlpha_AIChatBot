# AI Chatbot — Internship Project No. 3

A complete, offline, Java-based AI chatbot with a Swing GUI, built using rule-based
Natural Language Processing (NLP) and intent classification — **no external APIs,
no API keys, no internet connection required**.

---

## 1. Project Structure

```
AIChatbot/
│
├── Main.java              -> Application entry point
├── ChatbotGUI.java        -> Swing GUI (chat window, buttons, styling)
├── ChatbotEngine.java     -> Wires NLP + intents + knowledge base together
├── NLPProcessor.java      -> Text preprocessing, tokenization, keyword extraction
├── Intent.java            -> Data model for one conversational intent
├── IntentClassifier.java  -> Rule-based scoring/classification engine
├── KnowledgeBase.java     -> FAQ question-answer database
├── ResponseGenerator.java -> Combines FAQ + intent results into final reply
├── AIChatbot.jar          -> Prebuilt runnable JAR
├── .gitignore
└── README.md
```

---

## 2. What is an AI Chatbot?

An AI chatbot is a software program that simulates a conversation with a human user.
It receives text input, analyzes it, and produces a relevant response. Chatbots can
range from simple rule-based systems (like this project) to advanced systems powered
by large neural network models.

## 3. What is NLP?

Natural Language Processing (NLP) is a field of computer science and AI concerned with
enabling computers to understand, interpret, and generate human language. NLP covers
tasks such as tokenization (splitting text into words), removing noise (like punctuation),
and identifying meaning or intent behind a sentence.

## 4. How NLP is Used in This Project

`NLPProcessor.java` implements a small NLP pipeline:

1. **Lowercasing** — normalizes text so "Hello" and "hello" are treated the same.
2. **Punctuation removal** — strips symbols like `?`, `!`, `.` using a regex.
3. **Extra-space removal** — collapses multiple spaces into one.
4. **Tokenization** — splits the cleaned sentence into individual word tokens.
5. **Stop-word removal** — removes common filler words ("is", "the", "a"...) so that
   only meaningful keywords remain for matching.

This pipeline runs on every message before intent detection or FAQ lookup.

## 5. How Intent Detection Works

`IntentClassifier.java` implements a **weighted keyword scoring algorithm**:

- Every `Intent` (see `Intent.java`) has a list of keywords/phrases.
- For each intent, the classifier scores how many of its keywords appear in the
  user's cleaned tokens (+1 per matching single word, +2 for a matching multi-word
  phrase such as "good morning").
- A small similarity check also catches simple word variants (e.g. "program" vs
  "programs").
- The intent with the **highest score** wins, as long as it passes a minimum
  confidence threshold. Otherwise, the `UNKNOWN` fallback intent is used.

This means the bot can recognize "hello", "hi", "hey", and "good morning" as the
same `GREETING` intent, even though they use completely different words.

## 6. How the Knowledge Base Works

`KnowledgeBase.java` stores FAQ entries as `question-keywords -> answer` pairs
(e.g. "what is ai artificial intelligence" -> the AI definition). When a user asks
a question, their keywords are compared against each FAQ entry's keywords using an
overlap score. If at least two keywords match, the FAQ answer is returned. This lets
the bot answer specific factual questions ("What is NLP?") in addition to general
conversation.

To add a new FAQ, just add one line in `KnowledgeBase.buildDefaultData()`:

```java
addFAQ("what is your favorite color", "I don't have a favorite color, I'm a chatbot!");
```

## 7. How the Response Engine Works

`ResponseGenerator.java` follows this priority order:

```
User Input
     ↓
Text Preprocessing (NLPProcessor)
     ↓
Tokenization + Keyword Extraction
     ↓
Knowledge Base Search (specific FAQ match?)
     ↓ (if no FAQ match)
Intent Detection (IntentClassifier)
     ↓
Response Generation (pick a response from the matched intent)
     ↓
Display Response in GUI
```

If neither the knowledge base nor any intent matches confidently, the `UNKNOWN`
intent provides a graceful fallback message instead of crashing or staying silent.

## 8. How the Rule-Based "Machine Learning" Logic Works

This project does **not** use a trained neural network or an external AI API.
Instead, it uses an **NLP-based rule/classification approach**:

- Each intent acts like a lightweight "class" in a classifier.
- Keywords act as the learned "features" for each class.
- The classifier scores every class against the input and picks the highest-scoring
  one — the same fundamental idea behind a Naive Bayes or keyword-based classifier,
  just without probability training on a dataset.

This is intentionally simple, transparent, and fully explainable — every decision
the bot makes can be traced back to a specific keyword match, which makes it ideal
for a beginner-friendly internship project.

## 9. Why This Qualifies as an NLP-Based Chatbot

The chatbot performs genuine NLP steps (normalization, tokenization, stop-word
removal, keyword extraction) and uses those processed tokens to perform **intent
classification** — the same conceptual pipeline used in production NLP systems,
just implemented with rules instead of statistical training.

## 10. Difference Between This Chatbot and a Large AI Model (e.g. ChatGPT)

| Aspect | This Rule-Based Chatbot | Large AI Model (LLM) |
|---|---|---|
| Knowledge source | Hand-written intents/FAQ | Trained on massive text datasets |
| Understanding | Keyword/pattern matching | Deep contextual understanding |
| Flexibility | Limited to defined intents | Can respond to almost anything |
| Internet/API needed | No — fully offline | Usually yes (cloud API) |
| Cost | Free | Often paid |
| Transparency | Fully explainable rules | "Black box" neural network |
| Setup complexity | Simple, beginner-friendly | Requires large infrastructure |

---

## 11. How the GUI Communicates with the Chatbot

`ChatbotGUI.java` (Swing) never contains any NLP or intent logic itself. When the
user clicks **Send** or presses Enter:

1. The GUI reads the text from the input field.
2. It calls `chatbotEngine.getResponse(userText)`.
3. `ChatbotEngine` runs the full NLP → intent → knowledge base → response pipeline.
4. The returned string is displayed as a new "Bot" message bubble in the chat area.

This separation (GUI vs. logic) follows good Object-Oriented Programming practice —
you could swap the Swing GUI for a web GUI or console GUI without changing any
chatbot logic at all.

---

## 12. Running Instructions

**Required Java version:** JDK 11 or higher (JDK 17 recommended). No Maven/Gradle
needed — this is a plain Java project.

### Option A — IntelliJ IDEA
1. Open IntelliJ IDEA → `File > Open` → select the `AIChatbot` folder.
2. IntelliJ will detect the `.java` files in the project root; if not, right-click the `AIChatbot` folder → `Mark Directory as > Sources Root`.
3. Open `Main.java`, right-click inside the file → `Run 'Main.main()'`.

### Option B — VS Code
1. Install the "Extension Pack for Java" from the VS Code marketplace.
2. Open the `AIChatbot` folder in VS Code.
3. Open `Main.java`.
4. Click the **Run** button above the `main` method (or press `F5`).

### Option C — Eclipse
1. `File > New > Java Project`, name it `AIChatbot`, uncheck "Use default location" if needed and point it at this folder (or create the project then copy the `.java` files into its `src` folder).
2. Right-click the project → `Refresh`.
3. Right-click `Main.java` → `Run As > Java Application`.

### Option D — Command Line (any OS)
```
cd AIChatbot
javac -d out *.java
java -cp out Main
```
(On Mac/Linux the command is identical — use forward slashes for the path.)

---

## 13. Sample Conversation

```
User: Hello
Bot: Hello! I'm your AI chatbot. How can I help you?

User: What is AI?
Bot: Artificial Intelligence (AI) is the branch of computer science that focuses on
     building systems capable of performing tasks that normally require human
     intelligence...

User: What is NLP?
Bot: Natural Language Processing (NLP) is a field of AI that helps computers
     understand, interpret, and respond to human language...

User: What can you do?
Bot: I can answer frequently asked questions about AI, ML, NLP, and Java, and chat
     with you using rule-based intent detection.

User: Thank you
Bot: You're welcome!

User: random unknown question
Bot: I'm sorry, I don't understand that yet. Try asking me about AI, NLP, Java, or
     programming.
```

---

## 14. Internship Report Content

**Project Title:** Artificial Intelligence Chatbot (Java, Rule-Based NLP)

**Project Overview:** This project is a desktop chatbot application built in Java
using Swing for the GUI and a custom rule-based NLP engine for understanding user
messages. It answers frequently asked questions and handles general conversation
through intent recognition, entirely offline.

**Problem Statement:** Users often need quick answers to common questions but may
not have access to paid AI services or an internet connection. This project solves
that by providing a lightweight, self-contained conversational assistant that runs
entirely on the local machine.

**Objectives:**
- Build an interactive chatbot using core Java and OOP principles.
- Apply basic NLP techniques to process and understand user input.
- Implement a rule-based intent classification system as a lightweight alternative
  to a trained ML model.
- Provide a clean, modern GUI for real-time interaction.
- Maintain an easily extensible FAQ knowledge base.

**Technologies Used:** Java (JDK 11+), Java Swing, core `java.util` and `java.time`
libraries. No external dependencies or paid APIs.

**Features:**
- Real-time chat interface with auto-scroll
- Clear Chat and Exit controls
- Text preprocessing and tokenization
- Weighted-keyword intent classification (17 intents)
- FAQ knowledge base with keyword-overlap matching
- Date/time responses
- Graceful handling of empty or unrecognized input

**System Requirements:** Any OS with JDK 11+ installed (Windows, macOS, Linux). No
internet connection required. Minimum 100MB free disk space.

**Methodology:** The system was designed using a modular OOP architecture where
each class has a single responsibility (NLP processing, intent classification,
knowledge lookup, response generation, GUI). Development followed an iterative
approach: build the NLP pipeline, then the intent classifier, then the knowledge
base, then integrate the GUI.

**NLP Techniques Used:** Lowercasing, punctuation stripping, whitespace
normalization, tokenization, stop-word removal, keyword extraction, weighted
keyword scoring, and basic word-similarity matching.

**System Architecture:**
```
GUI (ChatbotGUI) -> ChatbotEngine -> NLPProcessor -> IntentClassifier
                                                    -> KnowledgeBase
                                   -> ResponseGenerator -> reply -> GUI
```

**Modules:** GUI Module, NLP Module, Intent Classification Module, Knowledge Base
Module, Response Generation Module.

**How the System Works:** See Section 7 ("How the Response Engine Works") above.

**Advantages:** Fully offline, no cost, fast responses, fully transparent/explainable
logic, easy to extend with new intents or FAQs, beginner-friendly codebase.

**Limitations:** Cannot understand complex or highly varied phrasing outside its
defined keywords; has no memory of conversation context across turns; not a true
machine-learning model, so it cannot "learn" from new conversations automatically.

**Future Scope:** Add conversation context/memory, integrate a lightweight trained
ML text classifier, support voice input/output, expand the FAQ database, or connect
to a real database for dynamically updatable knowledge.

**Learning Outcomes:** Practical experience with Java OOP design, Swing GUI
development, rule-based NLP pipelines, event-driven programming, and building a
multi-class, maintainable application architecture.

**Conclusion:** This project successfully demonstrates that an interactive,
NLP-aware chatbot can be built entirely in core Java without relying on paid
external AI services, using well-structured OOP design and rule-based text
classification techniques.

---

## 15. Presentation / Viva Preparation

### A. 10 Viva Questions with Simple Answers

1. **What is this project?**
   A Java desktop chatbot that uses rule-based NLP to answer questions through a
   Swing GUI, without any external AI API.

2. **What NLP techniques did you use?**
   Lowercasing, punctuation removal, tokenization, stop-word removal, and
   keyword-based similarity/scoring.

3. **How does the bot understand different phrasings of the same question?**
   Each intent has multiple keywords/phrases mapped to it, so "hi", "hello", and
   "hey" all score points for the same `GREETING` intent.

4. **Is this a machine learning model?**
   No, it's a rule-based classifier that mimics classification behavior using
   weighted keyword scoring instead of a trained statistical/neural model.

5. **How is the knowledge base structured?**
   As a map of question-keywords to answers, checked using keyword-overlap
   scoring in `KnowledgeBase.java`.

6. **What happens if the bot doesn't understand a message?**
   It falls back to the `UNKNOWN` intent, which returns a polite fallback message
   instead of crashing.

7. **How does the GUI talk to the chatbot logic?**
   The GUI calls a single method, `chatbotEngine.getResponse(text)`, keeping GUI
   and logic cleanly separated (OOP separation of concerns).

8. **Why did you use Swing instead of JavaFX?**
   Swing is bundled with the standard JDK, requires no extra setup, and is
   simple enough for a beginner-friendly, dependency-free project.

9. **How would you extend this project?**
   By adding more intents/FAQs, adding conversation memory, or replacing the
   rule-based classifier with a trained ML model (e.g. Naive Bayes).

10. **What design principle keeps the code organized?**
    Object-Oriented Programming with single-responsibility classes: NLP
    processing, classification, knowledge lookup, response generation, and GUI
    are all separate classes.

### B. 1-Minute Explanation

"My project is an AI-powered chatbot built entirely in Java. It uses basic Natural
Language Processing techniques — like tokenization and keyword extraction — to
understand what the user is asking, even if they phrase it differently. It then
uses a rule-based classification system to detect the user's intent, such as a
greeting or a question about AI, and either answers from a built-in FAQ knowledge
base or gives an intent-based response. Everything runs offline through a clean
Swing GUI, with no paid APIs or internet connection needed."

### C. 5-Minute Detailed Explanation

"This project is an Artificial Intelligence Chatbot built using core Java and
Object-Oriented Programming. The goal was to create an interactive chatbot that
works completely offline, without depending on any paid AI service like OpenAI.

The system is split into several classes, each with one responsibility. The
`NLPProcessor` class handles the Natural Language Processing steps: it converts
user input to lowercase, removes punctuation, trims extra spaces, and splits the
message into individual word tokens. It also removes common stop-words like 'is'
or 'the' so that only meaningful keywords remain.

Those keywords are passed to the `IntentClassifier`, which compares them against a
list of predefined `Intent` objects — things like GREETING, GOODBYE, JAVA, AI,
MACHINE_LEARNING, and so on. Each intent has a set of keywords and sample
responses. The classifier scores every intent based on how many keywords match,
and picks the highest-scoring one. This lets the bot recognize that 'hi', 'hello',
and 'good morning' all mean the same thing, even though the exact words differ.

Alongside the intents, there's a `KnowledgeBase` class that stores frequently
asked questions and their answers, like 'What is AI?' or 'What is NLP?'. It also
uses keyword-overlap scoring to find the best matching answer.

The `ResponseGenerator` class ties these two systems together: it checks the
knowledge base first for a specific factual answer, and falls back to the general
intent system if nothing specific matches. If nothing matches at all, a friendly
fallback message is shown instead of the app crashing or going silent.

All of this logic lives inside `ChatbotEngine`, which the GUI calls through one
simple method. The GUI itself, built with Java Swing in `ChatbotGUI`, displays a
scrolling chat history with styled message bubbles, an input field, a Send button,
a Clear Chat button, and an Exit button — with auto-scrolling so new messages are
always visible.

Because this is a rule-based system rather than a trained neural network, every
decision the bot makes is fully transparent and explainable — you can trace any
response back to the exact keyword match that triggered it. This makes it an
ideal, beginner-friendly demonstration of how NLP concepts and intent
classification work together in a real, working chatbot application."

### D. Every Major Class Explained Simply

- **Main.java** — The starting point of the program. It just opens the chat window.
- **ChatbotGUI.java** — The visual chat window: message area, input box, and buttons.
  Handles what the user sees and clicks, and forwards their messages to the engine.
- **ChatbotEngine.java** — The "brain's control center." Holds all the intents,
  creates the NLP processor, classifier, knowledge base, and response generator,
  and returns the final answer to the GUI.
- **NLPProcessor.java** — Cleans and breaks down text (lowercase, remove
  punctuation, tokenize, remove stop-words) so it's easier to analyze.
- **Intent.java** — A simple data holder representing one topic the bot can
  recognize (like "Greeting"), along with its keywords and possible replies.
- **IntentClassifier.java** — Compares the user's words against every intent's
  keywords and scores them to find the best match — this is the "rule-based ML"
  part of the project.
- **KnowledgeBase.java** — Stores specific FAQ question-answer pairs and searches
  them for a relevant answer.
- **ResponseGenerator.java** — Decides the final reply by checking the knowledge
  base first, then the intent system, then a fallback message.
