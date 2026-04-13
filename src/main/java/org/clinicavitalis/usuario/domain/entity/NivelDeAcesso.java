package org.clinicavitalis.usuario.domain.entity;

public enum NivelDeAcesso {
    GM("gm", "Game Master/Dono", 100),
    ADM("adm", "Administrador", 80),
    GESTOR("gestor", "Gestor/Gerente", 60),
    MEDICO("medico", "Médico", 40),
    ENFERMEIRA("enfermeira", "Enfermeira", 30),
    PACIENTE("paciente", "Paciente", 10);

    private final String codigo;
    private final String descricao;
    private final int hierarquia;

    NivelDeAcesso(String codigo, String descricao, int hierarquia) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.hierarquia = hierarquia;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getHierarquia() {
        return hierarquia;
    }

    public boolean podeGerenciar(NivelDeAcesso outro) {
        return this.hierarquia > outro.hierarquia;
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
