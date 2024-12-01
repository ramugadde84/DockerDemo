pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = "ramugadde84/sample-docker-images"
        DOCKER_TAG = "latest"
        DOCKER_CONTAINER = "spring-docker-container"
        K8S_DEPLOYMENT_YAML = "deployment.yaml"
        K8S_SERVICE_NAME = "my-app-service"
        K8S_DEPLOYMENT_NAME = "my-app-deployment"
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
                    // Log in to Docker Hub using Jenkins credentials
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials',
                                                      usernameVariable: 'DOCKER_USER',
                                                      passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USER --password-stdin'
                    }
                    // Push the Docker image to Docker Hub
                    sh 'docker push ${DOCKERHUB_REPO}:${DOCKER_TAG}'
                }
            }
        }

        stage('Clean Up Existing Deployment and Service') {
            steps {
                script {
                    // Delete the existing service and deployment if they exist
                    sh '''
                    kubectl delete service ${K8S_SERVICE_NAME} || true
                    kubectl delete deployment ${K8S_DEPLOYMENT_NAME} || true
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Deploy the Docker container to Kubernetes using kubectl and the deployment YAML
                    sh 'kubectl apply -f ${K8S_DEPLOYMENT_YAML}'
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    // Check the status of the pods in the Kubernetes cluster
                    sh 'kubectl get pods'
                }
            }
        }

        stage('Expose Kubernetes Service') {
            steps {
                script {
                    // Expose the service to access the app externally (optional, if not already exposed)
                    sh 'kubectl expose deployment ${K8S_DEPLOYMENT_NAME} --name=${K8S_SERVICE_NAME} --type=LoadBalancer --port=80 --target-port=8080'
                }
            }
        }

        stage('Port Forward to Localhost') {
            steps {
                script {
                    // Forward port 80 (service) from Kubernetes to port 8080 on localhost
                    echo 'Port forwarding from Kubernetes to localhost on port 8080'
                    sh 'kubectl port-forward service/${K8S_SERVICE_NAME} 9191:9191 &'
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
