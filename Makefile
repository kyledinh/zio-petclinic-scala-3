export SHELL := /bin/bash

## VARS AND ENVS
REPO_DIR ?= $(shell pwd | xargs echo -n)
GITTAG ?= $(shell git describe --tags --always --dirty)
SEMVER ?= $(shell head -n 1 sem-version)

DOCKER_PG_VOL := docker_pg_vol
DOCKER_PG_CONTAINER := docker_pg_container

## Sync vars with backend/src/main/scala/petclinic/QuillContext.scala
POSTGRES_DB := postgres
POSTGRES_USER := postgres
POSTGRES_PASSWORD := password

## MAIN ##############################
.PHONY: check postgres setup

backend-compile:
	@sbt backend/clean
	@sbt backend/compile 

backend-up:
## @sbt backend/reStart
	echo "Running in background, currenly does NOT work...."
	echo "Try this:"
	echo "Run `sbt` in termial, then `backend/reStart`"

check: 
	@echo "SEMVER: $(SEMVER)"
	@echo "REPO_DIR: $(REPO_DIR)"
	@echo "DOCKER_PG_VOL: $(DOCKER_PG_VOL)"
	@echo "$(REPO_DIR)/$(DOCKER_PG_VOL)"
	@scala --version

frontend-compile:
	@sbt frontend/fastLinkJS
	@cp frontend/target/scala-3.1.3/pet-clinic-frontend-fastopt/main.js js-frontend/.

frontend-up:
	@open http://localhost:3000
	@cd js-frontend && yarn exec vite

postgres-up:
	@docker run --name $(DOCKER_PG_CONTAINER) \
	-p 5432:5432 \
	-e POSTGRES_DB=$(POSTGRES_DB) \
	-e POSTGRES_USER=$(POSTGRES_USER) \
	-e POSTGRES_PASSWORD=$(POSTGRES_PASSWORD) \
	-v $(REPO_DIR)/$(DOCKER_PG_VOL):/var/lib/postgresql/data \
	-d postgres 

postgres-check:
	@docker exec -i docker_pg_container psql postgres postgres -c "\d"
	@docker exec -i docker_pg_container psql postgres postgres -c "select * from owner"
	@docker exec -i docker_pg_container psql postgres postgres -c "select * from pet"

postgres-down:
	@docker rm $(DOCKER_PG_CONTAINER) 

postgres-shell:
	@docker exec -it docker_pg_container psql postgres postgres

setup:
	@echo "SETTING UP DOCKER FILES/DIR"
	$(shell [ -d $(DOCKER_PG_VOL) ] || mkdir $(DOCKER_PG_VOL))
	@echo "yarn install for frontend"
	@cd js-frontend && yarn install

test-backend:
	@sbt test

test-compile:
	@sbt test:compile