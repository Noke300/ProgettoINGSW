package dietiEstates25.service;

import dietiEstates25.model.Cliente;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ClienteServiceMock implements ClienteService {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private List<Cliente> clienti = new ArrayList<>(Arrays.asList(new Cliente("francesco@gmail.com", "Francesco", "Trotti", "pass1!", sdf.parse("01/01/2003")),
                                                                  new Cliente("anna.rossi@example.com", "Anna", "Rossi", "annaPass123!", sdf.parse("15/03/1998")),
                                                                  new Cliente("luca.verdi@example.com", "Luca", "Verdi", "lucaSecure!", sdf.parse("22/07/1995"))));

    public ClienteServiceMock() throws ParseException {
    }

    @Override
    public List<Cliente> getAllClients() {
        return clienti;
    }

    @Override
    public Cliente getClienteByMail(String mail) {
        return clienti.stream()
                .filter(p -> p.getEmailCliente().equals(mail))
                .findFirst()
                .get();
    }

    @Override
    public void addCliente(Cliente cliente) {
        clienti.add(cliente);
    }

    @Override
    public void updateCliente(String mail, Cliente cliente) {
        for(int i = 0; i < clienti.size(); i++) {
            if(clienti.get(i).getEmailCliente().equals(mail)) {
                clienti.set(i, cliente);
            }
        }
    }

    @Override
    public void deleteCliente(String mail) {
        clienti.removeIf(cliente -> cliente.getEmailCliente().equals(mail));
    }
}
