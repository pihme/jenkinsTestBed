pipeline {
    agent any

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
                    jacoco(
                          execPattern: 'target/*.exec',
                          classPattern: 'target/classes',
                          sourcePattern: 'src/main/java',
                          exclusionPattern: 'src/test*'
                    )
                    junit testResults: "**/*/TEST*.xml", keepLongStdio: true
                    findText regexp: 'Failed tests:', alsoCheckConsoleOutput: true

                    publishCoverage adapters: [jacocoAdapter(mergeToOneReport: true, path: '**/*.xml')]
                }
            }
        }

    }
}