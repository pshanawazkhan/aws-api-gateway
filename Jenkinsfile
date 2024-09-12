pipeline {
    agent any

    environment {
        MAVEN_OPTS = '-Xms256m -Xmx512m'
        DOCKER_REGISTRY_CREDENTIALS = 'dockerhub_credentials'    // Jenkins Credentials ID for DockerHub
        DOCKER_IMAGE = 'phanawazkhan/apigateway'               // Docker image name
        DOCKER_TAG = '6.6'                                     // Tag for the Docker image
      //  KUBECONFIG_CREDENTIALS = 'kubeconfig_credentials'      // Jenkins Credentials ID for kubeconfig
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

                    // Build and push the Docker image
                    withCredentials([usernamePassword(credentialsId: 'dockerhub_credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
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
                    withCredentials([file(credentialsId: 'kubeconfig_credentials', variable: 'KUBECONFIG')]) {
                        // Use kubectl to apply Kubernetes resources
                        bat 'kubectl apply -f namespace.yaml'
						bat 'kubectl apply -f deployment.yaml'
						bat 'kubectl apply -f service.yaml'
                    }
                }
            }
        }
		
		  stage("Verify deployment") {
		  
		    steps{
			   script{
			   
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
