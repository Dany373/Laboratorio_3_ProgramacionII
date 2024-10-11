package umg.principal.formularios.productos;

import umg.principal.DaseDatos.Service.ProductoService;
import umg.principal.DaseDatos.model.Producto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmProductos {
    private JLabel lblTitulo;
    private JTextField textFieldProducto;
    private JLabel lblCodigo;
    private JTextField textFieldDescripcion;
    private JPanel lblDescripcion;
    private JLabel lblOrigen;
    private JButton buttonLimpiar;
    private JButton buttonGrabar;
    private JButton buttonBuscar;
    private JComboBox comboBoxOrigen;
    private JButton buttonSalir;
    private JLabel lblExistencia;
    private JLabel lblPrecio;
    private JTextField textFieldExistencia;
    private JTextField textFieldPrecio;
    private JButton buttonEliminar;
    private JButton buttonActualizar;

    public frmProductos() {

        //cargar valores del combobox origen con clave y valor de origen
        //ejemplo 1 china, 2 japon, 3 corea
        comboBoxOrigen.addItem("China");
        comboBoxOrigen.addItem("Japón");
        comboBoxOrigen.addItem("Corea");
        comboBoxOrigen.addItem("Estados Unidos");
        comboBoxOrigen.addItem("Alemania");
        comboBoxOrigen.addItem("Francia");
        comboBoxOrigen.addItem("Italia");
        comboBoxOrigen.addItem("España");
        comboBoxOrigen.addItem("Reino Unido");
        comboBoxOrigen.addItem("Brasil");
        comboBoxOrigen.addItem("México");
        comboBoxOrigen.addItem("Argentina");


        buttonLimpiar.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldProducto.setText("");
                textFieldDescripcion.setText("");
                comboBoxOrigen.setSelectedIndex(0);
                textFieldExistencia.setText("");
                textFieldPrecio.setText("");
            }
        });
        buttonGrabar.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Producto: " + textFieldProducto.getText() + "\n" +
                        "Descripcion: " + textFieldDescripcion.getText() + "\n" +
                        "Origen: " + comboBoxOrigen.getSelectedItem().toString() + "\n" +
                        "Existencia: " + textFieldExistencia.getText() + "\n" +
                        "Precio: " + textFieldPrecio.getText());




                Producto producto = new Producto();
                producto.setDescripcion(textFieldDescripcion.getText());
                producto.setOrigen(comboBoxOrigen.getSelectedItem().toString());
                producto.setExistencia(Integer.parseInt(textFieldExistencia.getText()));
                producto.setExistencia(Integer.parseInt(textFieldPrecio.getText()));
                //producto.setIdProducto(Integer.parseInt(textFieldProducto.getText()));
                try{
                    new ProductoService().crearProducto(producto);
                    JOptionPane.showMessageDialog(null, "Producto guardado correctamente");
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Error al guardar el producto");
                }



            }
        });
        buttonBuscar.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {


                int idProducto = textFieldProducto.getText().isEmpty() ? 0 : Integer.parseInt(textFieldProducto.getText());
                try{
                    Producto productoEncontrado = new ProductoService().obtenerProducto(idProducto);
                    if(productoEncontrado != null){
                        JOptionPane.showMessageDialog(null, "Producto encontrado: " + productoEncontrado.getDescripcion());
                        //llenar los campos con los valores encontrados
                        textFieldDescripcion.setText(productoEncontrado.getDescripcion());
                        comboBoxOrigen.setSelectedItem(productoEncontrado.getOrigen());
                        textFieldExistencia.setText(String.valueOf(productoEncontrado.getExistencia()));
                        textFieldPrecio.setText(String.valueOf(productoEncontrado.getPrecio()));

                        //convertir idProducto a string


                    } else {
                        JOptionPane.showMessageDialog(null, "Producto no encontrado");
                    }
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Error al buscar el producto");
                }
            }
        });
        buttonSalir.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar que el campo de producto no esté vacío
                int idProducto = textFieldProducto.getText().isEmpty() ? 0 : Integer.parseInt(textFieldProducto.getText());
                if (idProducto == 0) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa un ID de producto válido.");
                    return;
                }
                int confirmation = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar el producto con ID: " + idProducto + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        boolean eliminado = new ProductoService().eliminarProducto(idProducto);
                        if (eliminado) {
                            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente");
                            // Limpiar los campos después de eliminar
                            textFieldProducto.setText("");
                            textFieldDescripcion.setText("");
                            comboBoxOrigen.setSelectedIndex(0);
                            textFieldExistencia.setText("");
                            textFieldPrecio.setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontró el producto para eliminar.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + ex.getMessage());
                    }
                }
            }
        });


        buttonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idProducto = textFieldProducto.getText().isEmpty() ? 0 : Integer.parseInt(textFieldProducto.getText());

                if (idProducto == 0) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa un ID de producto válido.");
                    return;
                }

                Producto producto = new Producto();
                producto.setIdProducto(idProducto); // Establecer el ID del producto
                producto.setDescripcion(textFieldDescripcion.getText());
                producto.setOrigen(comboBoxOrigen.getSelectedItem().toString());
                producto.setExistencia(Integer.parseInt(textFieldExistencia.getText()));
                producto.setPrecio(Integer.parseInt(textFieldPrecio.getText()));

                try {
                    boolean actualizado = new ProductoService().actualizarProducto(producto);
                    if (actualizado) {
                        JOptionPane.showMessageDialog(null, "Producto actualizado correctamente");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró el producto para actualizar.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("frmProductos");
        frame.setContentPane(new frmProductos().lblDescripcion);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //centrar el formulario
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
