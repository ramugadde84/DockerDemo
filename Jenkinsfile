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

        stage('Stop Existing Docker Container by Port') {
            steps {
                script {
                    // Find container ID based on the port
                    def containerId = sh(script: "docker ps -q -f 'ancestor=${DOCKERHUB_REPO}:${DOCKER_TAG}' -f 'publish=${PORT}'", returnStdout: true).trim()

                    if (containerId) {
                        echo "Stopping existing container with ID ${containerId} using port ${PORT}"
                        // Stop and remove the container using its ID
                        sh "docker stop ${containerId}"
                        sh "docker rm ${containerId}"
                    } else {
                        echo "No container found using port ${PORT}, skipping stop."
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
