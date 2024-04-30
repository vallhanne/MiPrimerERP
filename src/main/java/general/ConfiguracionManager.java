package general;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfiguracionManager {

    private static final String CONFIG_FILE = "config.properties";
    private Properties propiedades;

    public ConfiguracionManager() {
        propiedades = new Properties();
        comprobarFicheroExiste();
        cargarPropiedades();
    }
    
    private void comprobarFicheroExiste() {
        File fichero = new File(CONFIG_FILE);
        if (!fichero.exists()) {
            try {
                fichero.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cargarPropiedades() {
    	//Comprobar que exista el fichero primero para evitar error
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            propiedades.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarPropiedad(String clave, String valor) {
        propiedades.setProperty(clave, valor);
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            propiedades.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String obtenerPropiedad(String clave) {
        return propiedades.getProperty(clave, "");
    }
}
