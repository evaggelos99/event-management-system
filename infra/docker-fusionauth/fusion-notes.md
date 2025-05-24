# Fusionauth

## Notes

- Tenant is a realm just like Keycloak
- Application is like a client in Keycloak
- User can belong to a tenant and then have a registration to Application
- ROLES belong to a tenant and can be registered to a application.

## Specific Configuration

For this specific system we have the following components:

- Admin user
- Tenant
- Application
- Roles for each CRUD operation for each Entity
- Users registered underneath the application

## REST API for exporting:

`
    curl -X GET "http://localhost:9011/api/application" -H "Authorization: API_KEY"
`

`
    curl -X GET "http://localhost:9011/api/tenant" -H "Authorization: API_KEY"
`

### Exporting users
There is no API for exporting all the users you would have to manually read from the DB and sync through some middleware or script to a new DB


## Importing application

`
    curl -X POST http://localhost:9011/api/application -H "Authorization: API_KEY" -H "Content-Type: application/json" -d @event-management-system.json
`