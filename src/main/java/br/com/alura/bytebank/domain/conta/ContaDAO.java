package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContaDAO {

    private final Connection connection;

    public ContaDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(ContaAberturaDTO dados) {
        var cliente = new Cliente(dados.dadosCliente());
        var conta = new Conta(dados.numero(), cliente);

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
