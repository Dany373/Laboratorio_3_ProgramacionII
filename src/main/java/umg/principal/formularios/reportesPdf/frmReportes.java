package umg.principal.formularios.reportesPdf;

import umg.principal.DaseDatos.Service.ProductoService;
import umg.principal.DaseDatos.model.Producto;
import umg.principal.reportes.PdfReport;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class frmReportes {
    private JLabel lblTitulo;
    private JLabel lblElegirReporte;
    private JPanel ReportesPDF;
    private JComboBox comboBoxSeleccionarReporte;
    private JButton buttonGenerarReporte;
    private JButton buttonCancelar;

    private ProductoService productoService;

    public frmReportes() {
        productoService = new ProductoService(); // Inicializar el servicio de producto
        comboBoxSeleccionarReporte.addItem("Productos con existencia menor a 20 unidades");
        comboBoxSeleccionarReporte.addItem("Reporte de Productos con precio mayor a 2000");
        comboBoxSeleccionarReporte.addItem("Reporte de Productos Por Pais");
        comboBoxSeleccionarReporte.addItem("Reporte agrupado por pais y ordenado por precio de mayor a menor");


            buttonGenerarReporte.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    generarReporte();
                }
            });
            buttonCancelar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }

    public static void main(String[] args) {
        JFrame frame = new JFrame("frmReportes");
        frame.setContentPane(new frmReportes().ReportesPDF);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void generarReporte() {
        String seleccion = (String) comboBoxSeleccionarReporte.getSelectedItem();
        List<Producto> productos = null;
        String outputPath = "C:\\tmp\\reporte.pdf";  // Cambia la ruta según sea necesario

        try {
            switch (seleccion) {
                case "Productos con existencia menor a 20 unidades":
                    productos = productoService.obtenerTodosMenores20();
                    break;
                case "Reporte de Productos con precio mayor a 2000":
                    productos = productoService.obtenerProductosConPrecioMayorDe2000(2000);
                    break;
                case "Reporte de Productos Por Pais":
                    String pais = JOptionPane.showInputDialog("Ingrese el nombre del pais");
                    if (pais != null && !pais.trim().isEmpty()) {
                        productos = productoService.obrenerProductosPorPais(pais.trim());
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor ingrese un pais valido.");
                        return;
                    }
                    break;

                case "Reporte agrupado por pais y ordenado por precio de mayor a menor":
                    //
                    productos = productoService.obtenerProductosAgrupadosPorPaisOrdenadosPorPrecio();
                    break;
            }

            if (productos != null && !productos.isEmpty()) {
                PdfReport pdfReport = new PdfReport();
                pdfReport.generateProductReport(productos, outputPath);
                JOptionPane.showMessageDialog(null, "Reporte generado exitosamente en: " + outputPath);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron productos para este reporte.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al acceder a la base de datos: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage());
        }
    }
    }
