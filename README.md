# RentalSpace Backend
A real-time platform for managing property rentals, maintenance requests, and amenity bookings all in one place - platform where tenants, landlord, and property manager can communicate transparently.

## Features

- **User Authentication** — secure login/signup for tenants, landlords, and admins with role-based access
- **Property Listings & Search** — browse, filter, and search available rental units by location, price, and amenities
- **Maintenance Request Tracking** — tenants can submit issues, track status, and get real-time updates
- **Amenity Booking** — reserve shared spaces (gym, pool, parking, etc.) with live availability
- **Real-Time Notifications** — instant updates on requests, bookings, and rent reminders via sockets/push
- **Payment Integration** — online rent payments and transaction history
- **Admin Dashboard** — manage properties, tenants, and maintenance staff from a central panel
- **Tenant Dashboard** — track properties, amenity bookings, raised complains, and rent payment

## Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Security:** Spring Security (JWT-based authentication)
- **Data Access:** Spring Data JPA / Hibernate
- **Database:** MySQL
- **Build Tool:** Maven
- **API:** RESTful APIs
- **Real-Time Communication:** WebSockets / STOMP (for live notifications & updates)
- **Version Control:** Git & GitHub
- **Testing Framework:** JUnit / Mockito
- **Logging Framework:** SLF4J

## Project Structure

```
src/main/java/com/RentSpace/
├── Amenity/           # Amenity-related entities, services, controllers
├── AmenityBooking/    # Booking logic for amenities
├── Booking/           # Property booking module
├── Config/            # Security & app configuration
├── Exception/         # Custom exceptions & global error handling
├── Maintenance/       # Maintenance request management
├── Payment/           # Payment processing & transactions
├── Property/          # Property listing & management
├── User/              # User management — handles Tenant, Owner, and Admin roles
├── common/            # Shared utilities, DTOs, base classes
└── RentspaceApplication.java   # Main Spring Boot application entry point

src/main/resources/
└── application.properties      # App configuration (DB, server, etc.)
```
