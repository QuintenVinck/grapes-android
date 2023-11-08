// pipeline {
//     agent any

//     stages {

//         stage('Checkout') {
//             steps {
//                 checkout scm
//             }
//         }

//         stage('Build and Push .NET App') {
//             steps {
//                 script {
//                     // Use --password-stdin for secure password handling
//                     withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
//                         def registry = 'https://registry.hub.docker.com'
//                         def image = 'quintenv/dotnet-app:latest'
                        
//                         sh "echo \${DOCKER_PASSWORD} | docker login -u \${DOCKER_USERNAME} --password-stdin \${registry}"
//                         sh "docker build -t quintenv/dotnet-app ."
//                         sh "docker push quintenv/dotnet-app"
//                     }
//                 }
//             }
//         }
//     }
// }

pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    checkout scm
                }
            }
        }
        
        // stage('Build Android Project') {
        //     steps {
        //         // Use Gradle to build the Android project
        //         sh './gradlew build'
        //     }
        // }
        
        stage('Build & Push to android app') {
            steps {
                script {
                    // Use --password-stdin for secure password handling
                    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        def registry = 'https://registry.hub.docker.com'
                        def image = 'quintenv/android-app:latest'
                        
                        sh "echo \${DOCKER_PASSWORD} | docker login -u \${DOCKER_USERNAME} --password-stdin \${registry}"
                        sh "docker build -t quintenv/android-app ."
                        sh "docker push quintenv/android-app"
                    }
                }
            }
        }
    }
}
