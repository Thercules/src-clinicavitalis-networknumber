package org.clinicavitalis.medico.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.clinicavitalis.medico.domain.entity.CrmSituacao;
import org.clinicavitalis.medico.domain.entity.Sexo;
import org.clinicavitalis.usuario.infrastructure.persistence.entity.UsuarioJpaEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "medicos", indexes = {
        @Index(name = "idx_medicos_usuario",    columnList = "usuario_id"),
        @Index(name = "idx_medicos_ativo",       columnList = "ativo"),
        @Index(name = "idx_medicos_crm_estado",  columnList = "crm_estado")
})
public class MedicoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false, unique = true)
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private UsuarioJpaEntity usuario;

    @Column(name = "crm_numero", length = 20, nullable = false)
    private String crmNumero;

    @Column(name = "crm_estado", length = 2, nullable = false)
    private String crmEstado;

    @Enumerated(EnumType.STRING)
    @Column(name = "crm_situacao", nullable = false)
    private CrmSituacao crmSituacao;

    @Column(name = "crm_data_emissao", nullable = false)
    private LocalDate crmDataEmissao;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false)
    private Sexo sexo;

    @Column(name = "nacionalidade", length = 100, nullable = false)
    private String nacionalidade;

    @Column(name = "email_profissional", length = 150, unique = true, nullable = false)
    private String emailProfissional;

    @Column(name = "endereco_rua", length = 200, nullable = false)
    private String enderecoRua;

    @Column(name = "endereco_numero", length = 20, nullable = false)
    private String enderecoNumero;

    @Column(name = "endereco_bairro", length = 100, nullable = false)
    private String enderecoBairro;

    @Column(name = "endereco_cidade", length = 100, nullable = false)
    private String enderecoCidade;

    @Column(name = "endereco_estado", length = 2, nullable = false)
    private String enderecoEstado;

    @Column(name = "endereco_cep", length = 9, nullable = false)
    private String enderecoCep;

    @Column(name = "especialidades", columnDefinition = "JSON", nullable = false)
    private String especialidades;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(name = "localizacao", length = 200)
    private String localizacao;

    @Column(name = "dias_atendimento", columnDefinition = "JSON", nullable = false)
    private String diasAtendimento;

    @Column(name = "horario_inicio_manha", nullable = false)
    private LocalTime horarioInicioManha;

    @Column(name = "horario_fim_manha", nullable = false)
    private LocalTime horarioFimManha;

    @Column(name = "horario_inicio_tarde")
    private LocalTime horarioInicioTarde;

    @Column(name = "horario_fim_tarde")
    private LocalTime horarioFimTarde;

    @Column(name = "intervalo_consulta_minutos", nullable = false)
    private int intervaloConsultaMinutos;

    @Column(name = "semanas_disponibilidade", nullable = false)
    private int semanasDisponibilidade;

    @Column(name = "data_criacao", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime dataAtualizacao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    public MedicoJpaEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public UsuarioJpaEntity getUsuario() { return usuario; }
    public void setUsuario(UsuarioJpaEntity usuario) { this.usuario = usuario; }

    public String getCrmNumero() { return crmNumero; }
    public void setCrmNumero(String crmNumero) { this.crmNumero = crmNumero; }

    public String getCrmEstado() { return crmEstado; }
    public void setCrmEstado(String crmEstado) { this.crmEstado = crmEstado; }

    public CrmSituacao getCrmSituacao() { return crmSituacao; }
    public void setCrmSituacao(CrmSituacao crmSituacao) { this.crmSituacao = crmSituacao; }

    public LocalDate getCrmDataEmissao() { return crmDataEmissao; }
    public void setCrmDataEmissao(LocalDate crmDataEmissao) { this.crmDataEmissao = crmDataEmissao; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

    public String getEmailProfissional() { return emailProfissional; }
    public void setEmailProfissional(String emailProfissional) { this.emailProfissional = emailProfissional; }

    public String getEnderecoRua() { return enderecoRua; }
    public void setEnderecoRua(String enderecoRua) { this.enderecoRua = enderecoRua; }

    public String getEnderecoNumero() { return enderecoNumero; }
    public void setEnderecoNumero(String enderecoNumero) { this.enderecoNumero = enderecoNumero; }

    public String getEnderecoBairro() { return enderecoBairro; }
    public void setEnderecoBairro(String enderecoBairro) { this.enderecoBairro = enderecoBairro; }

    public String getEnderecoCidade() { return enderecoCidade; }
    public void setEnderecoCidade(String enderecoCidade) { this.enderecoCidade = enderecoCidade; }

    public String getEnderecoEstado() { return enderecoEstado; }
    public void setEnderecoEstado(String enderecoEstado) { this.enderecoEstado = enderecoEstado; }

    public String getEnderecoCep() { return enderecoCep; }
    public void setEnderecoCep(String enderecoCep) { this.enderecoCep = enderecoCep; }

    public String getEspecialidades() { return especialidades; }
    public void setEspecialidades(String especialidades) { this.especialidades = especialidades; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getDiasAtendimento() { return diasAtendimento; }
    public void setDiasAtendimento(String diasAtendimento) { this.diasAtendimento = diasAtendimento; }

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

    public int getSemanasDisponibilidade() { return semanasDisponibilidade; }
    public void setSemanasDisponibilidade(int semanasDisponibilidade) { this.semanasDisponibilidade = semanasDisponibilidade; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
