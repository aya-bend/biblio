pipeline {
    agent any
    environment {
        SONAR_PROJECT_KEY = 'gestionBib'
        SONAR_SCANNER_HOME = tool 'SonarQubeScanner'

    }
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }
    stages {
        stage('Checkout') {
            steps {

               checkout scm

            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

         stage('Quality Analysis') {


                     steps {
                                         withCredentials([string(credentialsId: 'gestionBib_token', variable: 'SONAR_TOKEN')]) {

                                                 withSonarQubeEnv('SonarQube') {
                                                         sh """
                                      mvn sonar:sonar \
                                     -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                                     -Dsonar.login=${SONAR_TOKEN}
                                     """
                                                 }
                                         }
                                 }
                 }

        stage('Deploy') {
            steps {
                echo 'Déploiement simulé réussi'
            }
        }
    }
    post {
        success {
            emailext to: 'bendaoude.aya@gmail.com',
                subject: 'Build Success',
                body: 'Le build a été complété avec succès.'
        }
        failure {
            emailext to: 'bendaoude.aya@gmail.com',
                subject: 'Build Failed',
                body: 'Le build a échoué.'
        }
    }
}