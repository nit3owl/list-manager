# Variables
IMAGE_NAME = list-manager
GIT_HASH = $(shell git rev-parse --short HEAD)
GIT_BRANCH = $(shell git rev-parse --abbrev-ref HEAD)
DOCKER_REGISTRY ?= # Add your registry here if needed

# Default target
.DEFAULT_GOAL := help

# Help target
help: ## Display this help
	@awk 'BEGIN {FS = ":.*##"; printf "\nUsage:\n  make \033[36m<target>\033[0m\n\nTargets:\n"} /^[a-zA-Z_-]+:.*?##/ { printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2 }' $(MAKEFILE_LIST)

# Build targets
build: ## Build Docker image with git hash tag
	docker build -t $(IMAGE_NAME):$(GIT_HASH) .

build-latest: build ## Build and tag as latest
	docker tag $(IMAGE_NAME):$(GIT_HASH) $(IMAGE_NAME):latest

build-branch: build ## Build and tag with branch name
	docker tag $(IMAGE_NAME):$(GIT_HASH) $(IMAGE_NAME):$(GIT_BRANCH)-$(GIT_HASH)

# Clean targets
clean: ## Remove Docker images
	-docker rmi $(IMAGE_NAME):$(GIT_HASH) 2>/dev/null
	-docker rmi $(IMAGE_NAME):latest 2>/dev/null
	-docker rmi $(IMAGE_NAME):$(GIT_BRANCH)-$(GIT_HASH) 2>/dev/null

# Run targets
run: ## Run the container with the git hash tag
	docker run -p 8080:8080 $(IMAGE_NAME):$(GIT_HASH)

run-latest: ## Run the container with latest tag
	docker run -p 8080:8080 $(IMAGE_NAME):latest

# Push targets (if using a registry)
push: build ## Push image with git hash tag
	@if [ -n "$(DOCKER_REGISTRY)" ]; then \
		docker tag $(IMAGE_NAME):$(GIT_HASH) $(DOCKER_REGISTRY)/$(IMAGE_NAME):$(GIT_HASH); \
		docker push $(DOCKER_REGISTRY)/$(IMAGE_NAME):$(GIT_HASH); \
	else \
		echo "DOCKER_REGISTRY is not set"; \
		exit 1; \
	fi

push-latest: build-latest ## Push latest tag
	@if [ -n "$(DOCKER_REGISTRY)" ]; then \
		docker tag $(IMAGE_NAME):latest $(DOCKER_REGISTRY)/$(IMAGE_NAME):latest; \
		docker push $(DOCKER_REGISTRY)/$(IMAGE_NAME):latest; \
	else \
		echo "DOCKER_REGISTRY is not set"; \
		exit 1; \
	fi

# Info target
info: ## Show build information
	@echo "Image name: $(IMAGE_NAME)"
	@echo "Git hash: $(GIT_HASH)"
	@echo "Git branch: $(GIT_BRANCH)"
	@if [ -n "$(DOCKER_REGISTRY)" ]; then \
		echo "Docker registry: $(DOCKER_REGISTRY)"; \
	else \
		echo "Docker registry: not set"; \
	fi

.PHONY: help build build-latest build-branch build-all clean run run-latest push push-latest push-all info