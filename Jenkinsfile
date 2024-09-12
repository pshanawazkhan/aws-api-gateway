pipeline {
    agent any

    environment {
        MAVEN_OPTS = '-Xms256m -Xmx512m'
        DOCKER_REGISTRY_CREDENTIALS = 'dockerhub_credentials'   //DOCKER CREDENTIALS
		K8S_REGESTRY_CREDENTIALS ='kubeconfig_credentials'      //K8S Credentials
        DOCKER_IMAGE = 'phanawazkhan/apigateway'
        DOCKER_TAG = '6.6'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'shanawaz', url: 'https://github.com/pshanawazkhan/aws-api-gateway.git'
            }
        }
        stage('Build and Push Docker Image') {
            steps {
                script {
                    def mvnHome = tool 'maven'
                    bat "${mvnHome}\\bin\\mvn clean install"

                    withCredentials([usernamePassword(credentialsId: DOCKER_REGISTRY_CREDENTIALS, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        bat """
                            docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                            docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}
                            docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                        """
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withCredentials([file(credentialsId: K8S_REGESTRY_CREDENTIALS, variable: 'KUBECONFIG')]) {
                        bat 'kubectl apply -f namespace.yaml'
                        bat 'kubectl apply -f deployment.yaml'
                        bat 'kubectl apply -f service.yaml'
                    }
                }
            }
        }
        stage("Verify Deployment") {
            steps {
                script {
                    bat 'kubectl get pods -n application'
                }
            }
        }
    }

    post {
        success {
            echo 'Build and deployment succeeded!'
        }
        failure {
            echo 'Build or deployment failed!'
        }
    }
}