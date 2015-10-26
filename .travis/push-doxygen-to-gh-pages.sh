#!/bin/bash

# Example taken from 
#   https://github.com/ReadyTalk/swt-bling/blob/master/.utility/push-javadoc-to-gh-pages.sh
#   http://benlimmer.com/2013/12/26/automatically-publish-javadoc-to-gh-pages-with-travis-ci/

if [ "$TRAVIS_REPO_SLUG" == "joernano/top-roaddetection" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_BUILD_TYPE" == "Release" ]; then

    echo -e "Publishing scaladoc. \n"

    cd $PROJ_ROOT
    cp -R doc $HOME/doc-latest

    cd $HOME
    git config --global user.email "travis@travis-ci.org"
    git config --global user.name "travis-ci"
    git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/joernano/top-roaddetection gh-pages > /dev/null

    cd gh-pages
    git rm -rf doc
    mkdir -p doc/
    cp -Rf $HOME/doc-latest ./doc
    git add -f .
    git commit -m "[GH-PAGES] [DOC] Updated scaladoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
    git push -fq origin gh-pages

    echo -e "Published scaladoc to gh-pages. \n"

fi
