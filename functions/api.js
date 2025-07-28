const { spawn } = require('child_process');
const path = require('path');
const fs = require('fs');

// Path to the compiled Spring Boot JAR
const JAR_PATH = path.join(process.cwd(), 'target', 'backend-0.0.1-SNAPSHOT.jar');

let server = null;

// Environment variables for the Java process
const env = {
  ...process.env,
  SPRING_PROFILES_ACTIVE: 'production',
  SERVER_PORT: 0, // Use random port
  DATABASE_URL: process.env.DATABASE_URL,
  DATABASE_USERNAME: process.env.DATABASE_USERNAME,
  DATABASE_PASSWORD: process.env.DATABASE_PASSWORD,
  JWT_SECRET: process.env.JWT_SECRET,
  JWT_EXPIRATION: process.env.JWT_EXPIRATION || '86400000'
};

// Start the Spring Boot application
function startServer() {
  return new Promise((resolve, reject) => {
    if (!fs.existsSync(JAR_PATH)) {
      throw new Error(`JAR file not found at ${JAR_PATH}. Please build the project first.`);
    }

    server = spawn('java', ['-jar', JAR_PATH], {
      env,
      stdio: ['ignore', 'pipe', 'pipe']
    });

    // Log server output
    server.stdout.on('data', (data) => {
      console.log(`[Spring Boot] ${data}`);
      // Check for the server startup message
      if (data.toString().includes('Started BackendApplication')) {
        resolve(server);
      }
    });

    server.stderr.on('data', (data) => {
      console.error(`[Spring Boot Error] ${data}`);
    });

    server.on('error', (error) => {
      console.error('Failed to start Spring Boot application:', error);
      reject(error);
    });

    // Set a timeout for server startup
    setTimeout(() => {
      reject(new Error('Timeout starting Spring Boot application'));
    }, 30000); // 30 seconds timeout
  });
}

// Netlify serverless function handler
exports.handler = async (event, context) => {
  // Start the server if not already running
  if (!server) {
    try {
      await startServer();
    } catch (error) {
      console.error('Error starting server:', error);
      return {
        statusCode: 500,
        body: JSON.stringify({ error: 'Failed to start server', details: error.message })
      };
    }
  }

  // Forward the request to the Spring Boot application
  return new Promise((resolve) => {
    const request = require('http').request(
      {
        hostname: 'localhost',
        port: 8080, // This should match the port in your application.properties
        path: event.path.replace('/.netlify/functions/api', ''),
        method: event.httpMethod,
        headers: {
          ...event.headers,
          'x-netlify-event': 'true',
          host: 'localhost:8080'
        }
      },
      (response) => {
        const chunks = [];
        response.on('data', (chunk) => chunks.push(chunk));
        response.on('end', () => {
          const body = Buffer.concat(chunks);
          resolve({
            statusCode: response.statusCode,
            headers: response.headers,
            body: body.toString()
          });
        });
      }
    );

    request.on('error', (error) => {
      console.error('Error forwarding request:', error);
      resolve({
        statusCode: 500,
        body: JSON.stringify({ error: 'Failed to forward request', details: error.message })
      });
    });

    if (event.body) {
      request.write(event.body);
    }
    request.end();
  });
};

// Cleanup on exit
process.on('SIGTERM', () => {
  if (server) {
    server.kill();
  }
  process.exit(0);
});
