## Configurazione Ambiente

Per eseguire correttamente il progetto, è necessario creare un file `.env` nella **root del progetto**. Questo file deve contenere le credenziali per la connessione al database PostgreSQL nel seguente formato:

```env
DB_URL=jdbc:postgresql://localhost:5432/NOME-DATABASE
DB_USER=USERNAME-PROPRIETARIO-DATABASE
DB_PASSWORD=PASSWORD-DATABASE
