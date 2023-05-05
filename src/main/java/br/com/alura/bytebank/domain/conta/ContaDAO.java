package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.RegraDeNegocioException;
import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.ClienteCadastroDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {

    private final Connection connection;

    public ContaDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(ContaAberturaDTO dados) {
        Cliente cliente = new Cliente(dados.dadosCliente());
        Conta conta = new Conta(dados.numero(), cliente);

        String sql = """
                INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email)
                VALUES (?, ?, ?, ?, ?)
                """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, conta.getNumero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);

            preparedStatement.setString(3, cliente.getNome());
            preparedStatement.setString(4, cliente.getCpf());
            preparedStatement.setString(5, cliente.getEmail());

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Conta> listar() {
        Set<Conta> contas = new HashSet<>();

        String sql = "SELECT * from conta";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int numero = resultSet.getInt(1);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);

                ClienteCadastroDTO dadosCliente = new ClienteCadastroDTO(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCliente);
                Conta conta = new Conta(numero, cliente);

                contas.add(conta);
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contas;
    }

    public Conta detalhar(int numero) {
        Conta conta;

        String sql = "SELECT * FROM conta WHERE numero = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, numero);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            String nome = resultSet.getString(3);
            String cpf = resultSet.getString(4);
            String email = resultSet.getString(5);

            ClienteCadastroDTO dadosCliente = new ClienteCadastroDTO(nome, cpf, email);
            Cliente cliente = new Cliente(dadosCliente);

            conta = new Conta(numero, cliente);

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RegraDeNegocioException("Não existe conta com esse número!");
        }

        return conta;
    }
}
