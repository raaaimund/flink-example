#!/bin/sh
echo "Start submitting jobs"
for job in $JOBS; do
    echo "Submitting job $job"
    flink run --detach -c $job -m jobmanager:8081 ./usrlib/flink-example.jar
done
echo "Finished submitting jobs"
