# --- Build Stage ---
# Use a lightweight Node image matching your local version (Node 20)
FROM node:20-alpine AS builder
WORKDIR /app

# Optimize caching: Copy package files first to cache node_modules
COPY package.json package-lock.json ./
RUN npm ci

# Copy the rest of the frontend source code
COPY . .

# Build the static production assets via Vite (generates the /dist folder)
RUN npm run build

# --- Run Stage ---
# Use a lightweight Nginx alpine image to serve the static files
FROM nginx:1.25-alpine

# Copy the built static files from the builder stage to Nginx's default public folder
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy a custom Nginx configuration to handle React routing safely
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

# Start Nginx in the foreground so the Docker container stays alive
CMD ["nginx", "-g", "daemon off;"]