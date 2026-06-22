# ============================================
# Stage 1: Build the React app with Vite
# ============================================
FROM node:20-alpine AS builder

WORKDIR /build

# Install dependencies first (cached layer)
COPY package.json package-lock.json* ./
RUN npm ci

# Copy source and build static files
COPY . .
RUN npm run build

# ============================================
# Stage 2: Serve static files via nginx
# ============================================
FROM nginx:alpine

# Custom nginx config (SPA routing + backend proxy)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy built static files from the builder stage
COPY --from=builder /build/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]