#!/usr/bin/env bash

set -e

TEST_RUNNERS=("Accounts" "BankOperations" "Customers" "Loans" "Login" "Transactions")

echo "========================================="
echo "Executing Karate API Tests"
echo "========================================="

for testName in "${TEST_RUNNERS[@]}"
do
    FILENAME="${testName}TestRunner"

    echo ""
    echo "Running ${FILENAME}.java"

    mvn test -Dtest="${FILENAME}"
done

echo ""
echo "========================================="
echo "Karate API tests have finished running"
echo "========================================="