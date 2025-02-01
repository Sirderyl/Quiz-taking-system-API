package uk.ac.ncl.advancedjava.quizapi.questions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MultipleChoiceQuestionTest {

    Question mcq1, mcq2, mcq3;
    Map<Character, String> options1, options2;
    Set<Character> correctOptions1, correctOptions2;

    @BeforeEach
    void setUp() {
        options1 = Map.of(
                'A', "4 (in decimal)",
                'B', "2 (in decimal)",
                'C', "0 (in binary)",
                'D', "1 (in binary)"
        );
        correctOptions1 = Set.of('B', 'D');

        options2 = Map.of(
                'A', "wrong",
                'B', "correct",
                'C', "correct"
        );
        correctOptions2 = Set.of('B', 'C');

        mcq1 = new MultipleChoiceQuestion(
                "What is 1+1?",
                options1,
                correctOptions1
        );
        mcq2 = new MultipleChoiceQuestion(
                "Select correct answers",
                options2,
                correctOptions2
        );
        mcq3 = new MultipleChoiceQuestion(
                "What is 1+1?",
                options1,
                correctOptions1
        );

        // *** TESTING EXCEPTIONS ***
        Map<Character, String> options4 = Map.of(
                'A', "wrong",
                'B', "correct",
                'C', "correct",
                'D', "correct",
                'E', "wrong"
        );
        Set<Character> correctOptions4 = Set.of('B', 'C');
        assertThrowsExactly(IllegalArgumentException.class, () -> new MultipleChoiceQuestion(
                "Exception test", options4, correctOptions4));

        Map<Character, String> options5 = Map.of(
                'A', "wrong",
                'B', "correct",
                'C', "correct"
        );
        Set<Character> correctOptions5 = Set.of('B', 'C');
        assertThrowsExactly(IllegalArgumentException.class, () -> new MultipleChoiceQuestion(
                "Test", null, correctOptions5));
        assertThrows(IllegalArgumentException.class, () -> new MultipleChoiceQuestion(
                "Test", new HashMap<>(), correctOptions5));
        assertThrowsExactly(IllegalArgumentException.class, () -> new MultipleChoiceQuestion(
                "Test", options5, null));
        assertThrowsExactly(IllegalArgumentException.class, () -> new MultipleChoiceQuestion(
                "Test", options5, new HashSet<>()));
        assertThrowsExactly(IllegalArgumentException.class, () -> new MultipleChoiceQuestion(
                "", options5, correctOptions5));

        Set<Character> correctOptions6 = Set.of('B', 'C', 'D', 'E');
        assertThrowsExactly(IllegalArgumentException.class, () -> new MultipleChoiceQuestion(
                "Test", options5, correctOptions6));
        // ***********
    }

    @AfterEach
    void tearDown() {
        mcq1 = null;
        mcq2 = null;
        mcq3 = null;
        options1 = null;
        options2 = null;
        correctOptions1 = null;
        correctOptions2 = null;
    }

    @Test
    void getQuestionText() {
        assertEquals("What is 1+1?", mcq1.getQuestionText());
        assertEquals("Select correct answers", mcq2.getQuestionText());
        assertEquals("What is 1+1?", mcq3.getQuestionText());
    }

    @Test
    void isCorrectAnswer() {
        String studentAnswers = "B,D";
        assertTrue(mcq1.isCorrectAnswer(studentAnswers));

        studentAnswers = "B, D";
        assertTrue(mcq1.isCorrectAnswer(studentAnswers));

        studentAnswers = "D, B";
        assertTrue(mcq1.isCorrectAnswer(studentAnswers));

        studentAnswers = " d,   b  ";
        assertTrue(mcq1.isCorrectAnswer(studentAnswers));

        studentAnswers = "A";
        assertFalse(mcq1.isCorrectAnswer(studentAnswers));

        studentAnswers = "B,C";
        assertTrue(mcq2.isCorrectAnswer(studentAnswers));

        studentAnswers = "B, C";
        assertTrue(mcq2.isCorrectAnswer(studentAnswers));

        studentAnswers = "C, B";
        assertTrue(mcq2.isCorrectAnswer(studentAnswers));

        studentAnswers = " c,   b  ";
        assertTrue(mcq2.isCorrectAnswer(studentAnswers));

        studentAnswers = "A";
        assertFalse(mcq2.isCorrectAnswer(studentAnswers));
    }

    @Test
    void getOptions() {
        Map<Character, String> optionsTest = Map.of(
                'A', "4 (in decimal)",
                'B', "2 (in decimal)",
                'C', "0 (in binary)",
                'D', "1 (in binary)"
        );
        if (mcq1 instanceof OptionableQuestion) {
            assertEquals(optionsTest, ((OptionableQuestion) mcq1).getOptions());
        } else {
            fail("Incorrect question type");
        }

        optionsTest = Map.of(
                'A', "4",
                'B', "2",
                'C', "0",
                'D', "1"
        );

        if (mcq1 instanceof OptionableQuestion) {
            assertNotEquals(optionsTest, ((OptionableQuestion) mcq1).getOptions());
        } else {
            fail("Incorrect question type");
        }
    }

    @Test
    void testEquals() {
        assertFalse(mcq1.equals(mcq2));
        assertTrue(mcq1.equals(mcq3));
    }

    @Test
    void testHashCode() {
        assertEquals(mcq1.hashCode(), mcq3.hashCode());
        assertNotEquals(mcq1.hashCode(), mcq2.hashCode());
    }

    @Test
    void testToString() {
        if (mcq1 instanceof OptionableQuestion) {
            assertEquals(mcq1.toString(), "MultipleChoiceQuestion{questionText='What is 1+1?', options=" + ((OptionableQuestion) mcq1).getOptions() + "}");
        } else {
            fail("Incorrect question type");
        }
    }
}