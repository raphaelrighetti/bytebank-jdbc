package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.ClienteCadastroDTO;

public record ContaAberturaDTO(Integer numero, ClienteCadastroDTO dadosCliente) {
}
