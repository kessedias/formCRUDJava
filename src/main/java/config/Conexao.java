/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

    public static Connection semBanco() throws Exception {
        String url = "jdbc:mysql://" +
                EnvLoader.get("DB_HOST") + ":" +
                EnvLoader.get("DB_PORT");

        return DriverManager.getConnection(
                url,
                EnvLoader.get("DB_USER"),
                EnvLoader.get("DB_PASS")
        );
    }

    public static Connection comBanco() throws Exception {
        String url = "jdbc:mysql://" +
                EnvLoader.get("DB_HOST") + ":" +
                EnvLoader.get("DB_PORT") + "/" +
                EnvLoader.get("DB_NAME") +
                "?useSSL=false&serverTimezone=UTC";

        return DriverManager.getConnection(
                url,
                EnvLoader.get("DB_USER"),
                EnvLoader.get("DB_PASS")
        );
    }
}
