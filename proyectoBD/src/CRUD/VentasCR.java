package CRUD;

import ConexionSQL.SqlConection;
import java.sql.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentasCR {
    public static int crearFactura(Date fecha, double total, String id_cliente, int id_sucursal, SqlConection conexionSQL) throws SQLException {
        String tablaFactura;
        switch (conexionSQL.index) {
            case 1: // Norte
                tablaFactura = "Factura_Norte";
                break;
            case 2: // Sur
                tablaFactura = "Factura_Sur";
                break;
            default:
                throw new SQLException("Índice de sede inválido.");
        }

        String sqlInsertFactura = "INSERT INTO " + tablaFactura + " (fecha, total, id_cliente, id_sucursal) VALUES (?, ?, ?, ?)";

        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement psFactura = con.prepareStatement(sqlInsertFactura, Statement.RETURN_GENERATED_KEYS)) {

            psFactura.setDate(1, new java.sql.Date(fecha.getTime()));
            psFactura.setDouble(2, total);
            psFactura.setString(3, id_cliente);
            psFactura.setInt(4, id_sucursal);
            psFactura.executeUpdate();

            try (ResultSet rs = psFactura.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // ID generado
                } else {
                    throw new SQLException("No se pudo obtener el ID de la factura generada.");
                }
            }
        }
    }

    public static void insertarDetalleFactura(int idFactura, DetalleFactura detalle, SqlConection conexionSQL) throws SQLException {
        String tablaDetalle;
        switch (conexionSQL.index) {
            case 1: // Norte
                tablaDetalle = "Detalle_Factura_Norte";
                break;
            case 2: // Sur
                tablaDetalle = "Detalle_Factura_Sur";
                break;
            default:
                throw new SQLException("Índice de sede inválido.");
        }

        String sqlInsertDetalle = "INSERT INTO " + tablaDetalle +
                " (id_factura, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexionSQL.getConexion(conexionSQL.index, conexionSQL.password);
             PreparedStatement psDetalle = con.prepareStatement(sqlInsertDetalle)) {

            psDetalle.setInt(1, idFactura);
            psDetalle.setInt(2, detalle.getIdProducto());
            psDetalle.setInt(3, detalle.getCantidad());
            psDetalle.setDouble(4, detalle.getPrecioUnitario());
            psDetalle.setDouble(5, detalle.getSubtotal());

            psDetalle.executeUpdate();
        }
    }
    
}
