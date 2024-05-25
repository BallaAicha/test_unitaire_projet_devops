pipeline {
    agent any
    tools{
        maven 'maven383'
    }
    stages {
        stage('Unit Tests') {
            steps {
                echo 'Running Unit Tests'
                sh 'mvn --version'
                sh 'mvn test'
                junit 'target/surefire-reports/*.xml'
            }
        }
        stage('package') {
            steps {
                echo 'Packaging the application'
                sh 'mvn package -DskipTests=true'
                archiveArtifacts artifacts: 'target/*.jar'

            }
        }
    }
}
