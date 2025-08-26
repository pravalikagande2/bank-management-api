// File: Jenkinsfile
pipeline {
    agent any

    tools {
        jdk 'JDK-22'
        maven 'Maven-3.9'
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building the application...'
                // Use 'bat' for Windows commands
                bat 'mvn clean package -DskipTests'
            }
        }
        stage('Test') {
            steps {
                echo 'Running all tests...'
                bat 'mvn test'
            }
        }
    }
}
