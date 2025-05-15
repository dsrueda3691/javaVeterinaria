/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package veterinarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author HP
 */
public class frmReporte extends javax.swing.JFrame {

    /**
     * Creates new form frmReporte
     */
    DefaultTableModel tableModel;
    TableRowSorter<DefaultTableModel> sorter;
    infoConexion con;
    String user;
    int idUser;
    int idProfile;

    public frmReporte(String user, int iduser, int idprofile) {
        initComponents();
        this.user = user.replace(" ", "");
        this.idUser = iduser;
        this.idProfile = idprofile;
        con = new infoConexion();

        lblUser.setText("User:" + this.user.toUpperCase());

        if (this.idProfile != 1) {

            btnIngresos.setVisible(false);
            lbConsulta.setVisible(false);
            comboConsulta.setVisible(false);
            btnFrecuencia.setVisible(false);

        }

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("VitalVet");
        setResizable(true);
        txtFecha.setText("");
        txtDocumento.setText("");
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Fecha", "Paciente", "Documento-Cliente", "Cliente",
            "Veterinario", "TipoPaciente", "TipoConsulta",
            "Precio", "Observaciones"});

        tbReporte.setModel(tableModel);

        sorter = new TableRowSorter<>(tableModel);
        tbReporte.setRowSorter(sorter);
        mostrar();

    }

    public void mostrar() {
        try {

            Connection connection = DriverManager.getConnection(con.getUrl(), con.getUsername(), con.getPassword());

            PreparedStatement statement = connection.prepareStatement("call MostrarConsultas()");
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

    private void calcularIngresosFiltrados() {
        double totalIngresosFiltrados = 0;
        JTable tabla = tbReporte;

        for (int i = 0; i < tabla.getRowCount(); i++) {

            try {

                String precioStr = tabla.getValueAt(i, 7).toString();
                double precio = Double.parseDouble(precioStr);
                totalIngresosFiltrados += precio;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error al leer el precio en la fila " + (i + 1), "Error de formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Total de ingresos con los filtros aplicados: $" + String.format("%.2f", totalIngresosFiltrados), "Total de Ingresos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void filtrarTabla() {
        String formatofecha = "yyyy-mm-DD";
        String fecha = txtFecha.getText().trim().toLowerCase();
        String documento=txtDocumento.getText().trim().toLowerCase();

        if (!fecha.isEmpty()) {
            if (!isValidDate(fecha, formatofecha)) {
                JOptionPane.showMessageDialog(this, "Fecha inválida. Ingrese una fecha válida en formato yyyy-mm-DD.", "Error", JOptionPane.INFORMATION_MESSAGE);
                txtFecha.setText("");
                return; // Detener el filtrado si la fecha es inválida
            }
        }

        String veterinario = comboVeterinario.getSelectedItem().toString();
        String consulta = comboConsulta.getSelectedItem().toString();
        boolean veterinarioB = false;
        boolean consultaB = false;
        if (veterinario == "Seleccione..." && consulta == "Seleccione...") {
            veterinarioB = true;
            consultaB = true;
        }


        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                boolean coincideFecha = entry.getStringValue(0).toLowerCase().contains(fecha);
                boolean coincideDocumento = documento.isEmpty() || entry.getStringValue(2).trim().equalsIgnoreCase(documento.trim());
                boolean coincideVeterinario = veterinario.equals("Seleccione...") || entry.getStringValue(4).equals(veterinario);
                boolean coincideConsulta = consulta.equals("Seleccione...") || entry.getStringValue(6).equals(consulta);
                return coincideFecha && coincideVeterinario && coincideConsulta && coincideDocumento;
            }
        };

        sorter.setRowFilter(filter);
        if (sorter.getViewRowCount() == 0) {
            if (veterinarioB && consultaB) {
                JOptionPane.showMessageDialog(this,
                        "Campos no encontrados", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Campos no encontrados",
                         "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }

            txtFecha.setText("");
            txtDocumento.setText("");
            comboVeterinario.setSelectedIndex(0);
            comboConsulta.setSelectedIndex(0);
            filtrarTabla();
        }

    }

    public static boolean isValidDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false); // No permitir fechas inválidas (como 30/02/2024)
        try {
            Date date = sdf.parse(dateStr); // Intenta parsear la fecha
            return true; // Si no hay excepción, la fecha es válida
        } catch (ParseException e) {
            return false; // Si hay excepción, la fecha es inválida
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbReporte = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        comboVeterinario = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        lbConsulta = new javax.swing.JLabel();
        btnIngresos = new javax.swing.JButton();
        comboConsulta = new javax.swing.JComboBox<>();
        btnFrecuencia = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtDocumento = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        tbReporte.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbReporte);

        jLabel1.setFont(new java.awt.Font("Arial", 2, 24)); // NOI18N
        jLabel1.setText("VitalVet - Reportes");

        jLabel2.setText("Fecha");

        txtFecha.setText("jTextField1");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        comboVeterinario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione...", "Dra. Patty Pata", "Raúl Mora", "Dra. Felicia Lame", "Dr. Bernardo Cura", "Margarita Pica" }));
        comboVeterinario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboVeterinarioActionPerformed(evt);
            }
        });

        jLabel3.setText("Veterinario");

        lblUser.setText("User:");

        lbConsulta.setText("Consulta");

        btnIngresos.setText("Reporte ingresos");
        btnIngresos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresosActionPerformed(evt);
            }
        });

        comboConsulta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione...", "Consulta de Rutina", "Consulta de Diagnóstico", "Consulta de Especialidad", "Consulta Preventiva", "Hospitalización", "Peluquería", "Adiestramiento" }));

        btnFrecuencia.setText("Reporte Frecuencia");
        btnFrecuencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrecuenciaActionPerformed(evt);
            }
        });

        jLabel4.setText("Documento");

        txtDocumento.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(221, 221, 221)
                                .addComponent(lblUser)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(lbConsulta)
                                .addGap(18, 18, 18)
                                .addComponent(comboConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(comboVeterinario, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addComponent(btnBuscar)
                                .addGap(18, 18, 18)
                                .addComponent(btnIngresos)
                                .addGap(18, 18, 18)
                                .addComponent(btnFrecuencia))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(36, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblUser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbConsulta)
                        .addComponent(comboConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboVeterinario, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btnBuscar)
                    .addComponent(btnIngresos)
                    .addComponent(btnFrecuencia))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        filtrarTabla();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void comboVeterinarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboVeterinarioActionPerformed


    }//GEN-LAST:event_comboVeterinarioActionPerformed

    private void btnIngresosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresosActionPerformed
        // TODO add your handling code here:
        filtrarTabla();
        calcularIngresosFiltrados();


    }//GEN-LAST:event_btnIngresosActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        tableModel.setRowCount(0);
        mostrar();
        
    }//GEN-LAST:event_formComponentShown

    private void btnFrecuenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrecuenciaActionPerformed
        // TODO add your handling code here:
        
        
        frmFrecuencias fe = new frmFrecuencias();
        fe.setVisible(true);
    }//GEN-LAST:event_btnFrecuenciaActionPerformed

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
            java.util.logging.Logger.getLogger(frmReporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmReporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmReporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmReporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmReporte(null, 0, 0).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnFrecuencia;
    private javax.swing.JButton btnIngresos;
    private javax.swing.JComboBox<String> comboConsulta;
    private javax.swing.JComboBox<String> comboVeterinario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbConsulta;
    private javax.swing.JLabel lblUser;
    private javax.swing.JTable tbReporte;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JTextField txtFecha;
    // End of variables declaration//GEN-END:variables

}
