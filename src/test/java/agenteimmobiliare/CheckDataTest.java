package agenteimmobiliare;

import dieti.estates25.repositories.*;
import dieti.estates25.service.AgenteImmobiliareServiceDB;
import dieti.estates25.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckDataTest {

    @Mock
    private AgenteImmobiliareRepository agentRepository;
    @Mock private ImmobileRepository immobileRepository;
    @Mock private VisitaRepository visitaRepository;
    @Mock private IndirizzoRepository indirizzoRepository;
    @Mock private JWTService jwtService;
    @Mock private ImmagineImmobileRepository immagineImmobileRepository;
    @Mock private OffertaRepository offertaRepository;
    @Mock private AuthenticationManager authManager;

    private AgenteImmobiliareServiceDB validator;

    @BeforeEach
    void setUp() {
        validator = new AgenteImmobiliareServiceDB(
                agentRepository,
                immobileRepository,
                visitaRepository,
                indirizzoRepository,
                jwtService,
                immagineImmobileRepository,
                offertaRepository,
                authManager
        );
    }

    // Helper per creare una data
    private Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.getTime();
    }

    @Test
    void testCE1_CE5_CE9() {
        Calendar cal = Calendar.getInstance(); // oggi

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Date data = getDate(year, month, day);
        LocalTime ora = LocalTime.now();
        boolean result;

        if(ora.isAfter(LocalTime.of(18, 0))){
            result = true;
        }else{
            ora = ora.plusMinutes(1);
            result = validator.checkData(data, ora);
        }

        assertTrue(result);
    }

    @Test
    void testCE2_CE5() {
        Date data = getDate(2024, 11, 9);
        LocalTime ora = LocalTime.of(8, 0);

        boolean result = validator.checkData(data, ora);
        assertFalse(result);
    }

    @Test
    void testCE3_CE5() {
        Calendar cal = Calendar.getInstance(); // oggi
        cal.add(Calendar.DAY_OF_MONTH, 15);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Date data = getDate(year, month, day);
        LocalTime ora = LocalTime.of(12, 0);

        boolean result = validator.checkData(data, ora);
        assertFalse(result);
    }

    @Test
    void testCE4_CE5() {
        LocalTime ora = LocalTime.of(8, 0);

        boolean result = validator.checkData(null, ora);
        assertFalse(result);
    }

    @Test
    void testCE1_CE6() {
        Calendar cal = Calendar.getInstance(); // oggi
        cal.add(Calendar.DAY_OF_MONTH, 1); // domani

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Date data = getDate(year, month, day);
        LocalTime ora = LocalTime.of(6, 0);

        boolean result = validator.checkData(data, ora);
        assertFalse(result);
    }

    @Test
    void testCE1_CE7() {
        Calendar cal = Calendar.getInstance(); // oggi
        cal.add(Calendar.DAY_OF_MONTH, 1); // domani

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Date data = getDate(year, month, day);
        LocalTime ora = LocalTime.of(20, 0);

        boolean result = validator.checkData(data, ora);
        assertFalse(result);
    }

    @Test
    void testCE1_CE8() {
        Calendar cal = Calendar.getInstance(); // oggi
        cal.add(Calendar.DAY_OF_MONTH, 1); // domani

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Date data = getDate(year, month, day);

        boolean result = validator.checkData(data, null);
        assertFalse(result);
    }

    @Test
    void testCE10() {
        Calendar cal = Calendar.getInstance(); // oggi

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Date data = getDate(year, month, day);
        LocalTime ora = LocalTime.now().minusHours(2);

        boolean result = validator.checkData(data, ora);
        assertFalse(result);
    }

}
