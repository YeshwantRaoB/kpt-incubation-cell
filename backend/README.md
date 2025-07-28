# KPT Incubation Cell - Backend API

This is the backend API for the KPT Incubation Cell website, built with Spring Boot and PostgreSQL. It provides RESTful endpoints for managing gallery photos, startups, and projects, with JWT-based authentication for admin users.

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- PostgreSQL 13 or higher (or Neon PostgreSQL)
- Node.js and npm (for frontend development)

## Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd kpt-incubation-cell/backend
   ```

2. **Configure the database**
   - Create a new PostgreSQL database
   - Update `application.properties` with your database credentials:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```

3. **Build and run the application**
   ```bash
   mvn spring-boot:run
   ```

   The API will be available at `http://localhost:8080`

## API Documentation

### Authentication

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@example.com",
  "password": "yourpassword"
}
```

**Response**
```json
{
  "token": "jwt.token.here",
  "email": "admin@example.com",
  "name": "Admin User",
  "message": "Login successful"
}
```

### Gallery Photos

#### Get all photos
```http
GET /api/gallery
```

#### Get featured photos
```http
GET /api/gallery/featured
```

#### Upload a new photo (Admin only)
```http
POST /api/gallery
Authorization: Bearer your.jwt.token
Content-Type: application/json

{
  "title": "Event Photo",
  "description": "Annual meetup 2023",
  "imageUrl": "https://example.com/photos/event.jpg",
  "altText": "Group photo at annual meetup",
  "featured": true
}
```

### Startups

#### Get all startups
```http
GET /api/startups
```

#### Get featured startups
```http
GET /api/startups/featured
```

#### Create a new startup (Admin only)
```http
POST /api/startups
Authorization: Bearer your.jwt.token
Content-Type: application/json

{
  "name": "Tech Innovations Inc.",
  "description": "Cutting-edge technology solutions",
  "industry": "Technology",
  "founderName": "Jane Doe",
  "websiteUrl": "https://techinnovations.example.com"
}
```

### Projects

#### Get all projects
```http
GET /api/projects
```

#### Get projects by startup
```http
GET /api/projects/startup/{startupId}
```

#### Create a new project (Admin only)
```http
POST /api/projects?startupId=1
Authorization: Bearer your.jwt.token
Content-Type: application/json

{
  "name": "AI Assistant",
  "description": "An intelligent assistant for daily tasks",
  "status": "In Progress",
  "technologies": "Java, Spring Boot, React"
}
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/kpt_incubation` |
| `DATABASE_USERNAME` | Database username | - |
| `DATABASE_PASSWORD` | Database password | - |
| `JWT_SECRET` | Secret key for JWT token generation | Random string |
| `JWT_EXPIRATION` | JWT token expiration time in ms | 86400000 (24 hours) |

## Deployment

### Local Development
```bash
mvn spring-boot:run
```

### Production Build
```bash
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Environment Setup
For production, set the following environment variables:

```bash
export DATABASE_URL=your_production_db_url
export DATABASE_USERNAME=your_db_username
export DATABASE_PASSWORD=your_db_password
export JWT_SECRET=your_secure_jwt_secret
export JWT_EXPIRATION=86400000
```

## Frontend Integration

1. Store the JWT token in localStorage or sessionStorage after login
2. Include the token in the Authorization header for protected routes:
   ```javascript
   const response = await fetch('/api/protected-route', {
     headers: {
       'Authorization': `Bearer ${localStorage.getItem('token')}`,
       'Content-Type': 'application/json'
     }
   });
   ```

3. Handle token expiration and refresh as needed

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
