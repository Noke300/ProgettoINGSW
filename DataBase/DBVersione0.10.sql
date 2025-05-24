CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE AgenziaImmobiliare (
	NomeAgenzia VARCHAR(255) PRIMARY KEY,
	EmailAgenzia VARCHAR(255) NOT NULL UNIQUE,
	EmailAdmin VARCHAR(255) NOT NULL UNIQUE,
	PasswordAdmin VARCHAR(255) NOT NULL,
	Regione VARCHAR(255) NOT NULL
);

CREATE TABLE Cliente (
	Nome VARCHAR(255) NOT NULL,
	Cognome VARCHAR(255) NOT NULL,
	EmailCliente VARCHAR(255) PRIMARY KEY,
	PasswordCliente VARCHAR(255) NOT NULL,
	DataN DATE NOT NULL
);

CREATE TABLE AgenteImmobiliare(
	NomeAgente VARCHAR(255) NOT NULL,
	CognomeAgente VARCHAR(255) NOT NULL,
	EmailAgente VARCHAR(255) PRIMARY KEY,
	PasswordAgente VARCHAR(255) NOT NULL UNIQUE,
	NomeAgenzia VARCHAR(255),
FOREIGN KEY(NomeAgenzia) REFERENCES AgenziaImmobiliare(NomeAgenzia)

);

CREATE TABLE AdminSupport(
	NomeSupport VARCHAR(255) NOT NULL,
	CognomeSupport VARCHAR(255) NOT NULL,
	EmailSupport VARCHAR(255) PRIMARY KEY,
	PasswordSupport VARCHAR(255) NOT NULL UNIQUE,
	NomeAgenzia VARCHAR(255),
    FOREIGN KEY(NomeAgenzia) REFERENCES AgenziaImmobiliare(NomeAgenzia)
	
);

CREATE TABLE Indirizzo (
    IdIndirizzo UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    Via TEXT NOT NULL,
    Citta TEXT NOT NULL,
    Latitudine DOUBLE PRECISION NOT NULL,
    Longitudine DOUBLE PRECISION NOT NULL,
    Provincia TEXT NOT NULL,
    NumeroCivico INTEGER NOT NULL,
    Cap INTEGER NOT NULL,
    Regione TEXT NOT NULL
);

CREATE TABLE Immobile (
    IdImmobile UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	
	IdIndirizzo UUID,
    NomeAgente VARCHAR(255),
    NomeAgenzia VARCHAR(255) NOT NULL,
    Prezzo DECIMAL(10,2) NOT NULL,
    TipoVendita VARCHAR(255) NOT NULL,
    Superficie INT NOT NULL,
    TipoImmobile VARCHAR(255) NOT NULL,
    NumBagni INT NOT NULL,
    ClasseEnergetica VARCHAR(10) NOT NULL,
    NumStanze INT NOT NULL,
    Piano INT NOT NULL,
    Ascensore BOOLEAN,
    Balcone BOOLEAN,
    Cantina BOOLEAN,
    Portineria BOOLEAN,
    Climatizzatione BOOLEAN,
    Venduto BOOLEAN DEFAULT FALSE,
	
	FOREIGN KEY (IdIndirizzo) REFERENCES Indirizzo(IdIndirizzo),
    FOREIGN KEY (NomeAgente) REFERENCES AgenteImmobiliare(EmailAgente) ON DELETE SET NULL,
    FOREIGN KEY (NomeAgenzia) REFERENCES AgenziaImmobiliare(NomeAgenzia)
);

CREATE TABLE Offerta (
    IdOfferta UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    IdImmobile UUID,
    NomeCliente VARCHAR(255),
    PrezzoOfferta DECIMAL(10,2) NOT NULL,
    DataOfferta DATE NOT NULL,
    EsitoOfferta BOOLEAN,
    FOREIGN KEY (IdImmobile) REFERENCES Immobile(IdImmobile),
    FOREIGN KEY (NomeCliente) REFERENCES Cliente(EmailCliente) ON DELETE CASCADE
);

CREATE TABLE Visita (
    IdImmobile UUID,
    NomeCliente VARCHAR(255),
    OraVisita TIME NOT NULL,
    DataVisita DATE NOT NULL,
    Conferma BOOLEAN,
    PRIMARY KEY (IdImmobile, NomeCliente),
    FOREIGN KEY (IdImmobile) REFERENCES Immobile(IdImmobile),
    FOREIGN KEY (NomeCliente) REFERENCES Cliente(EmailCliente) ON DELETE CASCADE
);
 
 --Constreins
 	--Cliente

ALTER TABLE Cliente ADD CONSTRAINT EmailClienteValida 
CHECK(Cliente.EmailCliente LIKE '_%@%.__%');

 ALTER TABLE Cliente ADD CONSTRAINT DataNascita
CHECK (
    	Cliente.DataN <= CURRENT_DATE - INTERVAL '18 years'
    AND EXTRACT(YEAR FROM Cliente.DataN) >= 1900);

	-- AgenziaImmobiliare
	ALTER TABLE AgenziaImmobiliare ADD CONSTRAINT EmailAgenziaImmobiliare
	CHECK(AgenziaImmobiliare.EmailAgenzia LIKE '_%@%.__%');

	ALTER TABLE AgenziaImmobiliare ADD CONSTRAINT EmailAdmin
	CHECK(AgenziaImmobiliare.EmailAdmin LIKE '_%@%.__%');

		
	--AdminSupport
	ALTER TABLE AdminSupport ADD CONSTRAINT EmailAdminSupport 
	CHECK(AdminSupport.EmailSupport LIKE '_%@%.__%');
	
	--AgenteImmobiliare
	
	ALTER TABLE AgenteImmobiliare ADD CONSTRAINT EmailAgenteImmobiliareValida 
	CHECK(AgenteImmobiliare.EmailAgente LIKE '_%@%.__%');
	

	--Immobile

		ALTER TABLE Immobile ADD CONSTRAINT PrezzoImmobile
		CHECK(Immobile.Prezzo > 0);
 
                ALTER TABLE Immobile ADD CONSTRAINT SuperficieImmobile
                CHECK(Immobile.Superficie > 0);
               
                ALTER TABLE Immobile ADD CONSTRAINT NumeroBagni
                CHECK(Immobile.NumBagni >= 0);

                ALTER TABLE Immobile ADD CONSTRAINT NumeroStanze
                CHECK(Immobile.NumStanze > 0);
 
         --Offerta
                ALTER TABLE Offerta ADD CONSTRAINT PrezzoOfferta
                CHECK(Offerta.PrezzoOfferta > 0);
 
         --Visita
                ALTER TABLE Visita ADD CONSTRAINT DataValida
                CHECK(Visita.DataVisita > CURRENT_DATE);
	
--Trigger:


--Agenzia Immobiliare


--Cliente


--Agente Immobiliare

--Admin Support


--Trigger Cancellazione Offerte e Visita in caso di Venduto = TRUE:

CREATE OR REPLACE FUNCTION EliminaOfferteVisita()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.Venduto = TRUE AND OLD.Venduto = FALSE THEN
        DELETE FROM offerta WHERE IdImmobile = NEW.IdImmobile;
        DELETE FROM visita WHERE IdImmobile = NEW.IdImmobile;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER TriggerEliminaOfferteVisita
BEFORE UPDATE ON Immobile
FOR EACH ROW
EXECUTE FUNCTION EliminaOfferteVisita();


--Insert

-- Inserimento dati nella tabella AgenziaImmobiliare
INSERT INTO AgenziaImmobiliare (NomeAgenzia, EmailAgenzia, EmailAdmin, PasswordAdmin, Regione) VALUES
('CasaFacile', 'info@casafacile.it', 'admin@casafacile.it', 'pass1234!', 'Lombardia'),
('ImmobiliareRoma', 'info@immobiliareroma.it', 'admin@immobiliareroma.it', 'pass5678!', 'Lazio'),
('MilanCase', 'info@milancase.it', 'admin@milancase.it', 'pass91011!', 'Lombardia'),
('ViviCasa', 'info@vivicasa.it', 'admin@vivicasa.it', 'pass1213!', 'Veneto'),
('TorinoHome', 'info@torinohome.it', 'admin@torinohome.it', 'pass1415!', 'Piemonte');

-- Inserimento dati nella tabella Cliente
INSERT INTO Cliente (Nome, Cognome, EmailCliente, PasswordCliente, DataN) VALUES
('Mario', 'Rossi', 'mario.rossi@email.com', '$2a$12$Am3HxCFrTnstR6q0yOA9K.254UR2pBCcaWiomKv9EjO5m4NoV1wuO', '1985-06-15'),
('Luca', 'Bianchi', 'luca.bianchi@email.com', 'lucapass5!', '1990-09-22'),
('Giulia', 'Verdi', 'giulia.verdi@email.com', 'giuliapass6!', '1987-04-10'),
('Anna', 'Neri', 'anna.neri@email.com', 'annapass7!', '1995-11-30'),
('Marco', 'Gialli', 'marco.gialli@email.com', 'marcopass8!', '1982-02-18');

-- Inserimento dati nella tabella AgenteImmobiliare
INSERT INTO AgenteImmobiliare (NomeAgente, CognomeAgente, EmailAgente, PasswordAgente, NomeAgenzia) VALUES
('Giovanni', 'Moretti', 'giovanni.moretti@email.com', 'agente1231!', 'CasaFacile'),
('Elisa', 'Ferrari', 'elisa.ferrari@email.com', 'agente456!', 'ImmobiliareRoma'),
('Paolo', 'Ricci', 'paolo.ricci@email.com', 'agente789!', 'MilanCase'),
('Martina', 'De Luca', 'martina.deluca@email.com', 'agente101!', 'ViviCasa'),
('Prova', 'Prova', 'prova@gmail.com', '$2a$12$lUaRC1xdQN8wTamPTVfDQeFLnweub8LWPf6GbNUZ6dqNd0SR.jaa6', 'CasaFacile'),
('Andrea', 'Conti', 'andrea.conti@email.com', 'agente102!', 'TorinoHome');

-- Inserimento dati nella tabella AdminSupport
INSERT INTO AdminSupport (NomeSupport, CognomeSupport, EmailSupport, PasswordSupport, NomeAgenzia) VALUES
('Francesco', 'Bianchi', 'support.bianchi@email.com', 'support123!', 'CasaFacile'),
('Sara', 'Galli', 'support.galli@email.com', 'support456!', 'ImmobiliareRoma'),
('Davide', 'Rizzo', 'support.rizzo@email.com', 'support789!', 'MilanCase'),
('Lucia', 'Marini', 'support.marini@email.com', 'support101!', 'ViviCasa'),
('Roberto', 'Serra', 'support.serra@email.com', 'support102!', 'TorinoHome');

--Inserimento dati nella tabella Indirizzo
INSERT INTO Indirizzo (via, citta, latitudine, longitudine, provincia, NumeroCivico, cap, regione) VALUES
('Via Roma', 'Torino', 45.0703, 7.6869, 'TO', 12, 10121, 'Piemonte'),
('Corso Italia', 'Milano', 45.4642, 9.1900, 'MI', 25, 20122, 'Lombardia'),
('Via Mazzini', 'Firenze', 43.7696, 11.2558, 'FI', 5, 50122, 'Toscana'),
('Via Garibaldi', 'Genova', 44.4056, 8.9463, 'GE', 18, 16124, 'Liguria'),
('Via Dante', 'Palermo', 38.1157, 13.3615, 'PA', 30, 90141, 'Sicilia');


WITH new_indirizzo AS (
    INSERT INTO Indirizzo (Via, Citta, Latitudine, Longitudine, Provincia, NumeroCivico, Cap, Regione)
    VALUES ('Via Roma', 'Torino', 0.0, 0.0, '', 12, 00000, '')
    RETURNING IdIndirizzo
),
new_immobile AS (
    INSERT INTO Immobile (
        IdIndirizzo, NomeAgente, NomeAgenzia, Prezzo, TipoVendita,
        TipoImmobile, Superficie, NumBagni, ClasseEnergetica, NumStanze,
        Piano, Ascensore, Balcone, Cantina, Portineria, Climatizzatione, Venduto
    )
    SELECT IdIndirizzo, 'giovanni.moretti@email.com', 'CasaFacile', 250000, 'Vendita',
           'Tipo', 100, 2, 'A', 4, 1, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE
    FROM new_indirizzo
    RETURNING IdImmobile
),
insert_offerta AS (
    INSERT INTO Offerta (IdImmobile, NomeCliente, PrezzoOfferta, DataOfferta, EsitoOfferta)
    SELECT IdImmobile, 'mario.rossi@email.com', 245000, '2024-01-11', TRUE
    FROM new_immobile
)
INSERT INTO Visita (IdImmobile, NomeCliente, OraVisita, DataVisita, Conferma)
SELECT IdImmobile, 'mario.rossi@email.com', '10:00', '2025-12-01', TRUE
FROM new_immobile;

-- Inserimento indirizzo Milano
WITH new_indirizzo AS (
    INSERT INTO Indirizzo (Via, Citta, Latitudine, Longitudine, Provincia, NumeroCivico, Cap, Regione)
    VALUES ('Corso Italia', 'Milano', 0.0, 0.0, '', 25, 00000, '')
    RETURNING IdIndirizzo
),
new_immobile AS (
    INSERT INTO Immobile (
        IdIndirizzo, NomeAgente, NomeAgenzia, Prezzo, TipoVendita,
        TipoImmobile, Superficie, NumBagni, ClasseEnergetica, NumStanze,
        Piano, Ascensore, Balcone, Cantina, Portineria, Climatizzatione, Venduto
    )
    SELECT IdIndirizzo, 'prova@gmail.com', 'ImmobiliareRoma', 320000, 'Vendita',
           'Tipo', 100, 2, 'A', 4, 1, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE
    FROM new_indirizzo
    RETURNING IdImmobile
),
insert_offerta AS (
    INSERT INTO Offerta (IdImmobile, NomeCliente, PrezzoOfferta, DataOfferta, EsitoOfferta)
    SELECT IdImmobile, 'luca.bianchi@email.com', 315000, '2024-01-12', FALSE
    FROM new_immobile
)
INSERT INTO Visita (IdImmobile, NomeCliente, OraVisita, DataVisita, Conferma)
SELECT IdImmobile, 'luca.bianchi@email.com', '10:00', '2025-12-02', FALSE
FROM new_immobile;

-- Inserimento indirizzo Firenze
WITH new_indirizzo AS (
    INSERT INTO Indirizzo (Via, Citta, Latitudine, Longitudine, Provincia, NumeroCivico, Cap, Regione)
    VALUES ('Via Mazzini', 'Firenze', 0.0, 0.0, '', 5, 00000, '')
    RETURNING IdIndirizzo
),
new_immobile AS (
    INSERT INTO Immobile (
        IdIndirizzo, NomeAgente, NomeAgenzia, Prezzo, TipoVendita,
        TipoImmobile, Superficie, NumBagni, ClasseEnergetica, NumStanze,
        Piano, Ascensore, Balcone, Cantina, Portineria, Climatizzatione, Venduto
    )
    SELECT IdIndirizzo, 'prova@gmail.com', 'MilanCase', 180000, 'Vendita',
           'Tipo', 100, 2, 'A', 4, 1, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE
    FROM new_indirizzo
    RETURNING IdImmobile
),
insert_offerta AS (
    INSERT INTO Offerta (IdImmobile, NomeCliente, PrezzoOfferta, DataOfferta, EsitoOfferta)
    SELECT IdImmobile, 'giulia.verdi@email.com', 175000, '2024-01-13', TRUE
    FROM new_immobile
)
INSERT INTO Visita (IdImmobile, NomeCliente, OraVisita, DataVisita, Conferma)
SELECT IdImmobile, 'giulia.verdi@email.com', '10:00', '2025-12-03', TRUE
FROM new_immobile;

-- Inserimento indirizzo Genova
WITH new_indirizzo AS (
    INSERT INTO Indirizzo (Via, Citta, Latitudine, Longitudine, Provincia, NumeroCivico, Cap, Regione)
    VALUES ('Via Garibaldi', 'Genova', 0.0, 0.0, '', 18, 00000, '')
    RETURNING IdIndirizzo
),
new_immobile AS (
    INSERT INTO Immobile (
        IdIndirizzo, NomeAgente, NomeAgenzia, Prezzo, TipoVendita,
        TipoImmobile, Superficie, NumBagni, ClasseEnergetica, NumStanze,
        Piano, Ascensore, Balcone, Cantina, Portineria, Climatizzatione, Venduto
    )
    SELECT IdIndirizzo, 'martina.deluca@email.com', 'ViviCasa', 275000, 'Vendita',
           'Tipo', 100, 2, 'A', 4, 1, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE
    FROM new_indirizzo
    RETURNING IdImmobile
),
insert_offerta AS (
    INSERT INTO Offerta (IdImmobile, NomeCliente, PrezzoOfferta, DataOfferta, EsitoOfferta)
    SELECT IdImmobile, 'anna.neri@email.com', 270000, '2024-01-14', FALSE
    FROM new_immobile
)
INSERT INTO Visita (IdImmobile, NomeCliente, OraVisita, DataVisita, Conferma)
SELECT IdImmobile, 'anna.neri@email.com', '10:00', '2025-12-04', FALSE
FROM new_immobile;

-- Inserimento indirizzo Palermo
WITH new_indirizzo AS (
    INSERT INTO Indirizzo (Via, Citta, Latitudine, Longitudine, Provincia, NumeroCivico, Cap, Regione)
    VALUES ('Via Dante', 'Palermo', 0.0, 0.0, '', 30, 00000, '')
    RETURNING IdIndirizzo
),
new_immobile AS (
    INSERT INTO Immobile (
        IdIndirizzo, NomeAgente, NomeAgenzia, Prezzo, TipoVendita,
        TipoImmobile, Superficie, NumBagni, ClasseEnergetica, NumStanze,
        Piano, Ascensore, Balcone, Cantina, Portineria, Climatizzatione, Venduto
    )
    SELECT IdIndirizzo, 'andrea.conti@email.com', 'TorinoHome', 220000, 'Vendita',
           'Tipo', 100, 2, 'A', 4, 1, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE
    FROM new_indirizzo
    RETURNING IdImmobile
),
insert_offerta AS (
    INSERT INTO Offerta (IdImmobile, NomeCliente, PrezzoOfferta, DataOfferta, EsitoOfferta)
    SELECT IdImmobile, 'marco.gialli@email.com', 215000, '2024-01-15', TRUE
    FROM new_immobile
)
INSERT INTO Visita (IdImmobile, NomeCliente, OraVisita, DataVisita, Conferma)
SELECT IdImmobile, 'marco.gialli@email.com', '10:00', '2025-12-05', TRUE
FROM new_immobile;


