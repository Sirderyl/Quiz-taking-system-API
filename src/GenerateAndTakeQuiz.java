import uk.ac.ncl.advancedjava.quizapi.quiztaking.Student;
import uk.ac.ncl.advancedjava.quizapi.quiztaking.StudentStatistics;
import uk.ac.ncl.advancedjava.quizapi.questions.Question;
import uk.ac.ncl.advancedjava.quizapi.questions.QuestionFactory;
import uk.ac.ncl.advancedjava.quizapi.questions.QuestionType;
import uk.ac.ncl.advancedjava.quizapi.quiztaking.Quiz;
import uk.ac.ncl.advancedjava.quizapi.quiztaking.QuizGenerator;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class GenerateAndTakeQuiz {
    public static void main(String[] args) {

        // ****** Defining questions ******
        Map<Character, String> EUCountriesOptions = Map.of(
                'A', "United Kingdom",
                'B', "France",
                'C', "Germany",
                'D', "United States"
        );
        Set<Character> EUCountriesCorrectOptions = Set.of('B', 'C');

        Question EUCountriesQuestion = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "Which are member countries of the EU?",
                EUCountriesOptions,
                EUCountriesCorrectOptions
        );


        Map<Character, String> additionOptions = Map.of(
                'A', "4 (in decimal)",
                'B', "2 (in decimal)",
                'C', "0 (in binary)",
                'D', "1 (in binary)"
        );
        Set<Character> additionCorrectOptions = Set.of('B', 'D');

        Question additionQuestion = QuestionFactory.getInstance(
                QuestionType.MULTIPLE_CHOICE,
                "What is 1+1?",
                additionOptions,
                additionCorrectOptions
        );



        Question franceQuestion = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is the capital of France?",
                "Paris"
        );

        Question czechiaQuestion = QuestionFactory.getInstance(
                QuestionType.FREE_RESPONSE,
                "What is the capital of Czechia?",
                "Prague"
        );

        // ************************

        // ******* Creating a question pool *******

        Set<Question> questionPool = Set.of(EUCountriesQuestion, additionQuestion, franceQuestion, czechiaQuestion);
        QuizGenerator quizGenerator = new QuizGenerator(questionPool);

        // *************************

        // ****** Generating a regular quiz (marked) ******

        Quiz regularQuiz = quizGenerator.generateQuiz(4);

        // *************************


        // ********* Creating a student who will take the quizzes *********

        Student bob = Student.getInstance("Bob", "Smith", LocalDate.of(
                2000, 4, 20));
        StudentStatistics bobStatistics = StudentStatistics.getInstance(bob);

        // *************************

        // ********* Student taking the first regular quiz and failing (overall mark is TBD) ********

        Map<Question, String> studentAnswers = Map.of(
                franceQuestion, "Berlin",
                EUCountriesQuestion, "a,b",
                czechiaQuestion, "Prague",
                additionQuestion, "d,a"
        );

        bobStatistics.takeQuiz(regularQuiz, studentAnswers);

        System.out.println(regularQuiz.getQuestions());
        System.out.println(bob);

        // ********************

        // ****** Generating a revision quiz containing only the incorrectly answered questions ******

        Quiz revisionQuiz = quizGenerator.revise(bobStatistics, 3);

        // *************************

        // ********** Student taking the revision quiz *********

        studentAnswers = Map.of(
                franceQuestion, "Paris",
                EUCountriesQuestion, "c,b",
                additionQuestion, "b,d"
        );

        bobStatistics.takeRevisionQuiz(revisionQuiz, studentAnswers);

        // *********************

        // ********** Student taking the regular quiz again and passing (overall mark is PASS) ***********

        studentAnswers = Map.of(
                franceQuestion, "Paris",
                EUCountriesQuestion, "c,b",
                czechiaQuestion, "Prague",
                additionQuestion, "b,d"
        );

        Quiz regularQuiz2 = quizGenerator.generateQuiz(4);
        bobStatistics.takeQuiz(regularQuiz2, studentAnswers);

        // ********************

        System.out.println(bobStatistics.generateStatistics());
    }
}
