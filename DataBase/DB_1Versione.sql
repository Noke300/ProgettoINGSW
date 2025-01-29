CREATE TABLE AgenziaImmobiliare (
NomeAgenzia VARCHAR(255) PRIMARY KEY,
EmailAgenzia VARCHAR(255) NOT NULL UNIQUE,
EmailAdmin VARCHAR(255) NOT NULL UNIQUE,
PasswordAdmin VARCHAR(255) NOT NULL
);

CREATE TABLE Cliente (
Nome VARCHAR(255) NOT NULL,
EmailCliente VARCHAR(255) PRIMARY KEY,
PasswordCliente VARCHAR(255) NOT NULL,
DataN DATE NOT NULL
);

CREATE TABLE AgenteImmobiliare(
NomeAI VARCHAR(255),
EmailAI VARCHAR(255) PRIMARY KEY,
PasswordAI VARCHAR(255) NOT NULL UNIQUE,
CompagniaAI VARCHAR(255),
NomeAgenzia VARCHAR(255),
FOREIGN KEY(NomeAgenzia) REFERENCES AgenziaImmobiliare(NomeAgenzia)

);

CREATE TABLE Indirizzo(
	IdIndirizzio SERIAL PRIMARY KEY,
    Cap  INT NOT NULL,
	Provincia VARCHAR(255) NOT NULL,
    Citta VARCHAR(255) NOT NULL,
	Via VARCHAR(255) NULL NULL,
	NumeroCivico VARCHAR(10) NULL NULL
 );
 
 CREATE TABLE Immobile(
 IdImmobile SERIAL PRIMARY KEY,
 IdIndirizzo INT,
 NomeAI VARCHAR(255),
 
 Prezzo FLOAT NOT NULL,
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
 FOREIGN KEY(NomeAI) REFERENCES AgenteImmobiliare(EmailAI)

 );
 
 CREATE TABLE Offerta(
 IdImmobile  INT,
 NomeCliente VARCHAR(255),
 Prezzo FLOAT,
 DataOfferta DATE,
 Esito BOOLEAN,
 PRIMARY KEY (IdImmobile, NomeCliente),
 FOREIGN KEY(IdImmobile) REFERENCES Immobile(IdImmobile),
 FOREIGN KEY(NomeCliente) REFERENCES Cliente(EmailCliente)
 
 );
 
 CREATE TABLE Visita(
 IdImmobile  INT,
 NomeCliente VARCHAR(255),
 Ora FLOAT,
 DataOfferta DATE,
 PRIMARY KEY (IdImmobile, NomeCliente),
 FOREIGN KEY(IdImmobile) REFERENCES Immobile(IdImmobile),
 FOREIGN KEY(NomeCliente) REFERENCES Cliente(EmailCliente)

 );
 
 
