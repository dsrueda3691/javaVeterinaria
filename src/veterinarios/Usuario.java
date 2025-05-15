/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package veterinarios;

/**
 *
 * @author CBN
 */
public class Usuario {
    private int id;
    private String user;
    private String contraseña;
    private int idProfile;

    public Usuario(int id, String user, String contraseña, int idProfile) {
        this.id = id;
        this.user = user;
        this.contraseña = contraseña;
        this.idProfile = idProfile;
    }

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getContraseña() {
        return contraseña;
    }

    public int getIdProfile() {
        return idProfile;
    }
    
}
