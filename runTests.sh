#!/bin/bash

mvn test -Dsurefire.rerunFailingTestsCount=15 | tee test.txt

if grep -q "\[WARNING\] Flakes:" "test.txt"; then
  

  echo ERROR: Flaky Tests detected>&2
  exit 1
fi
