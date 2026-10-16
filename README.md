# Property Rental & Management Platform
A real-time platform for managing property rentals, maintenance requests, and amenity bookings all in one place - platform where tenants, owner, and property manager can communicate transparently.

## Features

- **User Authentication** — secure login/signup for tenants, owner, and admins with role-based access
- **Property Listings & Search** — browse, filter, and search available rental units by location, price, and type
- **Flat Booking** — reserve available flat for future or present
- **Amenity Booking** — reserve shared spaces (gym, pool, parking, etc.) with live availability
- **Payment Integration** — rent can pay online and manage transaction history
- **Amenity Booking Alert Notification** — tenants will receive 2 alert email notification 1st on day start and 2nd 1 hour before booking
- **Validation & Centralized exception handling** — used proper validation to restrict invalid input and handled all expected exceptions
- **Pagination** — For better performance and database stability data is fetching in chunks
- **Database migrations & automated tests** — track database changes and written test driven methods and logic

## Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Security:** Spring Security (JWT-based authentication)
- **Data Access:** Spring Data JPA / Hibernate
- **Database:** MySQL
- **Build Tool:** Maven
- **API:** RESTful APIs
- **API Testing:** Postman
- **Version Control:** Git & GitHub
- **Testing Framework:** JUnit / Mockito
- **Logging Framework:** SLF4J

## System Architecture

This project follows a **Layered Architecture** pattern to ensure clean separation of concerns, scalability, and ease of testing.

- **Controller Layer** — Handle incoming HTTPS requests, maps endpoints and return response
- **Service Layer** — Handle data transformation and perform business calculation
- **Repository Layer** — Handle CRUD(Create, Read, Update, Delete) operations using spring JPA/Hibernate to talk to Database
- **Database Layer** — Store and manage relational data

- **Data Flow:** Client (HTTP Request) ──> Controller ──> Service ──> Repository (JPA) ──> MySQL Database