CREATE TABLE ristorante(
	id_ristorante SERIAL PRIMARY KEY,
	nome VARCHAR(30) NOT NULL,
	telefono VARCHAR(30),
	indirizzo VARCHAR(50),
	logo bytea,
	nome_immagine VARCHAR(70),
	id_proprietario INTEGER
);

CREATE TYPE role AS ENUM ('admin', 'supervisore', 'addetto_sala', 'addetto_cucina');

CREATE TABLE utente(
	id_utente SERIAL PRIMARY KEY,
	nome VARCHAR(30) NOT NULL,
	cognome VARCHAR(30) NOT NULL,
	data_nascita DATE NOT NULL,
	email VARCHAR(50) NOT NULL UNIQUE,
	password VARCHAR(100) NOT NULL,
	ruolo ROLE NOT NULL,
	isFirstAccess BOOLEAN NOT NULL,
	aggiunto_da Integer,
	data_aggiunta DATE,
	id_ristorante INTEGER NOT NULL,
	FOREIGN KEY(id_ristorante) REFERENCES ristorante(id_ristorante)
);

CREATE TABLE avviso(
	id_avviso SERIAL PRIMARY KEY,
	id_utente INTEGER NOT NULL,
	id_ristorante INTEGER NOT NULL,
	testo VARCHAR(200) NOT NULL,
	data_ora TIMESTAMP NOT NULL,
	FOREIGN KEY(id_utente) REFERENCES utente(id_utente),
	FOREIGN KEY(id_ristorante) REFERENCES ristorante(id_ristorante)
);

CREATE TABLE cronologia_lettura_avviso(
	id_utente INTEGER NOT NULL,
	id_avviso INTEGER NOT NULL,
	data_lettura DATE NOT NULL,
	FOREIGN KEY(id_avviso) REFERENCES avviso(id_avviso),
	FOREIGN KEY(id_utente) REFERENCES utente(id_utente)
);

CREATE TABLE cronologia_nascosti_avviso(
	id_utente INTEGER NOT NULL,
	id_avviso INTEGER NOT NULL,
	data_nascosto DATE NOT NULL,
	FOREIGN KEY(id_avviso) REFERENCES avviso(id_avviso),
	FOREIGN KEY(id_utente) REFERENCES utente(id_utente)
);


CREATE TYPE categoria_p AS ENUM ('frutta', 'verdura', 'carne', 'pesce', 'uova', 'latte_e_derivati', 'cereali_e_derivati', 'legumi', 'altro');

CREATE TYPE unita_di_misura AS ENUM ('kg', 'lt');


CREATE TABLE prodotto(
	id_prodotto SERIAL PRIMARY KEY,
	id_ristorante INTEGER NOT NULL,
	nome VARCHAR(100) NOT NULL,
	stato INTEGER NOT NULL,
	descrizione VARCHAR(1000) NOT NULL,
	prezzo REAL NOT NULL,
	quantita REAL NOT NULL,
	unita_misura UNITA_DI_MISURA,
	categoria_prodotto CATEGORIA_P NOT NULL,
	FOREIGN KEY(id_ristorante) REFERENCES ristorante(id_ristorante)
);

CREATE TABLE categorie_menu(
	id_categoria SERIAL PRIMARY KEY,
	id_ristorante INTEGER NOT NULL,
	nome VARCHAR(30) NOT NULL,
	posizione_categoria INTEGER,
	UNIQUE(id_categoria, id_ristorante),
	FOREIGN KEY(id_ristorante) REFERENCES ristorante(id_ristorante)
);

CREATE TABLE elementi_menu(
	id_elemento SERIAL PRIMARY KEY,
	id_ristorante INTEGER NOT NULL,
	id_categoria INTEGER NOT NULL,
	nome VARCHAR(100) NOT NULL,
	prezzo REAL NOT NULL,
	descrizione VARCHAR(1000) NOT NULL,
	allergeni VARCHAR(200) NOT NULL,
	nome_seconda_lingua VARCHAR(100),
	descrizione_seconda_lingua VARCHAR(1000),
	posizione_elemento INTEGER,
	UNIQUE(posizione_elemento, id_categoria),
	FOREIGN KEY(id_ristorante) REFERENCES ristorante(id_ristorante),
	FOREIGN KEY(id_categoria) REFERENCES categorie_menu(id_categoria)
);