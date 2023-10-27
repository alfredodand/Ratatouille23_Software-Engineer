CREATE TABLE attivita(
	id_attivita SERIAL PRIMARY KEY,
	nome varchar(20) NOT NULL,
	telefono varchar(20) NOT NULL UNIQUE,
	indirizzo varchar(20) UNIQUE,
	logo bytea,
	img_name varchar(20)

);

CREATE TABLE utente(
	id_utente SERIAL PRIMARY KEY,
	nome varchar(20) NOT NULL,
	cognome varchar(20) NOT NULL,
	email varchar(30) NOT NULL UNIQUE,
	password varchar(30) NOT NULL,
	telefono varchar(20) NOT NULL UNIQUE,
	ruolo varchar(20) NOT NULL,
	data_nascita varchar(20) NOT NULL,
	isFirstAccess boolean NOT NULL,
	aggiuntoda integer,
	data_aggiunta varchar(20) NOT NULL,
	id_attivita integer,
	FOREIGN KEY (id_attivita) REFERENCES attivita (id_attivita)
);
	
CREATE TABLE dispensa(
	id_dispensa SERIAL PRIMARY KEY,
	nome varchar(20) NOT NULL,
	stato integer NOT NULL,
	id_utente integer,
	FOREIGN KEY (id_utente) REFERENCES utente (id_utente)
);

CREATE TYPE categoria_prodotti AS ENUM (
	'frutta', 
	'verdura', 
	'carne',
	'pesce',
	'uova',
	'latte e derivati',
	'legumi',
	'cereali e derivati',
	'condimento',
	'altro'
);

CREATE TABLE prodotto(
	id_prodotto SERIAL PRIMARY KEY,
	nome varchar(20) NOT NULL,
	descrizione varchar(100),
	quantita integer NOT NULL,
	prezzo real NOT NULL,
	categoria CATEGORIA_PRODOTTI NOT NULL,
	id_dispensa integer,
	FOREIGN KEY (id_dispensa) REFERENCES dispensa (id_dispensa)
);

CREATE TABLE avviso (
    	id_avviso SERIAL PRIMARY KEY,
    	testo varchar(300) NOT NULL,
    	autore varchar(30) NOT NULL,
    	data_ora timestamp NOT NULL
);

CREATE TABLE nascosto_visualizzato (
    	id_avviso integer,
    	id_user_who_hidden integer,
    	id_user_who_viewed int,
   	FOREIGN KEY (id_avviso) REFERENCES avviso(id_avviso),
   	FOREIGN KEY (id_user_who_hidden) REFERENCES utente(id_utente),
   	FOREIGN KEY (id_user_who_viewed) REFERENCES utente(id_utente)
);

CREATE TABLE categoria (
   	id_categoria serial PRIMARY KEY,
  	nome varchar(15) NOT NULL
);

CREATE TABLE piatto (
	id_piatto SERIAL PRIMARY KEY,
	nome varchar(30) NOT NULL,
	descrizione varchar(100),
	costo real NOT NULL,
	allergeni varchar(100),
	id_categoria integer NOT NULL,
	name varchar(30),
 	description varchar(100),
    	allergens varchar(100),
    	FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

CREATE TABLE menu (
	id_attivita integer,
    	id_menu integer,
   	id_categoria integer,
   	id_piatto integer,
   	FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),
	FOREIGN KEY (id_attivita) REFERENCES attivita(id_attivita),
   	FOREIGN KEY (id_piatto) REFERENCES piatto(id_piatto)
);