export SHELL := /bin/bash

## VARS AND ENVS
REPO_DIR ?= $(shell pwd | xargs echo -n)
GITTAG ?= $(shell git describe --tags --always --dirty)
SEMVER ?= $(shell head -n 1 sem-version)

DOCKER_PG_VOL := docker_pg_vol
DOCKER_PG_CONTAINER := docker_pg_container

## Sync vars with backend/src/main/scala/petclinic/QuillContext.scala
POSTGRES_DB := petclinic
POSTGRES_USER := postgres
POSTGRES_PASSWORD := password

## MAIN ##############################
.PHONY: check postgres setup

backend-compile:
	@sbtn backend/clean
	@sbtn backend/compile 

backend-down:
	@sbtn backend/reStop

backend-up:
	@sbtn backend/reStart

check: 
	@echo "SEMVER: $(SEMVER)"
	@echo "REPO_DIR: $(REPO_DIR)"
	@echo "DOCKER_PG_VOL: $(DOCKER_PG_VOL)"
	@echo "$(REPO_DIR)/$(DOCKER_PG_VOL)"
	@scala --version

frontend-compile:
	@sbtn frontend/fastLinkJS
	@cp frontend/target/scala-3.1.3/pet-clinic-frontend-fastopt/main.js js-frontend/.

frontend-up:
	@open http://localhost:3000
	@cd js-frontend && yarn exec vite

postgres-check:
	@docker exec -i docker_pg_container psql petclinic postgres -c "\l"
	@docker exec -i docker_pg_container psql petclinic postgres -c "\d"
	@docker exec -i docker_pg_container psql petclinic postgres -c "select * from owner"
	@docker exec -i docker_pg_container psql petclinic postgres -c "select * from pet"
	@docker exec -i docker_pg_container psql petclinic postgres -c "select * from visit"

postgres-down:
	@docker stop $(DOCKER_PG_CONTAINER) 
	@docker rm $(DOCKER_PG_CONTAINER)

postgres-init-migrate:
	@docker exec -i docker_pg_container createdb -h localhost -p 5432 -U $(POSTGRES_USER) $(POSTGRES_DB) 
	@cat ./backend/src/main/resources/db/migration/V1__create_pet_clinic.sql | docker exec -i docker_pg_container psql -U postgres -d petclinic  
	@cat ./backend/src/main/resources/db/migration/V2__add_fixtures.sql | docker exec -i docker_pg_container psql -U postgres -d petclinic  

postgres-reset:
	@$(MAKE) postgres-down
	@rm -rf $(DOCKER_PG_VOL)
	@mkdir $(DOCKER_PG_VOL)
	@$(MAKE) postgres-up
	@echo ".. wait for db to restart then (~10 secs), run: make postgres-init-migrate"

postgres-shell:
	@docker exec -it docker_pg_container psql petclinic postgres

postgres-up:
	@docker run --name $(DOCKER_PG_CONTAINER) \
	-p 5432:5432 \
	-e POSTGRES_DB=$(POSTGRES_DB) \
	-e POSTGRES_USER=$(POSTGRES_USER) \
	-e POSTGRES_PASSWORD=$(POSTGRES_PASSWORD) \
	-v $(REPO_DIR)/$(DOCKER_PG_VOL):/var/lib/postgresql/data \
	-d postgres:14

setup:
	@echo "SETTING UP DOCKER FILES/DIR"
	$(shell [ -d $(DOCKER_PG_VOL) ] || mkdir $(DOCKER_PG_VOL))
	@echo "yarn install for frontend"
	@cd js-frontend && yarn install

status:
	sbtn backend/reStatus
	docker ps 
	ps -aef | grep vite

test-backend:
	@sbtn test

test-compile:
	@sbtn test:compile