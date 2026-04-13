-- Script de inicialização do banco de dados
-- Execute este script para criar o banco e tabela do zero

-- Criar database
CREATE DATABASE IF NOT EXISTS clinicavitalis 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Usar o banco
USE clinicavitalis;

-- Criar tabela usuarios com schema correto
CREATE TABLE usuarios (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID único do usuário',
  email VARCHAR(150) UNIQUE NOT NULL COMMENT 'Email único do usuário',
  senha VARCHAR(255) NOT NULL COMMENT 'Senha criptografada (bcrypt)',
  nome_completo VARCHAR(200) NOT NULL COMMENT 'Nome completo do usuário',
  nivel_de_acesso ENUM('GM','ADM','GESTOR','MEDICO','ENFERMEIRA','PACIENTE') 
    DEFAULT 'PACIENTE' COMMENT 'Nível de acesso e permissões',
  data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação da conta',
  data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Última atualização',
  ativo BOOLEAN DEFAULT TRUE COMMENT 'Se o usuário está ativo',
  ultimo_acesso TIMESTAMP NULL COMMENT 'Data e hora do último acesso',
  telefone VARCHAR(20) NULL COMMENT 'Telefone do usuário',
  cpf VARCHAR(14) UNIQUE NULL COMMENT 'CPF do usuário (apenas se paciente/médico/enfermeira)',
  reseta_senha_token VARCHAR(255) NULL COMMENT 'Token para reset de senha',
  reseta_senha_expiracao TIMESTAMP NULL COMMENT 'Expiração do token de reset',
  email_verificado BOOLEAN DEFAULT FALSE COMMENT 'Se o email foi verificado',
  verificacao_email_token VARCHAR(255) NULL COMMENT 'Token para verification de email',
  criado_por BIGINT NULL COMMENT 'ID do usuário que criou este registro (para auditoria)',
  FOREIGN KEY (criado_por) REFERENCES usuarios(id),
  INDEX idx_email (email),
  INDEX idx_nivel_acesso (nivel_de_acesso),
  INDEX idx_ativo (ativo),
  INDEX idx_cpf (cpf)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Criar usuário GM (Game Master) padrão
-- Senha padrão: Admin@123456 (altere em produção)
INSERT INTO usuarios 
(email, senha, nome_completo, nivel_de_acesso, ativo) 
VALUES 
(
  'admin@clinicavitalis.com',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gBS/pG', -- Admin@123456
  'Administrador Sistema',
  'GM',
  TRUE
);
