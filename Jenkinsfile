pipeline {
    agent any
    tools {
            maven '3.6.3'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '100'))
    }
    stages {
        stage('clean') {
            steps {
                sh 'mvn clean'

                sh 'rm -f flaky-tests-log.txt'
                sh 'rm -f flakyTests.txt'
                sh 'rm -f testresults.zip'
            }
        }

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
                failure {
                    script {

                      def files = findFiles(glob: '**/*/TEST-*.xml')

                      files.each {
                        extractFlakyTestReport(it);
                      }

                      echo "entering script block"

                      echo "condition" + fileExists('flakyTests.txt')

                      if (fileExists('flakyTests.txt')) {
                          currentBuild.description = "Flaky Tests: +\n" + readFile('flakyTests.txt')
                      }

                      echo "leaving script block"
                    }
                }
                always {
                    junit testResults: "**/*/TEST*.xml", keepLongStdio: true

                    zip zipFile: 'testresults.zip', archive: true, glob: "**/surefire-reports/**"
                }
            }
        }

    }
}

    void extractFlakyTestReport(file) {
        def inputFile = file.path
        println("Processing: ${inputFile}");

        def input = readFile(inputFile)

        def doc = parseXML(input);

        if (hasFlakyTests(doc)) {
            def output = inputFile.substring(0, inputFile.lastIndexOf('.')) + "-FLAKY.xml"
            println("Generating: ${output}");

            writeFile( file: output, text: transformTestReport(doc))
        } else {
            println("Skipping, because it does not contain flaky tests")
        }
    }

    @NonCPS
    groovy.util.Node parseXML(input) {
        def parser = new XmlParser()
        return parser.parseText(input);
    }

    @NonCPS
    String transformTestReport(doc) {
        modifyTestReport(doc)

        def stringWriter = new StringWriter()
        def nodePrinter = new XmlNodePrinter(new PrintWriter(stringWriter))
        nodePrinter.setPreserveWhitespace(true)
        nodePrinter.print(doc)

        return stringWriter.toString()
    }

    @NonCPS
    boolean hasFlakyTests(root) {
        def result = false;
        root['testcase'].each {
            if (isFlakyTest(it)) {
                result = true;
            }
        }
        return result;
    }

    @NonCPS
    boolean isFlakyTest(testcase) {
        return !testcase['flakyFailure'].isEmpty()
    }

    @NonCPS
    void modifyTestReport(root) {
        appendFlakyToName(root)
        pruneNonFlakyTests(root)
        elevateFlakyTests(root)
        adjustTestCount(root)
    }

    @NonCPS
    void appendFlakyToName(root) {
        def newName = root['@name'] + "-Flaky";
        root['@name'] = newName;
    }

    @NonCPS
    void pruneNonFlakyTests(root) {
        root['testcase']
                .findAll { !isFlakyTest(it) }
                .each { root.remove(it) }
    }

    @NonCPS
    void elevateFlakyTests(root) {
        root['testcase']
                .findAll { isFlakyTest(it) }
                .each { elevateFlakyFailure(it) }
    }

    @NonCPS
    void elevateFlakyFailure(testcase) {
        appendFlakyToName(testcase)

        def newClassName = testcase['@classname'] + " (Flaky)"
        testcase['@classname'] = newClassName

        def flakyFailure = testcase['flakyFailure'][0]

        def failure = new NodeBuilder().failure(message: flakyFailure['@message'], type: flakyFailure['@type'], flakyFailure['stackTrace'].text())

        testcase.append(failure);
        testcase.append(flakyFailure['system-out'][0])

        testcase['flakyFailure'].each { testcase.remove(it)}
    }

    @NonCPS
    void adjustTestCount(root) {
        def testcaseCount = root['testcase'].size();

        root['@tests'] = testcaseCount
        root['@failures'] = testcaseCount
    }
