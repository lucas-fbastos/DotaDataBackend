# Dota Data
 _Created to help players improve their own skills in  Dota 2 (while I improve mine in java and SpringBoot ✌️_)

### Powered by Open dota https://github.com/odota
The idea behind this app is to collect data (provided by open dota) and analyze to show my weak spots as a casual player. 
In a near future I want to add a feature to compare personal data with professional players, to see what are the differences and what the average player could do to improve.

## 🌠Features

- Query trough recent matches
- Search info about professional players
- Compare your stats with other players

## 🧑‍💻Tech

Dota data uses this tech listed bellow:

- [Spring Boot] - Framework used to create standalone spring aplication
- [PostgreSQL] - Open source DataBase
- [Spring Data] - JPA imeplementation




   [PostgreSQL]: <https://www.postgresql.org>
   [Spring Boot]: <https://spring.io/projects/spring-boot>
   [Spring Data]: <https://spring.io/projects/spring-data>

## Browse the database with pgAdmin

Start either Compose setup from its directory:

- Full app: run `docker compose up -d` in `AghsLabsApi`.
- Database only: run `docker compose up -d` in `AghsLabsApi/docker/pgsql`.

Open <http://localhost:5050>. Sign in with `admin@example.com` and the
`POSTGRES_PASSWORD` from that setup's `.env` file. You can set
`PGADMIN_DEFAULT_EMAIL` and `PGADMIN_DEFAULT_PASSWORD` in your environment to
use different pgAdmin login credentials before the first start.

In pgAdmin, register a server with host `db`, port `5432`, maintenance database
and username from `POSTGRES_DB` and `POSTGRES_USER`, and password from
`POSTGRES_PASSWORD`. Then select the database and open **Query Tool** to run SQL.
pgAdmin keeps its settings in a Docker volume. Run one Compose setup at a time,
since both use ports 5050 and 5432.

  
