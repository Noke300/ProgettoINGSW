package controllers;

import dieti.estates25.controllers.AuthController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckFiltriTest {

    private final AuthController validator = new AuthController();

    @Test
    void testCE1_CE4_CE7_C10_C13() {
        String classeEnergetica = "A+";
        int numStanze = 5;
        int numBagni = 2;
        int piano = 0;

        boolean result = validator.checkFiltri(classeEnergetica, numStanze, numBagni, piano);
        assertTrue(result);
    }

    @Test
    void testCE2_CE4_CE7_C10_C14() {
        int numStanze = 0;
        int numBagni = 3;
        int piano = 10;

        boolean result = validator.checkFiltri(null, numStanze, numBagni, piano);
        assertTrue(result);
    }

    @Test
    void testCE3_CE4_CE7_C10() {
        String classeEnergetica = "prova";
        int numStanze = 0;
        int numBagni = 0;
        int piano = 0;

        boolean result = validator.checkFiltri(classeEnergetica, numStanze, numBagni, piano);
        assertFalse(result);
    }

    @Test
    void testCE1_CE5_CE7_C10() {
        String classeEnergetica = "B";
        int numStanze = -1;
        int numBagni = 3;
        int piano = 7;

        boolean result = validator.checkFiltri(classeEnergetica, numStanze, numBagni, piano);
        assertFalse(result);
    }

    @Test
    void testCE1_CE6_CE7_C10() {
        String classeEnergetica = "C";
        int numStanze = 11;
        int numBagni = 2;
        int piano = 0;

        boolean result = validator.checkFiltri(classeEnergetica, numStanze, numBagni, piano);
        assertFalse(result);
    }

    @Test
    void testCE1_CE4_CE8_C10() {
        String classeEnergetica = "A+";
        int numStanze = 5;
        int numBagni = -3;
        int piano = 9;

        boolean result = validator.checkFiltri(classeEnergetica, numStanze, numBagni, piano);
        assertFalse(result);
    }

    @Test
    void testCE1_CE4_CE9_C10() {
        String classeEnergetica = "A+";
        int numStanze = 0;
        int numBagni = 13;
        int piano = 9;

        boolean result = validator.checkFiltri(classeEnergetica, numStanze, numBagni, piano);
        assertFalse(result);
    }

    @Test
    void testCE1_CE4_CE7_C11() {
        String classeEnergetica = "A+";
        int numStanze = 5;
        int numBagni = 3;
        int piano = -5;

        boolean result = validator.checkFiltri(classeEnergetica, numStanze, numBagni, piano);
        assertFalse(result);
    }

    @Test
    void testCE1_CE4_CE7_C12() {
        String classeEnergetica = "E";
        int numStanze = 5;
        int numBagni = 3;
        int piano = 51;

        boolean result = validator.checkFiltri(classeEnergetica, numStanze, numBagni, piano);
        assertFalse(result);
    }

    @Test
    void testCE15() {
        String classeEnergetica = "E";
        int numStanze = 5;
        int numBagni = 6;
        int piano = 3;

        boolean result = validator.checkFiltri(classeEnergetica, numStanze, numBagni, piano);
        assertFalse(result);
    }
}
