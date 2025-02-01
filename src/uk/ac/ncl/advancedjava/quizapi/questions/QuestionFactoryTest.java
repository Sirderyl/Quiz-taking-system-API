package uk.ac.ncl.advancedjava.quizapi.questions;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QuestionFactoryTest {

    Question frq, frqDuplicate, frq2, frq3, mcq, mcqDuplicate, mcq2, mcq3;
    Map<Character, String> options, options2;
    Set<Character> correctOptions, correctOptions2;

    @BeforeEach
    void setUp() {
        frq = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is 2+2?",
                "4"
        );

        frq2 = new FreeResponseQuestion("What is 2+2?", "4");

        frqDuplicate = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is 2+2?",
                "4"
        );

        frq3 = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is 3+3?",
                "6"
        );

        options = Map.of(
                'A', "4 (in decimal)",
                'B', "2 (in decimal)",
                'C', "0 (in binary)",
                'D', "1 (in binary)"
        );
        correctOptions = Set.of('B', 'C');

        mcq = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "What is 1+1?",
                options,
                correctOptions
        );
        mcq2 = new MultipleChoiceQuestion("What is 1+1?", options, correctOptions);
        mcqDuplicate = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "What is 1+1?",
                options,
                correctOptions
        );
        options2 = Map.of(
                'A', "correct",
                'B', "correct",
                'C', "incorrect",
                'D', "incorrect"
        );
        correctOptions2 = Set.of('A', 'B');

        mcq3 = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "Select correct options",
                options2,
                correctOptions2
        );
    }

    @AfterEach
    void tearDown() {
        frq = null;
        frqDuplicate = null;
        frq2 = null;
        frq3 = null;
        mcq = null;
        mcqDuplicate = null;
        mcq2 = null;
        mcq3 = null;
        options = null;
        options2 = null;
        correctOptions = null;
        correctOptions2 = null;
        QuestionFactory.resetFactory();
    }

    @Test
    void getInstance() {
        // *** TESTING FREE RESPONSE QUESTION ***
        assertEquals(frq, frq2);
        assertSame(frq, frqDuplicate);
        assertNotEquals(frq, frq3);
        assertThrowsExactly(IllegalArgumentException.class, () -> QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "What is 2+2?",
                "3"
        ));
        // **************

        // *** TESTING MULTIPLE CHOICE QUESTION ***
        assertEquals(mcq, mcq2);
        assertSame(mcq, mcqDuplicate);
        assertNotEquals(mcq, mcq3);
        assertThrowsExactly(IllegalArgumentException.class, () -> QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is 1+1?",
                options,
                correctOptions
        ));
        // *************
    }
}