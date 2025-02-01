package uk.ac.ncl.advancedjava.quizapi.quiztaking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.ncl.advancedjava.quizapi.questions.Question;
import uk.ac.ncl.advancedjava.quizapi.questions.QuestionFactory;
import uk.ac.ncl.advancedjava.quizapi.questions.QuestionType;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QuizGeneratorTest {

    private QuizGenerator quizGenerator;
    private Set<Question> questionPool;
    private Question frq1, frq2, frq3, frq4, mcq1, mcq2, mcq3, mcq4;
    Student bob;
    StudentStatistics bobStats;

    @BeforeEach
    void setUp() {
        frq1 = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is the capital of France?",
                "Paris"
        );
        frq2 = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is the capital of Czechia?",
                "Prague"
        );
        frq3 = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is 2+2?",
                "4"
        );
        frq4 = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is 4+4?",
                "8"
        );

        Map<Character, String> options1 = Map.of(
                'A', "4 (in decimal)",
                'B', "2 (in decimal)",
                'C', "0 (in binary)",
                'D', "1 (in binary)"
        );
        Set<Character> correctOptions1 = Set.of('B', 'D');

        Map<Character, String> options2 = Map.of(
                'A', "wrong",
                'B', "correct",
                'C', "correct"
        );
        Set<Character> correctOptions2 = Set.of('B', 'C');

        Map<Character, String> options3 = Map.of(
                'A', "United Kingdom",
                'B', "France",
                'C', "Germany",
                'D', "United States"
        );
        Set<Character> correctOptions3 = Set.of('B', 'C');

        Map<Character, String> options4 = Map.of(
                'A', "correct",
                'B', "wrong",
                'C', "wrong",
                'D', "wrong"
        );
        Set<Character> correctOptions4 = Set.of('A');

        mcq1 = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "What is 1+1?",
                options1,
                correctOptions1
        );
        mcq2 = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "Select correct options(B,C)",
                options2,
                correctOptions2
        );
        mcq3 = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "Which are member states of the EU?",
                options3,
                correctOptions3
        );
        mcq4 = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "Select correct options (A)",
                options4,
                correctOptions4
        );

        questionPool = Set.of(frq1, frq2, frq3, frq4, mcq1, mcq2, mcq3, mcq4);
        quizGenerator = new QuizGenerator(questionPool);

        bob = Student.getInstance("Bob", "Smith", LocalDate.of(2001, 7, 24));
        bobStats = StudentStatistics.getInstance(bob);
    }

    @AfterEach
    void tearDown() throws Exception {
        quizGenerator = null;
        questionPool = null;
        frq1 = null;
        frq2 = null;
        frq3 = null;
        frq4 = null;
        mcq1 = null;
        mcq2 = null;
        mcq3 = null;
        mcq4 = null;
        bob = null;
        bobStats = null;

        Field field = StudentStatistics.class.getDeclaredField("ALLSTUDENTSTATISTICS");
        field.setAccessible(true);
        Map<?, ?> map = (Map<?, ?>) field.get(null);
        map.clear();
    }

    @Test
    void testGenerateQuiz_ValidNumberOfQuestions() {
        Quiz quiz = quizGenerator.generateQuiz(2);

        assertEquals(2, quiz.getQuestions().size());
        assertTrue(questionPool.containsAll(quiz.getQuestions()));

        // Test that the quiz contains at least 1 free response and 1 multiple choice question
        assertTrue(quiz.getQuestions().stream().anyMatch(q -> q.getType() == QuestionType.FREE_RESPONSE));
        assertTrue(quiz.getQuestions().stream().anyMatch(q -> q.getType() == QuestionType.MULTIPLE_CHOICE));

        quiz = quizGenerator.generateQuiz(8);

        assertEquals(8, quiz.getQuestions().size());
        assertTrue(questionPool.containsAll(quiz.getQuestions()));
    }

    @Test
    void testGenerateQuiz_InvalidNumberOfQuestions() {
        assertThrowsExactly(IllegalArgumentException.class, () -> quizGenerator.generateQuiz(9));
        assertThrowsExactly(IllegalArgumentException.class, () -> quizGenerator.generateQuiz(1));
    }

    @Test
    void testGenerateQuiz_WithoutQuestionsOfBothTypes() {
        questionPool = Set.of(frq1, frq2, frq3, frq4);
        quizGenerator = new QuizGenerator(questionPool);

        assertThrowsExactly(IllegalStateException.class, () -> quizGenerator.generateQuiz(3));
    }

    @Test
    void testRevise() {
        // Setting up question history for Bob (frq1 - seen and correct, frq2 - seen and correct,
        // mcq1 - seen but incorrect
        QuizGenerator quizGenerator2 = new QuizGenerator(Set.of(frq1, frq2, mcq1));
        Quiz quiz = quizGenerator2.generateQuiz(3);
        Map<Question, String> answers = Map.of(
                frq1, "Paris",
                frq2, "Prague",
                mcq1, "A,C"
        );
        bobStats.takeQuiz(quiz, answers);

        quiz = quizGenerator.revise(bobStats, 2);

        assertEquals(2, quiz.getQuestions().size());

        // Not containing questions student has answered correctly before
        assertFalse(quiz.getQuestions().contains(frq1));
        assertFalse(quiz.getQuestions().contains(frq2));
        assertTrue(quiz.getQuestions().stream().anyMatch(q -> q.getType() == QuestionType.FREE_RESPONSE));
        assertTrue(quiz.getQuestions().stream().anyMatch(q -> q.getType() == QuestionType.MULTIPLE_CHOICE));
    }

    @Test
    void testRevise_NotEnoughQuestions() {
        // Setting up question history for Bob (frq1 - seen and correct, frq2 - seen and correct,
        // mcq1 - seen but incorrect
        QuizGenerator quizGenerator2 = new QuizGenerator(Set.of(frq1, frq2, mcq1));
        Quiz quiz = quizGenerator2.generateQuiz(3);
        Map<Question, String> answers = Map.of(
                frq1, "Paris",
                frq2, "Prague",
                mcq1, "A,C"
        );
        bobStats.takeQuiz(quiz, answers);

        // There are now 6 questions left that the student has either not seen or answered incorrectly
        assertThrows(IllegalArgumentException.class, () -> quizGenerator.revise(bobStats, 7));
    }

    @Test
    void testRevise_WithQuestionsOfOneType() {
        // Setting up question history for Bob (frq1 - seen and correct, frq2 - seen and correct,
        // mcq1 - seen but incorrect
        QuizGenerator quizGenerator2 = new QuizGenerator(Set.of(frq1, frq2, mcq1));
        Quiz quiz = quizGenerator2.generateQuiz(3);
        Map<Question, String> answers = Map.of(
                frq1, "Paris",
                frq2, "Prague",
                mcq1, "A,C"
        );
        bobStats.takeQuiz(quiz, answers);

        quiz = quizGenerator.revise(bobStats, 1);
        assertEquals(1, quiz.getQuestions().size());
    }
}