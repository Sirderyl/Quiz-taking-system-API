package uk.ac.ncl.advancedjava.quizapi.quiztaking;

import uk.ac.ncl.advancedjava.quizapi.questions.Question;
import uk.ac.ncl.advancedjava.quizapi.questions.QuestionType;

import java.util.*;

/**
 * QuizGenerator - responsible for generating quizzes from a pool of questions.
 * It supports both regular and revision quizzes, ensuring that quizzes contain a mix of free-response
 * and multiple-choice questions. For revision quizzes, it only selects questions that were not seen yet
 * or were answered incorrectly in the past by the student.
 *
 * @author Filip Kovarik
 */
public final class QuizGenerator {

    private final Set<Question> questionPool;

    /**
     * Constructs a new {@code QuizGenerator} with an empty question pool.
     */
    public QuizGenerator() {
        questionPool = new HashSet<>();
    }

    /**
     * Constructs a new {@code QuizGenerator} with the given set of questions.
     *
     * @param questionPool the pool of questions from which quizzes will be generated.
     */
    public QuizGenerator(Set<Question> questionPool) {
        this.questionPool = new HashSet<>(questionPool);
    }

    /**
     * Generates a regular quiz with the specified number of questions. The quiz will contain a mix of
     * free-response and multiple-choice questions, provided that both types are available in the question pool.
     *
     * @param numberOfQuestions the number of questions to include in the quiz
     * @return a {@code Quiz} containing the randomly-picked questions
     * @throws IllegalStateException if there are no free-response or multiple-choice question in the pool.
     * @throws IllegalArgumentException if the number of questions selected is less than 2
     */
    public Quiz generateQuiz(int numberOfQuestions) {
        List<Question> freeResponseQuestions = filterQuestionsByType(QuestionType.FREE_RESPONSE);
        List<Question> multipleChoiceQuestions = filterQuestionsByType(QuestionType.MULTIPLE_CHOICE);

        if (freeResponseQuestions.isEmpty() || multipleChoiceQuestions.isEmpty()) {
            throw new IllegalStateException("The question pool must contain questions of both types");
        }

        List<Question> selectedQuestions = getRandomQuestions(freeResponseQuestions, multipleChoiceQuestions,
                numberOfQuestions, false);

        return new Quiz(selectedQuestions);
    }

    /**
     * Generates a revision quiz for the provided student statistics. The quiz will only include questions
     * that the student has not seen before or answered incorrectly in the past, ensuring it is targeted for revision.
     *
     * @param studentStats      the student's statistics to base the revision quiz on
     * @param numberOfQuestions the number of questions to include in the revision quiz
     * @return a {@code Quiz} containing the randomly-picked questions
     * @throws IllegalArgumentException if the number of questions is less than 1
     */
    public Quiz revise(StudentStatistics studentStats, int numberOfQuestions) {
        List<Question> unseenOrIncorrectQuestions = getUnseenOrIncorrectQuestions(studentStats);

        List<Question> freeResponseQuestions = filterQuestionsByType(unseenOrIncorrectQuestions,
                QuestionType.FREE_RESPONSE);
        List<Question> multipleChoiceQuestions = filterQuestionsByType(unseenOrIncorrectQuestions,
                QuestionType.MULTIPLE_CHOICE);

        List<Question> selectedQuestions = getRandomQuestions(freeResponseQuestions, multipleChoiceQuestions,
                numberOfQuestions, true);

        return new Quiz(selectedQuestions);
    }

    /**
     * Filters questions from the pool based on their type (free-response or multiple-choice).
     *
     * @param type the type of questions to filter by
     * @return a list of questions of the specified type
     */
    private List<Question> filterQuestionsByType(QuestionType type) {
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question question : questionPool) {
            if (question.getType() == type) {
                filteredQuestions.add(question);
            }
        }

        return filteredQuestions;
    }

    /**
     * Filters questions from the provided list based on their type (free-response or multiple-choice).
     *
     * @param questions the list of questions to filter from
     * @param type      the type of questions to filter by
     * @return a list of questions of the specified type
     */
    private List<Question> filterQuestionsByType(List<Question> questions, QuestionType type) {
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question question : questions) {
            if (question.getType() == type) {
                filteredQuestions.add(question);
            }
        }

        return filteredQuestions;
    }

    /**
     * Randomly selects a specified number of questions from the given lists of free-response and multiple-choice
     * questions. Ensures at least one question of each type is selected if the quiz is not a revision.
     *
     * @param freeResponseQuestions     the list of free-response questions
     * @param multipleChoiceQuestions   the list of multiple-choice questions
     * @param numberOfQuestions         the total number of questions to select
     * @param isRevision                whether the quiz is a revision quiz
     * @return a list of randomly selected questions
     * @throws IllegalArgumentException if there are not enough questions available in the pool
     */
    private List<Question> getRandomQuestions(List<Question> freeResponseQuestions,
                                              List<Question> multipleChoiceQuestions, int numberOfQuestions,
                                              boolean isRevision) {
        if (!isRevision && numberOfQuestions < 2) {
            throw new IllegalArgumentException("You must select at least 2 questions to generate a quiz");
        } else if (isRevision && numberOfQuestions < 1) {
            throw new IllegalArgumentException("You must select at least 1 question to generate a revision quiz");
        }

        List<Question> selectedQuestions = new ArrayList<>();

        // Only add one from each type if the requested number is 2 or more for regular quizzes
        if (!isRevision || numberOfQuestions >= 2) {
            if (!freeResponseQuestions.isEmpty()) {
                selectedQuestions.add(freeResponseQuestions.remove(0));
            }
            if (!multipleChoiceQuestions.isEmpty()) {
                selectedQuestions.add(multipleChoiceQuestions.remove(0));
            }
        }

        List<Question> combinedPool = new ArrayList<>();
        combinedPool.addAll(freeResponseQuestions);
        combinedPool.addAll(multipleChoiceQuestions);

        int remainingQuestions = numberOfQuestions - selectedQuestions.size();
        if (remainingQuestions > 0) {
            selectedQuestions.addAll(getQuestions(combinedPool, remainingQuestions));
        }

        return selectedQuestions;
    }

    /**
     * Randomly selects the specified number of questions from the provided list.
     *
     * @param questions         the list of questions to select from
     * @param numberOfQuestions the number of questions to select
     * @return a list of randomly selected questions
     * @throws IllegalArgumentException if there are not enough questions available
     */
    private List<Question> getQuestions(List<Question> questions, int numberOfQuestions) {
        if (numberOfQuestions > questions.size()) {
            throw new IllegalArgumentException("Not enough questions in the pool for the chosen amount");
        }

        Collections.shuffle(questions);

        return questions.subList(0, numberOfQuestions);
    }

    /**
     * Retrieves a list of questions that the student has not yet seen or has answered incorrectly.
     *
     * @param studentStats the student's statistics to determine which questions are unseen or incorrect
     * @return a list of unseen or incorrect questions
     */
    private List<Question> getUnseenOrIncorrectQuestions(StudentStatistics studentStats) {
        List<Question> unseenOrIncorrect = new ArrayList<>();
        Map<Question, Boolean> questionHistory = studentStats.getQuestionHistory();

        for (Question question : questionPool) {
            if (!questionHistory.containsKey(question) || questionHistory.get(question) == false) {
                unseenOrIncorrect.add(question);
            }
        }

        return unseenOrIncorrect;
    }
}
