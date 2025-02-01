package uk.ac.ncl.advancedjava.quizapi.questions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * QuestionFactory - provides a factory for creating instances of {@link Question} types such as
 * {@link FreeResponseQuestion} and {@link MultipleChoiceQuestion}. It ensures that identical questions are reused
 * by maintaining and internal cache of previously created questions.
 * This class uses the Factory design pattern to manage the creation and caching of questions.
 *
 * @author Filip Kovarik - S24039999
 */
public class QuestionFactory {

    private static final Map<String, Question> QUESTIONS = new HashMap<>();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private QuestionFactory() {}

    /**
     * Retrieves an instance of {@link FreeResponseQuestion} based on the provided type and question text.
     * If the question already exists in the internal cache, it is returned. Otherwise, a new instance is created,
     * cached, and then returned.
     *
     * @param type          the type of question to create; must be {@link QuestionType#FREE_RESPONSE}
     * @param questionText  the text of the question
     * @param correctAnswer the correct answer for the question
     * @return an instance of {@link FreeResponseQuestion}
     * @throws IllegalArgumentException if the provided type is not {@link QuestionType#FREE_RESPONSE}
     */
    public static Question getInstance(QuestionType type, String questionText, String correctAnswer) {
        String key = generateKey(type, questionText);
        Question question = QUESTIONS.get(key);

        if (question != null) { return question; }

        if (type == QuestionType.FREE_RESPONSE) {
            question = new FreeResponseQuestion(questionText, correctAnswer);
        } else {
            throw new IllegalArgumentException("Incorrect question type: " + type);
        }

        QUESTIONS.put(key, question);

        return question;
    }

    /**
     * Retrieves an instance of {@link MultipleChoiceQuestion} based on the provided type, question text, options,
     * and correct answers. If the question already exists in the internal cache, it is returned.
     * Otherwise, a new instance is created, cached, and then returned.
     *
     * @param type              the type of question to create; must be {@link QuestionType#MULTIPLE_CHOICE}
     * @param questionText      the text of the question
     * @param options           a map of option identifiers to option text
     * @param correctOptions    a set of identifiers for the correct options
     * @return an instance of {@link MultipleChoiceQuestion}
     * @throws IllegalArgumentException if the provided type is not {@link QuestionType#MULTIPLE_CHOICE}
     */
    public static Question getInstance(QuestionType type, String questionText, Map<Character, String> options, Set<Character> correctOptions) {
        String key = generateKey(type, questionText);
        Question question = QUESTIONS.get(key);

        if (question != null) { return question; }

        if (type == QuestionType.MULTIPLE_CHOICE) {
            question = new MultipleChoiceQuestion(questionText, options, correctOptions);
        } else {
            throw new IllegalArgumentException("Incorrect question type: " + type);
        }

        QUESTIONS.put(key, question);

        return question;
    }

    /**
     * Generates a unique key based on the question type and question text. This key is used to cache and retrieve
     * question instances.
     *
     * @param type          the type of question
     * @param questionText  the text of the question
     * @return a unique key representing the question
     */
    private static String generateKey(QuestionType type, String questionText) {
        return type + ":" + questionText;
    }

    /**
     * Clears the internal question cache. This method is intended for use in unit tests to reset the factory state.
     */
    static void resetFactory() {
        QUESTIONS.clear();
    }
}
