package org.clinicavitalis.usuario.domain.entity;

public enum NivelDeAcesso {
    GM("gm", "Game Master/Dono"),
    ADM("adm", "Administrador"),
    GESTOR("gestor", "Gestor/Gerente"),
    MEDICO("medico", "Médico"),
    ENFERMEIRA("enfermeira", "Enfermeira"),
    PACIENTE("paciente", "Paciente");

    private final String codigo;
    private final String descricao;

    NivelDeAcesso(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static NivelDeAcesso fromCodigo(String codigo) {
        if (codigo == null) {
            return PACIENTE;
        }

        for (NivelDeAcesso nivel : values()) {
            if (nivel.codigo.equalsIgnoreCase(codigo)) {
                return nivel;
            }
        }

        throw new IllegalArgumentException("Nível de acesso inválido: " + codigo);
    }
}
