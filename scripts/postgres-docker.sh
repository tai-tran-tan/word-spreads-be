docker run -d --name postgres-docker \
    -e POSTGRES_PASSWORD=postgres \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_DB=word_spreads \
    -e PGDATA=/var/lib/postgresql/data/pgdata \
    -v ~/data/mount:/var/lib/postgresql/data \
    -p 5432:5432 \
    postgres:15-alpine3.16
