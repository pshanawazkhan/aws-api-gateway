pipeline {
    agent any

    tools {
        maven 'maven' // Name defined in the Global Tool Configuration
        jdk 'jdk17'   // JDK tool
    }

    environment {
        MAVEN_OPTS = '-Xms256m -Xmx512m'
        DOCKER_IMAGE = 'phanawazkhan/apigateway'               // Docker image name
        DOCKER_TAG = '4.6'                                     // Tag for the Docker image
        DOCKER_CREDENTIALS_ID = 'docker_id'    // Credentials ID which we configure ->Id:- docker_i
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'shanawaz', url: 'https://github.com/pshanawazkhan/aws-api-gateway.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    def mvnHome = tool 'maven'
                    // Build the project using Maven
                    bat "${mvnHome}\\bin\\mvn clean install"
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    // Build Docker image
                    bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    // Use Jenkins credentials to log in to DockerHub
                  withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'user', passwordVariable: 'password')]) {
                        bat "docker login -u ${user} -p ${password}"
                    }
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    // Push the Docker image to DockerHub
                    bat "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }
    }

    post {
        success {
            echo 'Docker image build and push succeeded!'
        }
        failure {
            echo 'Build failed. Docker image not pushed.'
        }
    }
}