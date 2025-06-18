# Module de Facturation AriMayi

## Description

Module de facturation REST développé avec Spring Boot pour la plateforme de gestion de projets AriMayi. Ce module permet la gestion complète des clients et des factures avec calcul automatique des montants et export JSON.

## Fonctionnalités

### 🏢 Gestion des Clients
- ✅ Liste des clients
- ✅ Création d'un client (nom, email, SIRET, date de création)
- ✅ Détail d'un client
- ✅ Modification et suppression

### 📄 Gestion des Factures
- ✅ Liste des factures
- ✅ Création d'une facture avec lignes détaillées
- ✅ Calcul automatique des totaux (HT, TVA, TTC)
- ✅ Recherche par client ou date
- ✅ Export JSON complet

### 📊 Règles Métier
- ✅ Une facture doit avoir au moins une ligne
- ✅ Validation des champs obligatoires
- ✅ Taux de TVA autorisés : 0%, 5.5%, 10%, 20%
- ✅ Numérotation automatique des factures

### 🔒 Sécurité
- ✅ Authentification HTTP Basic
- ✅ Utilisateurs en mémoire (admin/admin123, user/user123)

## Technologies Utilisées

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **Base de données H2** (en mémoire)
- **Swagger/OpenAPI 3**
- **JUnit 5** pour les tests

## Installation et Démarrage

### Prérequis
- Java 17 ou supérieur
- Maven 3.6+

### Démarrage
\`\`\`bash
# Cloner le projet
git clone https://github.com/MerzoukHafsa/facturation.git

# Compiler et démarrer
mvn spring-boot:run
\`\`\`

L'application sera accessible sur `http://localhost:8080`

## Documentation API

### Swagger UI
Accédez à la documentation interactive : `http://localhost:8080/swagger-ui.html`

### Authentification
Utilisez l'authentification HTTP Basic :
- **Admin** : `admin` / `admin123`
- **Utilisateur** : `user` / `user123`

### Endpoints Principaux

#### Clients
- `GET /api/clients` - Liste tous les clients
- `GET /api/clients/{id}` - Détail d'un client
- `POST /api/clients` - Créer un client
- `PUT /api/clients/{id}` - Modifier un client
- `DELETE /api/clients/{id}` - Supprimer un client

#### Factures
- `GET /api/factures` - Liste toutes les factures
- `GET /api/factures/{id}` - Détail d'une facture
- `POST /api/factures` - Créer une facture
- `GET /api/factures/client/{clientId}` - Factures d'un client
- `GET /api/factures/date/{date}` - Factures par date
- `GET /api/factures/periode?dateDebut=...&dateFin=...` - Factures par période
- `GET /api/factures/{id}/export` - Export JSON d'une facture

## Exemples d'Utilisation

### Créer un Client
\`\`\`json
POST /api/clients
{
  "nom": "Nouvelle Entreprise",
  "email": "contact@nouvelle-entreprise.com",
  "siret": "12345678901234"
}
\`\`\`

### Créer une Facture
\`\`\`json
POST /api/factures
{
  "date": "2024-01-15",
  "clientId": 1,
  "lignes": [
    {
      "description": "Développement application",
      "quantite": 10,
      "prixUnitaireHT": 80.00,
      "tauxTVA": 20
    },
    {
      "description": "Formation utilisateurs",
      "quantite": 2,
      "prixUnitaireHT": 150.00,
      "tauxTVA": 20
    }
  ]
}
\`\`\`

## Base de Données

### Console H2
Accédez à la console H2 : `http://localhost:8080/h2-console`
- **URL JDBC** : `jdbc:h2:mem:billing`
- **Utilisateur** : `sa`
- **Mot de passe** : (vide)

### Données de Test
Le système est livré avec des données de test pré-chargées :
- 4 clients d'exemple
- 4 factures avec lignes détaillées

## Tests

### Exécuter les Tests
\`\`\`bash
mvn test
\`\`\`

### Couverture
- Tests unitaires des services métier
- Tests d'intégration des contrôleurs
- Validation des règles métier

## Structure du Projet

\`\`\`
src/
├── main/java/com/arimayi/billing/
│   ├── entity/          # Entités JPA
│   ├── dto/             # Data Transfer Objects
│   ├── repository/      # Repositories Spring Data
│   ├── service/         # Services métier
│   ├── controller/      # Contrôleurs REST
│   ├── config/          # Configuration
│   └── exception/       # Gestion des exceptions
├── main/resources/
│   ├── application.properties
│   └── data.sql         # Données de test
└── test/                # Tests unitaires et d'intégration
\`\`\`

## Évolutions Possibles

- 🔄 Migration vers PostgreSQL
- 📧 Envoi de factures par email
- 📊 Tableau de bord avec statistiques
- 🧾 Génération PDF des factures
- 🔍 Recherche avancée avec filtres
- 📱 API mobile dédiée

## Support

Pour toute question ou problème, contactez l'équipe de développement AriMayi.
