#!/bin/sh

mvn clean package
cp -rpv other/de.belaso.mongolyn.repository/target/repository/* ../belaso.github.com/updates
cd ../belaso.github.com
git add .
git commit -s -m "new mongolyn release"
git push
