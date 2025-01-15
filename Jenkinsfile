pipeline {
  agent any

  environment {
          POSTGRES_USER = "${params.POSTGRES_USER}"
          POSTGRES_PASSWORD = "${params.POSTGRES_PASSWORD}"
          POSTGRES_DB = "${params.POSTGRES_DB}"
          HIBERNATE_DDL_AUTO = "${params.HIBERNATE_DDL_AUTO}"
  }

  stages {
    stage('Test & Build Images') {
      steps {
        sh 'mvn clean spring-boot:build-image -Pprod'
      }
    }

    stage('Start Docker Containers') {
      steps {
        sh 'docker-compose up --build'
      }
    }
  }
}