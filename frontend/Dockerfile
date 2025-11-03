# Build stage
FROM node:18-alpine AS build  # Bumped from 16 to 18
WORKDIR /app
# Copy package files first
COPY package*.json ./
RUN npm install
# Copy source code
COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
# Fix: Copy from build stage (includes nginx.config via COPY . .)
COPY --from=build /app/nginx.config /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]