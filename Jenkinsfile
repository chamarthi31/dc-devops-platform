pipeline {
    agent any

    environment {
        APP_NAME = 'dc-devops-app'
        VERSION = '1.0'
        REGISTRY = 'localhost:5000'
        IMAGE = "${REGISTRY}/${APP_NAME}:${VERSION}"
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Source code checkout will be handled by Jenkins SCM.'
            }
        }

        stage('Maven Test') {
            steps {
                sh '''
                    cd application
                    mvn test
                '''
            }
        }

        stage('Maven Package') {
            steps {
                sh '''
                    cd application
                    mvn clean package -DskipTests
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build \
                        -f docker/Dockerfile \
                        -t ${APP_NAME}:${VERSION} \
                        .
                '''
            }
        }

        stage('Docker Tag') {
            steps {
                sh '''
                    docker tag \
                        ${APP_NAME}:${VERSION} \
                        ${IMAGE}
                '''
            }
        }

        stage('Docker Push') {
            steps {
                sh '''
                    docker push ${IMAGE}
                '''
            }
        }

        stage('Verify Registry') {
            steps {
                sh '''
                    curl -f http://${REGISTRY}/v2/${APP_NAME}/tags/list
                '''
            }
        }
    }

    post {
        success {
            echo 'CI pipeline completed successfully.'
        }

        failure {
            echo 'CI pipeline failed. Check the failed stage and console output.'
        }
    }
}
