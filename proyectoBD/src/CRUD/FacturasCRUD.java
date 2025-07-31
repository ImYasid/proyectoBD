package CRUD;

import ConexionSQL.SqlConection;
import java.sql.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FacturasCRUD {
    
    public static void crearFactura(Date fecha, double total, int idCliente, SqlConection conexionSQL) {
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password)) {
            String sql = "INSERT INTO Factura (fecha, total, id_cliente, rowguid) VALUES (?, ?, ?, NEWID())";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            ps.setDouble(2, total);
            ps.setInt(3, idCliente);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Factura creada correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear factura: " + e.getMessage());
        }
    }
    
    public static void buscarFacturaPorId(int id, SqlConection conexionSQL, javax.swing.JTable tablaFacturas) {
        String sql = "SELECT id_factura, fecha, total, id_cliente, rowguid FROM Factura WHERE id_factura = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = (DefaultTableModel) tablaFacturas.getModel();
                modelo.setRowCount(0);

                while (rs.next()) {
                    Object[] fila = new Object[5];
                    fila[0] = rs.getInt("id_factura");
                    fila[1] = rs.getDate("fecha");
                    fila[2] = rs.getDouble("total");
                    fila[3] = rs.getInt("id_cliente");
                    fila[4] = rs.getString("rowguid");
                    modelo.addRow(fila);
                }

                if (modelo.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontró factura con ese ID");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar factura: " + e.getMessage());
        }
    }
    
    public static void listarFacturasPorCliente(int idCliente, SqlConection conexionSQL, javax.swing.JTable tablaFacturas) {
        String sql = "SELECT id_factura, fecha, total, id_cliente, rowguid FROM Factura WHERE id_cliente = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = (DefaultTableModel) tablaFacturas.getModel();
                modelo.setRowCount(0);

                while (rs.next()) {
                    Object[] fila = new Object[5];
                    fila[0] = rs.getInt("id_factura");
                    fila[1] = rs.getDate("fecha");
                    fila[2] = rs.getDouble("total");
                    fila[3] = rs.getInt("id_cliente");
                    fila[4] = rs.getString("rowguid");
                    modelo.addRow(fila);
                }

                if (modelo.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontraron facturas para este cliente");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar facturas por cliente: " + e.getMessage());
        }
    }
    
    public static void listarTodasFacturas(SqlConection conexionSQL, javax.swing.JTable tablaFacturas) {
        String sql = "SELECT id_factura, fecha, total, id_cliente, rowguid FROM Factura";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            DefaultTableModel modelo = (DefaultTableModel) tablaFacturas.getModel();
            modelo.setRowCount(0);

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id_factura");
                fila[1] = rs.getDate("fecha");
                fila[2] = rs.getDouble("total");
                fila[3] = rs.getInt("id_cliente");
                fila[4] = rs.getString("rowguid");
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar facturas: " + e.getMessage());
        }
    }
    
    public static void buscarFacturaPorGuid(String guid, SqlConection conexionSQL, javax.swing.JTable tablaFacturas) {
        String sql = "SELECT id_factura, fecha, total, id_cliente, rowguid FROM Factura WHERE rowguid = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, guid);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = (DefaultTableModel) tablaFacturas.getModel();
                modelo.setRowCount(0);

                while (rs.next()) {
                    Object[] fila = new Object[5];
                    fila[0] = rs.getInt("id_factura");
                    fila[1] = rs.getDate("fecha");
                    fila[2] = rs.getDouble("total");
                    fila[3] = rs.getInt("id_cliente");
                    fila[4] = rs.getString("rowguid");
                    modelo.addRow(fila);
                }

                if (modelo.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontró factura con ese GUID");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar factura por GUID: " + e.getMessage());
        }
    }
}