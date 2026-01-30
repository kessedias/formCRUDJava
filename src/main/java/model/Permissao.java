/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

public class Permissao {
    private int id;
    private String nome;
    private int status; // 0 = inativo, 1 = ativo
    private Timestamp timecreated;
    private Timestamp timemodified;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Timestamp getTimecreated() { return timecreated; }
    public void setTimecreated(Timestamp timecreated) { this.timecreated = timecreated; }

    public Timestamp getTimemodified() { return timemodified; }
    public void setTimemodified(Timestamp timemodified) { this.timemodified = timemodified; }
}
