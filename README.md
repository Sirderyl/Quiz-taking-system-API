# Quiz taking system API

This API is designed to manage quizzes for student assessments. The system supports free response and
multiple choice questions and provides functionality for regular and revision quizzes. The quizzes are generated
from a pool of questions selected by the user. The system also tracks student performance statistics.

This project serves as a learning ground for utilising core Java OOP principles - overriding, static methods,
interface-based hierarchies, object factories, late binding, immutability, using appropriate data structures,
unit testing, Javadocs and more.

## How it works
A student starts with their final mark being undetermined - `TBD`.

**Revision quizzes:**\
A student has the option to take up to 2 revision quizzes with formative marking. A revision quiz only consists of
questions a student has either not seen before or answered incorrectly in previous quizzes. A student has the option to
take a revision quiz as long as their final mark is `TBD` and have not used up their 2 available quizzes.

**Regular quizzes:**\
A student has 2 attempts at a regular quiz (marked). If they pass on the first attempt, their final mark becomes `PASS`
and they can no longer take any regular or revision quizzes. If they fail, their final mark remains `TBD` until they
take a second regular quiz. If they fail the second regular quiz too, their final mark becomes `FAIL` and they cannot
take any more quizzes. If they pass their second regular quiz, their final mark becomes `PASS` and cannot
take any more quizzes too.

An example scenario is presented in the `GenerateAndTakeQuiz` class.