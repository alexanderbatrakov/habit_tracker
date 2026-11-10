#!/usr/bin/env bash
set -euo pipefail

: "${JENKINS_URL:?JENKINS_URL is required}"
: "${JENKINS_AGENT_NAME:?JENKINS_AGENT_NAME is required}"

if [[ -z "${JENKINS_SECRET:-}" ]]; then
    secret_file="${SECRET_FILE:-}"
    echo "[agent] waiting for secret file ${secret_file} ..."
    for _ in $(seq 1 120); do
        [[ -s "${secret_file}" ]] && break
        sleep 2
    done
    if [[ ! -s "${secret_file}" ]]; then
        echo "[agent] ERROR: secret file not found: ${secret_file}" >&2
        exit 1
    fi
    JENKINS_SECRET=$(tr -d '[:space:]' < "${secret_file}")
fi

echo "[agent] waiting for Jenkins at ${JENKINS_URL} ..."
until curl -fsS "${JENKINS_URL}" -o /dev/null; do
    sleep 2
done

echo "[agent] connecting as ${JENKINS_AGENT_NAME} ..."
exec java -jar /opt/jenkins/agent.jar \
    -url "${JENKINS_URL}" \
    -secret "${JENKINS_SECRET}" \
    -name "${JENKINS_AGENT_NAME}" \
    -workDir "${JENKINS_AGENT_WORKDIR:-}"
