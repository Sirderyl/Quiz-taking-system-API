package uk.ac.ncl.advancedjava.quizapi.quiztaking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.ncl.advancedjava.quizapi.questions.Question;
import uk.ac.ncl.advancedjava.quizapi.questions.QuestionFactory;
import uk.ac.ncl.advancedjava.quizapi.questions.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuizTest {

    Question frq1, frq2, mcq1, mcq2;
    List<Question> questionList;
    Quiz quiz;

    @BeforeEach
    void setUp() {
        frq1 = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is 1+1?",
                "2"
        );
        frq2 = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is 2+2?",
                "4"
        );
        Map<Character, String> options = Map.of(
                'A', "incorrect",
                'B', "correct",
                'C', "correct"
        );
        Set<Character> correctOptions = Set.of('B', 'C');
        mcq1 = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "Select correct options (B,C)",
                options,
                correctOptions
        );

        Map<Character, String> options2 = Map.of(
                'A', "correct",
                'B', "incorrect",
                'C', "incorrect"
        );
        Set<Character> correctOptions2 = Set.of('A');
        mcq2 = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "Select correct options (A)",
                options2,
                correctOptions2
        );

        questionList = new ArrayList<>();
        questionList.addAll(List.of(frq1, frq2, mcq1, mcq2));
        quiz = new Quiz(questionList);
    }

    @AfterEach
    void tearDown() {
        frq1 = null;
        frq2 = null;
        mcq1 = null;
        mcq2 = null;
        questionList = null;
        quiz = null;
    }

    @Test
    void addQuestion() {
        assertEquals(questionList, quiz.getQuestions());

        Question frq3 = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is 3+3?",
                "6"
        );

        questionList.add(frq3);
        quiz.addQuestion(frq3);

        assertEquals(questionList, quiz.getQuestions());
    }

    @Test
    void removeQuestion() {
        questionList.remove(frq2);
        quiz.removeQuestion(frq2);
        assertEquals(questionList, quiz.getQuestions());
    }

    @Test
    void getQuestions() {
        assertEquals(questionList, quiz.getQuestions());
    }

    @Test
    void testToString() {
        assertEquals(quiz.toString(), "Quiz{questions=" + quiz.getQuestions() + "}");
    }
}