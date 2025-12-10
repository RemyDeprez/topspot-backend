# TopSpot Backend - API REST Sécurisée

API REST Spring Boot sécurisée avec JWT pour la gestion de spots de skateboard.

## 🚀 Technologies

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Security** avec JWT
- **Spring Data JPA**
- **H2 Database** (développement)
- **Maven**
- **Lombok**

## 📋 Prérequis

- Java 21 ou supérieur
- Maven 3.6+

## 🔧 Installation

1. Cloner le projet
```bash
cd topspot-backend
```

2. Installer les dépendances
```bash
mvn clean install
```

3. Lancer l'application
```bash
mvn spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

## 🔐 Authentification

L'API utilise JWT (JSON Web Tokens) pour l'authentification. Tous les endpoints (sauf `/api/auth/**`) nécessitent un token valide.

### Endpoints d'authentification

~~#### Inscription
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePassword123"
}
```

**Réponse :**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "email": "john@example.com"
}
```

#### Connexion
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePassword123"
}
```

**Réponse :**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "email": "john@example.com"
}
```~~

## 📍 Endpoints Spot

### Récupérer tous les spots
```http
GET /api/spots
Authorization: Bearer {token}
```

**Réponse :**
```json
[
  {
    "id": 1,
    "name": "Skatepark Central",
    "description": "Super skatepark avec bowl et street",
    "location": "Paris",
    "latitude": 48.8566,
    "longitude": 2.3522,
    "createdBy": "john_doe"
  }
]
```

### Récupérer un spot par ID
```http
GET /api/spots/{id}
Authorization: Bearer {token}
```

### Créer un spot
```http
POST /api/spots
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Nouveau Spot",
  "description": "Description du spot",
  "location": "Lyon",
  "latitude": 45.7640,
  "longitude": 4.8357
}
```

### Mettre à jour un spot
```http
PUT /api/spots/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Spot Modifié",
  "description": "Nouvelle description",
  "location": "Marseille",
  "latitude": 43.2965,
  "longitude": 5.3698
}
```

### Supprimer un spot
```http
DELETE /api/spots/{id}
Authorization: Bearer {token}
```

## 🧪 Tests

Lancer les tests :
```bash
mvn test
```

## 🗄️ Base de données

### H2 Console (Développement)

La console H2 est accessible sur : `http://localhost:8080/h2-console`

**Informations de connexion :**
- JDBC URL: `jdbc:h2:mem:topspotdb`
- Username: `sa`
- Password: *(vide)*

## 📁 Structure du projet

```
src/
├── main/
│   ├── java/org/example/
│   │   ├── config/              # Configuration Spring Security, JWT
│   │   ├── controller/          # Contrôleurs REST
│   │   ├── dto/                 # Objets de transfert de données
│   │   ├── exception/           # Gestion des exceptions
│   │   ├── model/               # Entités JPA
│   │   ├── repository/          # Repositories JPA
│   │   ├── service/             # Logique métier
│   │   └── Main.java            # Point d'entrée
│   └── resources/
│       └── application.yml      # Configuration de l'application
└── test/
    └── java/org/example/        # Tests unitaires
```

## 🔒 Sécurité

- **Authentification JWT** : Token Bearer dans l'en-tête Authorization
- **Hashage des mots de passe** : BCrypt
- **CORS** : À configurer selon vos besoins
- **CSRF** : Désactivé (API REST stateless)
- **Expiration des tokens** : 24 heures

## 🛠️ Configuration

Modifier le fichier `src/main/resources/application.yml` :

```yaml
server:
  port: 8080

jwt:
  secret: {votre-secret-key}
  expiration: 86400000  # 24h en millisecondes
```

## 📝 Exemple d'utilisation avec cURL

1. **S'inscrire**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","email":"user1@example.com","password":"password123"}'
```

2. **Se connecter et récupérer le token**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"password123"}'
```

3. **Créer un spot (avec le token)**
```bash
curl -X POST http://localhost:8080/api/spots \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {votre-token}" \
  -d '{"name":"Mon Spot","description":"Super spot","location":"Paris","latitude":48.8566,"longitude":2.3522}'
```

4. **Récupérer tous les spots**
```bash
curl -X GET http://localhost:8080/api/spots \
  -H "Authorization: Bearer {votre-token}"
```

## 📄 Licence

Ce projet est sous licence MIT.

