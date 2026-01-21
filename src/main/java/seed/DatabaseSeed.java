/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seed;

import config.Conexao;
import config.EnvLoader;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSeed {

    public static void run() {
        try (Connection c = Conexao.semBanco();
             Statement st = c.createStatement()) {

            st.execute("CREATE DATABASE IF NOT EXISTS " + EnvLoader.get("DB_NAME"));
            st.execute("USE " + EnvLoader.get("DB_NAME"));

            st.execute("""
                CREATE TABLE IF NOT EXISTS pessoa (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100),
                    sobrenome VARCHAR(100),
                    email VARCHAR(150),
                    telefone VARCHAR(20)
                )
            """);

            System.out.println("Banco e tabela prontos 🚀");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

