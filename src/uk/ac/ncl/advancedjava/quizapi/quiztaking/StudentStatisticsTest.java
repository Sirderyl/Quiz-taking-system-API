package uk.ac.ncl.advancedjava.quizapi.quiztaking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.ncl.advancedjava.quizapi.questions.Question;
import uk.ac.ncl.advancedjava.quizapi.questions.QuestionFactory;
import uk.ac.ncl.advancedjava.quizapi.questions.QuestionType;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StudentStatisticsTest {

    Student bob, jim;
    StudentStatistics bobStats, jimStats;
    Question frq1, frq2, frq3, mcq1, mcq2, mcq3;
    Quiz regularQuiz, regularQuiz2, revisionQuiz;

    @BeforeEach
    void setUp() {
        // *** SET UP STUDENT ***
        bob = Student.getInstance("Bob", "Smith", LocalDate.of(2001, 7, 24));
        bobStats = StudentStatistics.getInstance(bob);
        jim = Student.getInstance("Jim", "Jean", LocalDate.of(2000, 3, 5));
        jimStats = StudentStatistics.getInstance(jim);
        // **********************

        // *** SET UP QUESTIONS ***
        frq1 = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is the capital of France?",
                "Paris"
        );
        frq2 = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is 1+1?",
                "2"
        );
        frq3 = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "Example question with a three-word answer",
                "Three word answer"
        );

        Map<Character, String> mcq1Options = Map.of(
                'A', "4 (in decimal)",
                'B', "2 (in decimal)",
                'C', "0 (in binary)",
                'D', "1 (in binary)"
        );
        Set<Character> mcq1CorrectOptions = Set.of('B', 'D');
        mcq1 = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "What is 1+1?",
                mcq1Options,
                mcq1CorrectOptions
        );

        Map<Character, String> mcq2Options = Map.of(
                'A', "correct",
                'B', "correct",
                'C', "incorrect",
                'D', "correct"
        );
        Set<Character> mcq2CorrectOptions = Set.of('A', 'B', 'D');
        mcq2 = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "Select correct options (A,B,D)",
                mcq2Options,
                mcq2CorrectOptions
        );

        Map<Character, String> mcq3Options = Map.of(
                'A', "correct",
                'B', "correct",
                'C', "correct",
                'D', "correct"
        );
        Set<Character> mcq3CorrectOptions = Set.of('A', 'B', 'C', 'D');
        mcq3 = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "Select correct options (all)",
                mcq3Options,
                mcq3CorrectOptions
        );
        // **********************

        // *** SET UP QUIZ ***
        List<Question> questionList = List.of(frq1, frq2, mcq1, mcq2);
        List<Question> questionList2 = List.of(frq3, mcq1, mcq3);
        regularQuiz = new Quiz(questionList);
        regularQuiz2 = new Quiz(questionList2);
        revisionQuiz = new Quiz(questionList);
        // **********************
    }

    @AfterEach
    void tearDown() throws Exception {
        bob = null;
        bobStats = null;
        jim = null;
        jimStats = null;
        frq1 = null;
        frq2 = null;
        frq3 = null;
        mcq1 = null;
        mcq2 = null;
        mcq3 = null;
        regularQuiz = null;
        regularQuiz2 = null;
        revisionQuiz = null;

        Field field = StudentStatistics.class.getDeclaredField("ALLSTUDENTSTATISTICS");
        field.setAccessible(true);
        Map<?, ?> map = (Map<?, ?>) field.get(null);
        map.clear();
    }

    @Test
    void getInstance() {
        StudentStatistics bobStats2 = StudentStatistics.getInstance(bob);
        assertSame(bobStats, bobStats2);

        Student bob2 = Student.getInstance("Bob", "Smith", LocalDate.of(2001, 7, 24));
        bobStats2 = StudentStatistics.getInstance(bob2);
        assertSame(bobStats, bobStats2);

        Student jim = Student.getInstance("Jim", "Jean", LocalDate.of(2000, 3, 5));
        StudentStatistics jimStats = StudentStatistics.getInstance(jim);
        assertNotSame(bobStats, jimStats);
    }

    @Test
    void takeQuiz() {
        // *** FIRST REGULAR QUIZ (test fail) ***
        Map<Question, String> answers = Map.of(
                mcq2, "d,b",
                frq1, " pariS ",
                frq2, " 3",
                mcq1, "d, a "
        );

        float score = bobStats.takeQuiz(regularQuiz, answers);
        assertThrows(IllegalArgumentException.class, () -> bobStats.takeQuiz(regularQuiz, answers));

        assertEquals(0.25f, score); // 1 correct answer out of 4
        assertEquals(1, bobStats.getAttemptedRegularQuizzes().size()); // first quiz attempted
        assertTrue(bobStats.getAttemptedRegularQuizzes().containsKey(regularQuiz));
        assertEquals(bobStats.getAttemptedRegularQuizzes().get(regularQuiz), 0.25f);

        assertEquals(4, bobStats.getQuestionHistory().size()); // 4 questions answered in total
        assertTrue(bobStats.getQuestionHistory().containsKey(frq1));
        assertTrue(bobStats.getQuestionHistory().containsKey(frq2));
        assertTrue(bobStats.getQuestionHistory().containsKey(mcq1));
        assertTrue(bobStats.getQuestionHistory().containsKey(mcq2));
        assertEquals(bobStats.getQuestionHistory().get(frq1), true);
        assertEquals(bobStats.getQuestionHistory().get(frq2), false);
        assertEquals(bobStats.getQuestionHistory().get(mcq1), false);
        assertEquals(bobStats.getQuestionHistory().get(mcq2), false);

        assertEquals(bobStats.getFinalVerdict(), Verdict.TBD);
        // *******

        // *** SECOND REGULAR QUIZ (test fail) ***
        // Not answering all questions here and also answering different question than in quiz
        // Should only assess frq3 here
        Map<Question, String> answers2 = Map.of(
                frq3, "Three word answer",
                mcq2, "d,b"
        );

        float score2 = bobStats.takeQuiz(regularQuiz2, answers2);

        assertEquals(1/3f, score2);
        assertEquals(2, bobStats.getAttemptedRegularQuizzes().size());
        assertEquals(6, bobStats.getQuestionHistory().size());
        assertEquals(bobStats.getFinalVerdict(), Verdict.FAIL);
        assertThrows(IllegalStateException.class, () -> bobStats.takeQuiz(regularQuiz2, answers2));
        // ********

        // *** FIRST REGULAR QUIZ WITH NEW STUDENT (test pass) ***
        // More answers than questions, should be fine as the irrelevant answer is not read
        Map<Question, String> answers3 = Map.of(
                mcq2, "d,a, B",
                frq1, " pariS ",
                frq2, " 2",
                frq3, "Three word answer",
                mcq1, "d, a "
        );
        score = jimStats.takeQuiz(regularQuiz, answers3);

        assertEquals(3/4f, score);
        assertEquals(jimStats.getFinalVerdict(), Verdict.PASS);
        assertThrows(IllegalStateException.class, () -> jimStats.takeQuiz(regularQuiz2, answers2));
        // *******
    }

    @Test
    void takeRevisionQuiz() {
        Map<Question, String> answers = Map.of(
                mcq2, "d,b",
                frq1, " pariS ",
                frq2, " 3",
                mcq1, "d, a "
        );

        // *** FIRST REVISION QUIZ (with verdict TBD) ***
        float score = bobStats.takeRevisionQuiz(revisionQuiz, answers);

        assertEquals(0.25f, score);
        assertEquals(1, bobStats.getAttemptedRevisionQuizzes().size());
        assertTrue(bobStats.getAttemptedRevisionQuizzes().containsKey(revisionQuiz));
        assertEquals(bobStats.getAttemptedRevisionQuizzes().get(revisionQuiz), 0.25f);
        assertEquals(4, bobStats.getQuestionHistory().size());
        // ******

        // *** SECOND REVISION QUIZ (with verdict TBD) ***
        score = bobStats.takeRevisionQuiz(regularQuiz, answers);

        assertEquals(2, bobStats.getAttemptedRevisionQuizzes().size());
        assertEquals(4, bobStats.getQuestionHistory().size());
        // *******

        // *** THIRD REVISION QUIZ (with verdict TBD) ***
        assertThrows(IllegalStateException.class, () -> bobStats.takeRevisionQuiz(regularQuiz2, answers));

        // *** FIRST REVISION QUIZ WITH NEW STUDENT (with verdict fail) ***
        jimStats.takeQuiz(regularQuiz, answers);
        jimStats.takeQuiz(regularQuiz2, answers);
        assertThrows(IllegalStateException.class, () -> jimStats.takeRevisionQuiz(revisionQuiz, answers));
    }

    @Test
    void generateStatistics() {
        // *** FIRST REVISION QUIZ ***
        Map<Question, String> revisionAnswers = Map.of(
                frq1, "paris",
                frq2, "3",  // Wrong answer for second question
                mcq1, "b,d",
                mcq2, "a,d,b"
        );

        float score = bobStats.takeRevisionQuiz(revisionQuiz, revisionAnswers);
        assertEquals(0.75f, score);  // 3 out of 4 answers are correct
        // *********

        // *** SECOND REGULAR QUIZ ***
        Map<Question, String> answers = Map.of(
                frq1, "pariS",
                frq2, "2",
                mcq1, "b,d",
                mcq2, "a,b,d"
        );

        score = bobStats.takeQuiz(regularQuiz, answers);
        assertEquals(1.0f, score);  // All answers are correct
        // *********

        String expectedStatistics = """
                Student Statistics:
                Student{firstName='Bob', lastName='Smith', dateOfBirth=2001-07-24}
                Attempted Regular Quizzes: 1
                Quiz 1: Score 1.0
                Attempted Revision Quizzes: 1
                Quiz 1: Score 0.75
                Final Verdict: PASS
                """;

        String generatedStatistics = bobStats.generateStatistics();
        assertEquals(expectedStatistics, generatedStatistics);
    }

    @Test
    void getStudent() {
        assertEquals(bob, bobStats.getStudent());
    }

    @Test
    void getAttemptedRegularQuizzes() {
        Map<Question, String> answers = Map.of(
                frq1, "pariS",
                frq2, "2",
                mcq1, "b,a,d", // wrong answer
                mcq2, "a,b,d"
        );
        bobStats.takeQuiz(regularQuiz, answers);
        Map<Quiz, Float> attemptedRegularQuizzes = Map.of(regularQuiz, 0.75f);

        assertEquals(attemptedRegularQuizzes, bobStats.getAttemptedRegularQuizzes());
    }

    @Test
    void getAttemptedRevisionQuizzes() {
        Map<Question, String> revisionAnswers = Map.of(
                frq1, "paris",
                frq2, "3",  // Wrong answer
                mcq1, "b,d",
                mcq2, "a,d,b"
        );
        bobStats.takeRevisionQuiz(revisionQuiz, revisionAnswers);
        Map<Quiz, Float> attemptedRevisionQuizzes = Map.of(revisionQuiz, 0.75f);

        assertEquals(attemptedRevisionQuizzes, bobStats.getAttemptedRevisionQuizzes());
    }

    @Test
    void getQuestionHistory() {
        Map<Question, String> answers = Map.of(
                frq1, "pariS",
                frq2, "2",
                mcq1, "b,a,d", // wrong answer
                mcq2, "a,b,d"
        );
        bobStats.takeQuiz(regularQuiz, answers);
        Map<Question, Boolean> questionHistory = Map.of(
                frq2, true,
                frq1, true,
                mcq1, false,
                mcq2, true
        );

        assertEquals(questionHistory, bobStats.getQuestionHistory());
    }

    @Test
    void getFinalVerdict() {
        assertEquals(Verdict.TBD, bobStats.getFinalVerdict());

        Map<Question, String> answers = Map.of(
                frq1, "pariS",
                frq2, "2",
                mcq1, "b,a,d", // wrong answer
                mcq2, "a,b,d"
        );
        bobStats.takeQuiz(regularQuiz, answers);
        assertEquals(Verdict.PASS, bobStats.getFinalVerdict());

        answers = Map.of(
                frq1, "pariSs", // wrong answer
                frq2, "3", // wrong answer
                mcq1, "b,a,d", // wrong answer
                mcq2, "a,b,d"
        );

        jimStats.takeQuiz(regularQuiz2, answers);
        jimStats.takeQuiz(revisionQuiz, answers);
        assertEquals(Verdict.FAIL, jimStats.getFinalVerdict());
    }
}