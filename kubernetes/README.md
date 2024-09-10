# Kubernetes

## Minikube

### Setup Local Multinode Cluster using minikube

`minikube start --nodes 3 -p galaxycluster`
`kubectl get nodes`
`kubectl label node <node_name> node-role.kubernetes.io/worker=worker`

```
minikube addons enable default-storageclass
minikube addons enable storage-provisioner
minikube addons enable metrics-server
```
