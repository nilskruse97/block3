-- Datenbanken Praktikum WS 2019/2020
-- Musterlösung - Create Table-Statements
-- Schritt 1) Erzeugung der Datenbank: db2 "create database funder"
-- Schritt 2) Verbindung zur Datenbank herstellen: db2 "connect to funder"
-- Schritt 3) Importierung der Beispieldaten: db2 -vtf create_tables.sql


CREATE TABLE benutzer (
  	email VARCHAR(50) NOT NULL,
  	name VARCHAR(50) NOT NULL,
  	beschreibung CLOB (1M),
  	PRIMARY KEY (email)
);

CREATE TABLE konto (
  	inhaber VARCHAR(50) NOT NULL,
  	guthaben DECIMAL(10,2) NOT NULL,
 	geheimzahl VARCHAR(10) NOT NULL,
  	PRIMARY KEY (inhaber),
  	FOREIGN KEY (inhaber) REFERENCES benutzer(email) ON UPDATE RESTRICT ON DELETE CASCADE
);

CREATE TABLE kategorie (
  	id SMALLINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  	name VARCHAR(50) NOT NULL,
  	icon VARCHAR(50) NOT NULL,
  	PRIMARY KEY (id)
);

CREATE TABLE projekt (
  	kennung SMALLINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  	titel VARCHAR(50) NOT NULL,
  	beschreibung CLOB (1M),
  	status VARCHAR(11) NOT NULL CHECK (status IN ('offen', 'geschlossen')) DEFAULT 'offen',
  	finanzierungslimit DECIMAL(10,2) NOT NULL,
  	ersteller VARCHAR(50) NOT NULL,
  	vorgaenger SMALLINT,
  	kategorie SMALLINT NOT NULL,
  	PRIMARY KEY (kennung),
  	FOREIGN KEY (ersteller) REFERENCES benutzer(email),
  	FOREIGN KEY (kategorie) REFERENCES kategorie(id),
  	FOREIGN KEY (vorgaenger) REFERENCES projekt(kennung)
);

CREATE TABLE kommentar (
  	id SMALLINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  	text CLOB(1M) NOT NULL,
  	datum timestamp DEFAULT CURRENT TIMESTAMP,
  	sichtbarkeit VARCHAR(11) NOT NULL CHECK (sichtbarkeit IN ('oeffentlich', 'privat')),
  	PRIMARY KEY (id)
);

CREATE TABLE spenden (
  	spender VARCHAR(50) NOT NULL,
  	projekt SMALLINT NOT NULL,
  	spendenbetrag DECIMAL(10,2) NOT NULL,
  	sichtbarkeit VARCHAR(11) NOT NULL CHECK (sichtbarkeit IN ('oeffentlich', 'privat')),
  	PRIMARY KEY (spender, projekt),
  	FOREIGN KEY (spender) REFERENCES benutzer(email),
  	FOREIGN KEY (projekt) REFERENCES projekt(kennung)
);

CREATE TABLE schreibt (
  	benutzer VARCHAR(50) NOT NULL,
  	projekt SMALLINT NOT NULL,
  	kommentar SMALLINT NOT NULL,
  	PRIMARY KEY (benutzer, projekt, kommentar),
  	FOREIGN KEY (benutzer) REFERENCES benutzer(email),
  	FOREIGN KEY (projekt) REFERENCES projekt(kennung),
  	FOREIGN KEY (kommentar) REFERENCES kommentar(id)
);

