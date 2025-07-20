package controllers;

import dieti.estates25.config.TokenBlacklist;
import dieti.estates25.controllers.AuthController;
import dieti.estates25.repositories.ClienteRepository;
import dieti.estates25.repositories.ImmagineImmobileRepository;
import dieti.estates25.repositories.ImmobileRepository;
import dieti.estates25.repositories.OffertaRepository;
import dieti.estates25.service.NotificaService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CheckPrezziTest {

    AuthController validator = new AuthController(
            mock(TokenBlacklist.class),
            mock(ImmobileRepository.class),
            mock(OffertaRepository.class),
            mock(ClienteRepository.class),
            mock(ImmagineImmobileRepository.class),
            mock(NotificaService.class)
    );

    @Test
    void testCE1_CE4_CE7() {
        double prezzoMin = 100000;
        double prezzoMax = 200000;

        boolean result = validator.checkPrezzi(prezzoMin, prezzoMax);
        assertTrue(result);
    }

    @Test
    void testCE2_CE4() {
        double prezzoMin = -100000;
        double prezzoMax = 200000;

        boolean result = validator.checkPrezzi(prezzoMin, prezzoMax);
        assertFalse(result);
    }

    @Test
    void testCE3_CE4() {
        double prezzoMin = 900000;
        double prezzoMax = 200000;

        boolean result = validator.checkPrezzi(prezzoMin, prezzoMax);
        assertFalse(result);
    }

    @Test
    void testCE1_CE5() {
        double prezzoMin = 100000;
        double prezzoMax = -200000;

        boolean result = validator.checkPrezzi(prezzoMin, prezzoMax);
        assertFalse(result);
    }

    @Test
    void testCE1_CE6() {
        double prezzoMin = 100000;
        double prezzoMax = 1500000;

        boolean result = validator.checkPrezzi(prezzoMin, prezzoMax);
        assertFalse(result);
    }

    @Test
    void testCE8() {
        double prezzoMin = 200.000;
        double prezzoMax = 100.000;

        boolean result = validator.checkPrezzi(prezzoMin, prezzoMax);
        assertFalse(result);
    }

    @Test
    void testCE9() {
        double prezzoMin = 200.000;
        double prezzoMax = 0;

        boolean result = validator.checkPrezzi(prezzoMin, prezzoMax);
        assertTrue(result);
    }


}
