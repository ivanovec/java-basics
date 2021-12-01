pipeline {
    agent {
        label 'maven'
    }

    parameters {
        choice(choices: ['server1', 'server2'], description: 'select server fo test run', name: 'server')
        booleanParam(defaultValue: true, description: 'run rest tests', name: 'rest')
        booleanParam(defaultValue: false, description: 'run web tests', name: 'web')
    }

    stages {
//         stage('check test server health'){
//             steps {
//                 sh """
//                     attempt_counter=0
//                     max_attempts=120
//
//                     echo "Wait until server response 200"
//                     while [[ "\$(curl -s -o /dev/null -w ''%{http_code}'' https://${params.server})" != "200" ]]
//                     do
//                         if [ \${attempt_counter} -eq \${max_attempts} ];then
//                             echo "Max attempts reached"
//                             exit 1
//                         fi
//
//                         attempt_counter=\$((\\$attempt_counter+1))
//                         echo "wait 5 seconds"
//                         sleep 5
//                     done
//                 """
//             }
//         }
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
                  script{
//                     if(!fileExists('google-chrome-stable_current_amd64.deb')){
//                         sh "wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb"
//                         sh "apt install ./google-chrome-stable_current_amd64.deb"
//                     }
                    sh "mvn -Dtest=web.simple.** verify"
                  }
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
