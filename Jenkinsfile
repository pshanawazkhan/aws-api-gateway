import java.util.Base64
pipeline {
    agent any

    environment {
        MAVEN_OPTS = '-Xms256m -Xmx512m'
        DOCKER_REGISTRY_CREDENTIALS = 'dockerhub_credentials'
        K8S_REGESTRY_CREDENTIALS ='kubeconfig_credentials'
        DOCKER_IMAGE = 'phanawazkhan/apigateway'
        DOCKER_TAG = '6.6'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'shanawaz', url: 'https://github.com/pshanawazkhan/aws-api-gateway.git'
            }
        }
      /*  stage('Build and Push Docker Image') {
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
        } */
       
      
      stage('Check KUBECONFIG') {
    steps {
        script {
            withCredentials([file(credentialsId: K8S_REGESTRY_CREDENTIALS, variable: 'KUBECONFIG')]) {
                // Decode Base64 strings inside the kubeconfig using Groovy
                def kubeconfig = readFile(env.KUBECONFIG)

                def caCrtBase64 = kubeconfig.split('certificate-authority-data: ')[1].split('\n')[0]
                def clientCrtBase64 = kubeconfig.split('client-certificate-data: ')[1].split('\n')[0]
                def clientKeyBase64 = kubeconfig.split('client-key-data: ')[1].split('\n')[0]

                def caCrt = Base64.decoder.decode(caCrtBase64)
                def clientCrt = Base64.decoder.decode(clientCrtBase64)
                def clientKey = Base64.decoder.decode(clientKeyBase64)

                // Write decoded data to files
                writeFile file: 'ca.crt', text: new String(caCrt)
                writeFile file: 'client.crt', text: new String(clientCrt)
                writeFile file: 'client.key', text: new String(clientKey)

                // Print the contents of decoded files (optional)
                bat 'type ca.crt'
                bat 'type client.crt'
                bat 'type client.key'

                // Check the KUBECONFIG content
                bat 'type %KUBECONFIG%'
                bat 'kubectl config view'
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
