# syntax=docker/dockerfile:1

# Comments are provided throughout this file to help you get started.
# If you need more help, visit the Dockerfile reference guide at
# https://docs.docker.com/go/dockerfile-reference/

# Want to help us make this template better? Share your feedback here: https://forms.gle/ybq9Krt8jtBL3iCk7

ARG NODE_VERSION=22.22.2

################################################################################
# Use node image for base image for all stages.
FROM node:${NODE_VERSION}-alpine AS base

# Set working directory for all build stages.
WORKDIR /usr/src/app


################################################################################
# Create a stage for installing development dependencies.
FROM base AS deps

# Download dependencies as a separate step to take advantage of Docker's caching.
# Leverage a cache mount to /root/.npm to speed up subsequent builds.
# Leverage bind mounts to package.json and package-lock.json to avoid having to copy them
# into this layer.
RUN --mount=type=bind,source=package.json,target=package.json \
    --mount=type=bind,source=package-lock.json,target=package-lock.json \
    --mount=type=cache,target=/root/.npm \
    npm ci

################################################################################
# Create a development runtime stage.
FROM base AS final

# Use development node environment by default.
ENV NODE_ENV development
ENV HOST 0.0.0.0
ENV PORT 5173

# Run the application as a non-root user.
USER node

# Copy package metadata and dependencies.
COPY --chown=node:node package.json package-lock.json ./
COPY --from=deps --chown=node:node /usr/src/app/node_modules ./node_modules

# Copy source files for local development.
COPY --chown=node:node . .


# Expose the port that the application listens on.
EXPOSE 5173

# Run the application.
CMD ["npm", "start", "--", "--host", "0.0.0.0", "--port", "5173"]
