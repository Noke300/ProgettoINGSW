package dietiEstates25.service;

import dietiEstates25.model.Cliente;
import java.util.List;

public interface ClienteService {

    public List<Cliente> getAllClients();

    public Cliente getClienteByMail(String mail);

    public void addCliente(Cliente cliente);

    public void updateCliente(String mail, Cliente cliente);

    public void deleteCliente(String mail);
}
