#!/bin/bash

# --- CONFIGURATION ---
TARGET_IP="localhost"
PORT="8080"
OUTPUT_DIR=~/lab_results
mkdir -p $OUTPUT_DIR

echo "=================================================="
echo "   NoSQL Database Benchmark Suite"
echo "   Target: http://$TARGET_IP:$PORT"
echo "   Results directory: $OUTPUT_DIR"
echo "=================================================="

echo "[1/6] Running Redis performance test..."
siege -H "Accept: application/json" -c10 -r100 "http://$TARGET_IP:$PORT/nosql-lab-rd/student_no=2025000001" > $OUTPUT_DIR/redis-siege.results 2>&1

echo "[2/6] Running Hazelcast performance test..."
siege -H "Accept: application/json" -c10 -r100 "http://$TARGET_IP:$PORT/nosql-lab-hz/student_no=2025000001" > $OUTPUT_DIR/hz-siege.results 2>&1

echo "[3/6] Running MongoDB performance test..."
siege -H "Accept: application/json" -c10 -r100 "http://$TARGET_IP:$PORT/nosql-lab-mon/student_no=2025000001" > $OUTPUT_DIR/mongodb-siege.results 2>&1

echo "[4/6] Running Redis execution time benchmark..."
(time seq 1 100 | xargs -n1 -P10 -I{} curl -s "http://$TARGET_IP:$PORT/nosql-lab-rd/student_no=2025000001") 2> $OUTPUT_DIR/redis-time.results

echo "[5/6] Running Hazelcast execution time benchmark..."
(time seq 1 100 | xargs -n1 -P10 -I{} curl -s "http://$TARGET_IP:$PORT/nosql-lab-hz/student_no=2025000001") 2> $OUTPUT_DIR/hz-time.results

echo "[6/6] Running MongoDB execution time benchmark..."
(time seq 1 100 | xargs -n1 -P10 -I{} curl -s "http://$TARGET_IP:$PORT/nosql-lab-mon/student_no=2025000001") 2> $OUTPUT_DIR/mongodb-time.results

echo "=================================================="
echo "   Benchmark suite completed successfully!"
echo "=================================================="

