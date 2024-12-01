pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = " "
        DOCKER_IMAGE = "my-app"
        DOCKER_TAG = "latest"
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
                    // Run the Docker container
                    // -d flag runs the container in detached mode
                    sh 'docker run -d -p 8080:9191 ${DOCKERHUB_REPO}:${DOCKER_TAG}'
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
