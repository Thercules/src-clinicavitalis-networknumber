package org.clinicavitalis.shared.domain.vo;

import java.util.Objects;

public class Endereco {

    private final String rua;
    private final String numero;
    private final String bairro;
    private final String cidade;
    private final String estado;
    private final String cep;

    private Endereco(String rua, String numero, String bairro, String cidade, String estado, String cep) {
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public static Endereco of(String rua, String numero, String bairro, String cidade, String estado, String cep) {
        Objects.requireNonNull(rua, "Rua não pode ser nula");
        Objects.requireNonNull(numero, "Número não pode ser nulo");
        Objects.requireNonNull(bairro, "Bairro não pode ser nulo");
        Objects.requireNonNull(cidade, "Cidade não pode ser nula");
        Objects.requireNonNull(estado, "Estado não pode ser nulo");
        Objects.requireNonNull(cep, "CEP não pode ser nulo");
        if (estado.length() != 2)
            throw new IllegalArgumentException("Estado deve ter 2 caracteres (UF)");
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        if (cepLimpo.length() != 8)
            throw new IllegalArgumentException("CEP inválido: " + cep);
        return new Endereco(rua.trim(), numero.trim(), bairro.trim(), cidade.trim(), estado.trim().toUpperCase(), cep.trim());
    }

    public String getRua() { return rua; }
    public String getNumero() { return numero; }
    public String getBairro() { return bairro; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getCep() { return cep; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco e = (Endereco) o;
        return Objects.equals(rua, e.rua) && Objects.equals(numero, e.numero)
                && Objects.equals(bairro, e.bairro) && Objects.equals(cidade, e.cidade)
                && Objects.equals(estado, e.estado) && Objects.equals(cep, e.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rua, numero, bairro, cidade, estado, cep);
    }

    @Override
    public String toString() {
        return rua + ", " + numero + " - " + bairro + ", " + cidade + "/" + estado + " - " + cep;
    }
}
