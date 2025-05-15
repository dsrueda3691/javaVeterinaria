/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package veterinarios;

/**
 *
 * @author jcami
 */
public class infoConexion {
    
     String url = "jdbc:mysql://localhost/veterinaria";
    String username="root";
    String password="";

    public infoConexion() {
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
