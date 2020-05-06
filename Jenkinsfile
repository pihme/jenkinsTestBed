pipeline {
    agent any
    triggers {
        cron('H/5 * * * *')
    }
    tools {
            maven '3.6.3'
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
                    "unit tests": { sh './runTests.sh' },
                )
            }
            post {
                always {
                    junit testResults: "**/*/TEST*.xml", keepLongStdio: true
                    findText regexp: '\\[WARNING\\] Flakes:', alsoCheckConsoleOutput: true
                }
            }
        }

    }
}