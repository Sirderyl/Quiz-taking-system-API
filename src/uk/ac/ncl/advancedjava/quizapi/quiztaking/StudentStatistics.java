package uk.ac.ncl.advancedjava.quizapi.quiztaking;

import uk.ac.ncl.advancedjava.quizapi.questions.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StudentStatistics - maintains the statistics for a student - attempted regular and revision quizzes,
 * the correctness of answers for individual questions and the state of final verdict.
 * It handles scoring, tracking quiz attempts, and determining final verdict.
 * This class follows a singleton-like approach to ensure that each student has only one associated
 * StudentStatistics instance.
 *
 * @author Filip Kovarik - S24039999
 */
public final class StudentStatistics {

    private final Student student;
    private final Map<Quiz, Float> attemptedRegularQuizzes;
    private final Map<Quiz, Float> attemptedRevisionQuizzes;
    private final Map<Question, Boolean> questionHistory;
    private Verdict finalVerdict;

    private static final Map<Student, StudentStatistics> ALLSTUDENTSTATISTICS = new HashMap<>();

    /**
     * Private constructor for creating a StudentStatistics instance. This is called when a new statistics object
     * for the student needs to be created.
     *
     * @param student the student for whom the statistics are tracked
     */
    private StudentStatistics(Student student) {
        this.student = student;
        this.attemptedRegularQuizzes = new HashMap<>();
        this.attemptedRevisionQuizzes = new HashMap<>();
        this.questionHistory = new HashMap<>();
        this.finalVerdict = Verdict.TBD;
    }

    /**
     * Returns the StudentStatistics instance for the given student, creating it if it does not already exist.
     *
     * @param student the student for whom to retrieve statistics
     * @return the {@code StudentStatistics} instance for the given student
     */
    public static StudentStatistics getInstance(Student student) {
        return ALLSTUDENTSTATISTICS.computeIfAbsent(student, StudentStatistics::new);
    }

    /**
     * Processes the student's answers for a regular quiz, calculates the score, and updates the statistics.
     * Throws an exception if the quiz has already been attempted or if a final verdict has been assigned.
     *
     * @param quiz              the quiz to be taken
     * @param studentAnswers    a map of the student's answers, keyed by the questions
     * @return the score for the regular quiz
     * @throws IllegalStateException    if the student has already received a final verdict
     * @throws IllegalArgumentException if the same quiz has already been attempted
     */
    public float takeQuiz(Quiz quiz, Map<Question, String> studentAnswers) {
        if (finalVerdict != Verdict.TBD) {
            throw new IllegalStateException("Final verdict has already been received");
        } else if (attemptedRegularQuizzes.containsKey(quiz)) {
            throw new IllegalArgumentException("You have already taken this quiz. Generate a new one");
        }
        float score = calculateScore(quiz, studentAnswers);
        addRegularAttempt(quiz, score);

        return score;
    }

    /**
     * Processes the student's answers for a revision quiz, calculates the score, and updates the statistics.
     * Throws an exception if more than two revision quizzes have been taken or if a final verdict has been assigned.
     *
     * @param quiz              the revision quiz to be taken
     * @param studentAnswers    a map of the student's answers, keyed by the questions
     * @return the score for the revision quiz
     * @throws IllegalStateException if the student has already received a final verdict or exceeded revision attempts
     */
    public float takeRevisionQuiz(Quiz quiz, Map<Question, String> studentAnswers) {
        if (finalVerdict != Verdict.TBD) {
            throw new IllegalStateException("Final verdict has already been received");
        } else if (attemptedRevisionQuizzes.size() == 2) {
            throw new IllegalStateException("You cannot take more than two revision quizzes");
        }
        float score = calculateScore(quiz, studentAnswers);
        addRevisionAttempt(quiz, score);

        return score;
    }

    /**
     * Updates the final verdict for the student based on the latest score.
     *
     * @param score the score for the latest quiz
     */
    private void updateFinalVerdict(float score) {
        float PASS_MARK = 0.5f;
        if (score >= PASS_MARK) {
            finalVerdict = Verdict.PASS;
        } else if (attemptedRegularQuizzes.size() == 2 && score < PASS_MARK) {
            finalVerdict = Verdict.FAIL;
        }
    }

    /**
     * Adds a regular quiz attempt to the student's statistics and updates the final verdict if necessary.
     *
     * @param quiz  the quiz taken by the student
     * @param score the score the student has achieved for the quiz
     */
    private void addRegularAttempt(Quiz quiz, float score) {
        attemptedRegularQuizzes.put(quiz, score);
        updateFinalVerdict(score);
    }

    /**
     * Adds a revision quiz attempt to the student's statistics.
     *
     * @param quiz  the revision quiz taken by the student
     * @param score the score the student has achieved for the revision quiz
     */
    private void addRevisionAttempt(Quiz quiz, float score) {
        attemptedRevisionQuizzes.put(quiz, score);
    }

    /**
     * Adds the question and whether it has been answered correctly to the student's question history.
     *
     * @param question  the question being answered
     * @param isCorrect {@code true} if the answer was correct, {@code false} otherwise
     */
    private void addQuestionHistory(Question question, boolean isCorrect) {
        questionHistory.put(question, isCorrect);
    }

    /**
     * Generates a summary of the student's quiz statistics, including the quizzes taken and the final verdict.
     *
     * @return a string representation of the student's statistics
     */
    public String generateStatistics() {
        StringBuilder statistics = new StringBuilder();
        statistics.append("Student Statistics:\n");
        statistics.append(student.toString()).append("\n");

        statistics.append("Attempted Regular Quizzes: ").append(attemptedRegularQuizzes.size()).append("\n");
        printQuizScores(statistics, attemptedRegularQuizzes);

        statistics.append("Attempted Revision Quizzes: ").append(attemptedRevisionQuizzes.size()).append("\n");
        printQuizScores(statistics, attemptedRevisionQuizzes);

        statistics.append("Final Verdict: ").append(finalVerdict.toString()).append("\n");

        return statistics.toString();
    }

    /**
     * Appends quiz scores to the statistics string for both regular and revision quizzes.
     *
     * @param statistics        the StringBuilder to append quiz scores
     * @param attemptedQuizzes  the map of quizzes and their scores
     */
    private void printQuizScores(StringBuilder statistics, Map<Quiz, Float> attemptedQuizzes) {
        if (!attemptedQuizzes.isEmpty()) {
            int quizCounter = 1;
            for (Map.Entry<Quiz, Float> entry : attemptedQuizzes.entrySet()) {
                float score = entry.getValue();
                statistics.append("Quiz ").append(quizCounter).append(": Score ").append(score).append("\n");
                quizCounter++;
            }
        }
    }

    /**
     * Calculates the score for a quiz based on the student's answers.
     * Only the relevant questions from the student's answers are used in the calculation.
     * If the student answers contains question that are not in the quiz, those are discarded.
     *
     * @param quiz              the quiz taken by the student
     * @param studentAnswers    a map of the student's answers, keyed by the questions
     * @return the calculated score as a float
     */
    private float calculateScore(Quiz quiz, Map<Question, String> studentAnswers) {
        int correctAnswers = 0;
        List<Question> questions = quiz.getQuestions();
        for (Question question : questions) {
            String answer = studentAnswers.get(question);
            boolean isCorrect = question.isCorrectAnswer(answer);

            addQuestionHistory(question, isCorrect);

            if (isCorrect) {
                correctAnswers++;
            }
        }

        return (float) correctAnswers / questions.size();
    }

    /**
     * Returns the student associated with this {@code StudentStatistics}.
     *
     * @return the student associated with these statistics
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Returns a map of regular quizzes attempted by the student and their corresponding scores.
     *
     * @return a map of regular quizzes and their scores
     */
    public Map<Quiz, Float> getAttemptedRegularQuizzes() {
        return attemptedRegularQuizzes;
    }

    /**
     * Returns a map of revision quizzes attempted by the student and their corresponding scores.
     *
     * @return a map of revision quizzes and their scores
     */
    public Map<Quiz, Float> getAttemptedRevisionQuizzes() {
        return attemptedRevisionQuizzes;
    }

    /**
     * Returns a map of questions attempted by the student and whether they were answered correctly.
     *
     * @return a map of questions and the correctness of the answers
     */
    public Map<Question, Boolean> getQuestionHistory() {
        return questionHistory;
    }

    /**
     * Returns the final verdict for the student (PASS, FAIL, or TBD).
     *
     * @return the final verdict for the student
     */
    public Verdict getFinalVerdict() {
        return finalVerdict;
    }
}
