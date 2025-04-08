#!/bin/sh
set -e
export GRADLE_HOME="$(dirname "$0")/gradle"
exec "$GRADLE_HOME/bin/gradle" "$@"
