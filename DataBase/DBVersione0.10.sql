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
    id SERIAL PRIMARY KEY,
    via TEXT NOT NULL,
    citta TEXT NOT NULL,
	Latitudine DECIMAL(9,6),
	Longitudine DECIMAL(9,6),
    provincia TEXT NOT NULL,
    numero_civico INTEGER NOT NULL,
    cap INTEGER NOT NULL,
    regione TEXT NOT NULL
);
 
 CREATE TABLE Immobile(
         IdImmobile SERIAL PRIMARY KEY,
	 NomeAgente VARCHAR(255),
         NomeAgenzia VARCHAR(255) NOT NULL,

	 
	 Prezzo DECIMAL(10,2) NOT NULL,
	 TipoVendita VARCHAR(255) NOT NULL,
	 Superficie INT NOT NULL,
	 TipoImmobile VARCHAR(255) NOT NULL,
	 NumBagni INT NOT NULL,
	 ClasseEnergetica VARCHAR(10) NOT NULL,
	 NumStanze INT NOT NULL,
	 Piano BOOLEAN,
	 Ascensore BOOLEAN,
	 Balcone BOOLEAN,
	 Cantina BOOLEAN,
	 Portineria BOOLEAN,
	 Climatizzatione BOOLEAN,
	 Venduto BOOLEAN DEFAULT FALSE,

 FOREIGN KEY(NomeAgente) REFERENCES AgenteImmobiliare(EmailAgente) ON DELETE SET NULL,
 FOREIGN KEY (NomeAgenzia) REFERENCES AgenziaImmobiliare(NomeAgenzia)

 );
 
 CREATE TABLE Offerta(
	 IdOfferta SERIAL PRIMARY KEY,
	 IdImmobile  INT,
	 NomeCliente VARCHAR(255),
	 PrezzoOfferta DECIMAL(10,2) NOT NULL,
	 DataOfferta DATE NOT NULL,
	 EsitoOfferta BOOLEAN,
 FOREIGN KEY(IdImmobile) REFERENCES Immobile(IdImmobile),
 FOREIGN KEY(NomeCliente) REFERENCES Cliente(EmailCliente) ON DELETE CASCADE
 
 );
 
 CREATE TABLE Visita(
	 IdImmobile  INT,
	 NomeCliente VARCHAR(255),
	 OraVisita TIME NOT NULL,
	 DataVisita DATE NOT NULL,
	 Conferma BOOLEAN,
 PRIMARY KEY (IdImmobile, NomeCliente),
 FOREIGN KEY(IdImmobile) REFERENCES Immobile(IdImmobile),
 FOREIGN KEY(NomeCliente) REFERENCES Cliente(EmailCliente) ON DELETE CASCADE

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

CREATE OR REPLACE FUNCTION CheckPasswordAgenziaImmobiliare()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.PasswordAdmin !~ '^(?=.*\d)(?=.*[\W_]).{8,}$' THEN
        RAISE EXCEPTION 'La password deve essere lunga almeno 8 caratteri, contenere almeno un numero e un carattere speciale.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



CREATE TRIGGER AgenziaImmobiliarePasswordTrigger
BEFORE INSERT OR UPDATE ON AgenziaImmobiliare
FOR EACH ROW
EXECUTE FUNCTION CheckPasswordAgenziaImmobiliare();

--Cliente

CREATE OR REPLACE FUNCTION CheckPasswordCliente()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.PasswordCliente !~ '^(?=.*\d)(?=.*[\W_]).{8,}$' THEN
        RAISE EXCEPTION 'La password deve essere lunga almeno 8 caratteri, contenere almeno un numero e un carattere speciale.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER ClientePasswordTrigger
BEFORE INSERT OR UPDATE ON Cliente
FOR EACH ROW
EXECUTE FUNCTION CheckPasswordCliente();

--Agente Immobiliare

CREATE OR REPLACE FUNCTION CheckPasswordAgenteImmobiliare()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.PasswordAgente !~ '^(?=.*\d)(?=.*[\W_]).{8,}$' THEN
        RAISE EXCEPTION 'La password deve essere lunga almeno 8 caratteri, contenere almeno un numero e un carattere speciale.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER AgenteImmobiliarePasswordTrigger
BEFORE INSERT OR UPDATE ON AgenteImmobiliare
FOR EACH ROW
EXECUTE FUNCTION CheckPasswordAgenteImmobiliare();

--Admin Support

CREATE OR REPLACE FUNCTION CheckPasswordAdmin()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.PasswordSupport !~ '^(?=.*\d)(?=.*[\W_]).{8,}$' THEN
        RAISE EXCEPTION 'La password deve essere lunga almeno 8 caratteri, contenere almeno un numero e un carattere speciale.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER AdminSupportPasswordTrigger
BEFORE INSERT OR UPDATE ON AdminSupport
FOR EACH ROW
EXECUTE FUNCTION CheckPasswordAdmin();



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
INSERT INTO Indirizzo (via, citta, latitudine, longitudine, provincia, numero_civico, cap, regione) VALUES
('Via Roma', 'Torino', 45.0703, 7.6869, 'TO', 12, 10121, 'Piemonte'),
('Corso Italia', 'Milano', 45.4642, 9.1900, 'MI', 25, 20122, 'Lombardia'),
('Via Mazzini', 'Firenze', 43.7696, 11.2558, 'FI', 5, 50122, 'Toscana'),
('Via Garibaldi', 'Genova', 44.4056, 8.9463, 'GE', 18, 16124, 'Liguria'),
('Via Dante', 'Palermo', 38.1157, 13.3615, 'PA', 30, 90141, 'Sicilia');

-- Inserimento dati nella tabella Immobile (senza IdImmobile, Latitudine e Longitudine)
INSERT INTO Immobile (NomeAgente, NomeAgenzia, Prezzo, TipoVendita, TipoImmobile, Superficie, NumBagni, ClasseEnergetica, NumStanze, Piano, Ascensore, Balcone, Cantina, Portineria, Climatizzatione, Venduto) VALUES
('giovanni.moretti@email.com', 'CasaFacile', 250000, 'Vendita', 'Appartamento', 80, 2, 'A+', 3, TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE),
('prova@gmail.com', 'ImmobiliareRoma', 320000, 'Vendita', 'Villa', 100, 3, 'A', 5, FALSE, FALSE, TRUE, TRUE, TRUE, TRUE, FALSE),
('prova@gmail.com', 'MilanCase', 180000, 'Affitto', 'Bilocale', 60, 1, 'B', 2, TRUE, TRUE, FALSE, FALSE, FALSE, TRUE, FALSE),
('martina.deluca@email.com', 'ViviCasa', 275000, 'Vendita', 'Attico', 85, 2, 'A+', 4, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, FALSE),
('andrea.conti@email.com', 'TorinoHome', 220000, 'Affitto', 'Trilocale', 75, 1, 'B', 3, FALSE, TRUE, TRUE, FALSE, FALSE, TRUE, FALSE);


-- Inserimento dati nella tabella Offerta
INSERT INTO Offerta (IdImmobile, NomeCliente, PrezzoOfferta, DataOfferta, EsitoOfferta) VALUES
(1, 'mario.rossi@email.com', 245000.00, '2024-01-10', FALSE),
(2, 'luca.bianchi@email.com', 310000.00, '2024-01-12', FALSE),
(3, 'giulia.verdi@email.com', 175000.00, '2024-01-15', TRUE),
(4, 'anna.neri@email.com', 270000.00, '2024-01-18', FALSE),
(5, 'marco.gialli@email.com', 215000.00, '2024-01-20', TRUE);

-- Inserimento dati nella tabella Visita
INSERT INTO Visita (IdImmobile, NomeCliente, OraVisita, DataVisita, Conferma) VALUES
(1, 'mario.rossi@email.com', '10:00', '2025-12-05', TRUE),
(2, 'luca.bianchi@email.com', '14:30', '2025-09-06', FALSE),
(3, 'giulia.verdi@email.com', '16:00', '2025-08-07', TRUE),
(4, 'anna.neri@email.com', '11:00', '2025-07-08', FALSE),
(5, 'marco.gialli@email.com', '13:00', '2025-06-09', TRUE);
