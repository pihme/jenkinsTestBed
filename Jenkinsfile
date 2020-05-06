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
                sh './runTests.sh'

                script {
                  if (fileExists('flakyTests.txt')) {
                      currentBuild.description = new File('flakyTests.txt').getText('UTF-8')
                  }
                }
            }
            post {
                always {
                    junit testResults: "**/*/TEST*.xml", keepLongStdio: true
                }
            }
        }

    }
}
