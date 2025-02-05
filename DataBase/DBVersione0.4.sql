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
	PasswordSupPort VARCHAR(255) NOT NULL UNIQUE,
	NomeAgenzia VARCHAR(255),
    FOREIGN KEY(NomeAgenzia) REFERENCES AgenziaImmobiliare(NomeAgenzia)
	
);

CREATE TABLE Indirizzo(
	IdIndirizzio SERIAL PRIMARY KEY,
   	Cap  INT NOT NULL,
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
	 Vendutto BOOLEAN DEFAULT FALSE,
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
 FOREIGN KEY(NomeCliente) REFERENCES Cliente(EmailCliente)
 
 );
 
 CREATE TABLE Visita(
	 IdImmobile  INT,
	 NomeCliente VARCHAR(255),
	 OraVisita TIME NOT NULL,
	 DataVisita DATE NOT NULL,
 PRIMARY KEY (IdImmobile, NomeCliente),
 FOREIGN KEY(IdImmobile) REFERENCES Immobile(IdImmobile),
 FOREIGN KEY(NomeCliente) REFERENCES Cliente(EmailCliente)

 );
 
 --Constreins
 	--Cliente
 
ALTER TABLE Cliente ADD CONSTRAINT EmailClienteValida 
CHECK(Cliente.EmailCliente LIKE '_%@%.__%');

 ALTER TABLE Cliente ADD CONSTRAINT DataNascita
CHECK (
    	Cliente.DataN <= CURRENT_DATE - INTERVAL '18 years'
    AND EXTRACT(YEAR FROM Cliente.DataN) >= 1900);
	
		-- Password Constrains ??

	-- AgenziaImmobiliare
	ALTER TABLE AgenziaImmobiliare ADD CONSTRAINT EmailAgenziaImmobiliare
	CHECK(AgenziaImmobiliare.EmailAgenzia LIKE '_%@%.__%');
	ALTER TABLE AgenziaImmobiliare ADD CONSTRAINT EmailAdmin
	CHECK(AgenziaImmobiliare.EmailAdmin LIKE '_%@%.__%');


		-- Password Constrains ??
		
	--AdminSupport
	ALTER TABLE AdminSupport ADD CONSTRAINT EmailAdminSupport 
	CHECK(AdminSupport.EmailSupport LIKE '_%@%.__%');
	
	--AgenteImmobiliare
	
	ALTER TABLE AgenteImmobiliare ADD CONSTRAINT EmailAgenteImmobiliareValida 
	CHECK(AgenteImmobiliare.EmailAgente LIKE '_%@%.__%');
	
		-- Password Constrains ??
	--Indirizzo
		--Possibili Constrains per limitare errori con le API da valutare 
		
	--Immobile
		--il boss
		ALTER TABLE Immobile ADD CONSTRAINT PrezzoImmobile
		CHECK(Immobile.Prezzo > 0);
	
