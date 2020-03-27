#!/bin/bash

# deploy snapshot from ONLY this branch
SNAPSHOT_BRANCH="master"
PROJECT_NAME="employees"

# Get the slug from the TRAVIS var, or parse the 'origin' remote
REPO_SLUG=${REPO_SLUG:-$(git remote get-url origin | sed 's_.*\:__; s_.*github.com/__; s_\.git__')}
PULL_REQUEST=${PULL_REQUEST:-true} # default to true
BRANCH=${PROJECT_BRANCH:-"$(git rev-parse --abbrev-ref HEAD)"}

# run the ITs if we have an ENV_VARS are set
if [ "${PROJECT_SECURE_ENV_VARS}" = true ] ; then
    RUN_ITS=true
fi
RUN_ITS=${RUN_ITS:-false}

# we only deploy from a given branch NOT for pull requests, and ONLY when we can run the ITs
# and do NOT deploy releases, only snapshots right now
if [ "$BRANCH" = "$SNAPSHOT_BRANCH" ] && [ "$PULL_REQUEST" = false ] && [ "$RUN_ITS" = true ] && [ ! "$IS_RELEASE" = true ]; then
        DEPLOY=true
fi
DEPLOY=${DEPLOY:-false}

# print the props so it is easier to debug on Travis or locally.
echo "REPO_SLUG: ${REPO_SLUG}"
echo "PULL_REQUEST: ${PULL_REQUEST}"
echo "BRANCH: ${BRANCH}"
echo "IS_RELEASE: ${IS_RELEASE}"
echo "RUN_ITS: ${RUN_ITS}"

# all the prep is done, lets run the build!
MVN_CMD="./mvnw -s src/cicd/settings.xml -B -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn -V"
