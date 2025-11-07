package ms.msferreteriajuncal.application.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import ms.msferreteriajuncal.application.dto.out.InventarioValorDto;
import ms.msferreteriajuncal.application.dto.out.StockBajoDto;
import ms.msferreteriajuncal.application.dto.out.TopProductoDto;
import ms.msferreteriajuncal.application.dto.out.VentaDTO;
import ms.msferreteriajuncal.application.dto.out.VentaDiariaDto;
import ms.msferreteriajuncal.application.dto.out.VentaResumenDto;

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

            addHeaderBasic(table, "Fecha", th);
            addHeaderBasic(table, "Total", th);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            BigDecimal granTotal = BigDecimal.ZERO;

            if (data != null) {
                for (VentaDiariaDto v : data) {
                    LocalDate f = v.getFecha();
                    BigDecimal total = v.getTotal() == null ? BigDecimal.ZERO : v.getTotal();
                    addCellBasic(table, (f == null ? "" : f.format(fmt)), td);
                    addCellBasic(table, total.toPlainString(), td);
                    granTotal = granTotal.add(total);
                }
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

            addHeaderBasic(table, "ID", th);
            addHeaderBasic(table, "Producto", th);
            addHeaderBasic(table, "Cantidad", th);
            addHeaderBasic(table, "Total", th);

            BigDecimal granTotal = BigDecimal.ZERO;

            if (data != null) {
                for (TopProductoDto t : data) {
                    Long id = t.getIdProducto();
                    String nombre = t.getNombreProducto();
                    long cantidad = t.getCantidadVendida();
                    BigDecimal total = t.getTotalVendido() == null ? BigDecimal.ZERO : t.getTotalVendido();

                    addCellBasic(table, String.valueOf(id == null ? "" : id), td);
                    addCellBasic(table, nombre == null ? "" : nombre, td);
                    addCellBasic(table, String.valueOf(cantidad), td);
                    addCellBasic(table, total.toPlainString(), td);

                    granTotal = granTotal.add(total);
                }
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

            addHeaderBasic(table, "Métrica", th);
            addHeaderBasic(table, "Valor", th);

            String periodo = (resumen.getDesde() == null ? "" : resumen.getDesde())
                    + " a "
                    + (resumen.getHasta() == null ? "" : resumen.getHasta());
            addCellBasic(table, "Periodo", td);
            addCellBasic(table, periodo, td);

            addCellBasic(table, "Cantidad de ventas", td);
            addCellBasic(table, String.valueOf(resumen.getCantidadVentas()), td);

            addCellBasic(table, "Items vendidos", td);
            addCellBasic(table, String.valueOf(resumen.getItemsVendidos()), td);

            BigDecimal totalVendido = resumen.getTotalVendido() == null ? BigDecimal.ZERO : resumen.getTotalVendido();
            addCellBasic(table, "Total vendido", td);
            addCellBasic(table, totalVendido.toPlainString(), td);

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

            addHeaderBasic(table, "ID", th);
            addHeaderBasic(table, "Producto", th);
            addHeaderBasic(table, "Cantidad", th);

            if (data != null) {
                for (StockBajoDto s : data) {
                    addCellBasic(table, String.valueOf(s.getIdProducto()), td);
                    addCellBasic(table, s.getNombreProducto() == null ? "" : s.getNombreProducto(), td);
                    addCellBasic(table, String.valueOf(s.getCantidadActual()), td);
                }
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

            addHeaderBasic(table, "Concepto", th);
            addHeaderBasic(table, "Valor", th);

            long cantidadProductos = dto == null ? 0 : dto.getCantidadProductos();
            BigDecimal valorTotal = (dto == null || dto.getValorTotal() == null)
                    ? BigDecimal.ZERO
                    : dto.getValorTotal();

            addCellBasic(table, "Cantidad de productos", td);
            addCellBasic(table, String.valueOf(cantidadProductos), td);

            addCellBasic(table, "Valor total (precio entrada x cantidad)", td);
            addCellBasic(table, valorTotal.toPlainString(), td);

            doc.add(table);
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF Valor Inventario", e);
        } finally {
            doc.close();
        }
        return baos.toByteArray();
    }

    // ===================== REMISIÓN INDIVIDUAL (PDF) – CABECERA BLANCA & TABLAS AZULES =====================
    public static byte[] remisionPdf(VentaDTO venta) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(doc, baos);
            doc.open();

            // Paleta
            Color NAVY   = new Color(13, 32, 67);
            Color BLUE   = new Color(30, 64, 175);
            Color BLACK  = Color.BLACK;
            Color WHITE  = Color.WHITE;
            Color GRAY   = new Color(245, 247, 250);
            Color BORDER = new Color(216, 222, 233); // para panel redondeado externo

            // Fuentes
            Font titleBig   = new Font(Font.HELVETICA, 16, Font.BOLD, NAVY);
            Font titleSmall = new Font(Font.HELVETICA, 10, Font.NORMAL, NAVY);
            Font h2w        = new Font(Font.HELVETICA, 11, Font.BOLD, WHITE);
            Font h2         = new Font(Font.HELVETICA, 11, Font.BOLD, NAVY);
            Font td         = new Font(Font.HELVETICA, 10, Font.NORMAL, BLACK);
            Font small      = new Font(Font.HELVETICA, 9,  Font.NORMAL, BLACK);

            // HEADER: logo izq + título/fecha centrados
            PdfPTable headerBand = new PdfPTable(2);
            headerBand.setWidthPercentage(100);
            headerBand.setWidths(new float[]{60, 40});

            Image logo = null;
            try {
                URL url = PdfUtil.class.getResource("/static/logo-ag.png");
                if (url != null) {
                    logo = Image.getInstance(url);
                    logo.scaleToFit(110, 60);
                    logo.setAlignment(Image.ALIGN_LEFT);
                }
            } catch (Exception ignore) {}

            PdfPCell left = new PdfPCell();
            left.setPadding(8);
            left.setBorder(Rectangle.NO_BORDER);
            left.setBackgroundColor(WHITE);
            if (logo != null) {
                left.addElement(logo);
            } else {
                Paragraph brand = new Paragraph("FERRETERÍA LA ECONOMÍA A y G", titleBig);
                brand.setAlignment(Element.ALIGN_LEFT);
                left.addElement(brand);
            }
            headerBand.addCell(left);

            PdfPCell right = new PdfPCell();
            right.setPaddingTop(12);
            right.setPaddingBottom(12);
            right.setBorder(Rectangle.NO_BORDER);
            right.setBackgroundColor(WHITE);

            Paragraph ttl = new Paragraph("REMISIÓN / VENTA", titleBig);
            ttl.setAlignment(Element.ALIGN_CENTER);
            Paragraph fch = new Paragraph((venta.getFecha() == null ? "" : String.valueOf(venta.getFecha())), titleSmall);
            fch.setAlignment(Element.ALIGN_CENTER);

            right.addElement(ttl);
            right.addElement(fch);
            headerBand.addCell(right);

            doc.add(headerBand);

            // Separador fino azul
            LineSeparator ls = new LineSeparator();
            ls.setLineColor(BLUE);
            ls.setPercentage(100);
            ls.setOffset(-2);
            doc.add(new Paragraph(" "));
            doc.add(ls);
            doc.add(new Paragraph(" "));

            // ===== DATOS DEL CLIENTE =====
            Paragraph secCliente = new Paragraph("Datos del cliente", h2);
            secCliente.setSpacingAfter(6);
            doc.add(secCliente);

            PdfPTable cliente = new PdfPTable(2);
            cliente.setWidthPercentage(100);
            cliente.setWidths(new float[]{25, 75});
            cliente.addCell(headerCell("Nombre", h2w, NAVY, BLUE));
            cliente.addCell(valueCell(nvl(venta.getClienteNombre()), td, WHITE, BLUE));
            cliente.addCell(headerCell("Documento", h2w, NAVY, BLUE));
            cliente.addCell(valueCell(nvl(venta.getClienteDocumento()), td, WHITE, BLUE));
            cliente.addCell(headerCell("Email", h2w, NAVY, BLUE));
            cliente.addCell(valueCell(nvl(venta.getClienteEmail()), td, WHITE, BLUE));

            PdfPTable clientePanel = new PdfPTable(1);
            clientePanel.setWidthPercentage(100);
            PdfPCell clienteBox = new PdfPCell();
            clienteBox.setBorder(Rectangle.NO_BORDER);
            clienteBox.setPadding(8);
            clienteBox.setCellEvent(new RoundedCell(BORDER, null, 8f)); // solo borde, sin fondo
            clienteBox.addElement(cliente);
            clientePanel.addCell(clienteBox);
            doc.add(clientePanel);

            doc.add(new Paragraph(" "));

            // ===== DETALLE DE ÍTEMS =====
            Paragraph secDetalle = new Paragraph("Detalle de productos", h2);
            secDetalle.setSpacingAfter(6);
            doc.add(secDetalle);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{10, 44, 12, 12, 22});

            table.addCell(headerCell("ID", h2w, BLUE, BLUE));
            table.addCell(headerCell("Producto", h2w, BLUE, BLUE));
            table.addCell(headerCell("Precio", h2w, BLUE, BLUE));
            table.addCell(headerCell("Cant.", h2w, BLUE, BLUE));
            table.addCell(headerCell("Subtotal", h2w, BLUE, BLUE));

            BigDecimal total = BigDecimal.ZERO;
            if (venta.getItems() != null) {
                for (VentaDTO.Item it : venta.getItems()) {
                    String idProd = it.getIdProducto() == null ? "" : String.valueOf(it.getIdProducto());
                    String nombre = it.getNombreProducto() == null ? "" : it.getNombreProducto();
                    String precio = it.getProUnidad() == null ? "0" : it.getProUnidad().toPlainString();
                    String cant   = it.getCantidad() == null ? "0" : String.valueOf(it.getCantidad());
                    String sub    = it.getSubtotal() == null ? "0" : it.getSubtotal().toPlainString();

                    total = total.add(it.getSubtotal() == null ? BigDecimal.ZERO : it.getSubtotal());

                    table.addCell(valueCell(idProd, td, WHITE, BLUE));
                    table.addCell(valueCell(nombre, td, WHITE, BLUE));
                    table.addCell(valueCell(precio, td, WHITE, BLUE));
                    table.addCell(valueCell(cant, td, WHITE, BLUE));
                    table.addCell(valueCell(sub, td, WHITE, BLUE));
                }
            }

            PdfPTable detallePanel = new PdfPTable(1);
            detallePanel.setWidthPercentage(100);
            PdfPCell detalleBox = new PdfPCell();
            detalleBox.setBorder(Rectangle.NO_BORDER);
            detalleBox.setPadding(8);
            detalleBox.setCellEvent(new RoundedCell(BORDER, null, 8f));
            detalleBox.addElement(table);
            detallePanel.addCell(detalleBox);
            doc.add(detallePanel);

            // ===== TOTAL (solo tabla, sin recuadro exterior) =====
            doc.add(new Paragraph(" "));

            PdfPTable totals = new PdfPTable(2);
            totals.setWidthPercentage(45); // mismo ancho que las demás tablas
            totals.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totals.setWidths(new float[]{60, 40});
            totals.getDefaultCell().setBorderColor(BLUE);

// Fuentes
            Font totalLabelFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);
            Font totalValueFont = new Font(Font.HELVETICA, 13, Font.BOLD, NAVY);

// Celda "TOTAL A PAGAR"
            PdfPCell lab = new PdfPCell(new Phrase("TOTAL A PAGAR", totalLabelFont));
            lab.setBackgroundColor(NAVY);
            lab.setHorizontalAlignment(Element.ALIGN_CENTER);
            lab.setVerticalAlignment(Element.ALIGN_MIDDLE);
            lab.setPaddingTop(8f);
            lab.setPaddingBottom(8f);
            lab.setPaddingLeft(10f);
            lab.setPaddingRight(10f);
            lab.setBorderColor(BLUE);

// Celda del valor
            PdfPCell val = new PdfPCell(new Phrase(formatMoney(total), totalValueFont));
            val.setHorizontalAlignment(Element.ALIGN_RIGHT);
            val.setVerticalAlignment(Element.ALIGN_MIDDLE);
            val.setPaddingTop(8f);
            val.setPaddingBottom(8f);
            val.setPaddingLeft(10f);
            val.setPaddingRight(14f);
            val.setBackgroundColor(WHITE);
            val.setBorderColor(BLUE);

            totals.addCell(lab);
            totals.addCell(val);

// Agregar directamente al documento sin recuadro adicional
            doc.add(totals);


            // Pie
            doc.add(new Paragraph(" "));
            Paragraph obs = new Paragraph("Gracias por su compra.", small);
            obs.setSpacingBefore(6);
            doc.add(obs);

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de la remisión", e);
        } finally {
            doc.close();
        }
        return baos.toByteArray();
    }

    // ===== Helpers básicos para otros reportes =====
    private static void addHeaderBasic(PdfPTable t, String text, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setPadding(6);
        t.addCell(c);
    }

    private static void addCellBasic(PdfPTable t, String text, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setPadding(6);
        t.addCell(c);
    }

    // ===== Helpers de estilo (con borde configurable) =====
    private static PdfPCell headerCell(String text, Font font, Color bg, Color border) {
        PdfPCell c = new PdfPCell(new Phrase(text, font));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c.setBackgroundColor(bg);
        c.setPadding(6);
        c.setBorderColor(border);
        return c;
    }

    private static PdfPCell valueCell(String text, Font font, Color bg, Color border) {
        PdfPCell c = new PdfPCell(new Phrase(text == null ? "" : text, font));
        c.setBackgroundColor(bg);
        c.setPadding(6);
        c.setBorderColor(border);
        return c;
    }

    private static String nvl(String s) { return s == null ? "" : s; }

    private static String formatMoney(BigDecimal v) {
        if (v == null) v = BigDecimal.ZERO;
        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(new java.util.Locale("es","CO"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(v);
    }

    // ===== Panel con bordes redondeados (sin fondo para no tapar cabeceras) =====
    private static class RoundedCell implements PdfPCellEvent {
        private final Color borderColor;
        private final Color background; // usa null para “sin fondo”
        private final float radius;

        RoundedCell(Color borderColor, Color background, float radius) {
            this.borderColor = borderColor;
            this.background  = background;
            this.radius      = radius;
        }

        @Override
        public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvases) {
            float x = rect.getLeft();
            float y = rect.getBottom();
            float w = rect.getWidth();
            float h = rect.getHeight();

            // Fondo (opcional)
            if (background != null) {
                PdfContentByte bg = canvases[PdfPTable.BACKGROUNDCANVAS];
                bg.saveState();
                bg.setColorFill(background);
                bg.roundRectangle(x, y, w, h, radius);
                bg.fill();
                bg.restoreState();
            }

            // Borde
            PdfContentByte ln = canvases[PdfPTable.LINECANVAS];
            ln.saveState();
            ln.setLineWidth(0.8f);
            ln.setColorStroke(borderColor == null ? Color.GRAY : borderColor);
            ln.roundRectangle(x, y, w, h, radius);
            ln.stroke();
            ln.restoreState();
        }
    }
}
