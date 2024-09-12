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
       
       stage('Check KUBECONFIG') {
    steps {
        script {
            withCredentials([file(credentialsId: K8S_REGESTRY_CREDENTIALS, variable: 'KUBECONFIG')]) {
                // PowerShell to decode Base64 content of the kubeconfig
                powershell '''
                $kubeconfigContent = Get-Content -Path $env:KUBECONFIG -Raw
                $caCrtContent = [System.Convert]::FromBase64String($kubeconfigContent | Select-String -Pattern "certificate-authority-data" | ForEach-Object {$_ -replace "certificate-authority-data:", ""})
                $clientCrtContent = [System.Convert]::FromBase64String($kubeconfigContent | Select-String -Pattern "client-certificate-data" | ForEach-Object {$_ -replace "client-certificate-data:", ""})
                $clientKeyContent = [System.Convert]::FromBase64String($kubeconfigContent | Select-String -Pattern "client-key-data" | ForEach-Object {$_ -replace "client-key-data:", ""})

                # Save the decoded files for debugging or future use
                [System.IO.File]::WriteAllBytes("C:\\path\\to\\ca.crt", $caCrtContent)
                [System.IO.File]::WriteAllBytes("C:\\path\\to\\client.crt", $clientCrtContent)
                [System.IO.File]::WriteAllBytes("C:\\path\\to\\client.key", $clientKeyContent)

                # Output the decoded content (optional)
                Write-Host "Decoded ca.crt:"
                Get-Content "C:\\path\\to\\ca.crt"
                '''
                
                // Check contents for debugging
                bat 'type %KUBECONFIG%' // Outputs the kubeconfig content
                bat 'kubectl config view' // Verifies the config used by kubectl
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
