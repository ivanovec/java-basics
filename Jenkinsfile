pipeline {
    agent {
        label 'docker'
    }

    parameters {
        choice(choices: ['server1', 'server2'], description: 'select server fo test run', name: 'server')
        booleanParam(defaultValue: true, description: 'run rest tests', name: 'rest')
        booleanParam(defaultValue: false, description: 'run web tests', name: 'web')
    }

    stages {
//         stage('rest tests') {
//             when {
//                 expression { return params.rest }
//             }
//             steps {
//                 sh "mvn -Dtest=rest.** verify"
//             }
//         }

        stage('web tests') {
             when {
                 expression { return params.web }
             }
             parallel {
                stage('chrome last'){
                    agent {
                        node {
                            label 'docker'
                            customWorkspace "workspace/chrome83"
                        }
                    }
                     steps {
                          script{
                            sh "docker build -t test -f src/test/resources/docker/Dockerfile --target chromelast ./"
                            sh "docker run -v `pwd`:/tests --privileged --shm-size='4g' --rm test ./gradlew clean cleanTest webtests"
                          }
                     }
                     post {
                           always {
                              allure([
                                   reportBuildPolicy: 'ALWAYS',
                                   results: [[path: 'build/allure-results']]
                              ])
                          }
                     }
                }
                stage('chrome 83') {
                    agent {
                         node {
                            label 'docker'
                            customWorkspace "workspace/chromelast"
                         }
                    }
                    steps {
                        script {
                            sh "docker build -t test -f src/test/resources/docker/Dockerfile --no-cache --target chrome83 ./"
                            sh "docker run -v `pwd`:/tests --privileged --shm-size='4g' --rm test ./gradlew clean cleanTest webtests"
                        }
                    }
                    post {
                            always {
                                allure([
                                    reportBuildPolicy: 'ALWAYS',
                                    results: [[path: 'build/allure-results']]
                                ])
                            }
                    }
                }
             }
        }
    }
//     post {
//         always {
//             allure([
//                 reportBuildPolicy: 'ALWAYS',
//                 results: [[path: 'allure-results']]
//             ])
//         }
//     }
}
