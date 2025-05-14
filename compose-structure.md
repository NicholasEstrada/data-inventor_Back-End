
# Estrutura do Projeto com Docker Compose

Para aplicar a configuração do `docker-compose.yml` presente neste projeto, ele deve estar estruturado com as seguintes pastas abaixo, para que funcione orquestradamente. Mova o arquivo `docker-compose.yml` para uma pasta superior criada para dar o clone dos projetos frontend e backend.

### Estrutura Final

```
data-inventor-compose/
│
├── data-inventor_Back-End/         ← Código Java + Dockerfile do backend
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── data-inventor_Front-End/        ← Código Angular + Dockerfile do frontend
│   ├── src/
│   └── Dockerfile
│
└── docker-compose.yml              ← Composição dos serviços
```