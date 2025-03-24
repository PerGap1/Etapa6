-- -----------------------------------------------------------------------------------
-- SCRIPT DE CRIAÇÃO DO BANCO DE DADOS
-- -----------------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS OGSBreakout;
USE OGSBreakout;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON OGSBreakout.* TO 'admin'@'localhost';

-- -----------------------------------------------------
-- Tabela Usuario
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Usuario(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR (255) NOT NULL,
    senha VARCHAR (255) NOT NULL,
    email VARCHAR (255) NOT NULL,
    apelido VARCHAR (255) NOT NULL,
    idade INT NOT NULL,
    data_entrada DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    acesso VARCHAR (20) NOT NULL
);

INSERT INTO Usuario(nome, senha, email, apelido, idade, acesso) VALUES ("OGSAdmin", "OGSAdmin", "OGSAdmin@gmail.com", "root", 99, "admin");
SELECT * FROM Usuario;
UPDATE Usuario SET acesso = "admin" WHERE id = 1;

-- -----------------------------------------------------
-- Tabela Jogo
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Jogo(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR (255) NOT NULL,
    categoria VARCHAR (255) NOT NULL,
    valor DECIMAL (6, 2) NOT NULL,
    lancamento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    criador VARCHAR(255) NOT NULL,
    descricao VARCHAR (400),
    classificacao INT NOT NULL
);

INSERT INTO Jogo(titulo, categoria, valor, criador, descricao, classificacao) VALUES ("Título", "Categoria", 20, "Criador", "Descrição", 12);
SELECT * FROM Jogo;

-- -----------------------------------------------------
-- Tabela UsuarioJogo
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS UsuarioJogo (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    jogo_id INT,
    possui BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (jogo_id) REFERENCES Jogo(id) ON DELETE CASCADE
);

UPDATE UsuarioJogo SET possui = TRUE WHERE usuario_id = 1 AND jogo_id = 1;
INSERT INTO UsuarioJogo (usuario_id, jogo_id, possui) VALUES (1, 1, TRUE);
SELECT * FROM UsuarioJogo;

-- DROP DATABASE OGSBreakout