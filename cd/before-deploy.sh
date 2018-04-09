#!/usr/bin/env bash
if ([ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]) ||  ["$TRAVIS_TAG" != ''] ; then
    openssl aes-256-cbc -K $encrypted_b4f84f3a0738_key -iv $encrypted_b4f84f3a0738_iv -in codesigning.asc.enc -out codesigning.asc -d
    gpg --fast-import codesigning.asc
fi
