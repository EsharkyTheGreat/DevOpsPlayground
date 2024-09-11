`helm repo add grafana https://grafana.github.io/helm-charts`
`helm repo update`
`helm install --namespace logging --values monolith.yml loki grafana/loki`
