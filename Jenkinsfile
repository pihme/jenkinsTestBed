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
                sh './runTests.sh'
            }
            post {
                always {
                    script {
                      def files = findFiles(glob: '**/*/TEST-*.xml')

                      files.each {
                        def input = readFile(it.path)
                        println("Read: ${input}");

                        extractFlakyTestReport(it);
                      }
                    }

                    junit testResults: "**/*/TEST*.xml", keepLongStdio: true

                    zip zipFile: 'testresults.zip', archive: true, glob: "**/surefire-reports/**"
                }
                failure {
                    script {
                      echo "entering script block"

                      echo "condition" + fileExists('flakyTests.txt')

                      if (fileExists('flakyTests.txt')) {
                          currentBuild.description = "Flaky Tests: +\n" + readFile('flakyTests.txt')
                      }

                      echo "leaving script block"
                    }
                }
            }
        }

    }
}

    void extractFlakyTestReport(file) {
        def inputFile = file.path
        println("Processing: ${inputFile}");



        println("Read: ${input}");

        def parser = new XmlParser()
        def doc = parser.parseText(input);

        if (hasFlakyTests(doc)) {

            def output = inputFile.substring(0, inputFile.lastIndexOf('.')) + "-FLAKY.xml"
            println("Generating: ${output}");
            modifyTestReport(doc)

            def stringWriter = new StringWriter(output)
            def nodePrinter = new XmlNodePrinter(new PrintWriter(stringWriter))
            nodePrinter.setPreserveWhitespace(true)
            nodePrinter.print(doc)

            writeFile( file: output, text: stringWriter.toString)
        } else {
            println("Skipping, because it does not contain flaky tests")
        }
    }

    boolean hasFlakyTests(root) {
        def result = false;
        root.testcase.each {
            if (isFlakyTest(it)) {
                result = true;
            }
        }
        return result;
    }

    boolean isFlakyTest(testcase) {
        return !testcase.flakyFailure.isEmpty()
    }

    void modifyTestReport(root) {
        modifyTestSuiteName(root)
        pruneNonFlakyTests(root)
        elevateFlakyTests(root)
        adjustTestCount(root)
    }

    void modifyTestSuiteName(root) {
        def newName = root.'@name' + "-Flaky";
        root.'@name' = newName;
    }

    void pruneNonFlakyTests(root) {
        root.testcase
                .findAll { !isFlakyTest(it) }
                .each { root.remove(it) }
    }

    void elevateFlakyTests(root) {
        root.testcase
                .findAll { isFlakyTest(it) }
                .each { elevateFlakyFailure(it) }
    }


    void elevateFlakyFailure(testcase) {
        def flakyFailure = testcase.flakyFailure[0]

        def failure = new NodeBuilder().failure(message: flakyFailure.'@message', type: flakyFailure.'@type', flakyFailure.stackTrace.text())

        testcase.append(failure);
        testcase.append(flakyFailure.'system-out')

        testcase.flakyFailure.each { testcase.remove(it)}
    }

    void adjustTestCount(root) {
        def testcaseCount = root.testcase.size();

        root.'@tests' = testcaseCount
        root.'@failures' = testcaseCount
    }
