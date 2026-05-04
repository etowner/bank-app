# 🏦 Bank Application

A full-stack banking web application built with **Spring Boot**, **React and Vite**, and **MongoDB Atlas** — featuring account management, secure authentication, real-time transactions, and data visualization.

![Java](https://img.shields.io/badge/Java-Spring%20Boot-brightgreen)
![React](https://img.shields.io/badge/Frontend-React%20%2B%20Vite-blue)
![MongoDB](https://img.shields.io/badge/Database-MongoDB-green)
![Docker](https://img.shields.io/badge/Container-Docker-blue)
![Node.js](https://img.shields.io/badge/Node.js-22.x-green)
![Axios](https://img.shields.io/badge/HTTP%20Client-Axios-ff69b4)

## ✨ Features

| Feature | Description |
| ------------------------- | ----------------------------------------------------- |
| 🔐 **Authentication**     | Secure sign-up, login, and session management         |
| 🏧 **Account Management** | Create and manage multiple bank accounts              |
| 💸 **Transactions**       | Deposits and withdrawals with full transaction history |
| 🔁 **Money Transfers**    | Transfer funds between accounts instantly              |
| 📊 **Data Visualization** | Pie charts and line graphs for account analytics |
| 👤 **Profile Management** | Update profile settings or delete your account |
| 📱 **Responsive Design**  | Optimized for both desktop and mobile devices |

## 🛠 Tech Stacks

| Layer       | Technology                  | Version  |
| ----------- | --------------------------- | -------- |
| Frontend    | React/Vite                  | 19.x/7.x |
| UI Library  | Bootstrap + React-Bootstrap | 5.x/2.x  |
| Charts      | Chart.js/react-chartjs-2    | -/5.x    |
| Routing     | React Router                | 7.x      |
| HTTP Client | Axios                       | 1.x      |
| Backend     | Spring Boot (Java)          | 4.x      |
| Container   | Docker + Docker Compose     | Latest   |

## ✅ Prerequisites

- [Node.js](https://nodejs.org/) (v22+) and npm
- [Java JDK](https://www.oracle.com/java/technologies/downloads/) (v25)
- [Maven](https://maven.apache.org/) (v3.9+)
- [MongoDB Atlas](https://www.mongodb.com/try)
- [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/) (for containerized setup)

***Note**: As this app utilizes MongoDB Atlas, you will also need to have access to your own cluster.

## ⚡ Quick Start

### Clone Repository

```bash
git clone https://github.com/etowner/bank_app.git
cd bank_app
```

### Create .env (`bank_app/.env`)

```text
MONGO_DATABASE=your_mongo_database
MONGO_USER=your_mongo_user
MONGO_PASSWORD=your_mongo_password
MONGO_CLUSTER=your_mongo_cluster
```

### Option 1: Docker Compose (Recommended)

See **[README.Docker.md](./README.Docker.md)** for full Docker instructions.

To build and start all services

```bash
docker compose up --build
```

Once running, the app is available at:

- **Frontend:** <http://localhost:3000>
- **Backend API:** <http://localhost:8080>

### Option 2: Local Development

#### Backend Setup

Navigate to the backend directory:

```bash
cd backend
```

Create application.properties file (`backend/src/main/resources/application.properties`)

```properties
# MongoDB
spring.data.mongodb.database=myapp
spring.mongodb.uri=mongodb+srv://${MONGO_USER}:${MONGO_PASSWORD}@${MONGO_CLUSTER}/${MONGO_DATABASE}

# Logging
logging.level.root=INFO
```

Build and run the Spring Boot backend:

```bash
mvn clean install
mvn spring-boot:run
```

The backend will start at `http://localhost:8080`.

#### Frontend Setup

```bash
cd frontend
npm install
npm start
```

The frontend will start on **<http://localhost:5173>** and proxy API requests to the backend.

## Project Structure

```text
bank_app/
├── backend/              # Spring Boot application
│   ├── src/
│   │   ├── main/java/    # Application source code
│   │   └── resources/    # Configuration files
│   └── pom.xml           # Maven dependencies
├── frontend/             # React + Vite application
|   ├── public/
│   ├── src/
│   │   ├── api/ 
│   │   ├── components/   # React components
│   │   └── styles/       # CSS stylesheets
│   ├── Dockerfile 
│   ├── package.json      # NPM dependencies
│   └── vite.config.js
├── compose.yaml          # Docker Compose configuration
└── README.md 
```

## 💡 Usage

1. Open your browser and go to `http://localhost:5173`
2. **Sign Up / Login** — Create a new account or sign in with existing credentials
3. **Dashboard** — View all accounts with balances and a pie chart overview
4. **Account Details** — Click an account to see its transaction history and line chart
5. **Transactions** — Perform deposits and withdrawals
6. **Transfers** — Move funds between your accounts
7. **Profile** — Manage your settings or log out


## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

Please ensure all tests pass and code follows existing style conventions before submitting a PR.
