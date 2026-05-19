package com.app.bank;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "com.app.bank.repo ")
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;
    
    @Bean
    MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    MongoOperations mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, databaseName);   
    }

    
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        if (mongoUri == null || mongoUri.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "MONGODB_URI is not configured. Please check application.properties"
            );
        }
        // Parse and validate the connection string
        ConnectionString connectionString = new ConnectionString(mongoUri);

        // Apply connection string and custom settings
        builder.applyConnectionString(connectionString)
                // Set application name
                .applicationName("bank")
                // Configure connection pool for optimal performance
                .applyToConnectionPoolSettings(poolBuilder ->
                    poolBuilder.maxSize(100)                                    // Maximum connections in pool
                           .minSize(5)                                          // Minimum connections to maintain
                           .maxConnectionIdleTime(60000, TimeUnit.MILLISECONDS) // Release idle connections after 60s
                           .maxWaitTime(10000, TimeUnit.MILLISECONDS)           // Wait up to 10s for available connection
                           .maintenanceInitialDelay(0, TimeUnit.MILLISECONDS)   // Start maintenance immediately
                           .maintenanceFrequency(60000, TimeUnit.MILLISECONDS)  // Run maintenance every 60s
                )
                // Configure socket timeouts to prevent hanging connections
                .applyToSocketSettings(socketBuilder ->
                    socketBuilder.connectTimeout(10000, TimeUnit.MILLISECONDS)  // 10s to establish connection
                           .readTimeout(60000, TimeUnit.MILLISECONDS)           // 60s to wait for server response (increased for aggregations)
                )
                // Configure server selection timeout
                .applyToClusterSettings(clusterBuilder ->
                    clusterBuilder.serverSelectionTimeout(10000, TimeUnit.MILLISECONDS)  // 10s to select server
                )
                // Retry writes for better reliability
                .retryWrites(true)
                .retryReads(true);

    }
}
