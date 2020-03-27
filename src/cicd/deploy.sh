#!/usr/bin/env bash

LOG_FILE=deploy.aws.log
serverless deploy -v | tee ${LOG_FILE}

export ServiceEndpoint=$(awk '/ServiceEndpoint:/' ${LOG_FILE} | cut -c18-200)
echo "API Service Endpoint: ${ServiceEndpoint}"

echo "----------------------POST------------------------------"
echo "Testing POST: /orders"
curl -H "Content-Type: application/json" \
        -X POST --data @src/cicd/Order.json \
        ${ServiceEndpoint}/orders
echo ""
echo "--------------------------------------------------------"

echo "-----------------------GET------------------------------"
echo "Testing GET: /orders"
curl -X GET \
        ${ServiceEndpoint}/orders
echo ""
echo "--------------------------------------------------------"

echo ""
echo "Cleanup..."
serverless remove
