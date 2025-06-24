package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigApi {
    private static final Properties props = new Properties();

    static{
        try {
            props.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            System.out.println("Erro ao carregar arquivo da API");
            e.printStackTrace();
        }
    }
    public static String get(String key){
        return props.getProperty(key);
    }

}
