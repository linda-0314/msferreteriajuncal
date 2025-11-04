package ms.msferreteriajuncal.application.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import ms.msferreteriajuncal.application.dto.out.VentaDiariaDto;
import ms.msferreteriajuncal.application.dto.out.TopProductoDto;
import ms.msferreteriajuncal.application.dto.out.StockBajoDto;
import ms.msferreteriajuncal.application.dto.out.InventarioValorDto;
import ms.msferreteriajuncal.application.dto.out.VentaResumenDto;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfUtil {

    // ===================== VENTAS DIARIAS (PDF) =====================
    public static byte[] ventasDiariasPdf(List<VentaDiariaDto> data, String titulo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font h1 = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font th = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font td = new Font(Font.HELVETICA, 11);

            Paragraph p = new Paragraph(titulo, h1);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(12);
            doc.add(p);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{40, 60});

            addHeader(table, "Fecha", th);
            addHeader(table, "Total", th);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            BigDecimal granTotal = BigDecimal.ZERO;

            for (VentaDiariaDto v : data) {
                addCell(table, v.getFecha().format(fmt), td);
                addCell(table, v.getTotal().toPlainString(), td);
                granTotal = granTotal.add(v.getTotal());
            }

            PdfPCell totalCell = new PdfPCell(new Phrase("Gran Total: " + granTotal.toPlainString(), th));
            totalCell.setColspan(2);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalCell.setPadding(6);
            table.addCell(totalCell);

            doc.add(table);
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de ventas diarias", e);
        } finally {
            doc.close();
        }
        return baos.toByteArray();
    }

    // ===================== TOP PRODUCTOS (PDF) =====================
    public static byte[] topProductosPdf(List<TopProductoDto> data, LocalDate desde, LocalDate hasta, int limit) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font h1 = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font th = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font td = new Font(Font.HELVETICA, 11);

            String titulo = String.format("Top %d productos más vendidos (%s a %s)", limit, desde, hasta);
            Paragraph p = new Paragraph(titulo, h1);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(12);
            doc.add(p);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{15, 45, 15, 25});

            addHeader(table, "ID", th);
            addHeader(table, "Producto", th);
            addHeader(table, "Cantidad", th);
            addHeader(table, "Total", th);

            BigDecimal granTotal = BigDecimal.ZERO;
            for (TopProductoDto t : data) {
                addCell(table, String.valueOf(t.getIdProducto()), td);
                addCell(table, t.getNombreProducto(), td);
                addCell(table, String.valueOf(t.getCantidadVendida()), td);
                addCell(table, t.getTotalVendido().toPlainString(), td);
                granTotal = granTotal.add(t.getTotalVendido());
            }

            PdfPCell totalCell = new PdfPCell(new Phrase("Gran Total: " + granTotal.toPlainString(), th));
            totalCell.setColspan(4);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalCell.setPadding(6);
            table.addCell(totalCell);

            doc.add(table);
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF Top Productos", e);
        } finally {
            doc.close();
        }
        return baos.toByteArray();
    }

    // ===================== RESUMEN VENTAS (PDF) =====================
    public static byte[] resumenVentasPdf(VentaResumenDto resumen, String titulo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font h1 = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font th = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font td = new Font(Font.HELVETICA, 11);

            Paragraph p = new Paragraph(titulo, h1);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(12);
            doc.add(p);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(70);
            table.setWidths(new float[]{50, 50});
            table.setHorizontalAlignment(Element.ALIGN_CENTER);

            addHeader(table, "Métrica", th);
            addHeader(table, "Valor", th);

            addCell(table, "Periodo", td);
            addCell(table, resumen.getDesde() + " a " + resumen.getHasta(), td);

            addCell(table, "Cantidad de ventas", td);
            addCell(table, String.valueOf(resumen.getCantidadVentas()), td);

            addCell(table, "Items vendidos", td);
            addCell(table, String.valueOf(resumen.getItemsVendidos()), td);

            addCell(table, "Total vendido", td);
            addCell(table, resumen.getTotalVendido().toPlainString(), td);

            doc.add(table);
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF Resumen de Ventas", e);
        } finally {
            doc.close();
        }
        return baos.toByteArray();
    }

    // ===================== STOCK BAJO (PDF) =====================
    public static byte[] stockBajoPdf(List<StockBajoDto> data, String titulo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font h1 = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font th = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font td = new Font(Font.HELVETICA, 11);

            Paragraph p = new Paragraph(titulo, h1);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(12);
            doc.add(p);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{15, 60, 25});

            addHeader(table, "ID", th);
            addHeader(table, "Producto", th);
            addHeader(table, "Cantidad", th);

            for (StockBajoDto s : data) {
                addCell(table, String.valueOf(s.getIdProducto()), td);
                addCell(table, s.getNombreProducto(), td);
                addCell(table, String.valueOf(s.getCantidadActual()), td);
            }

            doc.add(table);
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF Stock Bajo", e);
        } finally {
            doc.close();
        }
        return baos.toByteArray();
    }

    // ===================== VALOR INVENTARIO (PDF) =====================
    public static byte[] inventarioValorPdf(InventarioValorDto dto, String titulo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font h1 = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font th = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font td = new Font(Font.HELVETICA, 11);

            Paragraph p = new Paragraph(titulo, h1);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(12);
            doc.add(p);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(60);
            table.setWidths(new float[]{50, 50});
            table.setHorizontalAlignment(Element.ALIGN_CENTER);

            addHeader(table, "Concepto", th);
            addHeader(table, "Valor", th);

            addCell(table, "Cantidad de productos", td);
            addCell(table, String.valueOf(dto.getCantidadProductos()), td);

            addCell(table, "Valor total (precio entrada x cantidad)", td);
            addCell(table, dto.getValorTotal().toPlainString(), td);

            doc.add(table);
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF Valor Inventario", e);
        } finally {
            doc.close();
        }
        return baos.toByteArray();
    }

    // ===== Helpers =====
    private static void addHeader(PdfPTable t, String text, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setPadding(6);
        t.addCell(c);
    }

    private static void addCell(PdfPTable t, String text, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setPadding(6);
        t.addCell(c);
    }
}
