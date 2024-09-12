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

        stage('Check KUBECONFIG') {
            steps {
                script {
                    withCredentials([file(credentialsId: K8S_REGESTRY_CREDENTIALS, variable: 'KUBECONFIG')]) {
                        // Read kubeconfig content
                        def kubeconfig = readFile(env.KUBECONFIG)

                        // Extract and sanitize Base64-encoded data (removing spaces and newlines)
                        def caCrtBase64 = kubeconfig.split('certificate-authority-data: ')[1].split('\n')[0].trim().replaceAll("\\s", "")
                        def clientCrtBase64 = kubeconfig.split('client-certificate-data: ')[1].split('\n')[0].trim().replaceAll("\\s", "")
                        def clientKeyBase64 = kubeconfig.split('client-key-data: ')[1].split('\n')[0].trim().replaceAll("\\s", "")

                        // Decode Base64 strings
                        def caCrt = Base64.decoder.decode(caCrtBase64)
                        def clientCrt = Base64.decoder.decode(clientCrtBase64)
                        def clientKey = Base64.decoder.decode(clientKeyBase64)

                        // Write decoded data to files
                        writeFile file: 'ca.crt', text: new String(caCrt)
                        writeFile file: 'client.crt', text: new String(clientCrt)
                        writeFile file: 'client.key', text: new String(clientKey)

                         // Update the KUBECONFIG to point to these files instead of using base64-encoded inline data
                      def updatedKubeconfig = kubeconfig.replaceFirst('certificate-authority-data: .*', 'certificate-authority: ca.crt').replaceFirst('client-certificate-data: .*', 'client-certificate: client.crt').replaceFirst('client-key-data: .*', 'client-key: client.key')

                // Write the updated KUBECONFIG
                writeFile file: 'updated_kubeconfig', text: updatedKubeconfig

                // Print the contents of updated KUBECONFIG (optional)
                bat 'type updated_kubeconfig'

                // Use the updated KUBECONFIG for kubectl
                bat 'kubectl config view --kubeconfig=updated_kubeconfig'

                // Test kubectl commands with the new config
                bat 'kubectl get pods --kubeconfig=updated_kubeconfig'
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
