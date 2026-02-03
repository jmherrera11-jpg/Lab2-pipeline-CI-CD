package util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoDBConnection instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "attendance_system";
    
    private MongoDBConnection() {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Conexión a MongoDB establecida correctamente");
        } catch (Exception e) {
            System.err.println("Error al conectar a MongoDB: " + e.getMessage());
            throw new RuntimeException("Error de conexión a MongoDB", e);
        }
    }
    
    public static MongoDBConnection getInstance() {
        if (instance == null) {
            synchronized (MongoDBConnection.class) {
                if (instance == null) {
                    instance = new MongoDBConnection();
                }
            }
        }
        return instance;
    }
    
    public MongoDatabase getDatabase() {
        return database;
    }
    
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
    
}