#!/bin/bash

mvn test -Dsurefire.rerunFailingTestsCount=15 | tee test.txt

STATUS=${PIPESTATUS[0]}

echo Info: Tests completed with: $STATUS

if grep -q "\[WARNING\] Flakes:" test.txt; then

  awk '/^\[WARNING\] Flakes:.*$/{flag=1}/^\[ERROR\] Tests run:.*Flakes: [0-9]*$/{print;flag=0}flag' test.txt > flaky-tests-log.txt

  grep "\[ERROR\]   Run 1: " flaky-tests-log.txt | awk '{print $4}' > flakyTests.txt

  echo ERROR: Flaky Tests detected>&2
  exit 1
fi

exit $STATUS
