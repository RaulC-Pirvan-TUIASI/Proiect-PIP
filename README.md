# Proiect-PIP

The `Server` class implements a simple HTTP server that serves static files and handles file uploads. It includes functionality for handling GET requests to serve static files and POST requests to upload files.

## Features

- Serving static files
- Handling file uploads

## Dependencies

- Java Development Kit (JDK)
- Maven (for building and managing dependencies)

## Usage

1. Clone the repository:

   ```bash
   git clone <repository_url>
   ```
Navigate to the project directory:

   ```bash
   cd <project_directory>
   ```
Compile the project using Maven:

   ```bash
   mvn compile
   ```

Run the server:
   ```bash
   mvn exec:java -Dexec.mainClass="server.Server"
   ```

## Endpoints
GET /: Serves static files from the src/main/resources directory.
POST /upload: Handles file uploads to the pozici/ directory.
## Configuration
WEB_ROOT: The root directory for serving static files.
UPLOADED_PHOTOS_DIR: The directory where uploaded photos are stored.
PORT: The port number for the HTTP server (default is 8000).
## Contributing
Contributions are welcome! Feel free to open issues and pull requests.

## License
This project is licensed under the MIT License.
