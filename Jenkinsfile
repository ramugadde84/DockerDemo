pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = "ramugadde84/sample-docker-images"
        DOCKER_IMAGE = "my-app"
        DOCKER_TAG = "latest"
        CONTAINER_NAME = "my-app-container"  // Define a container name to make it easier to reference
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from SCM (GitHub)
                git url: 'https://github.com/ramugadde84/DockerDemo.git', branch: 'main'
            }
        }

        stage('Build Maven Project') {
            steps {
                script {
                    // Run Maven clean install to build the project and generate the target folder
                    sh 'mvn clean install'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image
                    sh 'docker build -t ${DOCKERHUB_REPO}:${DOCKER_TAG} .'
                }
            }
        }

        stage('Stop and Remove Existing Containers') {
            steps {
                script {
                    // Stop any running container with the same name
                    sh '''
                        if [ "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
                            echo "Stopping existing container..."
                            docker stop ${CONTAINER_NAME}
                        fi
                    '''
                    // Remove the container if it exists
                    sh '''
                        if [ "$(docker ps -a -q -f name=${CONTAINER_NAME})" ]; then
                            echo "Removing existing container..."
                            docker rm ${CONTAINER_NAME}
                        fi
                    '''
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Log in to Docker Hub (use Jenkins credentials or use a Docker login)
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USER --password-stdin'
                    }
                    // Push the Docker image to Docker Hub
                    sh 'docker push ${DOCKERHUB_REPO}:${DOCKER_TAG}'
                }
            }
        }

        stage('Run Docker Image') {
            steps {
                script {
                    // Run the Docker container with the specified name
                    sh 'docker run -d --name ${CONTAINER_NAME} -p 9191:9191 ${DOCKERHUB_REPO}:${DOCKER_TAG}'
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}