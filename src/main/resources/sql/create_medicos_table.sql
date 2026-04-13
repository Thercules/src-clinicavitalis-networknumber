-- ============================================================
-- Script de criação da tabela medicos
-- Relacionada à tabela usuarios (nivel_de_acesso = 'MEDICO')
-- O campo nome_completo do médico é obtido via JOIN com usuarios
-- ============================================================

USE clinicavitalis;

CREATE TABLE medicos (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID único do médico',

  -- Vínculo com usuarios (apenas usuários do tipo MEDICO)
  usuario_id BIGINT UNIQUE NOT NULL COMMENT 'FK para usuarios.id — somente nivel_de_acesso=MEDICO',

  -- Dados do CRM
  crm_numero   VARCHAR(20)  NOT NULL COMMENT 'Número do CRM',
  crm_estado   CHAR(2)      NOT NULL COMMENT 'UF do CRM (ex: SP, RJ)',
  crm_situacao ENUM('ATIVO','SUSPENSO','CANCELADO','INATIVO') NOT NULL DEFAULT 'ATIVO'
                COMMENT 'Situação do registro no CRM',
  crm_data_emissao DATE NOT NULL COMMENT 'Data de emissão do CRM',

  -- Dados pessoais
  data_nascimento DATE        NOT NULL COMMENT 'Data de nascimento',
  sexo            ENUM('MASCULINO','FEMININO','OUTRO') NOT NULL COMMENT 'Sexo',
  nacionalidade   VARCHAR(100) NOT NULL DEFAULT 'Brasileiro(a)' COMMENT 'Nacionalidade',

  -- Contato profissional
  email_profissional VARCHAR(150) UNIQUE NOT NULL COMMENT 'E-mail profissional (pode diferir do login)',

  -- Endereço completo
  endereco_rua    VARCHAR(200) NOT NULL COMMENT 'Logradouro',
  endereco_numero VARCHAR(20)  NOT NULL COMMENT 'Número',
  endereco_bairro VARCHAR(100) NOT NULL COMMENT 'Bairro',
  endereco_cidade VARCHAR(100) NOT NULL COMMENT 'Cidade',
  endereco_estado CHAR(2)      NOT NULL COMMENT 'UF do endereço',
  endereco_cep    VARCHAR(9)   NOT NULL COMMENT 'CEP (formato 00000-000)',

  -- Dados profissionais exibidos no front
  especialidades JSON NOT NULL
    COMMENT 'Array de especialidades. Ex: ["Cardiologia","Clínica Geral"]',
  foto_url    VARCHAR(500) NULL
    COMMENT 'URL da foto do médico — usada no card do front (selectedDoctor.image)',
  localizacao VARCHAR(200) NULL
    COMMENT 'Consultório/unidade — usada no card do front (selectedDoctor.location)',

  -- -------------------------------------------------------
  -- Configuração de agenda (alimenta o calendário do front)
  -- O front usa availableDates, availableTimes e lastAvailableDate
  -- -------------------------------------------------------

  -- Dias da semana de atendimento: array JSON com números ISO (1=Seg … 7=Dom)
  -- Ex: [1,2,3,4,5] = segunda a sexta
  dias_atendimento JSON NOT NULL
    COMMENT 'Dias da semana disponíveis. Ex: [1,2,3,4,5]',

  -- Turnos — espelham os horários do componente Vue:
  --   manhã  08:00–11:30  e  tarde  14:00–17:30
  horario_inicio_manha TIME    NOT NULL DEFAULT '08:00:00' COMMENT 'Início do turno manhã',
  horario_fim_manha    TIME    NOT NULL DEFAULT '11:30:00' COMMENT 'Fim do turno manhã',
  horario_inicio_tarde TIME        NULL DEFAULT '14:00:00' COMMENT 'Início do turno tarde (NULL = sem atendimento tarde)',
  horario_fim_tarde    TIME        NULL DEFAULT '17:30:00' COMMENT 'Fim do turno tarde',

  -- Intervalo entre consultas em minutos (front gera slots de 30 em 30 min)
  intervalo_consulta_minutos INT NOT NULL DEFAULT 30
    COMMENT 'Duração / intervalo de cada consulta em minutos',

  -- Janela de agendamento: quantas semanas à frente o paciente pode agendar
  -- Determina lastAvailableDate = HOJE + (semanas_disponibilidade * 7) dias
  semanas_disponibilidade INT NOT NULL DEFAULT 4
    COMMENT 'Janela de agendamento em semanas (define lastAvailableDate no front)',

  -- Auditoria
  data_criacao      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  ativo             BOOLEAN   DEFAULT TRUE COMMENT 'Se o cadastro do médico está ativo',

  -- Foreign key: garante que só usuários existentes são vinculados
  CONSTRAINT fk_medico_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),

  -- CRM único por estado
  CONSTRAINT uq_crm UNIQUE (crm_numero, crm_estado),

  INDEX idx_medicos_usuario   (usuario_id),
  INDEX idx_medicos_ativo      (ativo),
  INDEX idx_medicos_crm_estado (crm_estado)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Cadastro profissional dos médicos vinculados a usuarios.nivel_de_acesso=MEDICO';


-- ============================================================
-- Exemplo: vincular o médico Robiscleiton Evandro
-- usuario_id = 18 (conforme registro em usuarios)
-- ============================================================
INSERT INTO medicos (
  usuario_id,
  crm_numero, crm_estado, crm_situacao, crm_data_emissao,
  data_nascimento, sexo, nacionalidade,
  email_profissional,
  endereco_rua, endereco_numero, endereco_bairro,
  endereco_cidade, endereco_estado, endereco_cep,
  especialidades,
  foto_url,
  localizacao,
  dias_atendimento,
  horario_inicio_manha, horario_fim_manha,
  horario_inicio_tarde, horario_fim_tarde,
  intervalo_consulta_minutos,
  semanas_disponibilidade
) VALUES (
  18,
  '123456', 'SP', 'ATIVO', '2015-06-10',
  '1985-04-22', 'MASCULINO', 'Brasileiro',
  'robiscleiton.evandro@clinicavitalis.com',
  'Rua das Flores', '100', 'Centro',
  'São Paulo', 'SP', '01310-100',
  '["Clínica Geral","Medicina de Família"]',
  NULL,
  'Unidade São Paulo - Centro',
  '[1,2,3,4,5]',
  '08:00:00', '11:30:00',
  '14:00:00', '17:30:00',
  30,
  4
);


-- ============================================================
-- VIEW auxiliar: dados completos do médico para o front
-- Retorna tudo que o componente ScheduleConsultationView precisa:
--   selectedDoctor.name       → u.nome_completo
--   selectedDoctor.specialty  → m.especialidades (JSON)
--   selectedDoctor.image      → m.foto_url
--   selectedDoctor.location   → m.localizacao
--   availableTimes            → calculado a partir dos horários e intervalo
--   lastAvailableDate         → CURRENT_DATE + (semanas_disponibilidade * 7)
-- ============================================================
CREATE OR REPLACE VIEW vw_medicos_agenda AS
SELECT
  m.id                          AS medico_id,
  m.usuario_id,
  u.nome_completo               AS nome,          -- selectedDoctor.name
  m.especialidades,                               -- selectedDoctor.specialty (JSON array)
  m.foto_url                    AS imagem,        -- selectedDoctor.image
  m.localizacao,                                  -- selectedDoctor.location
  m.crm_numero,
  m.crm_estado,
  m.crm_situacao,
  m.dias_atendimento,
  m.horario_inicio_manha,
  m.horario_fim_manha,
  m.horario_inicio_tarde,
  m.horario_fim_tarde,
  m.intervalo_consulta_minutos,
  -- lastAvailableDate: data limite para agendamento
  DATE_ADD(CURRENT_DATE, INTERVAL (m.semanas_disponibilidade * 7) DAY) AS data_limite_agendamento,
  m.ativo
FROM medicos m
INNER JOIN usuarios u
       ON  u.id = m.usuario_id
      AND  u.nivel_de_acesso = 'MEDICO'
      AND  u.ativo = TRUE
WHERE m.ativo = TRUE;
