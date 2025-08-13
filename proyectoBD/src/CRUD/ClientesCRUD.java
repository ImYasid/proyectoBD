package CRUD;

import ConexionSQL.SqlConection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ClientesCRUD {
public static void crearCliente(String id_cliente, String nombres, String direccion, String telefono, int id_sucursal, SqlConection conexionSQL) {

    try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password)) {
        // Insertar en Cliente_id (si aplica)
        String sqlClienteId = "SET XACT_ABORT ON; INSERT INTO " + "Cliente_id" + " (id_cliente) VALUES (?)";
        try (PreparedStatement psClienteId = con.prepareStatement(sqlClienteId)) {
            psClienteId.setString(1, id_cliente);
            psClienteId.executeUpdate();
        }

        // Insertar en tabla fragmentada según sede
        String sql = "SET XACT_ABORT ON; INSERT INTO " + "Cliente_Info" + " (id_cliente, nombres, direccion, telefono, id_sucursal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id_cliente);
            ps.setString(2, nombres);
            ps.setString(3, direccion);
            ps.setString(4, telefono);
            ps.setInt(5, id_sucursal);
            ps.executeUpdate();
        }
        
        String tablafin = "";
        if (id_sucursal == 1){
            tablafin = "Cliente_informacion_norte";
        } else if(id_sucursal == 2){
            tablafin = "Cliente_Info_Sur";
        }

        JOptionPane.showMessageDialog(null, "Cliente creado correctamente en " + tablafin);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al crear cliente: " + e.getMessage());
    }
}

    
    public void buscarClientePorId(String id_cliente, SqlConection conexionSQL, javax.swing.JTable tablaClientes, java.awt.Component parent) {
    String sql = "SELECT id_cliente, nombres, direccion, telefono, id_sucursal FROM " + "Cliente_Info" + " WHERE id_cliente = ?";

    try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, id_cliente);
        try (ResultSet rs = ps.executeQuery()) {
            DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
            modelo.setRowCount(0); // Limpiar tabla

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("id_cliente");  // Mejor usar getString para id_cliente
                fila[1] = rs.getString("nombres");
                fila[2] = rs.getString("direccion");
                fila[3] = rs.getString("telefono");
                fila[4] = rs.getInt("id_sucursal");
                modelo.addRow(fila);
            }

            if (modelo.getRowCount() == 0) {
                JOptionPane.showMessageDialog(parent, "No se encontró cliente con esa cédula");
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(parent, "Error al buscar cliente: " + e.getMessage());
    }
}

    
    public static void actualizarCliente(String id_cliente, String nombres, String direccion, String telefono, int id_sucursal, SqlConection conexionSQL) {
    String sql = "SET XACT_ABORT ON; UPDATE " + "Cliente_Info" + " SET nombres = ?, direccion = ?, telefono = ?, id_sucursal = ? WHERE id_cliente = ?";

    try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, nombres);
        ps.setString(2, direccion);
        ps.setString(3, telefono);
        ps.setInt(4, id_sucursal);
        ps.setString(5, id_cliente);

        int filas = ps.executeUpdate();

        if (filas > 0) {
            JOptionPane.showMessageDialog(null, "Cliente actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró cliente con ese ID.");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar cliente: " + e.getMessage());
    }
}

    
    public static void eliminarCliente(String id_cliente, SqlConection conexionSQL) {

        String sql = "SET XACT_ABORT ON; DELETE FROM " + "Cliente_Info" + " WHERE id_cliente = ?";

        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id_cliente);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró cliente con ese ID.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar cliente Vista: " + e.getMessage());
        }

        String sqlClienteID = "SET XACT_ABORT ON; DELETE FROM " + "Cliente_id" + " WHERE id_cliente = ?";

        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sqlClienteID)) {
            ps.setString(1, id_cliente);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar cliente De Cliente_id: " + e.getMessage());
        }
    }
}
    

