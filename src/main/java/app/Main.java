/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import seed.DatabaseSeed;
import view.TelaLogin;

public class Main {
    public static void main(String[] args) {
        DatabaseSeed.run();
        new TelaLogin().setVisible(true);
    }
}
