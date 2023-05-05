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
                INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email, ativa)
                VALUES (?, ?, ?, ?, ?, true)
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

        String sql = "SELECT * from conta WHERE ativa = true";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int numero = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean ativa = resultSet.getBoolean(6);

                ClienteCadastroDTO dadosCliente = new ClienteCadastroDTO(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCliente);
                Conta conta = new Conta(numero, saldo, cliente, ativa);

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

        String sql = "SELECT * FROM conta WHERE numero = ? AND ativa = true";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, numero);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            int numeroDaConta = resultSet.getInt(1);
            BigDecimal saldo = resultSet.getBigDecimal(2);
            String nome = resultSet.getString(3);
            String cpf = resultSet.getString(4);
            String email = resultSet.getString(5);
            Boolean ativa = resultSet.getBoolean(6);

            ClienteCadastroDTO dadosCliente = new ClienteCadastroDTO(nome, cpf, email);
            Cliente cliente = new Cliente(dadosCliente);

            conta = new Conta(numeroDaConta, saldo, cliente, ativa);

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RegraDeNegocioException("Não existe conta com esse número!");
        }

        return conta;
    }

    public void atualizarSaldo(int numero, BigDecimal valor) {
        String sql = "UPDATE conta SET saldo = ? WHERE numero = ?";

        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(2, numero);
            preparedStatement.setBigDecimal(1, valor);

            preparedStatement.execute();

            connection.commit();

            preparedStatement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            throw new RuntimeException(e);
        }
    }

    public void inativar(int numero) {
        String sql = "UPDATE conta SET ativa = false WHERE numero = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, numero);

            preparedStatement.execute();

            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void encerrar(int numero) {
        String sql = "DELETE FROM conta WHERE numero = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, numero);

            preparedStatement.execute();

            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
