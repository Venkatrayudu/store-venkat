# Store Application API Documentation

## Overview

The Store Application is a RESTful API for managing customers, orders, and products. It provides endpoints for CRUD operations with search capabilities and comprehensive error handling.

## Base URL

```
http://localhost:8080
```

## API Endpoints

### Customer Endpoints

#### Get All Customers
```
GET /customer
```

Returns a list of all customers with their associated orders.

**Response Example:**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "orders": [
      {
        "id": 1,
        "description": "Test Order",
        "productIds": [1, 2]
      }
    ]
  }
]
```

#### Get Customer by ID
```
GET /customer/{id}
```

Returns a specific customer by ID.

**Path Parameters:**
- `id`: Customer ID (required)

**Response Example:**
```json
{
  "id": 1,
  "name": "John Doe",
  "orders": []
}
```

**Error Response:**
```json
{
  "timestamp": "2026-05-28T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with id: 999",
  "path": "/customer/999"
}
```

#### Search Customers
```
GET /customer/search?query={searchTerm}
```

Search customers by name using substring matching (case-insensitive).

**Query Parameters:**
- `query`: Search term to match against customer names (required)

**Response Example:**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "orders": []
  },
  {
    "id": 2,
    "name": "Johnson Smith",
    "orders": []
  }
]
```

#### Create Customer
```
POST /customer
Content-Type: application/json
```

Create a new customer.

**Request Body:**
```json
{
  "name": "Jane Doe"
}
```

**Response:**
```json
{
  "id": 101,
  "name": "Jane Doe",
  "orders": []
}
```

### Order Endpoints

#### Get All Orders
```
GET /order
```

Returns a list of all orders with their associated customers and products.

**Response Example:**
```json
[
  {
    "id": 1,
    "description": "Electronics Order",
    "customer": {
      "id": 1,
      "name": "John Doe"
    },
    "products": [
      {
        "id": 1,
        "description": "Laptop Computer",
        "orderIds": [1, 2]
      }
    ]
  }
]
```

#### Get Order by ID
```
GET /order/{id}
```

Returns a specific order by ID with full details.

**Path Parameters:**
- `id`: Order ID (required)

**Response Example:**
```json
{
  "id": 1,
  "description": "Electronics Order",
  "customer": {
    "id": 1,
    "name": "John Doe"
  },
  "products": [
    {
      "id": 1,
      "description": "Laptop Computer",
      "orderIds": [1]
    }
  ]
}
```

#### Create Order
```
POST /order
Content-Type: application/json
```

Create a new order.

**Request Body:**
```json
{
  "description": "New Order",
  "customer": {
    "id": 1
  },
  "products": []
}
```

**Response:**
```json
{
  "id": 101,
  "description": "New Order",
  "customer": {
    "id": 1,
    "name": "John Doe"
  },
  "products": []
}
```

#### Update Order
```
PUT /order/{id}
Content-Type: application/json
```

Update an existing order.

**Path Parameters:**
- `id`: Order ID (required)

**Request Body:**
```json
{
  "description": "Updated Order Description",
  "products": [1, 2, 3]
}
```

#### Delete Order
```
DELETE /order/{id}
```

Delete an order by ID.

**Path Parameters:**
- `id`: Order ID (required)

### Product Endpoints

#### Get All Products
```
GET /products
```

Returns a list of all products with their associated order IDs.

**Response Example:**
```json
[
  {
    "id": 1,
    "description": "Laptop Computer",
    "orderIds": [1, 2, 5]
  }
]
```

#### Get Product by ID
```
GET /products/{id}
```

Returns a specific product by ID.

**Path Parameters:**
- `id`: Product ID (required)

**Response Example:**
```json
{
  "id": 1,
  "description": "Laptop Computer",
  "orderIds": [1, 2, 5]
}
```

#### Search Products
```
GET /products/search?query={searchTerm}
```

Search products by description using substring matching (case-insensitive).

**Query Parameters:**
- `query`: Search term to match against product descriptions (required)

**Response Example:**
```json
[
  {
    "id": 1,
    "description": "Laptop Computer",
    "orderIds": [1, 2]
  },
  {
    "id": 2,
    "description": "Desktop Computer",
    "orderIds": [3]
  }
]
```

#### Create Product
```
POST /products
Content-Type: application/json
```

Create a new product.

**Request Body:**
```json
{
  "description": "Monitor 4K"
}
```

**Response:**
```json
{
  "id": 11,
  "description": "Monitor 4K",
  "orderIds": []
}
```

#### Update Product
```
PUT /products/{id}
Content-Type: application/json
```

Update an existing product.

**Path Parameters:**
- `id`: Product ID (required)

**Request Body:**
```json
{
  "description": "Ultra Monitor 4K"
}
```

#### Delete Product
```
DELETE /products/{id}
```

Delete a product by ID.

**Path Parameters:**
- `id`: Product ID (required)

## Error Handling

The API returns standardized error responses with appropriate HTTP status codes.

### Error Response Format

```json
{
  "timestamp": "2026-05-28T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request parameter",
  "path": "/customer/search"
}
```

### Status Codes

- `200 OK`: Successful GET request
- `201 Created`: Successful POST request
- `204 No Content`: Successful DELETE request
- `400 Bad Request`: Invalid request parameters
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## Performance Optimizations

### Database Optimization

1. **Indexes**: Added indexes on frequently searched columns:
   - `customer.name`
   - `product.description`
   - Foreign key columns for join optimization

2. **Lazy Loading**: Relationships use LAZY fetch strategy to avoid unnecessary data loading

3. **Eager Loading**: Search endpoints use JOIN FETCH to avoid N+1 query problems

4. **Query Optimization**: JPQL queries with explicit JOIN FETCH for performance

### Response Optimization

1. **DTO Mapping**: Only necessary data is serialized in responses
2. **Pagination-Ready**: API structure supports pagination for large result sets
3. **Batch Fetching**: Hibernate batch fetch size configured to 10

### Transaction Management

- Read-only transactions for GET endpoints for optimization
- Standard transactions for write operations

## Best Practices

### Search Queries

Searches use case-insensitive substring matching:

```bash
# Search for customers named "john"
curl http://localhost:8080/customer/search?query=john

# Search for products containing "laptop"
curl http://localhost:8080/products/search?query=laptop
```

### Pagination Support

While not implemented in current version, the API structure supports adding pagination:

```
GET /customer?page=0&size=20
GET /order?page=0&size=20
GET /products?page=0&size=20
```

## Rate Limiting

Not currently implemented, but recommended for production use.

## Authentication & Authorization

Not currently implemented. Recommended for production use with JWT or OAuth2.

## Database Schema

### Customer Table
- `id` (BIGINT, PK, Auto-increment)
- `name` (VARCHAR, NOT NULL)
- Index on `name` for search optimization

### Order Table
- `id` (BIGINT, PK, Auto-increment)
- `description` (VARCHAR, NOT NULL)
- `customer_id` (BIGINT, FK to Customer)
- Indexes on `customer_id` for join optimization

### Product Table
- `id` (BIGINT, PK, Auto-increment)
- `description` (VARCHAR, NOT NULL)
- Index on `description` for search optimization

### Order_Product Table (Junction Table)
- `order_id` (BIGINT, FK to Order)
- `product_id` (BIGINT, FK to Product)
- Composite primary key on (order_id, product_id)
- Indexes on both foreign keys for query optimization

## Changelog API

The API uses Liquibase for database versioning:

1. `db.changelog-1.yaml`: Initial schema creation
2. `db.changelog-2.yaml`: Initial data population
3. `db.changelog-3.yaml`: Product schema and data addition

## Testing

Comprehensive unit and integration tests are included:

- **Unit Tests**: Controller and Repository layer tests
- **Integration Tests**: DataJpaTest with H2 in-memory database
- **Mapper Tests**: MapStruct mapping validation

Run tests with:
```bash
./gradlew test
```

Generate coverage report:
```bash
./gradlew jacocoTestReport
```

## Examples

### Create a Customer
```bash
curl -X POST http://localhost:8080/customer \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice Johnson"}'
```

### Create an Order with Products
```bash
curl -X POST http://localhost:8080/order \
  -H "Content-Type: application/json" \
  -d '{
    "description": "New purchase",
    "customer": {"id": 1},
    "products": [1, 2, 3]
  }'
```

### Search Customers
```bash
curl http://localhost:8080/customer/search?query=john
```

### Update Product
```bash
curl -X PUT http://localhost:8080/products/1 \
  -H "Content-Type: application/json" \
  -d '{"description": "Gaming Laptop"}'
```

