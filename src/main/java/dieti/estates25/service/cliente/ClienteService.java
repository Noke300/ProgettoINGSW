package dieti.estates25.service.cliente;

import dieti.estates25.model.cliente.Cliente;

import java.util.List;

public interface ClienteService {

    List<Cliente> getAllClients();

    Cliente getClienteByMail(String mail);

    void addCliente(Cliente cliente);

    void updateCliente(String mail, Cliente cliente);

    void deleteCliente(String mail);
}
