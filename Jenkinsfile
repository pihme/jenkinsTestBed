pipeline {
    agent any
    environment {
        scmUrl = 'https://github.com/pihme/jenkinsTestBed.git'
    }
    stages {
        stage('checkout git') {
            steps {
                git branch: BRANCH_NAME , url: scmUrl
            }
        }

        // credentialsId: 'GitCredentials'

        stage('build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }

        stage ('test') {
            steps {
                parallel (
                    "unit tests": { sh 'mvn test' },
                    "integration tests": { sh 'mvn integration-test' }
                )
            }
        }

    }
}