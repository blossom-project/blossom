#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_954eb6f3e30c_key -iv $encrypted_954eb6f3e30c_iv -in codesigning.asc.enc -out codesigning.asc -d
    gpg --fast-import codesigning.asc
fi
