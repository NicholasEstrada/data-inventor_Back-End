# 🕵️‍♂️ Data-Inventor

> Aplicação para localizar dados sensíveis em arquivos PDF de sítios web, utilizando OCR, análise semântica e validação por expressões regulares.

![Java](https://img.shields.io/badge/Java-20-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![OCR](https://img.shields.io/badge/Tesseract-OCR-orange)
![Status](https://img.shields.io/badge/Status-Desenvolvimento-yellow)

---

## 📌 Descrição

O **Data-Inventor** é uma API desenvolvida em Spring Boot que identifica dados sensíveis, aplicando análise textual e OCR em arquivos de sites.  
Destinado à **validação de conformidade com a LGPD**, o projeto será apresentado no **Salão de Ferramentas do Simpósio Brasileiro de Cibersegurança**.

---

## 🚀 Tecnologias Utilizadas

- Java 20
- Spring Boot 3.2.2
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Tess4J (Tesseract OCR)
- Apache PDFBox
- Jsoup (para análise HTML)
- Commons Text (Levenshtein)

---

## 🏗️ Instalação e Execução

### 1. Pré-requisitos

- Java 20+
- Maven 3.8+
- PostgreSQL 13+
- Git

### 2. Clonar o projeto

```bash
git clone https://github.com/NicholasEstrada/data-inventor_Back-End.git
cd data-inventor
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

### 4. Configurar o OCR (Tesseract)

Aponte o caminho correto do Tesseract OCR no arquivo `application.properties`:

Este arquivo esta presente no repositório na pasta tessdata

```properties
tesseract.datapath=C:/{caminho}/{para}/data-inventor_Back-End/tessdata
tesseract.user-defined-dpi=96
tesseract.debug-file=/dev/null
```

> Baixe os arquivos `.traineddata` em: https://github.com/tesseract-ocr/tessdata

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

### Comandos úteis no front:

```bash
npm install
npm start
```

---

## 🔒 Segurança

- Login via JWT
- Endpoints protegidos por roles (`ADMIN`, `USER`)
- Autenticação via Spring Security

---

## 📂 Estrutura de Diretórios

```
src/
├── main/
│   ├── java/com/nicholastcc/datainventor/
│   └── resources/
│       └── application.properties
│       
```



---

## 📸 Demonstração

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
