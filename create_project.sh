#!/bin/bash

# Define the base directory
BASE_DIR="src/main/kotlin/com/lgweida/equityOMS"

# Create directories
mkdir -p "$BASE_DIR/config"
mkdir -p "$BASE_DIR/controller"
mkdir -p "$BASE_DIR/dto"
mkdir -p "$BASE_DIR/entity"
mkdir -p "$BASE_DIR/exception"
mkdir -p "$BASE_DIR/repository"
mkdir -p "$BASE_DIR/service/impl"
mkdir -p "src/main/resources"

# Create application files
touch "$BASE_DIR/EquityOMSApplication.kt"

# Create controller files
touch "$BASE_DIR/controller/OrderController.kt"
touch "$BASE_DIR/controller/ExecutionController.kt"

# Create DTO files
touch "$BASE_DIR/dto/OrderDto.kt"
touch "$BASE_DIR/dto/ExecutionDto.kt"
touch "$BASE_DIR/dto/OrderRequest.kt"
touch "$BASE_DIR/dto/ExecutionRequest.kt"

# Create entity files
touch "$BASE_DIR/entity/Order.kt"
touch "$BASE_DIR/entity/Execution.kt"

# Create exception files
touch "$BASE_DIR/exception/OrderNotFoundException.kt"
touch "$BASE_DIR/exception/GlobalExceptionHandler.kt"

# Create repository files
touch "$BASE_DIR/repository/OrderRepository.kt"
touch "$BASE_DIR/repository/ExecutionRepository.kt"

# Create service files
touch "$BASE_DIR/service/OrderService.kt"
touch "$BASE_DIR/service/ExecutionService.kt"
touch "$BASE_DIR/service/impl/OrderServiceImpl.kt"
touch "$BASE_DIR/service/impl/ExecutionServiceImpl.kt"

# Create resources
touch "src/main/resources/application.yml"

# Create build.gradle.kts
touch "build.gradle.kts"

# Create README.md
touch "README.md"

# Set permissions
chmod +x "$0"

