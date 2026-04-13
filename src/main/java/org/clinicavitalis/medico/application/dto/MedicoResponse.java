package org.clinicavitalis.medico.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MedicoResponse {

    private Long id;

    @JsonProperty("usuario_id")
    private Long usuarioId;

    @JsonProperty("nome_completo")
    private String nomeCompleto;

    @JsonProperty("crm_numero")
    private String crmNumero;

    @JsonProperty("crm_estado")
    private String crmEstado;

    @JsonProperty("crm_situacao")
    private String crmSituacao;

    @JsonProperty("crm_data_emissao")
    private LocalDate crmDataEmissao;

    @JsonProperty("email_profissional")
    private String emailProfissional;

    private List<String> especialidades;

    @JsonProperty("foto_url")
    private String fotoUrl;

    private String localizacao;

    @JsonProperty("dias_atendimento")
    private List<Integer> diasAtendimento;

    @JsonProperty("horario_inicio_manha")
    private LocalTime horarioInicioManha;

    @JsonProperty("horario_fim_manha")
    private LocalTime horarioFimManha;

    @JsonProperty("horario_inicio_tarde")
    private LocalTime horarioInicioTarde;

    @JsonProperty("horario_fim_tarde")
    private LocalTime horarioFimTarde;

    @JsonProperty("intervalo_consulta_minutos")
    private int intervaloConsultaMinutos;

    @JsonProperty("data_limite_agendamento")
    private LocalDate dataLimiteAgendamento;

    private Boolean ativo;

    public MedicoResponse() {}

    // ─── Getters & Setters ───────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getCrmNumero() { return crmNumero; }
    public void setCrmNumero(String crmNumero) { this.crmNumero = crmNumero; }

    public String getCrmEstado() { return crmEstado; }
    public void setCrmEstado(String crmEstado) { this.crmEstado = crmEstado; }

    public String getCrmSituacao() { return crmSituacao; }
    public void setCrmSituacao(String crmSituacao) { this.crmSituacao = crmSituacao; }

    public LocalDate getCrmDataEmissao() { return crmDataEmissao; }
    public void setCrmDataEmissao(LocalDate crmDataEmissao) { this.crmDataEmissao = crmDataEmissao; }

    public String getEmailProfissional() { return emailProfissional; }
    public void setEmailProfissional(String emailProfissional) { this.emailProfissional = emailProfissional; }

    public List<String> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<String> especialidades) { this.especialidades = especialidades; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public List<Integer> getDiasAtendimento() { return diasAtendimento; }
    public void setDiasAtendimento(List<Integer> diasAtendimento) { this.diasAtendimento = diasAtendimento; }

    public LocalTime getHorarioInicioManha() { return horarioInicioManha; }
    public void setHorarioInicioManha(LocalTime horarioInicioManha) { this.horarioInicioManha = horarioInicioManha; }

    public LocalTime getHorarioFimManha() { return horarioFimManha; }
    public void setHorarioFimManha(LocalTime horarioFimManha) { this.horarioFimManha = horarioFimManha; }

    public LocalTime getHorarioInicioTarde() { return horarioInicioTarde; }
    public void setHorarioInicioTarde(LocalTime horarioInicioTarde) { this.horarioInicioTarde = horarioInicioTarde; }

    public LocalTime getHorarioFimTarde() { return horarioFimTarde; }
    public void setHorarioFimTarde(LocalTime horarioFimTarde) { this.horarioFimTarde = horarioFimTarde; }

    public int getIntervaloConsultaMinutos() { return intervaloConsultaMinutos; }
    public void setIntervaloConsultaMinutos(int intervaloConsultaMinutos) { this.intervaloConsultaMinutos = intervaloConsultaMinutos; }

    public LocalDate getDataLimiteAgendamento() { return dataLimiteAgendamento; }
    public void setDataLimiteAgendamento(LocalDate dataLimiteAgendamento) { this.dataLimiteAgendamento = dataLimiteAgendamento; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
