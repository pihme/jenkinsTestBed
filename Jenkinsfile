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
                  echo "entering script block"

                  echo "condition" + fileExists('flakyTests.txt')

                  if (fileExists('flakyTests.txt')) {
                      echo "Content: " + new File('flakyTests.txt').getText('UTF-8')

                      currentBuild.description = new File('flakyTests.txt').getText('UTF-8')
                  }

                  echo "leaving script block"
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
