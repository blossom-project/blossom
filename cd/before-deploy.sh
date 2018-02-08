#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_4e3556a82943_key -iv $encrypted_4e3556a82943_iv -in codesigning.asc.enc -out codesigning.asc -d
    gpg --fast-import codesigning.asc
fi
