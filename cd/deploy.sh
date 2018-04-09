#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    mvn deploy -e -P build-extras,sign --settings cd/mvnsettings.xml
fi

if [ "$TRAVIS_TAG" != '' ]; then
    mvn versions:set -DnewVersion=${TRAVIS_TAG}
    mvn deploy -e -P build-extras,sign --settings cd/mvnsettings.xml
fi
