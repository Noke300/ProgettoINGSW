package cliente;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import dieti.estates25.repositories.ClienteRepository;
import dieti.estates25.repositories.ImmobileRepository;
import dieti.estates25.repositories.OffertaRepository;
import dieti.estates25.repositories.VisitaRepository;
import dieti.estates25.service.ClienteServiceDB;
import dieti.estates25.service.JWTService;
import dieti.estates25.service.NotificaService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Calendar;
import java.util.Date;

class CredenzialiTest {

    ClienteServiceDB validator = new ClienteServiceDB(
            mock(ClienteRepository.class),
            mock(VisitaRepository.class),
            mock(OffertaRepository.class),
            mock(ImmobileRepository.class),
            mock(JWTService.class),
            mock(NotificaService.class),
            mock(AuthenticationManager.class)
    );

    // Helper per creare una data
    private Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day); // Mese 0-based
        return cal.getTime();
    }

    @Test
    void testCE1_CE5_CE10() {
        String email = "prova@esempio.com";
        String password = "Password1!";
        Date data = getDate(2003, 11, 1);

        boolean result = validator.checkDatiCliente(email, password, data);
        assertTrue(result);
    }

    @Test
    void testCE2_CE5_CE10() {
        String email = "provaesempio.com"; // email non valida
        String password = "Password1!";
        Date data = getDate(2002, 12, 11);

        boolean result = validator.checkDatiCliente(email, password, data);
        assertFalse(result);
    }

    @Test
    void testCE3_CE5_CE10() {
        String email = "prova@esempio&.com"; // carattere speciale non ammesso
        String password = "password1!";
        Date data = getDate(2000, 1, 1);

        boolean result = validator.checkDatiCliente(email, password, data);
        assertFalse(result);
    }

    @Test
    void testCE4_CE5_CE10() {
        String email = null;
        String password = "password1!";
        Date data = getDate(2000, 1, 1);

        boolean result = validator.checkDatiCliente(email, password, data);
        assertFalse(result);
    }

    @Test
    void testCE1_CE6_CE10() {
        String email = "prova@esempio.com";
        String password = "Pass1!"; // troppo corta
        Date data = getDate(2003, 11, 1);

        boolean result = validator.checkDatiCliente(email, password, data);
        assertFalse(result);
    }

    @Test
    void testCE1_CE7_CE10() {
        String email = "prova@esempio.com";
        String password = "password1!"; // senza maiuscole
        Date data = getDate(2001, 10, 17);

        boolean result = validator.checkDatiCliente(email, password, data);
        assertFalse(result);
    }

    @Test
    void testCE1_CE8_CE10() {
        String email = "prova@esempio.com";
        String password = "Password1"; // senza caratteri speciali
        Date data = getDate(1999, 4, 14);

        boolean result = validator.checkDatiCliente(email, password, data);
        assertFalse(result);
    }

    @Test
    void testCE1_CE9_CE10() {
        String email = "prova@esempio.com";
        String password = null;
        Date data = getDate(2003, 11, 1);

        boolean result = validator.checkDatiCliente(email, password, data);
        assertFalse(result);
    }

    @Test
    void testCE1_CE5_CE11() {
        String email = "test@example.com";
        String password = "Password1!";

        Calendar oggi = Calendar.getInstance();
        oggi.add(Calendar.YEAR, -17);
        Date data = oggi.getTime(); // minorenne

        boolean result = validator.checkDatiCliente(email, password, data);
        assertFalse(result);
    }

    @Test
    void testCE1_CE5_CE12() {
        String email = "prova@esempio.com";
        String password = "Password1!";
        Date data = null;

        boolean result = validator.checkDatiCliente(email, password, data);
        assertFalse(result);
    }


}

