package umg.principal.reportes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import umg.principal.DaseDatos.model.Producto;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PdfReport {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.COURIER, 18, Font.BOLDITALIC);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    public void generateProductReport(List<Producto> productos, String outputPath) throws DocumentException, IOException {
        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        try{
            addTitle(document);
            addProductTable(document, productos);
            document.close();
        } catch (Exception e) {
            System.out.println("Error al generar el PDF: " + e.getMessage());
        }

    }


    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("Dany Miguel Mateo Hernández 0905-23-19399", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);
    }

    private void addProductTable(Document document, List<Producto> productos) throws DocumentException {
        PdfPTable table = new PdfPTable(6); // 4 columnas para id, descripción, origen y precio
        table.setWidthPercentage(100);
        addTableHeader(table);
        //ORDENAR POR ORIGEN
        List<Producto> productosOrdenados = productos.stream().sorted(Comparator.comparing(Producto::getOrigen)).collect(Collectors.toList());
        addRowsGroup(table, productosOrdenados);
        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
        // Este código utiliza la clase Stream de Java para crear un flujo de datos con los títulos de las columnas de una tabla PDF. Luego,
        // para cada título de columna, se crea una celda de encabezado en la tabla con ciertas propiedades (color de fondo, ancho del borde, y texto).



        Stream.of("ID", "Descripción", "Origen", "Precio", "Existencia","Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
                    table.addCell(header);
                });

        //Stream.of("ID", "Descripción", "Origen", "Precio"):
        //Crea un flujo (Stream) de datos con los elementos "ID", "Descripción", "Origen", y "Precio".


//        Este código utiliza la clase Stream de Java para crear un flujo de datos con los títulos de las columnas de una tabla PDF. Luego, para cada título de columna, se crea una celda de encabezado en la tabla con ciertas propiedades (color de fondo, ancho del borde, y texto).
//                Explicación del Código
//        Stream.of("ID", "Descripción", "Origen", "Precio"):
//        Crea un flujo (Stream) de datos con los elementos "ID", "Descripción", "Origen", y "Precio".
//.forEach(columnTitle -> { ... }):
//        Para cada elemento en el flujo (en este caso, cada título de columna), ejecuta el bloque de código dentro de las llaves { ... }.
//        Dentro del bloque forEach:
//        PdfPCell header = new PdfPCell();: Crea una nueva celda para la tabla PDF.
//        header.setBackgroundColor(BaseColor.LIGHT_GRAY);: Establece el color de fondo de la celda a gris claro.
//                header.setBorderWidth(2);: Establece el ancho del borde de la celda a 2 puntos.
//                header.setPhrase(new Phrase(columnTitle, HEADER_FONT));: Establece el texto de la celda con el título de la columna y la fuente de encabezado.
//                table.addCell(header);: Añade la celda a la tabla.
//¿Qué es Stream.of?
//                Stream.of es un método estático de la clase Stream en Java que se utiliza para crear un flujo (Stream) a partir de una secuencia de elementos. En este caso, se está utilizando para crear un flujo de cadenas de texto ("ID", "Descripción", "Origen", "Precio").
//        Código

    } //Fin de addTableHeader

    private void addRows(PdfPTable table, List<Producto> productos) {
        for (Producto producto : productos) {
            table.addCell(new Phrase(String.valueOf(producto.getIdProducto()), NORMAL_FONT));
            table.addCell(new Phrase(producto.getDescripcion(), NORMAL_FONT));

            if (producto.getOrigen().equals("")) {
                PdfPCell cell = new PdfPCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
                cell.setBackgroundColor(BaseColor.GREEN);
                table.addCell(cell);
            } else {
                table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
            }
            table.addCell(new Phrase(String.valueOf(producto.getPrecio()), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));

            //table.addCell(new Phrase(String.format("Q%.2f", producto.getPrecio()), NORMAL_FONT));
        }
    }


    private void addRowsGroupOld(PdfPTable table, List<Producto> productos) {
        String agrupado ="";

        for (Producto producto : productos) {

            if (agrupado.equals(producto.getOrigen())) { //Si el origen es igual al anterior
                table.addCell(new Phrase(String.valueOf(producto.getIdProducto()), NORMAL_FONT));
                table.addCell(new Phrase(producto.getDescripcion(), NORMAL_FONT));

                if (producto.getOrigen().equals("China")) {
                    PdfPCell cell = new PdfPCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
                    cell.setBackgroundColor(BaseColor.BLUE);
                    table.addCell(cell);
                } else {
                    table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
                }
                table.addCell(new Phrase(String.valueOf(producto.getPrecio()), NORMAL_FONT));
                table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));

            } else {
                agrupado = producto.getOrigen();
                try{
                    //add blank line into the table
                    table.addCell(new Phrase("Grupo:"+producto.getOrigen(), NORMAL_FONT));
                    table.addCell(new Phrase());
                    table.addCell(new Phrase());
                    table.addCell(new Phrase());
                    table.addCell(new Phrase());
                }   catch (Exception e) {
                    System.out.println("Error al agrupar el PDF: " + e.getMessage());
                }



            }
            //table.addCell(new Phrase(String.format("Q%.2f", producto.getPrecio()), NORMAL_FONT));
        }
    }



    private void addRowsGroup(PdfPTable table, List<Producto> productos) {
        String currentOrigen = null;
        double groupTotalPrecio = 0.0;
        int groupTotalExistencia = 0;
        double groupTotalGeneral = 0.0; // Total general del grupo

        double grandTotalPrecio = 0.0;
        int grandTotalExistencia = 0;
        double grandTotalGeneral = 0.0; // Gran total final

        for (Producto producto : productos) {
            if (currentOrigen == null || !producto.getOrigen().equals(currentOrigen)) {

                // Imprimir totales del grupo anterior (si no es el primer grupo)
                if (currentOrigen != null) {
                    // Esto es lo que funciona bien para Vietnam
                    PdfPCell totalCellLabel = new PdfPCell(new Phrase("Total Grupo " + currentOrigen, NORMAL_FONT));
                    totalCellLabel.setColspan(3); // Para ocupar las primeras 3 columnas
                    totalCellLabel.setBackgroundColor(BaseColor.GREEN);
                    table.addCell(totalCellLabel);

                    // Añadir las sumas del grupo
                    table.addCell(new Phrase(String.valueOf(groupTotalPrecio), NORMAL_FONT));
                    table.addCell(new Phrase(String.valueOf(groupTotalExistencia), NORMAL_FONT));
                    table.addCell(new Phrase(String.valueOf(groupTotalGeneral), NORMAL_FONT)); // Total del grupo
                }

                // Reiniciar totales del grupo
                groupTotalPrecio = 0.0;
                groupTotalExistencia = 0;
                groupTotalGeneral = 0.0;

                // Cambiar a nuevo grupo
                currentOrigen = producto.getOrigen();
                PdfPCell groupCell = new PdfPCell(new Phrase("Grupo: " + currentOrigen, NORMAL_FONT));
                groupCell.setColspan(6); // Colspan para abarcar todas las columnas
                groupCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(groupCell);
            }

            // Agregar fila del producto
            table.addCell(new Phrase(String.valueOf(producto.getIdProducto()), NORMAL_FONT));
            table.addCell(new Phrase(producto.getDescripcion(), NORMAL_FONT));
            table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getPrecio()), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));

            // Calcular el total del producto (precio * existencia)
            double totalProducto = producto.getPrecio() * producto.getExistencia();
            table.addCell(new Phrase(String.valueOf(totalProducto), NORMAL_FONT));

            // Acumular totales por grupo
            groupTotalPrecio += producto.getPrecio();
            groupTotalExistencia += producto.getExistencia();
            groupTotalGeneral += totalProducto;

            // Acumular gran total
            grandTotalPrecio += producto.getPrecio();
            grandTotalExistencia += producto.getExistencia();
            grandTotalGeneral += totalProducto;
        }

        // Imprimir totales para el último grupo, siguiendo el mismo patrón de Vietnam
        if (currentOrigen != null) {
            PdfPCell totalCellLabel = new PdfPCell(new Phrase("Total Grupo " + currentOrigen, NORMAL_FONT));
            totalCellLabel.setColspan(3); // Para ocupar las primeras 3 columnas
            table.addCell(totalCellLabel);



            // Añadir las sumas del grupo
            table.addCell(new Phrase(String.valueOf(groupTotalPrecio), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(groupTotalExistencia), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(groupTotalGeneral), NORMAL_FONT)); // Total del grupo
        }

        // Imprimir gran total final
        PdfPCell grandTotalCellLabel = new PdfPCell(new Phrase("Gran Total", NORMAL_FONT));
        grandTotalCellLabel.setColspan(3); // Colspan para abarcar las primeras 3 columnas
        grandTotalCellLabel.setBackgroundColor(BaseColor.YELLOW);
        table.addCell(grandTotalCellLabel);

        // Añadir los grandes totales
        table.addCell(new Phrase(String.valueOf(grandTotalPrecio), NORMAL_FONT));
        table.addCell(new Phrase(String.valueOf(grandTotalExistencia), NORMAL_FONT));
        table.addCell(new Phrase(String.valueOf(grandTotalGeneral), NORMAL_FONT));
    }
}