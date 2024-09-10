pipeline {
    agent any

    tools {
        maven 'maven' // Name defined in the Global Tool Configuration
        jdk 'jdk17'   // Name defined in the Global Tool Configuration
    }

    environment {
        MAVEN_OPTS = '-Xms256m -Xmx512m' // Memory settings for Maven
    }

    stages {
        stage('Checkout') {
            steps {
                // Checking out the specified branch from GitHub
                git branch: 'shanawaz', url: 'https://github.com/pshanawazkhan/aws-api-gateway.git'
            }
        }
        stage('Build') {
            steps {
                script {
                    // Defining Maven home directory using the Maven tool configuration
                    def mvnHome = tool 'maven'
                    
                    // Running Maven clean install using Windows `bat` commands
                    bat "${mvnHome}\\bin\\mvn clean install"
                    
                    // Checking the Java version
                    bat 'java -version'
                }
            }
        }
    }

    post {
        success {
            echo 'Build succeeded!' // Message to display on successful build
        }
        failure {
            echo 'Build failed!' // Message to display on build failure
        }
    }
}
