package org.clinicavitalis.medico.domain.entity;

import org.clinicavitalis.shared.domain.vo.Email;
import org.clinicavitalis.shared.domain.vo.Endereco;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Medico {

    private Long id;
    private Long usuarioId;
    private String nomeCompleto;

    private String crmNumero;
    private String crmEstado;
    private CrmSituacao crmSituacao;
    private LocalDate crmDataEmissao;

    private LocalDate dataNascimento;
    private Sexo sexo;
    private String nacionalidade;

    private Email emailProfissional;
    private Endereco endereco;

    private List<String> especialidades;
    private String fotoUrl;
    private String localizacao;

    private List<Integer> diasAtendimento;
    private LocalTime horarioInicioManha;
    private LocalTime horarioFimManha;
    private LocalTime horarioInicioTarde;
    private LocalTime horarioFimTarde;
    private int intervaloConsultaMinutos;
    private int semanasDisponibilidade;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Boolean ativo;

    private Medico() {}

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
            Email emailProfissional,
            Endereco endereco,
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

        validarHorarios(horarioInicioManha, horarioFimManha, horarioInicioTarde, horarioFimTarde);
        validarIntervalo(intervaloConsultaMinutos);

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
        medico.endereco = endereco;
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

    private static void validarHorarios(LocalTime inicioManha, LocalTime fimManha,
            LocalTime inicioTarde, LocalTime fimTarde) {
        if (inicioManha != null && fimManha != null && !inicioManha.isBefore(fimManha))
            throw new org.clinicavitalis.medico.domain.exception.MedicoDomainException(
                "Horário início manhã deve ser anterior ao fim manhã");
        if (inicioTarde != null && fimTarde != null && !inicioTarde.isBefore(fimTarde))
            throw new org.clinicavitalis.medico.domain.exception.MedicoDomainException(
                "Horário início tarde deve ser anterior ao fim tarde");
        if (fimManha != null && inicioTarde != null && !fimManha.isBefore(inicioTarde))
            throw new org.clinicavitalis.medico.domain.exception.MedicoDomainException(
                "Horário fim manhã deve ser anterior ao início tarde");
    }

    private static void validarIntervalo(int intervalo) {
        if (intervalo <= 0)
            throw new org.clinicavitalis.medico.domain.exception.MedicoDomainException(
                "Intervalo de consulta deve ser maior que zero");
    }

    public boolean isCrmAtivo() {
        return CrmSituacao.ATIVO == crmSituacao;
    }

    public LocalDate getDataLimiteAgendamento() {
        return LocalDate.now().plusDays((long) semanasDisponibilidade * 7);
    }

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
    public Email getEmailProfissional() { return emailProfissional; }
    public Endereco getEndereco() { return endereco; }
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
