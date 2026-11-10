pipeline {
    agent { label 'android' }

    options {
        timestamps()
        timeout(time: 45, unit: 'MINUTES')
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '10'))
    }

    parameters {
        booleanParam(name: 'RUN_BENCHMARK', defaultValue: false, description: 'Запустить benchmark gradle-profiler вручную')
        booleanParam(name: 'RUN_INSTRUMENTED', defaultValue: false, description: 'Instrumented-тесты (требуют эмулятор, в контейнере выключены)')
    }

    environment {
        GRADLE_USER_HOME = '/home/jenkins/.gradle'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Resolve branch') {
            steps {
                script {
                    env.SOURCE_BRANCH = env.BRANCH_NAME ?: sh(
                        script: 'git name-rev --name-only HEAD',
                        returnStdout: true
                    ).trim().replace('remotes/origin/', '')

                    if (env.SOURCE_BRANCH.startsWith('release/')) {
                        env.BRANCH_TYPE = 'release'
                    } else if (env.SOURCE_BRANCH == 'main' || env.SOURCE_BRANCH == 'master') {
                        env.BRANCH_TYPE = 'main'
                    } else {
                        env.BRANCH_TYPE = 'feature'
                    }
                    currentBuild.description = "${env.SOURCE_BRANCH} (${env.BRANCH_TYPE})"
                    echo "Source branch: ${env.SOURCE_BRANCH}, flow type: ${env.BRANCH_TYPE}"
                }
            }
        }

        stage('Unit tests') {
            steps {
                sh './gradlew --no-daemon testDebugUnitTest'
            }
        }

        stage('Lint') {
            when { expression { env.BRANCH_TYPE in ['main', 'release'] } }
            steps {
                sh './gradlew --no-daemon lintDebug'
            }
        }

        stage('Debug APK') {
            when { expression { env.BRANCH_TYPE != 'release' } }
            steps {
                sh './gradlew --no-daemon assembleDebug'
            }
        }

        stage('Release APK') {
            when { expression { env.BRANCH_TYPE in ['main', 'release'] } }
            steps {
                sh './gradlew --no-daemon assembleRelease'
            }
        }

        stage('Instrumented tests') {
            when { expression { params.RUN_INSTRUMENTED } }
            steps {
                echo 'Instrumented tests are skipped: emulator is not available in docker agent'
            }
        }

        stage('Nightly benchmark (gradle-profiler)') {
            when {
                anyOf {
                    triggeredBy cause: 'TimerTrigger'
                    expression { params.RUN_BENCHMARK }
                }
            }
            steps {
                sh """
                    gradle-profiler --benchmark \
                        --project-dir '${WORKSPACE}' \
                        --scenario-file gradle/profiler.scenarios \
                        --output-dir '${WORKSPACE}/build/profiler/${BUILD_NUMBER}' \
                        --iterations 3 \
                        --warm-ups 2
                """
                sh 'find build/profiler -name benchmark.csv | xargs cat || true'
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '**/build/test-results/**/TEST-*.xml'
            archiveArtifacts artifacts: 'app/build/outputs/apk/**/*.apk, build/profiler/**', allowEmptyArchive: true
            cleanWs deleteDirs: true, notFailBuild: true
        }
        success {
            echo "Build SUCCESS: branch=${env.SOURCE_BRANCH}, type=${env.BRANCH_TYPE}"
        }
        failure {
            echo "Build FAILED: branch=${env.SOURCE_BRANCH}, type=${env.BRANCH_TYPE}"
        }
    }
}
