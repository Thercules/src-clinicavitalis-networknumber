package org.clinicavitalis.medico.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Aggregate Root: Medico
 *
 * Representa o cadastro profissional de um médico vinculado a um usuário
 * com nivel_de_acesso = 'MEDICO'. O nome do médico é obtido da tabela
 * usuarios (usuarios.nome_completo) via join.
 */
public class Medico {

    private Long id;
    private Long usuarioId;
    private String nomeCompleto;

    // CRM
    private String crmNumero;
    private String crmEstado;
    private CrmSituacao crmSituacao;
    private LocalDate crmDataEmissao;

    // Dados pessoais
    private LocalDate dataNascimento;
    private Sexo sexo;
    private String nacionalidade;

    // Contato profissional
    private String emailProfissional;

    // Endereço
    private String enderecoRua;
    private String enderecoNumero;
    private String enderecoBairro;
    private String enderecoCidade;
    private String enderecoEstado;
    private String enderecoCep;

    // Dados do card
    private List<String> especialidades;
    private String fotoUrl;
    private String localizacao;

    // Agenda
    private List<Integer> diasAtendimento;
    private LocalTime horarioInicioManha;
    private LocalTime horarioFimManha;
    private LocalTime horarioInicioTarde;
    private LocalTime horarioFimTarde;
    private int intervaloConsultaMinutos;
    private int semanasDisponibilidade;

    // Auditoria
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Boolean ativo;

    private Medico() {}

    /**
     * Reconstrói o agregado a partir dos dados persistidos (usado pelo repositório).
     */
    public static Medico reconectar(
            Long id,
            Long usuarioId,
            String nomeCompleto,
            String crmNumero,
            String crmEstado,
            CrmSituacao crmSituacao,
            LocalDate crmDataEmissao,
            LocalDate dataNascimento,
            Sexo sexo,
            String nacionalidade,
            String emailProfissional,
            String enderecoRua,
            String enderecoNumero,
            String enderecoBairro,
            String enderecoCidade,
            String enderecoEstado,
            String enderecoCep,
            List<String> especialidades,
            String fotoUrl,
            String localizacao,
            List<Integer> diasAtendimento,
            LocalTime horarioInicioManha,
            LocalTime horarioFimManha,
            LocalTime horarioInicioTarde,
            LocalTime horarioFimTarde,
            int intervaloConsultaMinutos,
            int semanasDisponibilidade,
            LocalDateTime dataCriacao,
            LocalDateTime dataAtualizacao,
            Boolean ativo) {

        Medico medico = new Medico();
        medico.id = id;
        medico.usuarioId = usuarioId;
        medico.nomeCompleto = nomeCompleto;
        medico.crmNumero = crmNumero;
        medico.crmEstado = crmEstado;
        medico.crmSituacao = crmSituacao;
        medico.crmDataEmissao = crmDataEmissao;
        medico.dataNascimento = dataNascimento;
        medico.sexo = sexo;
        medico.nacionalidade = nacionalidade;
        medico.emailProfissional = emailProfissional;
        medico.enderecoRua = enderecoRua;
        medico.enderecoNumero = enderecoNumero;
        medico.enderecoBairro = enderecoBairro;
        medico.enderecoCidade = enderecoCidade;
        medico.enderecoEstado = enderecoEstado;
        medico.enderecoCep = enderecoCep;
        medico.especialidades = especialidades;
        medico.fotoUrl = fotoUrl;
        medico.localizacao = localizacao;
        medico.diasAtendimento = diasAtendimento;
        medico.horarioInicioManha = horarioInicioManha;
        medico.horarioFimManha = horarioFimManha;
        medico.horarioInicioTarde = horarioInicioTarde;
        medico.horarioFimTarde = horarioFimTarde;
        medico.intervaloConsultaMinutos = intervaloConsultaMinutos;
        medico.semanasDisponibilidade = semanasDisponibilidade;
        medico.dataCriacao = dataCriacao;
        medico.dataAtualizacao = dataAtualizacao;
        medico.ativo = ativo;
        return medico;
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public String getNomeCompleto() { return nomeCompleto; }
    public String getCrmNumero() { return crmNumero; }
    public String getCrmEstado() { return crmEstado; }
    public CrmSituacao getCrmSituacao() { return crmSituacao; }
    public LocalDate getCrmDataEmissao() { return crmDataEmissao; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public Sexo getSexo() { return sexo; }
    public String getNacionalidade() { return nacionalidade; }
    public String getEmailProfissional() { return emailProfissional; }
    public String getEnderecoRua() { return enderecoRua; }
    public String getEnderecoNumero() { return enderecoNumero; }
    public String getEnderecoBairro() { return enderecoBairro; }
    public String getEnderecoCidade() { return enderecoCidade; }
    public String getEnderecoEstado() { return enderecoEstado; }
    public String getEnderecoCep() { return enderecoCep; }
    public List<String> getEspecialidades() { return especialidades; }
    public String getFotoUrl() { return fotoUrl; }
    public String getLocalizacao() { return localizacao; }
    public List<Integer> getDiasAtendimento() { return diasAtendimento; }
    public LocalTime getHorarioInicioManha() { return horarioInicioManha; }
    public LocalTime getHorarioFimManha() { return horarioFimManha; }
    public LocalTime getHorarioInicioTarde() { return horarioInicioTarde; }
    public LocalTime getHorarioFimTarde() { return horarioFimTarde; }
    public int getIntervaloConsultaMinutos() { return intervaloConsultaMinutos; }
    public int getSemanasDisponibilidade() { return semanasDisponibilidade; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public Boolean getAtivo() { return ativo; }
}
