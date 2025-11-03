# Build stage
FROM node:16-alpine AS build
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
# ADD THIS LINE: Copy custom config to override default
COPY nginx.config /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]