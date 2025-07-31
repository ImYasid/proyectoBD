package CRUD;

import ConexionSQL.SqlConection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ClientesCRUD {
    public static void crearCliente(int id, String nombres, String direccion, String telefono, String correo, SqlConection conexionSQL) {
    if (conexionSQL.index != 1) {
        JOptionPane.showMessageDialog(null,
            "No se permite registrar clientes desde esta sucursal (replicación unidireccional).",
            "Acceso restringido", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password)) {
        String sql = "INSERT INTO Cliente (id_cliente, nombres, direccion, telefono, correo) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setString(2, nombres);
        ps.setString(3, direccion);
        ps.setString(4, telefono);
        ps.setString(5, correo);
        ps.executeUpdate();
        JOptionPane.showMessageDialog(null, "Cliente creado correctamente.");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al crear cliente: " + e.getMessage());
    }
}
    
    public void buscarClientePorId(int id, Connection con, javax.swing.JTable tablaClientes, java.awt.Component parent) {
    String sql = "SELECT id_cliente, nombres, direccion, telefono, correo FROM Cliente WHERE id_cliente = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id);
        try (ResultSet rs = ps.executeQuery()) {
            DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
            modelo.setRowCount(0); // Limpiar tabla

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id_cliente");
                fila[1] = rs.getString("nombres");
                fila[2] = rs.getString("direccion");
                fila[3] = rs.getString("telefono");
                fila[4] = rs.getString("correo");
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
    
    public static void actualizarCliente(int id, String nombres, String direccion, String telefono, String correo, SqlConection conexionSQL) {
        if (conexionSQL.index != 1) {
            JOptionPane.showMessageDialog(null,
                "No se permite actualizar clientes desde esta sucursal (replicación unidireccional).",
                "Acceso restringido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE Cliente SET nombres = ?, direccion = ?, telefono = ?, correo = ? WHERE id_cliente = ?";
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombres);
            ps.setString(2, direccion);
            ps.setString(3, telefono);
            ps.setString(4, correo);
            ps.setInt(5, id);
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
    
    public static void eliminarCliente(int id, SqlConection conexionSQL) {
        if (conexionSQL.index != 1) {
            JOptionPane.showMessageDialog(null,
                "No se permite eliminar clientes desde esta sucursal (replicación unidireccional).",
                "Acceso restringido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "DELETE FROM Cliente WHERE id_cliente = ?";
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró cliente con ese ID.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar cliente: " + e.getMessage());
        }
    }
}
