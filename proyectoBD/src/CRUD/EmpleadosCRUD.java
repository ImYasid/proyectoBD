package CRUD;

import ConexionSQL.SqlConection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class EmpleadosCRUD {

    public static void crearEmpleado(String id_empleado, String nombres, String cargo, int id_sucursal,
                                     String horario, String telefono, String correo, String direccion,
                                     SqlConection conexionSQL) {

        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password)) {

            // Insertar en Empleado_informacion (local o por linked server)
            String sqlInfo;
            switch (conexionSQL.index) {
                case 1: // Sede Norte - local
                    sqlInfo = "SET XACT_ABORT ON; INSERT INTO Empleado_informacion (id_empleado, horario, telefono, correo, direccion) VALUES (?, ?, ?, ?, ?)";
                    break;
                case 2: // Sede Sur - linked server hacia Norte
                    sqlInfo = "SET XACT_ABORT ON; INSERT INTO IV4SH.Quito_Norte.dbo.Empleado_informacion (id_empleado, horario, telefono, correo, direccion) VALUES (?, ?, ?, ?, ?)";
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Índice de sede inválido.");
                    return;
            }

            try (PreparedStatement psInfo = con.prepareStatement(sqlInfo)) {
                psInfo.setString(1, id_empleado);
                psInfo.setString(2, horario);
                psInfo.setString(3, telefono);
                psInfo.setString(4, correo);
                psInfo.setString(5, direccion);
                psInfo.executeUpdate();
            }

            // Insertar en Empleado_General (vista que une las dos sedes)
            String sqlGeneral = "SET XACT_ABORT ON; INSERT INTO Empleado_General (id_empleado, nombres, cargo, id_sucursal) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psGen = con.prepareStatement(sqlGeneral)) {
                psGen.setString(1, id_empleado);
                psGen.setString(2, nombres);
                psGen.setString(3, cargo);
                psGen.setInt(4, id_sucursal);
                psGen.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Empleado creado correctamente.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear empleado: " + e.getMessage());
        }
    }

    public static void buscarEmpleadoPorId(String id_empleado, SqlConection conexionSQL, javax.swing.JTable tablaEmpleado, java.awt.Component parent) throws SQLException {
        String sql = "SELECT id_empleado, nombres, cargo, horario, telefono, correo, direccion, id_sucursal " +
                     "FROM Empleados WHERE id_empleado = ?";

        Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, id_empleado); 
        try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel modelo = (DefaultTableModel) tablaEmpleado.getModel();
                modelo.setRowCount(0); // Limpiar tabla

                while (rs.next()) {
                    Object[] fila = new Object[8];
                    fila[0] = rs.getString("id_empleado");
                    fila[1] = rs.getString("nombres");
                    fila[2] = rs.getString("cargo");
                    fila[3] = rs.getString("horario");
                    fila[4] = rs.getString("telefono");
                    fila[5] = rs.getString("correo");
                    fila[6] = rs.getString("direccion");
                    fila[7] = rs.getInt("id_sucursal");
                    modelo.addRow(fila);
                }

                if (modelo.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontró producto con ese ID");
                }
            }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar producto: " + e.getMessage());
        }
    }

    public static void actualizarEmpleado(String id_empleado, String nombres, String cargo, int id_sucursal,
                                          String horario, String telefono, String correo, String direccion,
                                          SqlConection conexionSQL) {

        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password)) {

            // Actualizar Empleado_informacion (local o remoto)
            String sqlInfo;
            switch (conexionSQL.index) {
                case 1: // Norte
                    sqlInfo = "SET XACT_ABORT ON; UPDATE Empleado_informacion SET horario = ?, telefono = ?, correo = ?, direccion = ? WHERE id_empleado = ?";
                    break;
                case 2: // Sur -> linked server a Norte
                    sqlInfo = "SET XACT_ABORT ON; UPDATE IV4SH.Quito_Norte.dbo.Empleado_informacion SET horario = ?, telefono = ?, correo = ?, direccion = ? WHERE id_empleado = ?";
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Índice de sede inválido.");
                    return;
            }

            try (PreparedStatement psInfo = con.prepareStatement(sqlInfo)) {
                psInfo.setString(1, horario);
                psInfo.setString(2, telefono);
                psInfo.setString(3, correo);
                psInfo.setString(4, direccion);
                psInfo.setString(5, id_empleado);
                psInfo.executeUpdate();
            }

            // Actualizar en Empleado_General (vista)
            String sqlGeneral = "SET XACT_ABORT ON; UPDATE Empleado_General SET nombres = ?, cargo = ?, id_sucursal = ? WHERE id_empleado = ?";
            try (PreparedStatement psGen = con.prepareStatement(sqlGeneral)) {
                psGen.setString(1, nombres);
                psGen.setString(2, cargo);
                psGen.setInt(3, id_sucursal);
                psGen.setString(4, id_empleado);
                psGen.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Empleado actualizado correctamente.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar empleado: " + e.getMessage());
        }
    }

    public static void eliminarEmpleado(String id_empleado, SqlConection conexionSQL) {
        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password)) {

            // Eliminar de Empleado_General
            String sqlGeneral = "SET XACT_ABORT ON; DELETE FROM Empleado_General WHERE id_empleado = ?";
            try (PreparedStatement psGen = con.prepareStatement(sqlGeneral)) {
                psGen.setString(1, id_empleado);
                psGen.executeUpdate();
            }

            // Eliminar de Empleado_informacion
            String sqlInfo;
            switch (conexionSQL.index) {
                case 1:
                    sqlInfo = "SET XACT_ABORT ON; DELETE FROM Empleado_informacion WHERE id_empleado = ?";
                    break;
                case 2:
                    sqlInfo = "SET XACT_ABORT ON; DELETE FROM IV4SH.Quito_Norte.dbo.Empleado_informacion WHERE id_empleado = ?";
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Índice de sede inválido.");
                    return;
            }

            try (PreparedStatement psInfo = con.prepareStatement(sqlInfo)) {
                psInfo.setString(1, id_empleado);
                psInfo.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Empleado eliminado correctamente.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar empleado: " + e.getMessage());
        }
    }
    
    public static void llenarTablaDesdeResultSet(ResultSet rs, javax.swing.JTable tabla, java.awt.Component parent) {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            modelo.setRowCount(0);

            while (rs.next()) {
                Object[] fila = new Object[8];
                fila[0] = rs.getString("id_empleado");
                fila[1] = rs.getString("nombres");
                fila[2] = rs.getString("cargo");
                fila[3] = rs.getString("horario");
                fila[4] = rs.getString("telefono");
                fila[5] = rs.getString("correo");
                fila[6] = rs.getString("direccion");
                fila[7] = rs.getInt("id_sucursal");
                modelo.addRow(fila);
            }

            if (modelo.getRowCount() == 0) {
                JOptionPane.showMessageDialog(parent, "No se encontraron empleados.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Error al llenar la tabla: " + e.getMessage());
        }
    }
    
}
