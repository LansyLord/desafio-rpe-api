# Desafio Técnico Full-Stack - Gerenciador Financeiro

Este projeto foi desenvolvido como parte do desafio técnico para a vaga de Estagiário em Desenvolvimento Full-Stack na RPE. O objetivo foi criar uma aplicação completa (full-stack) para o gerenciamento de clientes, faturas e pagamentos, simulando uma solução para uma fintech.

A aplicação é composta por dois projetos em repositórios separados:

- **Back-end (API REST):** [Link para o Repositório do Back-end](https://github.com/LansyLord/desafio-rpe-api)
- **Front-end (Interface Web):** [Link para o Repositório do Front-end](https://github.com/LansyLord/desafio-rpe-frontend)

## ✨ Funcionalidades Implementadas

A aplicação atende a todos os requisitos propostos no desafio, incluindo:

- **API Back-end:**
    - API REST com os endpoints obrigatórios para gerenciamento de Clientes e Faturas.
    - Implementação das regras de negócio, como o bloqueio de clientes e a atualização de limites de crédito.
    - **Lógica de Juros Simples:** Para simular um cenário financeiro real onde o desbloqueio de um cliente depende do pagamento total do débito (valor principal + juros), foi implementada uma lógica de juros simples sobre as faturas atrasadas. A fórmula utilizada foi `Juros = Valor Principal * Taxa Diária * Dias em Atraso`, com uma taxa de **0.1% ao dia** (`BigDecimal("0.001")`). Para expor esse valor calculado sem alterar o dado original no banco, foi utilizado o padrão **DTO (Data Transfer Object)**, garantindo a integridade histórica da fatura.
    - **Agendador de Tarefas (`Scheduler`):** Utilização do Spring Scheduler para executar uma verificação automática a cada minuto, identificando faturas vencidas e aplicando as regras de negócio (marcar como 'Atrasada' e bloquear o cliente). Mantive a verificação a cada 1 minuto para facilitar o teste prático da lógica, mas em um ambiente de produção, este agendador seria configurado para rodar uma vez ao dia (ex: à meia-noite).
    - Documentação da API utilizando Swagger UI.
- **Front-end:**
    - Interface construída com React para uma boa experiência de usuário.
    - Tela de listagem de clientes com suas informações.
    - Tela de visualização de faturas por cliente, com botão de registrar pagamento.
- **Banco de Dados:**
    - Scripts para população inicial do banco de dados.
- **Testes:**
    - Cobertura de testes unitários para as camadas de `Service` e `Controller` do back-end.
- **Containerização:**
    - Ambiente de desenvolvimento para o back-end e banco de dados totalmente orquestrado com Docker Compose.

## 🛠️ Tecnologias Utilizadas

| Categoria           | Tecnologia                                           |
| ------------------- | ---------------------------------------------------- |
| **Back-end**        | Java 21, Spring Boot, Spring Data JPA, Hibernate     |
| **Front-end**       | React.js 19, Vite, Axios, React Router, Tailwind CSS |
| **Banco de Dados**  | MySQL 8.0                                            |
| **Testes**          | JUnit 5, Mockito, Spring Boot Test (MockMvc)         |
| **Documentação**    | Springdoc OpenAPI (Swagger UI)                       |
| **Containerização** | Docker, Docker Compose                               |

## Versionamento (Git Flow)

O desenvolvimento do projeto seguiu uma estratégia de versionamento baseada no Git Flow, utilizando branches para organizar o trabalho de forma clara:

- **`main`**: Branch principal, representando a versão estável e "padrão" do projeto.
- **`develop`**: Branch de desenvolvimento principal, onde as novas funcionalidades são integradas.
- **`feat/...`**: Branches de funcionalidade, criadas a partir do `develop`. Cada nova tarefa (como `feat/criar-endpoints`, `feat/unit-tests`) foi desenvolvida em sua própria branch para facilitar a manutenção do código.

## 🏗️ Arquitetura e Organização

### Back-end

O back-end foi estruturado seguindo o padrão **Model-View-Controller (MVC)** e uma arquitetura em camadas para separar responsabilidades:

- **`config`**: Classes de configuração do Spring, como CORS.
- **`controller`**: Camada responsável por expor os endpoints da API, receber as requisições HTTP e retornar as respostas.
- **`exception`**: Classes de exceções customizadas para um tratamento de erros mais semântico.
- **`job`**: Contém o `FaturaScheduler`, responsável pelas tarefas agendadas.
- **`model`**: Entidades JPA (`Cliente`, `Fatura`) que mapeiam as tabelas do banco de dados.
- **`repository`**: Interfaces do Spring Data JPA para abstrair o acesso aos dados.
- **`service`**: Camada onde reside toda a lógica de negócio da aplicação.

O design da arquitetura desenvolvido seguindo os princípios **SOLID**. Refletindo principalmente na separação de responsabilidades (**Princípio da Responsabilidade Única - SRP**), onde cada camada (`Controller`, `Service`, `Repository`) tem um papel único e bem definido. A estrutura também foi pensada para ser extensível sem a necessidade de alterar o código existente (**Princípio Aberto/Fechado - OCP**), permitindo que novas funcionalidades possam ser adicionadas de no futuro.

### Front-end

A organização de pastas do front-end foi pensada para ser de fácil manutenção:

- **`src/components`**: Componentes React reutilizáveis, divididos em `common` (genéricos como `Badge`) e `layout` (estruturais como `Header`).
- **`src/hooks`**: Hooks customizados, como o `useMidnightRefresh`.
- **`src/pages`**: Componentes que representam as telas completas da aplicação.
- **`src/routes`**: Centraliza a configuração das rotas com `react-router-dom`.
- **`src/service`**: Isola a lógica de comunicação com a API.
- **`src/utils`**: Funções utilitárias puras para formatação de dados.

## 📖 Endpoints da API

A API foi projetada com foco nos endpoints obrigatórios do desafio. Optei por não implementar um CRUD completo para `Cliente` e `Fatura`, tomando uma decisão de arquitetura baseada na possibilidade deste sistema ser um **microsserviço** dentro de um ecossistema maior. Nesse cenário, a criação de clientes ou a geração inicial de faturas poderiam ser responsabilidades de outros serviços, e este seria focado apenas no processamento e consulta de faturas existentes.

### Clientes (`/clientes`)

- `GET /clientes`: Lista todos os clientes.
- `POST /clientes`: Cadastra um novo cliente.
- `GET /clientes/{id}`: Consulta um cliente por seu ID.
- `PUT /clientes/{id}`: Atualiza os dados de um cliente ou o marca como bloqueado.
- `GET /clientes/bloqueados`: Lista todos os clientes com status "Bloqueado".

### Faturas (`/faturas`)

- `GET /faturas/{clienteId}`: Lista todas as faturas de um cliente específico.
- `PUT /faturas/{id}/pagamento`: Registra o pagamento para uma fatura específica.
- `GET /faturas/atrasadas`: Lista todas as faturas com status "Atrasada".

A documentação de todos os endpoints está disponível no **Swagger UI**.

## 🚀 Como Executar o Projeto

### Pré-requisitos

- [Docker](https://www.docker.com/get-started) e [Docker Compose](https://docs.docker.com/compose/install/)
- [Node.js](https://nodejs.org/en/) (versão 18 ou superior)
- [Git](https://git-scm.com/)

### Passo a Passo

1.  Clone os dois repositórios para a sua máquina local:

    ```bash
    # Repositório do Back-end
    git clone https://github.com/LansyLord/desafio-rpe-api.git

    # Repositório do Front-end
    git clone https://github.com/LansyLord/desafio-rpe-frontend.git
    ```

2.  **Execute o Back-end e o Banco de Dados:**

    - Navegue até a pasta raiz do projeto back-end.
    - Execute o Docker Compose.

    <!-- end list -->

    ```bash
    cd desafio-rpe-api
    docker-compose up --build
    ```

3.  **Execute o Front-end:**

    - Abra um **novo terminal** e navegue até a pasta do projeto front-end.
    - Instale as dependências e inicie o servidor de desenvolvimento.

    <!-- end list -->

    ```bash
    cd desafio-rpe-frontend
    npm install
    npm run dev
    ```

4.  **Acesse a Aplicação:**

    - **Front-end:** `http://localhost:5173`
    - **Swagger UI (API Docs):** `http://localhost:8081/swagger-ui.html`

## 🧪 Testes

A estratégia de testes do back-end foi dividida em duas camadas para garantir tanto a lógica de negócio quanto a integração da camada web:

- **Testes de Serviço (`Service Layer`)**: Foram criados testes de unidade para as classes `ClienteService` e `FaturaService`. Utilizando Mockito (com as anotações `@Mock` e `@InjectMocks`), a camada de repositório foi "mockada", permitindo testar a lógica de negócio (cálculos, validações, regras) de forma isolada do banco de dados.
- **Testes de Controller (`Controller Layer`)**: Para a camada de API, foram usados testes com `@WebMvcTest`. Esta abordagem carrega apenas o contexto web do Spring, e o `FaturaService` foi mockado. Com o `MockMvc`, foram simuladas requisições HTTP para cada endpoint, validando se as rotas estão corretas, os dados são processados e os códigos de status HTTP e o JSON de resposta são os esperados.

## 🔮 Pontos de Melhoria que eu implementaria

- **Persistência do Valor dos Juros:** Adicionaria um campo `valorJuros` na entidade `Fatura`. Embora o cálculo dinâmico atual funcione, persistir o valor dos juros no momento do pagamento facilitaria a geração de relatórios e o rastreamento histórico de forma mais direta, sem a necessidade de recálculos.
- **Containerização do Front-end**: Adicionaria um serviço para o front-end no `docker-compose.yml` para que toda a aplicação suba com um único comando.
- **Notificações de Status da Fatura**: Implementaria um sistema de notificações automáticas por e-mail, utilizando o **JavaMailSender** do Spring. A cada mudança de estado de uma fatura (ex: de "Aberta" para "Paga" ou "Atrasada"), o cliente associado receberia uma notificação.
- **Testes de Integração**: Adicionaria testes de integração (`@SpringBootTest`) para validar o fluxo completo, do controller ao banco de dados.
