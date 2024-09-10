# Grafana

`helm repo add grafana https://grafana.github.io/helm-charts`
`helm repo update`
`helm install my-grafana grafana/grafana --values grafana.yml --namespace logging`

## Password

`kubectl get secret --namespace logging my-grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo`

## Expose

`export POD_NAME=$(kubectl get pods --namespace logging -l "app.kubernetes.io/name=grafana,app.kubernetes.io/instance=my-grafana" -o jsonpath="{.items[0].metadata.name}")`
`kubectl --namespace logging port-forward $POD_NAME 3000`
