#!/bin/bash

mvn test -fn -Dsurefire.rerunFailingTestsCount=30 | tee test.txt

status=${PIPESTATUS[0]}

if [[ $status != 0 ]]; then
  rm test.txt
  exit $status;
fi

if grep -q "There are test failures\." test.txt; then
  rm test.txt
  exit 1
fi
