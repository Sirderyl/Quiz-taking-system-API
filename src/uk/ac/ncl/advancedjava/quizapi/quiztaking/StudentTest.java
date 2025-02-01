package uk.ac.ncl.advancedjava.quizapi.quiztaking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    Student bob;

    @BeforeEach
    void setUp() {
        bob = Student.getInstance("Bob", "Smith", LocalDate.of(2001, 7, 24));
    }

    @AfterEach
    void tearDown() {
        bob = null;
    }

    @Test
    void getInstance() {
        Student bob2 = Student.getInstance("Bob", "Smith", LocalDate.of(2001, 7, 24));
        assertSame(bob, bob2);

        Student jim = Student.getInstance("Jim", "Jean", LocalDate.of(2000, 3, 5));
        assertNotSame(bob, jim);
    }

    @Test
    void getFirstName() {
        assertEquals("Bob", bob.getFirstName());
        assertNotEquals("Jim", bob.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("Smith", bob.getLastName());
        assertNotEquals("Jean", bob.getLastName());
    }

    @Test
    void getDateOfBirth() {
        assertEquals(LocalDate.of(2001, 7, 24), bob.getDateOfBirth());
        assertNotEquals(LocalDate.of(2001, 7, 25), bob.getDateOfBirth());
        assertNotEquals(LocalDate.of(2002, 7, 24), bob.getDateOfBirth());
        assertNotEquals(LocalDate.of(2001, 8, 24), bob.getDateOfBirth());
    }

    @Test
    void testEquals() {
        Student jane = Student.getInstance("Jane", "Holmes", LocalDate.of(1999, 1, 1));
        Student jane2 = Student.getInstance("Jane", "Holmes", LocalDate.of(1999, 1, 1));
        assertTrue(jane.equals(jane2));
        assertTrue(jane2.equals(jane));

        Student jessica = Student.getInstance("Jessica", "Jones", LocalDate.of(1999, 2, 2));
        assertFalse(jane.equals(jessica));
        assertFalse(jessica.equals(jane));
    }

    @Test
    void testHashCode() {
        Student bob2 = Student.getInstance("Bob", "Smith", LocalDate.of(2001, 7, 24));
        assertEquals(bob.hashCode(), bob2.hashCode());

        Student jane = Student.getInstance("Jane", "Holmes", LocalDate.of(1999, 1, 1));
        assertNotEquals(bob2.hashCode(), jane.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Student{firstName='Bob', lastName='Smith', dateOfBirth=2001-07-24}", bob.toString());
    }
}