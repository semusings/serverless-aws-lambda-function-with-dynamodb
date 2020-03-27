#!/bin/bash

set -e

COMMON_SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/common.sh"
# shellcheck source=src/cicd/common.sh
source "${COMMON_SCRIPT}"

cron () {
    echo "Running PROJECT CRON task"
    ${MVN_CMD} dependency-check:aggregate -Powasp
}

deploy () {
    echo "Deploying SNAPSHOT build"
    ${MVN_CMD} deploy -Pci

    # also deploy the javadocs to the site
    git clone -b gh-pages "https://github.com/${REPO_SLUG}.git" target/gh-pages/
    ${MVN_CMD} javadoc:aggregate com.okta:okta-doclist-maven-plugin:generate jxr:aggregate -Ppub-docs -Pci
}

full_build () {
    echo "Running mvn install"
    ${MVN_CMD} install -Pci
}

no_its_build () {
    echo "Skipping ITs, likely this build is a pull request from a fork"
    ${MVN_CMD} install -DskipITs -Pci
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