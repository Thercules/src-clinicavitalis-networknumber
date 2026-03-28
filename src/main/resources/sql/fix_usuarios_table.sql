-- Script de correção da tabela usuarios
-- Execute esta query no seu MySQL para corrigir o tipo de dados

-- 1. Drop a constraint de chave estrangeira primeiro
ALTER TABLE usuarios DROP FOREIGN KEY usuarios_ibfk_1;

-- 2. Alterar os tipos de ID para BIGINT
ALTER TABLE usuarios MODIFY COLUMN id BIGINT AUTO_INCREMENT;
ALTER TABLE usuarios MODIFY COLUMN criado_por BIGINT NULL;

-- 3. Recriar a constraint de chave estrangeira
ALTER TABLE usuarios ADD CONSTRAINT usuarios_ibfk_1 
  FOREIGN KEY (criado_por) REFERENCES usuarios(id);

-- 4. Verificar se a tabela está correta
DESCRIBE usuarios;
