pipeline {
    agent any
    triggers {
        cron('H/5 * * * *')
    }
    tools {
            maven '3.6.3'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '100'))
    }
    stages {
        stage('build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }

        stage ('test') {
            steps {
                parallel (
                    "unit tests": { sh 'mvn test --fail-never' },
                )
            }
            post {
                always {
                    junit testResults: "**/*/TEST*.xml", keepLongStdio: true
                    findText regexp: 'Failed tests:', alsoCheckConsoleOutput: true
                }
            }
        }

    }
}