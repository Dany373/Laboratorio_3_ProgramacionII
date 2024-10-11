package umg.principal.DaseDatos.Service;

import umg.principal.DaseDatos.Dao.ProductoDAO;
import umg.principal.DaseDatos.model.Producto;

import java.sql.SQLException;
import java.util.List;

public class ProductoService {
    private ProductoDAO productoDAO;

    public ProductoService() {
        this.productoDAO = new ProductoDAO();
    }

    public void crearProducto(Producto producto) throws SQLException {
        productoDAO.insertar(producto);
    }

    public Producto obtenerProducto(int id) throws SQLException {
        return productoDAO.obtenerPorId(id);
    }

    public List<Producto> obtenerTodosLosProductos() throws SQLException {
        return productoDAO.obtenerTodos();
    }

    public List<Producto> obtenerTodosMenores20() throws SQLException {
        return productoDAO.obtenerTodosMenores20();
    }

    public boolean actualizarProducto(Producto producto) throws SQLException {
        return productoDAO.actualizarProducto(producto);
    }


//    public void actualizarProducto(Producto producto) throws SQLException {
//        productoDAO.actualizar(producto);
//    }

 public boolean eliminarProducto(int idProducto) throws SQLException {
        return productoDAO.eliminarProducto(idProducto);
 }

    // Método para obtener productos en un rango de precios
    public List<Producto>obtenerProductosConPrecioMayorDe2000(int minPrecio) throws SQLException{
        return productoDAO.obtenerProductosConPrecioMayorDe2000(minPrecio);
    }

    public List<Producto> obrenerProductosPorPais(String pais) throws SQLException{
        return productoDAO.obtenerProductoPorPais(pais);
    }

    public List<Producto> obtenerProductosAgrupadosPorPaisOrdenadosPorPrecio() throws SQLException{
        return productoDAO.obtenerProductosAgrupadosPorPaisOrdenadosPorPrecio();
    }

}


