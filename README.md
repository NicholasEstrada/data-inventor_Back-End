# Data-Inventor: Uma Abordagem Automatizada para Identificação de Dados Sensíveis em Arquivos Web

![Java](https://img.shields.io/badge/Java-20-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![OCR](https://img.shields.io/badge/Tesseract-OCR-orange)

---

**Resumo do Artigo:**

O **Data-Inventor** é uma ferramenta automatizada
 para identificação de dados sensíveis em arquivos PDF publicados em sítios web, visando
 a conformidade com a **Lei Geral de Proteção de Dados (LGPD)**. A ferramenta consiste em
 um crawler que varre URLs em busca de arquivos PDF, analisando seu conteúdo por meio
 de processamento de texto e OCR (Reconhecimento Óptico de Caracteres), para identificar
 informações sensíveis como CPF, e-mail, dados de saúde, entre outros. A arquitetura
 da ferramenta é composta de módulos de coleta, análise e armazenamento, com uma
 interface de usuário. Testes realizados no sítio web da UDESC comprovaram a eficácia
 da ferramenta, com a identificação de diversos dados sensíveis em um tempo menor do
 que o processo manual. O trabalho discute os desafios da busca manual, a arquitetura da
 ferramenta, os resultados obtidos e a importância de soluções automatizadas para garantir
 a conformidade com a LGPD.

---

# Estrutura do README.md

Este README apresenta a documentação da ferramenta Data-Inventor, estruturado com informações sobre:

1.  **Título e Resumo:** Título do projeto e um resumo conciso (cópia do resumo do artigo).
2.  **Funcionalidades:** Lista as principais funcionalidades da ferramenta.
3.  **Dependências:** Lista os requisitos de software (Python, APIs).
4.  **Preocupações com segurança:** Lista das preocupações com a segurança.
5.  **Instalação:** Instruções passo a passo para instalar a ferramenta.
6.  **Docker (Opcional):** Instruções para construir e executar a ferramenta usando Docker.
7.  **Configuração:** Instruções para configurar as chaves de API.
8.  **Uso:** Descreve como reproduzir os experimentos apresentados no artigo..
9.  **Experimentos:** Explicação dos argumentos de linha de comando e exemplos de uso.
10. **Estrutura do Código:** Breve visão geral da organização do código-fonte.
11. **Extensibilidade**: Como adicionar novas fontes e novos exportadores.
12. **Licença:** Informações sobre a licença do projeto.

- Objetivo do projeto e resumo
- Organização do repositório
- Selos considerados para avaliação
- Ambiente de execução
- Dependências
- Segurança
- Instruções de instalação e teste mínimo
- Reproduzibilidade dos experimentos
- Licença e contato

---
### Dependências principais:

- Java 20
- Spring Boot 3.2.2
- Spring Security + JWT
- PostgreSQL 13+
- Maven
- Tess4J (wrapper para Tesseract OCR)
- Apache PDFBox
- Jsoup (HTML parsing)
- Commons Text (Levenshtein distance)
- Node.js 18+
- npm 9+
- Angular CLI 16+


---

# Preocupações com Segurança

A ferramenta não apresenta riscos diretos ao avaliador. No entanto:

- O OCR pode consumir recursos consideráveis dependendo do volume de arquivos.
- É importante garantir que os arquivos analisados não contenham códigos maliciosos.
- Evitar executar a aplicação com permissões elevadas.
---
## Instalação e Execução

### 1. Pré-requisitos

- **Sistema Operacional Recomendado:** Ubuntu 22.04 ou Windows 10+
- **Java:** 20+
- **Maven:** 3.8+
- **Banco de Dados:** PostgreSQL 13+
- **OCR:** Tesseract 5.0+
- **Hardware mínimo:**  
  - 2 vCPUs  
  - 4GB RAM  
  - 2GB espaço em disco

### 2. Clonar o projeto

```bash
git clone https://github.com/NicholasEstrada/data-inventor_Back-End.git
cd data-inventor_Back-End
```

### 3. Configurar o banco de dados

Abra o PostgreSQL e crie o banco:

```sql
CREATE DATABASE data_inventor;
```

**Configure o usuário e senha no arquivo `application.properties`:**

```properties
spring.datasource.username=postgres
spring.datasource.password=postgres
```


---

### 5. Executar o projeto

```bash
./mvnw spring-boot:run
```

A API estará disponível em:  
📍 `http://localhost:8080`

---

## 🖥️ Front-end Angular

O projeto Angular foi desenvolvido para consumir essa API. Você pode encontrá-lo [aqui](https://github.com/NicholasEstrada/data-inventor_Front-End).

## Comandos úteis no para instalar o projeto:

### 1. Clonar o projeto
```bash
git clone https://github.com/NicholasEstrada/data-inventor_Front-End.git
cd data-inventor_Front-End
```

### 2. Instalação
```bash
npm install
```

### 3. Iniciando o projeto
```bash
npm start
```
A aplicação estará disponível em:  
📍 `http://localhost:4200`

---





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

Com o Docker iniciado, abra o CMD e navegue até o caminho raiz onde aplicou a estrutura final e execute o seguinte comando abaixo
```bash
docker compose -f docker-compose.yml up -d --build
```

---

# ✅ Teste Mínimo

1. Com o servidor ativo em `http://localhost:8080`, autentique-se via JWT.
2. Use a interface Angular ou Postman para enviar um domínio que contenha arquivos PDF públicos.
3. Verifique os resultados retornados em `/dados-sensiveis?dominio=example.com`.

Resultado esperado:  
→ JSON contendo URLs de arquivos analisados e os dados sensíveis encontrados.

---

# 🧪 Experimentos

### Reivindicação #1: Identificação de Dados Sensíveis em PDF

**Objetivo:** Demonstrar a capacidade do sistema em detectar CPFs, RGs, datas e nomes completos em arquivos PDF públicos.

**Procedimento:**

1. Configure o sistema como descrito.
2. Utilize o domínio de teste `http://www.exemplo.com.br/documentos`.
3. Execute a análise via POST em `/crawler/dominio`.
4. Consulte os dados extraídos via `/dados-sensiveis`.

**Recurso esperado:** 1 GB RAM, 500 MB Disco  
**Resultado:** JSON com campos classificados e trechos de texto contendo os dados sensíveis.

---

### Reivindicação #2: Análise de Arquivos com OCR

**Objetivo:** Avaliar a capacidade do OCR de extrair dados sensíveis de PDFs escaneados.

**Procedimento:**

1. Insira na pasta de teste um PDF escaneado (imagem).
2. Aponte o domínio para o recurso simulado.
3. Execute a análise.
4. Verifique se os dados foram extraídos corretamente.

**Resultado Esperado:**  
→ Extração precisa de dados após aplicação do Tesseract, mesmo em documentos com baixa resolução.


## 📸 Demonstração
GIF exibindo a navegação no projeto

![output](https://github.com/user-attachments/assets/111bc88d-0963-447c-ba25-ad64d8f4833c)

---

##  Contribuidores

| Nome            | Função             |
|-----------------|--------------------|
| Nicholas        | Desenvolvedor Fullstack |
| Marco Rojas     | Orientador |

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

## 📬 Contato

Em caso de dúvidas ou sugestões:

- nicholas26cervatti@gmail.com
- [LinkedIn](https://www.linkedin.com/in/nicholas-estrada-909242174/)
