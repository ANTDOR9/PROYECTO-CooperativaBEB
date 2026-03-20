package com.cooperativabeb.report;

import com.cooperativabeb.dao.ClienteDAO;
import com.cooperativabeb.dao.CuentaAhorroDAO;
import com.cooperativabeb.dao.PlanInversionDAO;
import com.cooperativabeb.dao.TransaccionDAO;
import com.cooperativabeb.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReporteExcel {

    private static XSSFCellStyle crearEstiloHeader(XSSFWorkbook wb) {
        XSSFCellStyle estilo = wb.createCellStyle();
        estilo.setFillForegroundColor(new XSSFColor(new byte[]{(byte)100, (byte)149, (byte)237}, null));
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);
        estilo.setBorderBottom(BorderStyle.THIN);
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setColor(new XSSFColor(new byte[]{(byte)255, (byte)255, (byte)255}, null));
        font.setFontHeightInPoints((short) 11);
        estilo.setFont(font);
        return estilo;
    }

    private static XSSFCellStyle crearEstiloTitulo(XSSFWorkbook wb) {
        XSSFCellStyle estilo = wb.createCellStyle();
        estilo.setFillForegroundColor(new XSSFColor(new byte[]{(byte)30, (byte)30, (byte)46}, null));
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estilo.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(new XSSFColor(new byte[]{(byte)100, (byte)149, (byte)237}, null));
        estilo.setFont(font);
        return estilo;
    }

    private static XSSFCellStyle crearEstiloFilaPar(XSSFWorkbook wb) {
        XSSFCellStyle estilo = wb.createCellStyle();
        estilo.setFillForegroundColor(new XSSFColor(new byte[]{(byte)240, (byte)244, (byte)255}, null));
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setBorderBottom(BorderStyle.THIN);
        estilo.setBorderLeft(BorderStyle.THIN);
        estilo.setBorderRight(BorderStyle.THIN);
        return estilo;
    }

    private static XSSFCellStyle crearEstiloFilaImpar(XSSFWorkbook wb) {
        XSSFCellStyle estilo = wb.createCellStyle();
        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setBorderBottom(BorderStyle.THIN);
        estilo.setBorderLeft(BorderStyle.THIN);
        estilo.setBorderRight(BorderStyle.THIN);
        return estilo;
    }

    private static void agregarEncabezado(XSSFSheet sheet, XSSFWorkbook wb,
                                           String titulo, int numColumnas) {
        Row rowTitulo = sheet.createRow(0);
        rowTitulo.setHeight((short) 700);
        Cell cell = rowTitulo.createCell(0);
        cell.setCellValue("COOPERATIVA BEB — " + titulo);
        cell.setCellStyle(crearEstiloTitulo(wb));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, numColumnas - 1));

        Row rowFecha = sheet.createRow(1);
        Cell cellFecha = rowFecha.createCell(0);
        cellFecha.setCellValue("Generado: " +
            new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, numColumnas - 1));
    }

    // =====================================================
    // Reporte Clientes
    // =====================================================
    public static String generarReporteClientes(String rutaDestino) throws IOException {
        String archivo = rutaDestino + "/reporte_clientes_" +
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Clientes");

        String[] headers = {"ID", "DNI", "Nombres", "Apellidos", "Telefono", "Email", "Estado"};
        agregarEncabezado(sheet, wb, "Reporte de Clientes", headers.length);

        Row rowHeader = sheet.createRow(2);
        rowHeader.setHeight((short) 500);
        XSSFCellStyle estiloH = crearEstiloHeader(wb);
        for (int i = 0; i < headers.length; i++) {
            Cell c = rowHeader.createCell(i);
            c.setCellValue(headers[i]);
            c.setCellStyle(estiloH);
        }

        List<Cliente> lista = new ClienteDAO().listarTodos();
        XSSFCellStyle par = crearEstiloFilaPar(wb);
        XSSFCellStyle impar = crearEstiloFilaImpar(wb);

        for (int i = 0; i < lista.size(); i++) {
            Cliente c = lista.get(i);
            Row row = sheet.createRow(i + 3);
            XSSFCellStyle estilo = (i % 2 == 0) ? par : impar;
            crearCelda(row, 0, String.valueOf(c.getIdCliente()), estilo);
            crearCelda(row, 1, c.getDni(), estilo);
            crearCelda(row, 2, c.getNombres(), estilo);
            crearCelda(row, 3, c.getApellidos(), estilo);
            crearCelda(row, 4, c.getTelefono() != null ? c.getTelefono() : "—", estilo);
            crearCelda(row, 5, c.getEmail() != null ? c.getEmail() : "—", estilo);
            crearCelda(row, 6, c.getEstado(), estilo);
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

        try (FileOutputStream fos = new FileOutputStream(archivo)) { wb.write(fos); }
        wb.close();
        return archivo;
    }

    // =====================================================
    // Reporte Cuentas
    // =====================================================
    public static String generarReporteCuentas(String rutaDestino) throws IOException {
        String archivo = rutaDestino + "/reporte_cuentas_" +
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Cuentas de Ahorro");

        String[] headers = {"ID", "Nro. Cuenta", "Cliente ID", "Saldo (S/.)", "Tipo", "Estado", "Apertura"};
        agregarEncabezado(sheet, wb, "Reporte de Cuentas", headers.length);

        Row rowHeader = sheet.createRow(2);
        rowHeader.setHeight((short) 500);
        XSSFCellStyle estiloH = crearEstiloHeader(wb);
        for (int i = 0; i < headers.length; i++) {
            Cell c = rowHeader.createCell(i);
            c.setCellValue(headers[i]);
            c.setCellStyle(estiloH);
        }

        List<CuentaAhorro> lista = new CuentaAhorroDAO().listarTodas();
        XSSFCellStyle par = crearEstiloFilaPar(wb);
        XSSFCellStyle impar = crearEstiloFilaImpar(wb);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < lista.size(); i++) {
            CuentaAhorro c = lista.get(i);
            Row row = sheet.createRow(i + 3);
            XSSFCellStyle estilo = (i % 2 == 0) ? par : impar;
            crearCelda(row, 0, String.valueOf(c.getIdCuenta()), estilo);
            crearCelda(row, 1, c.getNroCuenta(), estilo);
            crearCelda(row, 2, String.valueOf(c.getIdCliente()), estilo);
            crearCelda(row, 3, String.format("S/. %.2f", c.getSaldo()), estilo);
            crearCelda(row, 4, c.getTipoCuenta(), estilo);
            crearCelda(row, 5, c.getEstado(), estilo);
            crearCelda(row, 6, c.getFechaApertura() != null ?
                sdf.format(c.getFechaApertura()) : "—", estilo);
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
        try (FileOutputStream fos = new FileOutputStream(archivo)) { wb.write(fos); }
        wb.close();
        return archivo;
    }

    // =====================================================
    // Reporte Transacciones
    // =====================================================
    public static String generarReporteTransacciones(String rutaDestino) throws IOException {
        String archivo = rutaDestino + "/reporte_transacciones_" +
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Transacciones");

        String[] headers = {"ID", "Tipo", "Monto (S/.)", "Saldo anterior", "Saldo posterior", "Fecha", "Estado"};
        agregarEncabezado(sheet, wb, "Reporte de Transacciones", headers.length);

        Row rowHeader = sheet.createRow(2);
        rowHeader.setHeight((short) 500);
        XSSFCellStyle estiloH = crearEstiloHeader(wb);
        for (int i = 0; i < headers.length; i++) {
            Cell c = rowHeader.createCell(i);
            c.setCellValue(headers[i]);
            c.setCellStyle(estiloH);
        }

        List<Transaccion> lista = new TransaccionDAO().listarUltimas(100);
        XSSFCellStyle par = crearEstiloFilaPar(wb);
        XSSFCellStyle impar = crearEstiloFilaImpar(wb);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < lista.size(); i++) {
            Transaccion t = lista.get(i);
            Row row = sheet.createRow(i + 3);
            XSSFCellStyle estilo = (i % 2 == 0) ? par : impar;
            crearCelda(row, 0, String.valueOf(t.getIdTransaccion()), estilo);
            crearCelda(row, 1, t.getTipo(), estilo);
            crearCelda(row, 2, String.format("S/. %.2f", t.getMonto()), estilo);
            crearCelda(row, 3, String.format("S/. %.2f", t.getSaldoAnterior()), estilo);
            crearCelda(row, 4, String.format("S/. %.2f", t.getSaldoPosterior()), estilo);
            crearCelda(row, 5, t.getFechaTransaccion() != null ?
                sdf.format(t.getFechaTransaccion()) : "—", estilo);
            crearCelda(row, 6, t.getEstado(), estilo);
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
        try (FileOutputStream fos = new FileOutputStream(archivo)) { wb.write(fos); }
        wb.close();
        return archivo;
    }

    // =====================================================
    // Reporte Planes
    // =====================================================
    public static String generarReportePlanes(String rutaDestino) throws IOException {
        String archivo = rutaDestino + "/reporte_planes_" +
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Planes de Inversion");

        String[] headers = {"ID", "Cliente ID", "Monto invertido", "Tasa %", "Plazo", "Vencimiento", "Ganancia est.", "Estado"};
        agregarEncabezado(sheet, wb, "Reporte de Planes de Inversion", headers.length);

        Row rowHeader = sheet.createRow(2);
        rowHeader.setHeight((short) 500);
        XSSFCellStyle estiloH = crearEstiloHeader(wb);
        for (int i = 0; i < headers.length; i++) {
            Cell c = rowHeader.createCell(i);
            c.setCellValue(headers[i]);
            c.setCellStyle(estiloH);
        }

        List<PlanInversion> lista = new PlanInversionDAO().listarTodos();
        XSSFCellStyle par = crearEstiloFilaPar(wb);
        XSSFCellStyle impar = crearEstiloFilaImpar(wb);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < lista.size(); i++) {
            PlanInversion p = lista.get(i);
            Row row = sheet.createRow(i + 3);
            XSSFCellStyle estilo = (i % 2 == 0) ? par : impar;
            crearCelda(row, 0, String.valueOf(p.getIdPlan()), estilo);
            crearCelda(row, 1, String.valueOf(p.getIdCliente()), estilo);
            crearCelda(row, 2, String.format("S/. %.2f", p.getMontoInvertido()), estilo);
            crearCelda(row, 3, p.getTasaPactada() + "%", estilo);
            crearCelda(row, 4, p.getPlazoMeses() + " meses", estilo);
            crearCelda(row, 5, p.getFechaVencimiento() != null ?
                sdf.format(p.getFechaVencimiento()) : "—", estilo);
            crearCelda(row, 6, String.format("S/. %.2f", p.calcularGanancia()), estilo);
            crearCelda(row, 7, p.getEstado(), estilo);
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
        try (FileOutputStream fos = new FileOutputStream(archivo)) { wb.write(fos); }
        wb.close();
        return archivo;
    }

    private static void crearCelda(Row row, int col, String valor, CellStyle estilo) {
        Cell cell = row.createCell(col);
        cell.setCellValue(valor);
        cell.setCellStyle(estilo);
    }
}
