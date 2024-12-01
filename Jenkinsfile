pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = 'ramugadde84/sample-docker-images'  // Docker Hub repository name
        DOCKER_TAG = "latest"  // Use a tag such as 'latest' or version
    }

    tools {
        maven 'Apache maven'  // Maven version from Jenkins tool configuration
        dockerTool 'Docker'  // Docker tool configured in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from GitHub
                git 'https://github.com/ramugadde84/DockerDemo.git'
            }
        }

        stage('Build Maven Project') {
            steps {
                // Build the project using Maven
                sh 'mvn clean install'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image using the Dockerfile in the repository
                    def customImage = docker.build("${DOCKER_IMAGE_NAME}:${DOCKER_TAG}")
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    // Login to Docker Hub
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        // Push the Docker image to Docker Hub
                        customImage.push()
                    }
                }
            }
        }

        stage('Run Docker Image') {
            steps {
                // Run the Docker image in a container
                script {
                    // Run the container in the background
                    sh 'docker run -d ${DOCKER_IMAGE_NAME}:${DOCKER_TAG}'
                }
            }
        }
    }
}
