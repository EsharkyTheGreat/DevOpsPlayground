# Kubernetes

## Setup Local Multinode Cluster using minikube

`minikube start --nodes 3 -p galaxycluster`
`kubectl get nodes`
`kubectl label node <node_name> node-role.kubernetes.io/worker=worker`
