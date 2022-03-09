# Taking Notes Api

This is the api for the Taking Notes App

## Postgres

The `-e` flag determines environment variables

```
docker run --name taking_notes_db -p 5555:5432 -e POSTGRES_USER=taking_notes -e POSTGRES_PASSWORD=123456 -e POSTGRES_DB=taking_notes_db -d postgres:11
```

# Independent image

```
docker build . -t taking_notes_api
docker run -p 8080:8080 -t taking_notes_api
```

# Run all of the containers

```
docker-compose up
```