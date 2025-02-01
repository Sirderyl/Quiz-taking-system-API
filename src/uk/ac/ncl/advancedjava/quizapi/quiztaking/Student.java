package uk.ac.ncl.advancedjava.quizapi.quiztaking;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Student - hold information about a student - first name, last name, and date of birth.
 * It follows a singleton-like approach by maintaining a pool of unique student instances.
 * Use Student.getInstance() to get an instance.
 * The objects are immutable once created.
 *
 * @author Filip Kovarik - S24039999
 */
public final class Student {

    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;

    private static final Map<String, Student> STUDENTS = new HashMap<>();

    /**
     * Private constructor to create a new Student instance.
     * This is only called when the student does not already exist in the pool.
     *
     * @param firstName     the student's first name
     * @param lastName      the student's last name
     * @param dateOfBirth   the student's date of birth
     */
    private Student(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Retrieves a Student instance from the pool or creates a new one if that student does not exist yet.
     * This method ensures that only one instance per unique student is created.
     *
     * @param firstName     the student's first name
     * @param lastName      the student's last name
     * @param dateOfBirth   the student's date of birth
     * @return a unique Student instance for the given details
     */
    public static Student getInstance(String firstName, String lastName, LocalDate dateOfBirth) {
        String key = generateKey(firstName, lastName, dateOfBirth);

        return STUDENTS.computeIfAbsent(key, k -> new Student(firstName, lastName, dateOfBirth));
    }

    /**
     * Returns the student's first name.
     *
     * @return the student's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the student's last name.
     *
     * @return the student's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the student's date of birth.
     *
     * @return the student's date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Generates a unique key for identifying a student based on their details.
     * This key is used to store and retrieve student instances from the pool.
     *
     * @param firstName     the student's first name
     * @param lastName      the student's last name
     * @param dateOfBirth   the student's date of birth
     * @return a unique string key for identifying the student
     */
    private static String generateKey(String firstName, String lastName, LocalDate dateOfBirth) {
        return firstName + ":" + lastName + ":" + dateOfBirth.toString();
    }

    /**
     * Compares this student to another object for equality. Two students are considered equal
     * if they have the same first name, last name, and date of birth.
     *
     * @param o the object to be compared with this student
     * @return {@code true} if the students are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;

        return getFirstName().equals(student.getFirstName()) && getLastName().equals(student.getLastName()) && getDateOfBirth().equals(student.getDateOfBirth());
    }

    /**
     * Returns a hash code value for the student. The hash code is based on the student's first name, last name,
     * and date of birth.
     *
     * @return a hash code value for this student
     */
    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getDateOfBirth().hashCode();
        return result;
    }

    /**
     * Returns a string representation of the student. The string includes the first name, last name,
     * and date of birth of the student.
     *
     * @return a string representation of the student
     */
    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth.toString() +
                '}';
    }
}
