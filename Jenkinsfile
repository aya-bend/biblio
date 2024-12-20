pipeline {
    agent any
    environment {
        PATH = "C:/Program Files/Git/cmd;${env.PATH}"
    }
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }
    stages {
        stage('Clone') {
            steps {
                bat "git clone https://github.com/aya-bend/biblio.git"
            }
        }
        stage('Pull') {
            steps {
                bat "git pull"
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Run') {
            steps {
                sh 'mvn exec:java'
            }
        }
}