<h1 align="center">
<img src="Spy.png" alt=""/>
<p>SpyF</p>
</h1>

## Overview

**SpyF** is a price monitoring service that integrates with the Mercado Livre API and a custom web scraping API for Amazon. This service acts as a price spy, allowing users to input a product name and a minimum price, and in return, it provides the best product available across multiple stores.

## Features

- **Mercado Livre Integration**: Seamlessly fetches product data from Mercado Livre using their official API.
- **Amazon Scraping**: Utilizes a custom-built web scraping API to gather product information from Amazon.
- **Price Comparison**: Compares prices from both Mercado Livre and Amazon to find the best deal for the user.
- **User-Friendly API**: Simple endpoints for users to query the best product and set up price watch alerts.
- **Price Monitoring**: Allows logged-in users to submit product URLs for price monitoring via scheduling and notifies users via email when the product price drops.

## How It Works

1. **User Input**: The user provides the product name and a minimum price.
2. **Data Fetching**: The service fetches product data from both Mercado Livre and Amazon.
3. **Price Comparison**: The fetched data is analyzed to find the product with the best price.
4. **Result Delivery**: The best product details are returned to the user.
5. **Price Monitoring**: Logged-in users can submit product URLs to monitor prices. The service schedules regular checks and sends email notifications when the price drops.

## Endpoints

### Find Best Product

- **URL**: `/v1/products/best-product`
- **Method**: `GET`
- **Parameters**:
    - `productName` (String): The name of the product to search for.
    - `productMinPrice` (Double, default = 0.0): The minimum price to consider.
- **Response**: Returns the best product details including price, link, and title.

### Watch Offer

- **URL**: `/v1/products/offer-watcher/{url}`
- **Method**: `POST`
- **Parameters**:
    - `url` (String): The URL of the product to watch.
- **Response**: Confirms that the user will receive email updates on the product price.

## Technologies Used

- **Java**
- **Spring Boot**
- **Maven**
- **Jackson** for JSON processing
- **Spring Security** for authentication
- **WebClient** for API calls

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/GustavoLyra23/spy_price_finder.git
    ```
2. Navigate to the project directory:
    ```sh
   cd spy_price_finder
    ```
3. Build the project:
    ```sh
   mvn clean install
   ```
4. Run the application:
    ```sh
   mvn spring-boot:run
   ``` 
 ### Usage
 Once the application is running, you can use tools like Postman or cURL to interact with the API endpoints. For example, to find the best product, you can send a GET request to /v1/products/best-product with the required parameters.