package umg.principal.DaseDatos.Dao;

import umg.principal.DaseDatos.conexion.DatabaseConnection;
import umg.principal.DaseDatos.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
        public void insertar(Producto producto) throws SQLException {
            String sql = "INSERT INTO tb_producto (descripcion, origen, existencia, precio) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, producto.getDescripcion());
                pstmt.setString(2, producto.getOrigen());
                pstmt.setInt(3, producto.getExistencia());
                pstmt.setDouble(4, producto.getPrecio());
                pstmt.executeUpdate();
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        producto.setIdProducto(generatedKeys.getInt(1));
                    }
                }
            }
        }

    public Producto obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_producto WHERE id_producto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(rs.getInt("id_producto"), rs.getString("descripcion"), rs.getString("origen"), rs.getInt("precio"), rs.getInt("existencia"));
                }
            }
        }
        return null;
    }

    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM tb_producto order by origen";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(new Producto(rs.getInt("id_producto"), rs.getString("descripcion"), rs.getString("origen"), rs.getInt("precio"), rs.getInt("existencia")));
            }
        }
        return productos;
    }


    public List<Producto> obtenerTodosMenores20() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM tb_producto where  existencia < 20";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(new Producto(rs.getInt("id_producto"), rs.getString("descripcion"), rs.getString("origen"), rs.getInt("precio"), rs.getInt("existencia")));
            }
        }
        return productos;
    }

    // Obtener productos por rango de precio
    public List<Producto> obtenerProductosConPrecioMayorDe2000(int minPrecio) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM tb_producto WHERE precio > ? ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, minPrecio);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("descripcion"),
                            rs.getString("origen"),
                            rs.getInt("existencia"),
                            rs.getInt("precio")));
                }
            }
        }
        return productos;
    }

    public List<Producto>obtenerProductoPorPais(String pais) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM tb_producto WHERE origen = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setString(1, pais);
                 try (ResultSet rs = pstmt.executeQuery()) {
                     while (rs.next()) {
                         productos.add(new Producto(
                                 rs.getInt("id_producto"),
                                 rs.getString("descripcion"),
                                 rs.getString("origen"),
                                 rs.getInt("precio"),
                                 rs.getInt("existencia")));
                     }
        }

    }
        return productos;
    }

    public List<Producto> obtenerProductosAgrupadosPorPaisOrdenadosPorPrecio() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, descripcion,origen,precio,existencia FROM tb_producto GROUP BY origen ORDER BY precio DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("descripcion"),
                        rs.getString("origen"),
                        rs.getInt("precio"),
                        rs.getInt("existencia")));
            }
        }
        return productos;
    }


    // Método para actualizar un producto
    public boolean actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE tb_producto SET descripcion = ?, origen = ?, existencia = ?, precio = ? WHERE id_producto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getDescripcion());
            pstmt.setString(2, producto.getOrigen());
            pstmt.setInt(3, producto.getExistencia());
            pstmt.setInt(4, producto.getPrecio());
            pstmt.setInt(5, producto.getIdProducto()); // Asumiendo que tienes el idProducto en la clase Producto
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0; // Devuelve true si al menos una fila fue afectada
        }
    }


    public boolean eliminarProducto(int idProducto) throws SQLException {
        String sql = "DELETE FROM tb_producto WHERE id_producto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;  // Devuelve true si al menos una fila fue afectada
        }
    }
}



