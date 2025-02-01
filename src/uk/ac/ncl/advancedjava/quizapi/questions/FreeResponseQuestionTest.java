package uk.ac.ncl.advancedjava.quizapi.questions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class FreeResponseQuestionTest {

    FreeResponseQuestion frq1, frq2, frq3;

    @BeforeEach
    void setUp() {
        assertThrows(IllegalArgumentException.class, () ->
                new FreeResponseQuestion("What is 1+1", null));
        assertThrows(IllegalArgumentException.class, () ->
                new FreeResponseQuestion("What is 1+1", ""));
        assertThrows(IllegalArgumentException.class, () ->
                new FreeResponseQuestion("", "2"));
        assertThrows(IllegalArgumentException.class, () ->
                new FreeResponseQuestion(null, "2"));
        frq1 = new FreeResponseQuestion("Where is Time Square located?", "New York");
        frq2 = new FreeResponseQuestion("What is the capital of France?", "Paris");
        frq3 = new FreeResponseQuestion("Where is Time Square located?", "New York");
    }

    @AfterEach
    void tearDown() {
        frq1 = null;
        frq2 = null;
        frq3 = null;
    }

    @Test
    void getQuestionText() {
        assertEquals("Where is Time Square located?", frq1.getQuestionText());
        assertEquals("What is the capital of France?", frq2.getQuestionText());
        assertEquals("Where is Time Square located?", frq3.getQuestionText());
    }

    @Test
    void isCorrectAnswer() {
        String answer = "Paris";
        assertFalse(frq1.isCorrectAnswer(answer));
        assertTrue(frq2.isCorrectAnswer(answer));

        answer = "New York";
        assertTrue(frq1.isCorrectAnswer(answer));

        answer = "new york";
        assertTrue(frq1.isCorrectAnswer(answer));

        answer = "NEW YORK";
        assertTrue(frq1.isCorrectAnswer(answer));

        answer = "   neW     yoRK    ";
        assertTrue(frq1.isCorrectAnswer(answer));

        answer = "newyork";
        assertFalse(frq1.isCorrectAnswer(answer));
    }

    @Test
    void testEquals() {
        assertFalse(frq1.equals(frq2));
        assertTrue(frq1.equals(frq3));
    }

    @Test
    void testHashCode() {
        assertEquals(frq1.hashCode(), frq3.hashCode());
        assertNotEquals(frq1.hashCode(), frq2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(frq1.toString(), "FreeRangeQuestion{questionText='Where is Time Square located?'}");
    }
}