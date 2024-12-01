pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = "ramugadde84/sample-docker-images"
        DOCKER_IMAGE = "my-app"
        DOCKER_TAG = "latest"
        DOCKER_CONTAINER = "spring-docker-container"
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
                    // -t flag tags the Docker image with the provided tag
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
                    // Stop and remove the existing container (if it exists)
                    sh '''
                    if [ $(docker ps -q -f name= ${DOCKER_CONTAINER}) ]; then
                        docker stop ${DOCKER_CONTAINER}
                        docker rm ${DOCKER_CONTAINER}
                    fi
                    '''

                    // Run the Docker container with the updated image
                    sh 'docker run -d --name ${DOCKER_CONTAINER} -p 9191:8080 ${DOCKERHUB_REPO}:${DOCKER_TAG}'
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
