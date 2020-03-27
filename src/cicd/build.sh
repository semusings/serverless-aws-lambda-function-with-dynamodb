#!/bin/bash

set -e

COMMON_SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/common.sh"
# shellcheck source=src/cicd/common.sh
source "${COMMON_SCRIPT}"

full_build () {
    echo "Running mvn install"
    ${MVN_CMD} install
}

no_its_build () {
    echo "Skipping ITs, likely this build is a pull request from a fork"
    ${MVN_CMD} install -DskipITs
}

# if this build was triggered via a cron job, just scan the dependencies
if [ "${PROJECT_EVENT_TYPE}" = "cron" ] ; then
    cron
else
    # run 'mvn deploy' if we can
    if [ "${DEPLOY}" = true ] ; then
        deploy
    else
        # else try to run the ITs if possible (for someone who has push access to the repo
        if [ "${RUN_ITS}" = true ] ; then
            full_build
        else
            # fall back to running an install and skip the ITs
            no_its_build
        fi
    fi
fi