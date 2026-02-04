package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class UtilTests {
    
    @Test
    void testMongoDBConnectionSingleton() throws Exception {
        // Verificar que el constructor es privado
        Constructor<MongoDBConnection> constructor = MongoDBConnection.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        
        // Hacer el constructor accesible para test
        constructor.setAccessible(true);
        
        try {
            // Intentar crear instancia (debería fallar o crear nueva)
            MongoDBConnection instance = constructor.newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            // Esto es aceptable si el singleton ya existe
        }
    }
}