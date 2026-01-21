/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {

    private static final Map<String, String> env = new HashMap<>();

    static {
        try (BufferedReader br = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) continue;
                String[] parts = line.split("=", 2);
                env.put(parts[0], parts[1]);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar .env", e);
        }
    }

    public static String get(String key) {
        return env.get(key);
    }
}