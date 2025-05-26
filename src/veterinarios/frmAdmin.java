/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package veterinarios;

import com.mysql.cj.jdbc.CallableStatement;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jcami
 */
public class frmAdmin extends javax.swing.JFrame {

    /**
     * Creates new form frmAdmin
     */
    Veterinaria1 ve;
    frmLogin login;
    frmReporte re;
    String user;
    int iduser;
    int idprofile;
    int idRol = -1;
    infoConexion con;
    DefaultTableModel tableModel;
    int control = 0;
    frmLogin ventanaLogin=new frmLogin();
    ArrayList<Usuario> listaUsuario;
    int idusuario = 4;

    public frmAdmin(String user, int iduser, int idprofile) {
        initComponents();

        con = new infoConexion();
        this.user = user;
        this.iduser = iduser;
        this.idprofile = idprofile;
        ve = new Veterinaria1(user, iduser, idprofile);
        re = new frmReporte(user, iduser, idprofile);
        setTitle("Admin");

        tableModel = new DefaultTableModel();
        txtUsuario.setText("");
        txtUsuario.setVisible(false);
        listaUsuario=new ArrayList<>();
        tableModel.setColumnIdentifiers(new String[]{"id", "usuario", "password", "idRol", "ROL"});

        tbUsuarios.setModel(tableModel);
        ptabla.setVisible(false);
        setSize(450, 160);
        btnAgregar.setVisible(false);
        btnEliminar.setVisible(false);
        txtUsuario.setVisible(false);
        setResizable(false);
    }

    public void insertarUsuario() {

        try {

            java.sql.Connection connection = DriverManager.getConnection(con.getUrl(),
                    con.getUsername(), con.getPassword());
            PreparedStatement stmt = connection.prepareStatement("CALL InsertarUsuario(?, ?, ?)");

            for (int i = 0; i < listaUsuario.size(); i++) {
                stmt.setString(1, listaUsuario.get(i).getUser());
                stmt.setString(2, listaUsuario.get(i).getContraseña());
                stmt.setInt(3, listaUsuario.get(i).getIdProfile());
                stmt.executeUpdate();

            }

            stmt.executeUpdate();

            stmt.close();
            connection.close();

        } catch (SQLException ex) {

            System.out.println(ex);
            JOptionPane.showMessageDialog(rootPane, "Error");
        }

    }

    public void mostrar() {

        try {

            Connection connection = DriverManager.getConnection(con.getUrl(), con.getUsername(), con.getPassword());

            PreparedStatement statement = connection.prepareStatement("call LeerUsuarios()");
            ResultSet ps = statement.executeQuery();
            ResultSetMetaData metaData = ps.getMetaData();

            while (ps.next()) {

                Object[] filas = new Object[metaData.getColumnCount()];
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    filas[i] = ps.getObject(i + 1);
                }
                tableModel.addRow(filas);
            }

            ps.close();
            statement.close();
            connection.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error en la consulta");
            System.out.println(ex);

        }

    }

    public void panelUsuario() {

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField idRolField = new JTextField(5);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        inputPanel.add(new JLabel("Usuario:"));
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Contraseña:"));
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("ID Rol:"));
        inputPanel.add(idRolField);

        int result = JOptionPane.showConfirmDialog(
                this,
                inputPanel,
                "Ingresar Datos del Nuevo Usuario",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().toUpperCase();
            String password = new String(passwordField.getPassword());
            String idRolText = idRolField.getText();

            try {
                idRol = Integer.parseInt(idRolText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "El ID de Rol debe ser un número válido.",
                        "Error de Entrada",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

           
            listaUsuario.add(new Usuario(idusuario,username,password,idRol));
            insertarUsuario();

            JOptionPane.showMessageDialog(this,
                    "Usuario '" + username + "' con ID Rol " + idRol + " creado.",
                    "Usuario Creado",
                    JOptionPane.INFORMATION_MESSAGE);

        } else {
            System.out.println("Creación de usuario cancelada.");
        }
    }
    
    private boolean eliminarUsuarioDeBD(int idUsuario) {
        Connection connection = null;
        PreparedStatement st = null;

        try {
            connection = DriverManager.getConnection(con.getUrl(), con.getUsername(), con.getPassword());
            
            st=connection.prepareStatement("call EliminarUsuario(?)");
            st.setInt(1, idUsuario); 

            int filasAfectadas = st.executeUpdate(); 

            if (filasAfectadas > 0) {
                return true;
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró un usuario con el ID " + idUsuario + ".",
                        "Usuario No Encontrado", JOptionPane.WARNING_MESSAGE);
                return false; 
            }

        } catch (SQLException ex) {
            System.err.println("Error al eliminar usuario de la BD: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error de base de datos al intentar eliminar el usuario: " + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
           
            try {
                if (st != null) st.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar recursos de la BD después de eliminar: " + ex.getMessage());
                ex.printStackTrace();
            }
        }}
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnReporte = new javax.swing.JButton();
        btnUsuarios = new javax.swing.JButton();
        btnInsert = new javax.swing.JButton();
        ptabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUsuarios = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtUsuario = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel1.setText("VitalVet-ADMIN");

        btnReporte.setText("Reporte");
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });

        btnUsuarios.setText("Usuarios");
        btnUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuariosActionPerformed(evt);
            }
        });

        btnInsert.setText("Insertar Consultas");
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        tbUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbUsuarios);

        javax.swing.GroupLayout ptablaLayout = new javax.swing.GroupLayout(ptabla);
        ptabla.setLayout(ptablaLayout);
        ptablaLayout.setHorizontalGroup(
            ptablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        ptablaLayout.setVerticalGroup(
            ptablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ptablaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE))
        );

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        txtUsuario.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(btnReporte)
                        .addGap(44, 44, 44)
                        .addComponent(btnUsuarios)
                        .addGap(39, 39, 39)
                        .addComponent(btnInsert)
                        .addGap(0, 120, Short.MAX_VALUE))
                    .addComponent(ptabla, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAgregar)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnAgregar)
                    .addComponent(btnEliminar)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReporte)
                    .addComponent(btnUsuarios)
                    .addComponent(btnInsert))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ptabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        // TODO add your handling code here:
        ve.setVisible(true);

    }//GEN-LAST:event_btnInsertActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:

        login = new frmLogin();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosed

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        // TODO add your handling code here:
        re.setVisible(true);
    }//GEN-LAST:event_btnReporteActionPerformed

    private void btnUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuariosActionPerformed
        // TODO add your handling code here:
        if (control == 0) {
            setSize(504, 340);
            ptabla.setVisible(true);
            control = 1;
            btnAgregar.setVisible(true);
            btnEliminar.setVisible(true);
            
            mostrar();
        } else {
            ptabla.setVisible(false);
            setSize(450, 160);
            control = 0;
            tableModel.setRowCount(0);
            btnAgregar.setVisible(false);
            btnEliminar.setVisible(false);
            
        }

    }//GEN-LAST:event_btnUsuariosActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:

        panelUsuario();

    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        btnAgregar.setVisible(false);
        txtUsuario.setVisible(true);
         String idText = txtUsuario.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, ingrese el ID del usuario a eliminar.",
                    "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUsuarioAEliminar;
        try {
            idUsuarioAEliminar = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El ID debe ser un número válido.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

       
        int confirmResult = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar al usuario con ID: " + idUsuarioAEliminar + "? Esta acción es irreversible.",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmResult == JOptionPane.YES_OPTION) {
            boolean eliminadoExitosamente = eliminarUsuarioDeBD(idUsuarioAEliminar);

            if (eliminadoExitosamente) {
                JOptionPane.showMessageDialog(this,
                        "Usuario con ID " + idUsuarioAEliminar + " eliminado exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                
                if (ventanaLogin != null) {
                    ventanaLogin.cargarUsuariosDesdeBD(); 
                } else {
                    System.err.println("Advertencia: No se pudo actualizar la lista de usuarios en frmLogin porque la referencia es nula.");
                }
                
                txtUsuario.setText("");
                btnAgregar.setVisible(true);
                txtUsuario.setVisible(false);
            } 
        }
             
    }//GEN-LAST:event_btnEliminarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmAdmin(null, 0, 0).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnReporte;
    private javax.swing.JButton btnUsuarios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel ptabla;
    private javax.swing.JTable tbUsuarios;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
