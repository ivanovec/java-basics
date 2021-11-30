pipeline {
    agent any

    tools {
        jdk "1.8"
        maven "3.8.4"
    }

    parameters {
        booleanParam(defaultValue: true, description: 'run rest tests', name: 'rest')
        booleanParam(defaultValue: false, description: 'run web tests', name: 'web')
    }

    stages {
        stage('rest tests') {
            when {
                expression { return params.rest }
            }
            steps {
                sh "mvn -Dtest=rest.** verify"
            }
        }
        stage('web tests') {
             when {
                 expression { return params.web }
             }
             steps {
                  sh "mvn -Dtest=web.simple.** verify"
             }
        }
    }
    post {
        always {
            allure([
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'allure-results']]
            ])
        }
    }
}
