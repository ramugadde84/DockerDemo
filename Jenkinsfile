pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = "ramugadde84/sample-docker-images"
        DOCKER_TAG = "latest"
        DOCKER_CONTAINER = "spring-docker-container"
        K8S_DEPLOYMENT_YAML = "deployment.yaml"  // Path to your Kubernetes deployment YAML
        K8S_CLUSTER_NAME = "minikube"  // Minikube as the Kubernetes cluster name
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

        stage('Stop Existing Docker Container') {
            steps {
                script {
                    // Stop and remove existing container if it exists
                    //  -q: Only display numeric IDs
                    // -f: Filter output based on conditions provided
                    def containerId = sh(script: "docker ps -q -f name=${DOCKER_CONTAINER}", returnStdout: true).trim()

                    if (containerId) {
                        echo "Stopping and removing existing container with ID ${containerId}"
                        sh "docker stop ${containerId}"
                        sh "docker rm ${containerId}"
                    } else {
                        echo "No existing container with name ${DOCKER_CONTAINER} found."
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Ensure Minikube's kubectl context is set
                    sh 'kubectl config use-context minikube'

                    // Deploy the Docker container to Kubernetes using kubectl and the deployment YAML
                    sh '''
                    kubectl apply -f ${K8S_DEPLOYMENT_YAML}
                    '''
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    // Check the status of the pods in the Kubernetes cluster
                    sh '''
                    kubectl get pods
                    '''
                }
            }
        }

        stage('Expose Kubernetes Service') {
            steps {
                script {
                    // Expose the service to access the app externally (optional, if not already exposed)
                    sh '''
                    kubectl expose deployment my-app-deployment --type=LoadBalancer --port=80 --target-port=8080
                    '''
                }
            }
        }

        stage('Port Forward to Localhost') {
            steps {
                script {
                    // Forward port 80 (service) from Kubernetes to port 8080 on localhost
                    echo 'Port forwarding from Kubernetes to localhost on port 8080'
                    sh 'kubectl port-forward service/my-app-service 9191:9191 &'
                }
            }
        }

        stage('Kill Pods and Services (Cleanup)') {
            steps {
                script {
                    // Stop and delete the Kubernetes deployment and services
                    echo 'Cleaning up by deleting pods and services.'
                    sh '''
                    kubectl delete service my-app-service
                    kubectl delete deployment my-app-deployment
                    '''
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
