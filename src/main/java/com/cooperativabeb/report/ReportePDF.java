package com.cooperativabeb.report;

import com.cooperativabeb.dao.ClienteDAO;
import com.cooperativabeb.dao.CuentaAhorroDAO;
import com.cooperativabeb.dao.PlanInversionDAO;
import com.cooperativabeb.dao.TransaccionDAO;
import com.cooperativabeb.model.Cliente;
import com.cooperativabeb.model.CuentaAhorro;
import com.cooperativabeb.model.PlanInversion;
import com.cooperativabeb.model.Transaccion;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportePDF {

    private static final Font TITULO = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(100, 149, 237));
    private static final Font SUBTITULO = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(60, 60, 80));
    private static final Font HEADER_TABLA = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
    private static final Font CELDA = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, new BaseColor(40, 40, 40));
    private static final Font NORMAL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font NEGRITA = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

    private static final BaseColor COLOR_HEADER = new BaseColor(100, 149, 237);
    private static final BaseColor COLOR_FILA_PAR = new BaseColor(240, 244, 255);
    private static final BaseColor COLOR_FILA_IMPAR = BaseColor.WHITE;

    // =====================================================
    // Reporte de clientes
    // =====================================================
    public static String generarReporteClientes(String rutaDestino) throws DocumentException, IOException {
        String archivo = rutaDestino + "/reporte_clientes_" +
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(archivo));
        doc.open();

        agregarEncabezado(doc, "Reporte de Clientes");

        ClienteDAO dao = new ClienteDAO();
        List<Cliente> lista = dao.listarTodos();

        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15f);
        tabla.setWidths(new float[]{0.8f, 1.2f, 2.5f, 1.5f, 2.5f, 1f});

        agregarHeaderTabla(tabla, new String[]{
            "ID", "DNI", "Nombre completo", "Telefono", "Email", "Estado"
        });

        int fila = 0;
        for (Cliente c : lista) {
            BaseColor bg = (fila % 2 == 0) ? COLOR_FILA_PAR : COLOR_FILA_IMPAR;
            agregarCelda(tabla, String.valueOf(c.getIdCliente()), bg);
            agregarCelda(tabla, c.getDni(), bg);
            agregarCelda(tabla, c.getNombreCompleto(), bg);
            agregarCelda(tabla, c.getTelefono() != null ? c.getTelefono() : "—", bg);
            agregarCelda(tabla, c.getEmail() != null ? c.getEmail() : "—", bg);
            agregarCelda(tabla, c.getEstado(), bg);
            fila++;
        }

        doc.add(tabla);
        agregarTotalRegistros(doc, lista.size());
        agregarPiePagina(doc);
        doc.close();
        return archivo;
    }

    // =====================================================
    // Reporte de cuentas de ahorro
    // =====================================================
    public static String generarReporteCuentas(String rutaDestino) throws DocumentException, IOException {
        String archivo = rutaDestino + "/reporte_cuentas_" +
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(archivo));
        doc.open();

        agregarEncabezado(doc, "Reporte de Cuentas de Ahorro");

        CuentaAhorroDAO dao = new CuentaAhorroDAO();
        List<CuentaAhorro> lista = dao.listarTodas();

        double totalSaldo = lista.stream().mapToDouble(CuentaAhorro::getSaldo).sum();

        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15f);
        tabla.setWidths(new float[]{0.8f, 2f, 1.2f, 1.8f, 1.5f, 1.2f});

        agregarHeaderTabla(tabla, new String[]{
            "ID", "Nro. Cuenta", "Cliente ID", "Saldo (S/.)", "Tipo", "Estado"
        });

        int fila = 0;
        for (CuentaAhorro c : lista) {
            BaseColor bg = (fila % 2 == 0) ? COLOR_FILA_PAR : COLOR_FILA_IMPAR;
            agregarCelda(tabla, String.valueOf(c.getIdCuenta()), bg);
            agregarCelda(tabla, c.getNroCuenta(), bg);
            agregarCelda(tabla, String.valueOf(c.getIdCliente()), bg);
            agregarCelda(tabla, String.format("S/. %.2f", c.getSaldo()), bg);
            agregarCelda(tabla, c.getTipoCuenta(), bg);
            agregarCelda(tabla, c.getEstado(), bg);
            fila++;
        }

        doc.add(tabla);

        // Total saldo
        Paragraph total = new Paragraph(
            String.format("Total en ahorros: S/. %.2f", totalSaldo), NEGRITA);
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(10f);
        doc.add(total);

        agregarTotalRegistros(doc, lista.size());
        agregarPiePagina(doc);
        doc.close();
        return archivo;
    }

    // =====================================================
    // Reporte de transacciones
    // =====================================================
    public static String generarReporteTransacciones(String rutaDestino) throws DocumentException, IOException {
        String archivo = rutaDestino + "/reporte_transacciones_" +
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(archivo));
        doc.open();

        agregarEncabezado(doc, "Reporte de Transacciones");

        TransaccionDAO dao = new TransaccionDAO();
        List<Transaccion> lista = dao.listarUltimas(100);

        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15f);
        tabla.setWidths(new float[]{0.8f, 1.5f, 1.8f, 1.8f, 1.8f, 1.5f});

        agregarHeaderTabla(tabla, new String[]{
            "ID", "Tipo", "Monto (S/.)", "Saldo anterior", "Saldo posterior", "Fecha"
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int fila = 0;
        for (Transaccion t : lista) {
            BaseColor bg = (fila % 2 == 0) ? COLOR_FILA_PAR : COLOR_FILA_IMPAR;
            agregarCelda(tabla, String.valueOf(t.getIdTransaccion()), bg);
            agregarCelda(tabla, t.getTipo(), bg);
            agregarCelda(tabla, String.format("S/. %.2f", t.getMonto()), bg);
            agregarCelda(tabla, String.format("S/. %.2f", t.getSaldoAnterior()), bg);
            agregarCelda(tabla, String.format("S/. %.2f", t.getSaldoPosterior()), bg);
            agregarCelda(tabla, t.getFechaTransaccion() != null ?
                sdf.format(t.getFechaTransaccion()) : "—", bg);
            fila++;
        }

        doc.add(tabla);
        agregarTotalRegistros(doc, lista.size());
        agregarPiePagina(doc);
        doc.close();
        return archivo;
    }

    // =====================================================
    // Reporte de planes de inversion
    // =====================================================
    public static String generarReportePlanes(String rutaDestino) throws DocumentException, IOException {
        String archivo = rutaDestino + "/reporte_planes_" +
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(archivo));
        doc.open();

        agregarEncabezado(doc, "Reporte de Planes de Inversion");

        PlanInversionDAO dao = new PlanInversionDAO();
        List<PlanInversion> lista = dao.listarTodos();

        double totalInvertido = lista.stream().mapToDouble(PlanInversion::getMontoInvertido).sum();
        double totalGanancia  = lista.stream().mapToDouble(PlanInversion::calcularGanancia).sum();

        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15f);
        tabla.setWidths(new float[]{0.7f, 1f, 1.8f, 1.2f, 1f, 1.8f, 1.2f});

        agregarHeaderTabla(tabla, new String[]{
            "ID", "Cliente", "Monto invertido", "Tasa %",
            "Plazo", "Vencimiento", "Estado"
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int fila = 0;
        for (PlanInversion p : lista) {
            BaseColor bg = (fila % 2 == 0) ? COLOR_FILA_PAR : COLOR_FILA_IMPAR;
            agregarCelda(tabla, String.valueOf(p.getIdPlan()), bg);
            agregarCelda(tabla, "Cliente " + p.getIdCliente(), bg);
            agregarCelda(tabla, String.format("S/. %.2f", p.getMontoInvertido()), bg);
            agregarCelda(tabla, p.getTasaPactada() + "%", bg);
            agregarCelda(tabla, p.getPlazoMeses() + " meses", bg);
            agregarCelda(tabla, p.getFechaVencimiento() != null ?
                sdf.format(p.getFechaVencimiento()) : "—", bg);
            agregarCelda(tabla, p.getEstado(), bg);
            fila++;
        }

        doc.add(tabla);

        Paragraph totales = new Paragraph(
            String.format("Total invertido: S/. %.2f  |  Ganancia estimada total: S/. %.2f",
                totalInvertido, totalGanancia), NEGRITA);
        totales.setAlignment(Element.ALIGN_RIGHT);
        totales.setSpacingBefore(10f);
        doc.add(totales);

        agregarTotalRegistros(doc, lista.size());
        agregarPiePagina(doc);
        doc.close();
        return archivo;
    }

    // =====================================================
    // Helpers
    // =====================================================
    private static void agregarEncabezado(Document doc, String titulo)
            throws DocumentException {
        Paragraph encabezado = new Paragraph("COOPERATIVA BEB", TITULO);
        encabezado.setAlignment(Element.ALIGN_CENTER);
        doc.add(encabezado);

        Paragraph subtitulo = new Paragraph(titulo, SUBTITULO);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingBefore(6f);
        doc.add(subtitulo);

        Paragraph fecha = new Paragraph(
            "Generado: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), NORMAL);
        fecha.setAlignment(Element.ALIGN_CENTER);
        fecha.setSpacingBefore(4f);
        doc.add(fecha);

        doc.add(new LineSeparator());
    }

    private static void agregarHeaderTabla(PdfPTable tabla, String[] headers) {
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, HEADER_TABLA));
            cell.setBackgroundColor(COLOR_HEADER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(7f);
            tabla.addCell(cell);
        }
    }

    private static void agregarCelda(PdfPTable tabla, String texto, BaseColor bg) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, CELDA));
        cell.setBackgroundColor(bg);
        cell.setPadding(5f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(cell);
    }

    private static void agregarTotalRegistros(Document doc, int total)
            throws DocumentException {
        Paragraph p = new Paragraph("Total de registros: " + total, NORMAL);
        p.setSpacingBefore(8f);
        doc.add(p);
    }

    private static void agregarPiePagina(Document doc) throws DocumentException {
        Paragraph pie = new Paragraph(
            "Cooperativa BEB — Sistema Financiero — SENATI 2025", NORMAL);
        pie.setAlignment(Element.ALIGN_CENTER);
        pie.setSpacingBefore(20f);
        doc.add(pie);
    }

    public static String generarReporteCuentasCliente(String rutaDestino, int idCliente)
            throws DocumentException, IOException {
        String archivo = rutaDestino + "/mis_cuentas_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(archivo));
        doc.open();
        agregarEncabezado(doc, "Mis Cuentas de Ahorro");

        CuentaAhorroDAO dao = new CuentaAhorroDAO();
        List<CuentaAhorro> lista = dao.listarPorCliente(idCliente);

        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15f);
        tabla.setWidths(new float[]{2f, 2f, 1.5f, 1.5f, 2f});

        agregarHeaderTabla(tabla, new String[]{
                "Nro. Cuenta", "Saldo (S/.)", "Tipo", "Estado", "Fecha apertura"
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int fila = 0;
        double total = 0;
        for (CuentaAhorro c : lista) {
            BaseColor bg = (fila % 2 == 0) ? COLOR_FILA_PAR : COLOR_FILA_IMPAR;
            agregarCelda(tabla, c.getNroCuenta(), bg);
            agregarCelda(tabla, String.format("S/. %.2f", c.getSaldo()), bg);
            agregarCelda(tabla, c.getTipoCuenta(), bg);
            agregarCelda(tabla, c.getEstado(), bg);
            agregarCelda(tabla, c.getFechaApertura() != null ?
                    sdf.format(c.getFechaApertura()) : "—", bg);
            total += c.getSaldo();
            fila++;
        }

        doc.add(tabla);
        Paragraph totalP = new Paragraph(
                String.format("Saldo total: S/. %.2f", total), NEGRITA);
        totalP.setAlignment(Element.ALIGN_RIGHT);
        totalP.setSpacingBefore(10f);
        doc.add(totalP);
        agregarTotalRegistros(doc, lista.size());
        agregarPiePagina(doc);
        doc.close();
        return archivo;
    }

    public static String generarReporteTransaccionesCliente(String rutaDestino, int idCliente)
            throws DocumentException, IOException {
        String archivo = rutaDestino + "/mis_transacciones_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(archivo));
        doc.open();
        agregarEncabezado(doc, "Mis Transacciones");

        CuentaAhorroDAO cuentaDAO = new CuentaAhorroDAO();
        TransaccionDAO transDAO = new TransaccionDAO();
        List<CuentaAhorro> cuentas = cuentaDAO.listarPorCliente(idCliente);

        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15f);
        tabla.setWidths(new float[]{1.5f, 1.8f, 1.8f, 1.8f, 1.8f, 1.5f});

        agregarHeaderTabla(tabla, new String[]{
                "Tipo", "Monto (S/.)", "Saldo anterior", "Saldo posterior", "Fecha", "Estado"
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int fila = 0;
        for (CuentaAhorro c : cuentas) {
            for (com.cooperativabeb.model.Transaccion t : transDAO.listarPorCuenta(c.getIdCuenta())) {
                BaseColor bg = (fila % 2 == 0) ? COLOR_FILA_PAR : COLOR_FILA_IMPAR;
                agregarCelda(tabla, t.getTipo(), bg);
                agregarCelda(tabla, String.format("S/. %.2f", t.getMonto()), bg);
                agregarCelda(tabla, String.format("S/. %.2f", t.getSaldoAnterior()), bg);
                agregarCelda(tabla, String.format("S/. %.2f", t.getSaldoPosterior()), bg);
                agregarCelda(tabla, t.getFechaTransaccion() != null ?
                        sdf.format(t.getFechaTransaccion()) : "—", bg);
                agregarCelda(tabla, t.getEstado(), bg);
                fila++;
            }
        }

        doc.add(tabla);
        agregarTotalRegistros(doc, fila);
        agregarPiePagina(doc);
        doc.close();
        return archivo;
    }

    public static String generarReportePlanesCliente(String rutaDestino, int idCliente)
            throws DocumentException, IOException {
        String archivo = rutaDestino + "/mis_planes_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(archivo));
        doc.open();
        agregarEncabezado(doc, "Mis Planes de Inversion");

        com.cooperativabeb.dao.PlanInversionDAO dao =
                new com.cooperativabeb.dao.PlanInversionDAO();
        List<com.cooperativabeb.model.PlanInversion> lista = dao.listarPorCliente(idCliente);

        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15f);
        tabla.setWidths(new float[]{1.8f, 1.2f, 1f, 1.8f, 1.8f, 1.2f});

        agregarHeaderTabla(tabla, new String[]{
                "Monto invertido", "Tasa %", "Plazo", "Vencimiento", "Ganancia est.", "Estado"
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int fila = 0;
        for (com.cooperativabeb.model.PlanInversion p : lista) {
            BaseColor bg = (fila % 2 == 0) ? COLOR_FILA_PAR : COLOR_FILA_IMPAR;
            agregarCelda(tabla, String.format("S/. %.2f", p.getMontoInvertido()), bg);
            agregarCelda(tabla, p.getTasaPactada() + "%", bg);
            agregarCelda(tabla, p.getPlazoMeses() + " meses", bg);
            agregarCelda(tabla, p.getFechaVencimiento() != null ?
                    sdf.format(p.getFechaVencimiento()) : "—", bg);
            agregarCelda(tabla, String.format("S/. %.2f", p.calcularGanancia()), bg);
            agregarCelda(tabla, p.getEstado(), bg);
            fila++;
        }

        doc.add(tabla);
        agregarTotalRegistros(doc, lista.size());
        agregarPiePagina(doc);
        doc.close();
        return archivo;
    }
}
