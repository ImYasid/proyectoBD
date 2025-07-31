package CRUD;

import ConexionSQL.SqlConection;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DetalleFacturaCRUD {
    
    public static void agregarDetalle(int idFactura, int idProducto, int cantidad, 
                                    double precioUnit, double subtotal, SqlConection conexionSQL) {
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password)) {
            String sql = "INSERT INTO DetalleFactura (id_factura, id_producto, cantidad, " +
                        "precio_unit, subtotal, rowguid) VALUES (?, ?, ?, ?, ?, NEWID())";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idFactura);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);
            ps.setDouble(4, precioUnit);
            ps.setDouble(5, subtotal);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Detalle agregado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar detalle: " + e.getMessage());
        }
    }
    
    public static void buscarDetallesPorFactura(int idFactura, SqlConection conexionSQL, 
                                              javax.swing.JTable tablaDetalles) {
        String sql = "SELECT id_factura, id_producto, cantidad, precio_unit, subtotal, rowguid " +
                    "FROM DetalleFactura WHERE id_factura = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = (DefaultTableModel) tablaDetalles.getModel();
                modelo.setRowCount(0);

                while (rs.next()) {
                    Object[] fila = new Object[6];
                    fila[0] = rs.getInt("id_factura");
                    fila[1] = rs.getInt("id_producto");
                    fila[2] = rs.getInt("cantidad");
                    fila[3] = rs.getDouble("precio_unit");
                    fila[4] = rs.getDouble("subtotal");
                    fila[5] = rs.getString("rowguid");
                    modelo.addRow(fila);
                }

                if (modelo.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontraron detalles para esta factura");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar detalles: " + e.getMessage());
        }
    }
    
    public static void actualizarDetalle(int idFactura, int idProducto, int cantidad, 
                                       double precioUnit, double subtotal, SqlConection conexionSQL) {
        String sql = "UPDATE DetalleFactura SET cantidad = ?, precio_unit = ?, subtotal = ? " +
                    "WHERE id_factura = ? AND id_producto = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setDouble(2, precioUnit);
            ps.setDouble(3, subtotal);
            ps.setInt(4, idFactura);
            ps.setInt(5, idProducto);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Detalle actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el detalle especificado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar detalle: " + e.getMessage());
        }
    }
    
    public static void eliminarDetalle(int idFactura, int idProducto, SqlConection conexionSQL) {
        String sql = "DELETE FROM DetalleFactura WHERE id_factura = ? AND id_producto = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            ps.setInt(2, idProducto);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Detalle eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el detalle especificado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar detalle: " + e.getMessage());
        }
    }
    
    public static void calcularTotalFactura(int idFactura, SqlConection conexionSQL) {
        String sql = "UPDATE Factura SET total = (SELECT SUM(subtotal) FROM DetalleFactura " +
                    "WHERE id_factura = ?) WHERE id_factura = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFactura);
            ps.setInt(2, idFactura);
            ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al calcular total: " + e.getMessage());
        }
    }
}
