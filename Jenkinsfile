pipeline {
  agent any

  environment {
      POSTGRES_USER = "${POSTGRES_USER}"
      POSTGRES_PASSWORD = "${POSTGRES_PASSWORD}"
      POSTGRES_DB = "${POSTGRES_DB}"
      HIBERNATE_DDL_AUTO = "${HIBERNATE_DDL_AUTO}"
      API_AUTH_KEY = "${API_AUTH_KEY}"
  }

  stages {
    stage('Test') {
      steps {
        sh 'mvn clean test'
      }
    }

    stage('Build Docker Images') {
      steps {
        sh 'mvn clean spring-boot:build-image -Pprod'
      }
    }

    stage('Start Docker Containers') {
      steps {
        sh 'docker compose up -d -f compose.yml --build'
      }
    }
  }
}