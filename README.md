# Dynamic PDF Generation API

project provides an API to generate PDF invoices from structured JSON data. The PDF contains seller and buyer information, along with a list of items. The API returns the generated PDF as a downloadable file.

## API Documentation

### Generate Invoice PDF

#### Endpoint:

- **URL**: `/api/pdf`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Produces**: `application/pdf`

#### Request Body Parameters:

| Parameter        | Type   | Description                       |
|------------------|--------|-----------------------------------|
| `seller`         | String | Name of the seller                |
| `sellerGstin`    | String | GSTIN number of the seller        |
| `sellerAddress`  | String | Address of the seller             |
| `buyer`          | String | Name of the buyer                 |
| `buyerGstin`     | String | GSTIN number of the buyer         |
| `buyerAddress`   | String | Address of the buyer              |
| `items`          | Array  | List of items (name, quantity, rate, amount) |

#### Item Object:

| Parameter  | Type   | Description            |
|------------|--------|------------------------|
| `name`     | String | Name of the product     |
| `quantity` | String | Quantity of the product |
| `rate`     | Double | Rate per unit           |
| `amount`   | Double | Total amount for item   |

---

## Response

### Success Response:

- **Status**: `200 OK`
- Message - Given below based on 2 scenerios.
The response will be a PDF file generated based on the provided request data.

### Example Success Response for Newly Created PDF:
New PDF file created successfully at Path: D:\FreightFox Assignments\generatePDF\generated_pdfs\0RSpju9pX2xdIJ7r3MzYDlysGVXidBYX2mR8xDO7ey0=.pdf

### Example Success Response for Already Created PDF:

File already exists. Path: D:\FreightFox Assignments\generatePDF\generated_pdfs\0RSpju9pX2xdIJ7r3MzYDlysGVXidBYX2mR8xDO7ey0=.pdf

---
]
## Error Handling

In case of an error, the API will return a structured response with the following fields:

### Error Response:

- **Status**: Depends on the error (`400`, `404`, `500`).
- **Content-Type**: `application/json`

#### Error Response Body:

| Field       | Type    | Description                        |
|-------------|---------|------------------------------------|
| `timestamp` | String  | The time the error occurred        |
| `status`    | Integer | HTTP status code                   |
| `error`     | String  | A brief description of the error   |
| `message`   | String  | Detailed message for the error     |
| `path`      | String  | API path that caused the error     |

### Example Error Response:

```json
{
  "timestamp": "2024-10-23T08:21:40.284+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "No resource found for /api/pdf",
  "path": "/api/pdf"
}
