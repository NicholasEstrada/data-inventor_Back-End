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

1. [Selos Considerados](#selos-considerados) - Critérios de avaliação do projeto
2. [Informações Básicas](#informações-básicas) - Requisitos de hardware e software
3. [Dependências](#dependências) - Componentes e bibliotecas necessárias
4. [Preocupações com Segurança](#preocupações-com-segurança) - Considerações sobre segurança
5. [Instalação](#instalação) - Processo de configuração e execução
6. [Teste Mínimo](#teste-mínimo) - Validação básica do funcionamento
7. [Experimentos](#experimentos) - Reivindicações e procedimentos de teste
8. **Licença** - Termos de uso do software
9. **Contato** - Informações dos desenvolvedores


## Selos Considerados

Os selos considerados para este projeto são:
- **Disponível**: O artefato está publicamente acessível
- **Funcional**: Todas as funcionalidades descritas estão operacionais
- **Reproduzível**: Os experimentos podem ser replicados com os dados fornecidos

## Informações Básicas

### Ambiente de Execução Recomendado:
- **Sistema Operacional**: Ubuntu 22.04 LTS ou Windows 10+
- **Hardware Mínimo**:
  - Processador: 2 núcleos (x86_64)
  - Memória RAM: 4GB
  - Armazenamento: 10GB (SSD recomendado)
  - Conexão de internet estável

### Requisitos de Software:
- Java JDK 20+
- Node.js 18+
- PostgreSQL 13+
- Tesseract OCR 5.0+
- Docker (opcional para execução em containers)

---
## Dependências

### Backend (Java/Spring Boot):
- Java 20
- Spring Boot 3.2.2
- Spring Security + JWT
- PostgreSQL 13+
- Maven
- Tess4J (wrapper para Tesseract OCR)
- Apache PDFBox
- Jsoup (HTML parsing)
- Commons Text (Levenshtein distance)
### Frontend (Angular):
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
## Instalação

## Back-end Spring Boot

### 1. Clonar o projeto

```bash
git clone https://github.com/NicholasEstrada/data-inventor_Back-End.git
cd data-inventor_Back-End
```

### 2. Configurar o banco de dados

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

### 3. Executar o projeto

```bash
./mvnw spring-boot:run
```

A API estará disponível em:  
📍 `http://localhost:8080`

---

###  Front-end Angular

O projeto Angular foi desenvolvido para consumir essa API. Você pode encontrá-lo [aqui](https://github.com/NicholasEstrada/data-inventor_Front-End).

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

# Teste Mínimo

1. **Criando usuário:** Com o servidor ativo em `http://localhost:8080`, e a aplicação do Front End no endereço `http://localhost:4200`. Crie um usuário acessando a tela de cadastro, disponível no botão `Não possui usuário? Cadastre-se` ao abrir o endereço do Front End e aplique as informações para cadastro ou faça via CMD o CURL abaixo
```
curl -X POST http://localhost:8080/auth/register \
-H "Content-Type: application/json" \
-d '{"username": "nomeUsuario", "password": "12345678"}'
```
2. **Login:** Ao criar as credenciais autentique inserindo as informações registradas na tela de login. Se for usar a aplicação via terminal, guarde a resposta utilizar nas próximas requisições, exemplo: `{"token":"eyJhbGciOiJIUz...3VhcmlvIiwiZXhwI"}` e substitua nas linhas `-H "Authorization: Bearer AQUI`.

```
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "nomeUsuario", "password": "12345678", "grant_type": "password"}'
``` 


3. **Realizando Busca:** Ao acessar a tela do home, navegue na aba lateral acessando a opção `Dominio` logo após acesse o botão `+Novo`, inclua uma url qual deseja processar e inicie clicando em `Vasculhar`. Recomendação de link: [manualdoservidor.ifc.edu.br/programa-de-gestao-e-desempenho-modalidade-teletrabalho/](manualdoservidor.ifc.edu.br/programa-de-gestao-e-desempenho-modalidade-teletrabalho/)

```
curl -v -X POST http://localhost:8080/inventor/buscaPorDominio \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT_AQUI" \
  -d '{"dominio":"manualdoservidor.ifc.edu.br/programa-de-gestao-e-desempenho-modalidade-teletrabalho/"}'

```


4. Verifique os resultados retornados da busca acessando na aba lateral na opção `Dados Sensíveis Processados` e selecione o link que deseja verificar o resultado da busca e clique em `Consultar`. Isso ira mostrar os resultados de forma paginada de cada

```
curl -X GET http://localhost:8080/inventor/dominios \
  -H "Authorization: Bearer SEU_TOKEN_JWT_AQUI"
```
5. Se utilizar via terminal ao receber os resultados verifique o `id` que queira os resultados e aplique na URL de busca em `APLIQUE_AQUI_O_ID`, nesta consulta é possível filtrar a quantidade de arquivos encontrados indicando o `page=` e `size=`, respectivamente numero da página e tamanho da página.
```
curl -X GET "http://localhost:8080/inventor/dadosSensiveisDomain?dominioId=APLIQUE_AQUI_O_ID&page=0&size=100" \
  -H "Authorization: Bearer SEU_TOKEN_JWT_AQUI"

```

Resultado esperado:  
→ JSON contendo URLs de arquivos analisados e os dados sensíveis encontrados.

---

#  Experimentos

## Reivindicação #1: Identificação de Dados Sensíveis

**Objetivo:** Demonstrar a capacidade do sistema em detectar dados sensíveis em arquivos em sítios web públicos.

**Procedimento:**

1. Configure o sistema como descrito.
2. Utilize o domínio `https://udesc.br/`.
3. Execute a análise via POST em `/inventor/buscaPorDominio` ou na interface da aplicação.
4. Aguarde a analise, este dominio tem um tempo de 30~50 minutos de demora por conta de sua dimenção.
5. Consulte os dados extraídos via `/inventor/dadosSensiveisDomain` ou na interface da aplicação.

**Recurso esperado:** 4 GB RAM, 500 MB Disco  
**Resultado:** JSON com campos classificados e trechos de texto contendo os dados sensíveis.

### Amostragem dos Resultados

Os resultados com base realizado no dia 08/10/2024 no sitio web `https://udesc.br/` foram os seguintes:

| Métrica                                      | Valor |
| -------------------------------------------- | ----- |
| Páginas visitadas                            | 103   |
| Arquivos com dados sensíveis                 | 864   |
| Dados sensíveis identificados                | 2.866 |
| PDFs processados (OCR)                       | 23    |
| PDFs editáveis processados                   | 2.842 |
| CPFs encontrados                             | 17    |
| E-mails encontrados                          | 2.249 |
| Outros dados sensíveis (religião, política…) | 576   |

#### E-mails
Total de e-mails encontrados: 2.249

- Potencialmente expostos sem consentimento: 9 (que não faziam parte do dominio `@udesc.br`)
- Localizados em URLs específicas do domínio, úteis para auditoria e ações corretivas.

#### CPFs
Total de CPFs encontrados: 17

- CPFs únicos: 9
- Alguns registros estavam duplicados em diferentes arquivos.

#### Outros Dados Sensiveis

| Categoria                    | Ocorrências |
| ---------------------------- | ----------- |
| Convicção religiosa          | 129         |
| Origem racial (ex: "branco") | 137         |
| Origem racial (ex: "negro")  | 201         |
| Dados biométricos            | 15          |
| Dados de saúde               | 8           |
| Dados genéticos              | 1           |
| Opinião política             | 29          |


---

## Reivindicação #2: Análise de Arquivos com OCR

**Objetivo:** Avaliar a capacidade do OCR de extrair dados sensíveis de PDFs escaneados.

**Procedimento:**

1. Configure o sistema como descrito.
2. Utilize um link para um PDF de imagem como exemplo: `https://ifc.edu.br/wp-content/uploads/2023/03/quabro_horario_servidor.pdf`.
3. Execute a análise via POST em `/inventor/buscaPorDominio` ou na interface da aplicação.
4. Aguarde a analise, por se tratar de um arquivo o resultado é praticamente instantâneo.
5. Consulte os dados extraídos via `/inventor/dadosSensiveisDomain`.

**Resultado Esperado:**  
→ Extração de dados mesmo ele sendo imagem, aplicação do Tesseract.


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
