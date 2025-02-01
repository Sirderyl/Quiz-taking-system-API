package uk.ac.ncl.advancedjava.quizapi.quiztaking;

import uk.ac.ncl.advancedjava.quizapi.questions.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Quiz - represents a collection of questions that form a quiz.
 * This class allows adding, removing, and retrieving questions in the quiz.
 * A quiz is created through the QuizGenerator class.
 *
 * @author Filip Kovarik
 */
public final class Quiz {

    private final List<Question> questions = new ArrayList<>();

    /**
     * Default constructor for creating an empty Quiz instance.
     */
    Quiz() {}

    /**
     * Constructor for creating a Quiz with a predefined list of questions. This copies the provided list into
     * the quiz's internal question list
     *
     * @param questions the list of questions to be added to the quiz
     */
    Quiz(List<Question> questions) {
        this.questions.addAll(questions);
    }

    /**
     * Adds a question to the quiz.
     *
     * @param question the question to be added
     */
    void addQuestion(Question question) {
        questions.add(question);
    }

    /**
     * Removes a question from the quiz.
     *
     * @param question the question to be removed
     */
    void removeQuestion(Question question) {
        questions.remove(question);
    }

    /**
     * Returns the list of questions in the quiz.
     * The returned list is a defensive copy, meaning modifications to it will not affect
     * the internal list of questions.
     *
     * @return a defensive copy of the list of questions in the quiz
     */
    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    /**
     * Returns a string representation of the quiz, including the list of questions.
     *
     * @return a string representation of the quiz
     */
    @Override
    public String toString() {
        return "Quiz{" +
                "questions=" + questions +
                '}';
    }
}
