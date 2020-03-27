#!/bin/bash

#Using bash command is faster than invoking maven
#Make sure you are placing project <version></version> at top of the pom.xml file.
ARTIFACT_VERSION="$(cat pom.xml | grep '<version>' | head -1 | sed 's/ //g' | sed 's/<version>//g' | sed 's/<\/version>//g')"
PROJECT_BRANCH="$(cat .git/HEAD | awk -F '/' '{print $NF}')";
IS_RELEASE=$([ "${ARTIFACT_VERSION/SNAPSHOT}" == "${ARTIFACT_VERSION}" ] && [ "${PROJECT_BRANCH}" == 'master' ] && echo 'true')
export ARTIFACT_VERSION
export IS_RELEASE

echo "Build configuration:"
echo "Version:             ${ARTIFACT_VERSION}"
echo "Is release:          ${IS_RELEASE:-false}"
echo
echo "Java Version:"
java -version