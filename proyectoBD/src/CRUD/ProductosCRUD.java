package CRUD;

import ConexionSQL.SqlConection;
import java.sql.*;
import java.util.UUID;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductosCRUD {
    
    public static void crearProducto(int id, String nombre, double precio, int stock, int idSucursal, SqlConection conexionSQL) {
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password)) {
            String sql = "INSERT INTO Vista_Producto (id_producto, nombre, precio, stock, id_sucursal, rowguid) VALUES (?, ?, ?, ?, ?, NEWID())";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, nombre);
            ps.setDouble(3, precio);
            ps.setInt(4, stock);
            ps.setInt(5, idSucursal);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto creado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear producto: " + e.getMessage());
        }
    }
    
    public static void buscarProductoPorId(int id, SqlConection conexionSQL, javax.swing.JTable tablaProductos) {
        String sql = "SELECT id_producto, nombre, precio, stock, id_sucursal, rowguid FROM Vista_Producto WHERE id_producto = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
                modelo.setRowCount(0); // Limpiar tabla

                while (rs.next()) {
                    Object[] fila = new Object[6];
                    fila[0] = rs.getInt("id_producto");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getDouble("precio");
                    fila[3] = rs.getInt("stock");
                    fila[4] = rs.getInt("id_sucursal");
                    fila[5] = rs.getString("rowguid");
                    modelo.addRow(fila);
                }

                if (modelo.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontró producto con ese ID");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar producto: " + e.getMessage());
        }
    }
    
    public static void actualizarProducto(int id, String nombre, double precio, int stock, int idSucursal, SqlConection conexionSQL) {
        String sql = "UPDATE Vista_Producto SET nombre = ?, precio = ?, stock = ?, id_sucursal = ? WHERE id_producto = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, stock);
            ps.setInt(4, idSucursal);
            ps.setInt(5, id);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Producto actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró producto con ese ID.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar producto: " + e.getMessage());
        }
    }
    
    public static void eliminarProducto(int id, SqlConection conexionSQL) {
        String sql = "DELETE FROM Vista_Producto WHERE id_producto = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró producto con ese ID.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar producto: " + e.getMessage());
        }
    }
    
    public static void listarTodosProductos(SqlConection conexionSQL, javax.swing.JTable tablaProductos) {
        String sql = "SELECT id_producto, nombre, precio, stock, id_sucursal, rowguid FROM Vista_Producto";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
            modelo.setRowCount(0); // Limpiar tabla

            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getInt("id_producto");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getDouble("precio");
                fila[3] = rs.getInt("stock");
                fila[4] = rs.getInt("id_sucursal");
                fila[5] = rs.getString("rowguid");
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar productos: " + e.getMessage());
        }
    }
    
    public static void buscarProductosPorSucursal(int idSucursal, SqlConection conexionSQL, javax.swing.JTable tablaProductos) {
        String sql = "SELECT id_producto, nombre, precio, stock, id_sucursal, rowguid FROM Vista_Producto WHERE id_sucursal = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
                modelo.setRowCount(0); // Limpiar tabla

                while (rs.next()) {
                    Object[] fila = new Object[6];
                    fila[0] = rs.getInt("id_producto");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getDouble("precio");
                    fila[3] = rs.getInt("stock");
                    fila[4] = rs.getInt("id_sucursal");
                    fila[5] = rs.getString("rowguid");
                    modelo.addRow(fila);
                }

                if (modelo.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontraron productos para esta sucursal");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar productos por sucursal: " + e.getMessage());
        }
    }
    
    public static void actualizarStock(int idProducto, int cantidad, SqlConection conexionSQL) {
        String sql = "UPDATE Vista_Producto SET stock = stock + ? WHERE id_producto = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Stock actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró producto con ese ID.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar stock: " + e.getMessage());
        }
    }
    
    public static void buscarProductoPorGuid(String guid, SqlConection conexionSQL, javax.swing.JTable tablaProductos) {
        String sql = "SELECT id_producto, nombre, precio, stock, id_sucursal, rowguid FROM Vista_Producto WHERE id_producto = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, guid);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
                modelo.setRowCount(0); // Limpiar tabla

                while (rs.next()) {
                    Object[] fila = new Object[6];
                    fila[0] = rs.getInt("id_producto");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getDouble("precio");
                    fila[3] = rs.getInt("stock");
                    fila[4] = rs.getInt("id_sucursal");
                    fila[5] = rs.getString("rowguid");
                    modelo.addRow(fila);
                }

                if (modelo.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontró producto con ese GUID");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar producto por GUID: " + e.getMessage());
        }
    }
}