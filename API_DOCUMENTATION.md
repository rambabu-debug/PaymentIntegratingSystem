# API Documentation - Payment Integration System

## Base URL

```
Development: http://localhost:8080
Production: https://api.yourdomain.com
```

## Authentication

All endpoints (except `/api/auth/**` and `/api/webhooks/stripe`) require JWT authentication via Bearer token.

```
Authorization: Bearer {jwt_token}
```

---

## Authentication Endpoints

### Register User

Creates a new user account (MERCHANT or CLIENT role).

**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "email": "merchant@example.com",
  "password": "SecurePassword123!",
  "role": "MERCHANT",
  "businessName": "My Store"  // Optional, only for MERCHANT role
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZXJjaGFudEBleGFtcGxlLmNvbSIsImlhdCI6MTcwNDA2NzIwMCwiZXhwIjoxNzA0MDcwODAwfQ.xxx",
  "email": "merchant@example.com",
  "role": "MERCHANT"
}
```

**Status Codes:**
- `200 OK` - Registration successful
- `400 Bad Request` - Validation error or email already registered
- `500 Internal Server Error` - Server error

**Validation Rules:**
- Email: Valid email format
- Password: Minimum 8 characters
- Role: MERCHANT or CLIENT
- BusinessName: Optional for MERCHANT

---

### Login

Authenticates user and returns JWT token.

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "email": "merchant@example.com",
  "password": "SecurePassword123!"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "merchant@example.com",
  "role": "MERCHANT"
}
```

**Status Codes:**
- `200 OK` - Login successful
- `400 Bad Request` - Invalid credentials
- `401 Unauthorized` - Invalid credentials

---

### Get Current User

Retrieves information about the authenticated user.

**Endpoint:** `GET /api/auth/me`

**Headers:**
```
Authorization: Bearer {token}
```

**Response:**
```json
{
  "email": "merchant@example.com",
  "role": "MERCHANT"
}
```

**Status Codes:**
- `200 OK` - User information retrieved
- `401 Unauthorized` - Token invalid or expired
- `403 Forbidden` - Access denied

---

## Payment Endpoints

### Create Payment Intent

Creates a new payment intent with Stripe.

**Endpoint:** `POST /api/payments/intents`

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "amount": 5000,
  "currency": "USD",
  "description": "Customer Purchase",
  "orderReference": "ORDER-20240101-12345"
}
```

**Response:**
```json
{
  "transactionId": 1,
  "paymentIntentId": "pi_1234567890abcdef",
  "clientSecret": "pi_1234567890abcdef_secret_1234567890abcdef",
  "status": "CREATED"
}
```

**Request Details:**
- `amount` (required): Amount in cents (5000 = $50.00). Minimum: 50 cents
- `currency` (required): Currency code (USD, EUR, GBP, etc.)
- `description` (optional): Payment description
- `orderReference` (required): Unique order identifier for this merchant

**Response Details:**
- `transactionId`: Database transaction ID
- `paymentIntentId`: Stripe Payment Intent ID
- `clientSecret`: Stripe client secret for frontend
- `status`: Payment status (CREATED, REQUIRES_ACTION, SUCCEEDED, FAILED, CANCELED, PROCESSING)

**Status Codes:**
- `200 OK` - Payment intent created successfully
- `400 Bad Request` - Validation error or invalid request
- `401 Unauthorized` - Token invalid or expired
- `403 Forbidden` - User not authorized (non-merchants cannot create intents)
- `409 Conflict` - Order reference already exists for this merchant
- `500 Internal Server Error` - Stripe API error

**Error Responses:**
```json
{
  "error": "Only merchants can create payment intents"
}
```

```json
{
  "error": "Order reference already exists for this merchant"
}
```

---

### Get Payment Details

Retrieves details of a specific payment transaction.

**Endpoint:** `GET /api/payments/{transactionId}`

**Headers:**
```
Authorization: Bearer {token}
```

**Path Parameters:**
- `transactionId` (required): Transaction ID returned from create payment intent

**Response:**
```json
{
  "transactionId": 1,
  "paymentIntentId": "pi_1234567890abcdef",
  "clientSecret": "pi_1234567890abcdef_secret_xxx",
  "status": "SUCCEEDED"
}
```

**Status Codes:**
- `200 OK` - Payment details retrieved
- `401 Unauthorized` - Token invalid or expired
- `403 Forbidden` - User cannot access this transaction
- `404 Not Found` - Transaction not found

**Permission Rules:**
- ADMIN: Can view all transactions
- MERCHANT: Can view own transactions only
- CLIENT: Cannot access payment endpoints

---

## Webhook Endpoints

### Stripe Webhook

Receives and processes Stripe webhook events.

**Endpoint:** `POST /api/webhooks/stripe`

**Headers:**
```
Stripe-Signature: {signature}
Content-Type: application/json
```

**Supported Events:**
- `payment_intent.succeeded` - Payment completed successfully
- `payment_intent.payment_failed` - Payment failed
- `payment_intent.processing` - Payment is processing
- `payment_intent.canceled` - Payment was canceled

**Request Body (Example):**
```json
{
  "id": "evt_1234567890",
  "object": "event",
  "type": "payment_intent.succeeded",
  "data": {
    "object": {
      "id": "pi_1234567890",
      "object": "payment_intent",
      "amount": 5000,
      "currency": "usd",
      "status": "succeeded",
      "client_secret": "pi_1234567890_secret_xxx"
    }
  }
}
```

**Response:**
```json
{
  "message": "Webhook processed"
}
```

**Status Codes:**
- `200 OK` - Webhook processed successfully
- `400 Bad Request` - Invalid signature or payload

**Security Note:**
- Webhook signature verification is mandatory
- Only Stripe can call this endpoint
- Signature header must match the webhook secret

---

## Error Responses

### Common Error Codes

**400 Bad Request**
```json
{
  "error": "Invalid request format"
}
```

**401 Unauthorized**
```json
{
  "error": "Token expired or invalid"
}
```

**403 Forbidden**
```json
{
  "error": "Access denied"
}
```

**404 Not Found**
```json
{
  "error": "Resource not found"
}
```

**409 Conflict**
```json
{
  "error": "Resource already exists"
}
```

**500 Internal Server Error**
```json
{
  "error": "Unexpected server error"
}
```

### Validation Errors

```json
{
  "email": "Email should be valid",
  "password": "Password must be at least 8 characters",
  "amount": "Amount must be at least 50 cents"
}
```

---

## Payment Status Transitions

```
CREATED
  ↓
REQUIRES_ACTION (if additional action needed)
  ↓
SUCCEEDED ✓
  or
PROCESSING
  ↓
SUCCEEDED ✓
  or
FAILED ✗
  or
CANCELED ✗
```

---

## Rate Limiting

Current rate limits:
- Authentication endpoints: 10 requests/minute per IP
- Payment endpoints: 100 requests/minute per user
- Webhook endpoints: Unlimited (Stripe trusted)

---

## Pagination (Future)

Not currently implemented. All endpoints return single resources or events.

---

## Test Credentials

### User Roles

```
ADMIN - Full system access
MERCHANT - Can create and view own payments
CLIENT - Can view own transactions (future)
```

### Stripe Test Cards

| Card Number | Type | Result |
|-------------|------|--------|
| 4242 4242 4242 4242 | Visa | Success |
| 5555 5555 5555 4444 | Mastercard | Success |
| 3782 822463 10005 | Amex | Success |
| 4000 0000 0000 0002 | Visa | Declined |
| 4000 0025 0000 3155 | Visa | Requires Auth |

### Test JWT Token

```
Header: {"alg":"HS256","typ":"JWT"}
Payload: {"sub":"test@example.com","iat":1704067200,"exp":2000000000}
Secret: your-jwt-secret
```

---

## API Response Times

Target response times:
- Authentication: < 200ms
- Payment Creation: < 500ms
- Payment Retrieval: < 100ms
- Webhook Processing: < 1s

---

## SDK/Client Libraries

### JavaScript/TypeScript
```javascript
// Using Fetch API
const response = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'merchant@example.com',
    password: 'SecurePassword123!'
  })
});
const { token } = await response.json();
```

### Python
```python
import requests

response = requests.post('http://localhost:8080/api/auth/login', json={
    'email': 'merchant@example.com',
    'password': 'SecurePassword123!'
})
token = response.json()['token']
```

### Java
```java
OkHttpClient client = new OkHttpClient();
RequestBody body = RequestBody.create(
  MediaType.parse("application/json"),
  "{\"email\":\"merchant@example.com\",\"password\":\"SecurePassword123!\"}"
);
Request request = new Request.Builder()
  .url("http://localhost:8080/api/auth/login")
  .post(body)
  .build();
Response response = client.newCall(request).execute();
```

---

## Changelog

### v1.0.0 (April 5, 2026)
- Initial release
- User authentication with JWT
- Payment intent creation
- Stripe webhook integration
- Firebase integration for data storage
- Role-based access control

---

**Last Updated**: April 5, 2026
**API Version**: 1.0.0

