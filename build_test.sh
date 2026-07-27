#!/bin/bash
cd /workspaces/TLM
./gradlew build 2>&1 | tee build_output.log
