package CRUD;

import ConexionSQL.SqlConection;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EmpleadosCRUD {
    
    public static void crearEmpleado(int id, String nombre, String cargo, String horario, int idSucursal, SqlConection conexionSQL) {
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password)) {
            String sql = "INSERT INTO Empleado (id_empleado, nombre, cargo, horario, id_sucursal) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, nombre);
            ps.setString(3, cargo);
            ps.setString(4, horario);
            ps.setInt(5, idSucursal);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Empleado creado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear empleado: " + e.getMessage());
        }
    }
    
    public static void buscarEmpleadoPorId(int id, SqlConection conexionSQL, javax.swing.JTable tablaEmpleados) {
        String sql = "SELECT id_empleado, nombre, cargo, horario, id_sucursal FROM Empleado WHERE id_empleado = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = (DefaultTableModel) tablaEmpleados.getModel();
                modelo.setRowCount(0); // Limpiar tabla

                while (rs.next()) {
                    Object[] fila = new Object[5];
                    fila[0] = rs.getInt("id_empleado");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getString("cargo");
                    fila[3] = rs.getString("horario");
                    fila[4] = rs.getInt("id_sucursal");
                    modelo.addRow(fila);
                }

                if (modelo.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontró empleado con ese ID");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar empleado: " + e.getMessage());
        }
    }
    
    public static void actualizarEmpleado(int id, String nombre, String cargo, String horario, int idSucursal, SqlConection conexionSQL) {
        String sql = "UPDATE Empleado SET nombre = ?, cargo = ?, horario = ?, id_sucursal = ? WHERE id_empleado = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, cargo);
            ps.setString(3, horario);
            ps.setInt(4, idSucursal);
            ps.setInt(5, id);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Empleado actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró empleado con ese ID.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar empleado: " + e.getMessage());
        }
    }
    
    public static void eliminarEmpleado(int id, SqlConection conexionSQL) {
        String sql = "DELETE FROM Empleado WHERE id_empleado = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Empleado eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró empleado con ese ID.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar empleado: " + e.getMessage());
        }
    }
    
    public static void listarTodosEmpleados(SqlConection conexionSQL, javax.swing.JTable tablaEmpleados) {
        String sql = "SELECT id_empleado, nombre, cargo, horario, id_sucursal FROM Empleado";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            DefaultTableModel modelo = (DefaultTableModel) tablaEmpleados.getModel();
            modelo.setRowCount(0); // Limpiar tabla

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id_empleado");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("cargo");
                fila[3] = rs.getString("horario");
                fila[4] = rs.getInt("id_sucursal");
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar empleados: " + e.getMessage());
        }
    }
    
    public static void buscarEmpleadosPorSucursal(int idSucursal, SqlConection conexionSQL, javax.swing.JTable tablaEmpleados) {
        String sql = "SELECT id_empleado, nombre, cargo, horario, id_sucursal FROM Empleado WHERE id_sucursal = ?";
        
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = (DefaultTableModel) tablaEmpleados.getModel();
                modelo.setRowCount(0); // Limpiar tabla

                while (rs.next()) {
                    Object[] fila = new Object[5];
                    fila[0] = rs.getInt("id_empleado");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getString("cargo");
                    fila[3] = rs.getString("horario");
                    fila[4] = rs.getInt("id_sucursal");
                    modelo.addRow(fila);
                }

                if (modelo.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontraron empleados para esta sucursal");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar empleados por sucursal: " + e.getMessage());
        }
    }
}
