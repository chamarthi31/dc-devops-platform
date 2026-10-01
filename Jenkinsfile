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

        stage('Deploy') {
            steps {
                sh '''
                    echo "Pulling image from registry..."
                    docker pull ${IMAGE}

                    echo "Stopping existing application..."
                    docker stop ${APP_NAME} || true

                    echo "Removing existing application..."
                    docker rm ${APP_NAME} || true

                    echo "Starting new application..."
                    docker run -d \
                        --name ${APP_NAME} \
                        --network dc-devops-net \
                        -p 8081:8081 \
                        ${IMAGE}
                '''
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                    echo "Waiting for application to start..."
                    sleep 10

                    echo "Checking application health..."
                    curl -f http://localhost:8081/health

                    echo ""
                    echo "Application deployment successful."
                '''
            }
        }
    }

    post {
        success {
            echo 'CI/CD pipeline completed successfully.'
        }

        failure {
            echo 'CI/CD pipeline failed. Check the failed stage and console output.'
        }
    }
}
