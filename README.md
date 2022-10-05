# ZIO Pet Clinic: An sample application written with ZIO 2, Scala 3 and ScalaJS.

> This is repo is taking the zio/zio-petclinic project and building it with Scala 3.1.3

- Original repo: https://github.com/zio/zio-petclinic (Scala 2)
- Original video: https://www.youtube.com/watch?v=3lopiYfWmdQ (Awesome!)

### Changes from the original repo with:

- [x] Update from Scala 2 to Scala 3.1.3
- [x] Docker for Postgres Database
- [ ] Update Tests
- [ ] Deploy with Kubernetes 
- [ ] CI pipeline
- [ ] Metrics
- [ ] Tools 

<br><hr><br>

## Prerequisites

| Software       | Version        | Install                                        |
|----------------|----------------|------------------------------------------------|
| JVM            | openjdk 17.0.4 | https://sdkman.io/install                      |
| Scala          | 3.1.3          | https://www.scala-lang.org/download            |
| sbt            | 1.7.1          | https://www.scala-sbt.org/download.html        |  
| Zio            | 2.0.1          | |
| Docker Desktop | 4.3.x          | https://www.docker.com/products/docker-desktop |

<br><hr><br>

## Running localdev with Docker
> Requires Docker to be running  

### Setup

- `make check`
- `make setup`

Run the local database, start backend server in **Terminal 1**
```
make postgres-up
sbt 
~ backend/reStart

Crtl+c (to exit)
```

Run the frontend in a separate **Terminal 2**
```
open http://localhost:3000
make frontend-up
```

