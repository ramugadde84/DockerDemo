pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = "ramugadde84/sample-docker-images"
        DOCKER_IMAGE = "my-app"
        DOCKER_TAG = "latest"
        CONTAINER_NAME = "my-app-container"
        PORT = "9191"
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

        stage('Stop Existing Docker Container') {
            steps {
                script {
                    // Check if a container with the same name is already running and stop it
                    def containerExists = sh(script: "docker ps -q -f name=${CONTAINER_NAME}", returnStdout: true).trim()
                    if (containerExists) {
                        echo "Stopping existing container ${CONTAINER_NAME}"
                        sh "docker stop ${CONTAINER_NAME}"
                        sh "docker rm ${CONTAINER_NAME}"
                    } else {
                        echo "No existing container found, skipping stop."
                    }
                }
            }
        }

        stage('Run Docker Image') {
            steps {
                script {
                    // Run the Docker container on the specified port (9191)
                    sh 'docker run -d --name ${CONTAINER_NAME} -p ${PORT}:${PORT} ${DOCKERHUB_REPO}:${DOCKER_TAG}'
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
