package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaService {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    private Set<Conta> contas = new HashSet<>();

    public Set<Conta> listarContasAbertas() {
        try (Connection connection = connectionFactory.getConnection()) {
            contas = new ContaDAO(connection).listar();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contas;
    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(ContaAberturaDTO dadosDaConta) {
        try (Connection connection = connectionFactory.getConnection()) {
            new ContaDAO(connection).salvar(dadosDaConta);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }

        conta.sacar(valor);
    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }

        conta.depositar(valor);
    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        contas.remove(conta);
    }

    private Conta buscarContaPorNumero(Integer numero) {
        Conta conta;

        try (Connection connection = connectionFactory.getConnection()) {
            conta = new ContaDAO(connection).detalhar(numero);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conta;
    }
}
