CREATE TABLE AgenziaImmobiliare (
	NomeAgenzia VARCHAR(255) PRIMARY KEY,
	EmailAgenzia VARCHAR(255) NOT NULL UNIQUE,
	EmailAdmin VARCHAR(255) NOT NULL UNIQUE,
	PasswordAdmin VARCHAR(255) NOT NULL
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

CREATE TABLE Indirizzo(
	IdIndirizzo SERIAL PRIMARY KEY,
   	Cap DECIMAL(5,0) NOT NULL,
	Provincia VARCHAR(255) NOT NULL,
        Citta VARCHAR(255) NOT NULL,
	Via VARCHAR(255) NOT NULL,
	NumeroCivico VARCHAR(10) NOT NULL
 );
 
 CREATE TABLE Immobile(
         IdImmobile SERIAL PRIMARY KEY,
	 IdIndirizzo INT,
	 NomeAI VARCHAR(255),
	 
	 Prezzo DECIMAL(10,2) NOT NULL,
	 TipoVendita VARCHAR(255) NOT NULL,
	 Superfice INT NOT NULL,
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
 FOREIGN KEY(IdIndirizzo) REFERENCES Indirizzo(IdIndirizzio),
 FOREIGN KEY(NomeAgente) REFERENCES AgenteImmobiliare(EmailAgente)

 );
 
 CREATE TABLE Offerta(
	 IdImmobile  INT,
	 NomeCliente VARCHAR(255),
	 PrezzoOfferta DECIMAL(10,2) NOT NULL,
	 DataOfferta DATE NOT NULL,
	 EsitoOfferta BOOLEAN,
 PRIMARY KEY (IdImmobile, NomeCliente),
 FOREIGN KEY(IdImmobile) REFERENCES Immobile(IdImmobile),
 FOREIGN KEY(NomeCliente) REFERENCES Cliente(EmailCliente) ON DELETE CASCADE
 
 );
 
 CREATE TABLE Visita(
	 IdImmobile  INT,
	 NomeCliente VARCHAR(255),
	 OraVisita TIME NOT NULL,
	 DataVisita DATE NOT NULL,
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
	

	--Indirizzo
		--Possibili Constrains per limitare errori con le API da valutare 

                ALTER TABLE Indirizzo ADD CONSTRAINT NumeroCivico
                CHECK(Indirizzo.NumeroCivico > 0);
		
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
                CHECK(Offerta.NumStanze > 0);
 
         --Visita
                ALTER TABLE Visita ADD CONSTRAINT DataValida
                CHECK(Visita.DataVisita > CURRENT_DATE);
	
--Trigger:

CREATE OR REPLACE FUNCTION CheckPasswordGeneric(
    table_name TEXT,  -- Nome della tabella
    column_name TEXT  -- Nome della colonna che contiene la password
) RETURNS TRIGGER AS $$
DECLARE
    password_value TEXT;
BEGIN
    -- Ottiene dinamicamente il valore della password
    EXECUTE format('SELECT %I FROM %I WHERE id = $1', column_name, table_name)
    INTO password_value
    USING NEW.id;
    
    -- Controlla la password con espressione regolare
    IF password_value !~ '^(?=.*\d)(?=.*[\W_]).{8,}$' THEN
        RAISE EXCEPTION 'La password deve essere lunga almeno 8 caratteri, contenere almeno un numero e un carattere speciale.';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


--Agenzia Immobiliare

CREATE TRIGGER AgenziaImmobiliarePasswordTrigger
BEFORE INSERT OR UPDATE ON AgenziaImmobiliare
FOR EACH ROW
EXECUTE FUNCTION CheckPasswordGeneric('AgenziaImmobiliare', 'PasswordAdmin');

--Cliente

CREATE TRIGGER ClientePasswordTrigger
BEFORE INSERT OR UPDATE ON Cliente
FOR EACH ROW
EXECUTE FUNCTION CheckPasswordGeneric('Cliente', 'PasswordCliente');

--Agente Immobiliare

CREATE TRIGGER AgenteImmobiliarePasswordTrigger
BEFORE INSERT OR UPDATE ON AgenteImmobiliare
FOR EACH ROW
EXECUTE FUNCTION CheckPasswordGeneric('AgenteImmobiliare', 'PasswordAgente');

--Admin Support

CREATE TRIGGER AdminSupportPasswordTrigger
BEFORE INSERT OR UPDATE ON AdminSupport
FOR EACH ROW
EXECUTE FUNCTION CheckPasswordGeneric('AdminSupport', 'PasswordSupport');


--Trigger Cancellazione Offerte e Visite in caso di Venduto = TRUE:

CREATE OR REPLACE FUNCTION EliminaOfferteVisite()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.Venduto = TRUE AND OLD.Venduto = FALSE THEN
        DELETE FROM offerte WHERE IdImmobile = NEW.IdImmobile;
        DELETE FROM visite WHERE IdImmobile = NEW.IdImmobile;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER TriggerEliminaOfferteVisite
BEFORE UPDATE ON Immobile
FOR EACH ROW
EXECUTE FUNCTION EliminaOfferteVisite();
