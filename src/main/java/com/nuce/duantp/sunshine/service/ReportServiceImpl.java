//package com.nuce.duantp.sunshine.service;
//
//
//import com.nuce.duantp.sunshine.JasperReports.ReportService;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.HorizontalAlignment;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.usermodel.XSSFFont;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.time.LocalDateTime;
//import java.time.ZonedDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Service
//public class ReportServiceImpl implements ReportService {
//
//    private XSSFWorkbook workbook;
//
//    private XSSFSheet sheet;
//
//    public void createCell(Row row, int columnCount, Object value, CellStyle style) {
//        sheet.autoSizeColumn(columnCount);
//        Cell cell = row.createCell(columnCount);
//        if (value instanceof Long) {
//            cell.setCellValue((Long) value);
//        } else if (value instanceof Integer) {
//            cell.setCellValue((Integer) value);
//        } else if (value instanceof Boolean) {
//            cell.setCellValue((Boolean) value);
//        } else if (value instanceof Double) {
//            cell.setCellValue((Double) value);
//        } else if (value instanceof LocalDateTime) {
//            cell.setCellValue(String.valueOf((ZonedDateTime) value));
//        } else {
//            cell.setCellValue((String) value);
//        }
//        cell.setCellStyle(style);
//    }
//
//    public void writeHeaderLine() {
//        workbook = new XSSFWorkbook();
//        sheet = workbook.createSheet("Vimo's Risk Transaction");
//        Row row = sheet.createRow(0);
//        CellStyle style = workbook.createCellStyle();
//        XSSFFont font = workbook.createFont();
//        font.setBold(true);
//        font.setFontHeight(20);
//        style.setFont(font);
//        style.setAlignment(HorizontalAlignment.CENTER);
//        createCell(row, 0, "Risk Transaction", style);
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
//        font.setFontHeightInPoints((short) (10));
//        row = sheet.createRow(1);
//        font.setBold(true);
//        font.setFontHeight(16);
//        style.setFont(font);
//        createCell(row, 0, "Transaction Code", style);
//        createCell(row, 1, "Customer Name", style);
//        createCell(row, 2, "Amount", style);
//        createCell(row, 3, "Rules Description", style);
//        createCell(row, 4, "Method Name", style);
//        createCell(row, 5, "Transaction Time", style);
//
//    }
//
//    public void writeDetailLines(List<VimoRiskTrans> vimoRiskTransList) {
//        int rowCount = 2;
//        CellStyle style = workbook.createCellStyle();
//        XSSFFont font = workbook.createFont();
//        font.setFontHeight(14);
//        style.setFont(font);
//        for (VimoRiskTrans vimoRiskTrans : vimoRiskTransList) {
//            Row row = sheet.createRow(rowCount++);
//            int columnCount = 0;
//            createCell(row, columnCount++, vimoRiskTrans.getTransactionCode(), style);
//            createCell(row, columnCount++, vimoRiskTrans.getCustomerName(), style);
//            createCell(row, columnCount++, vimoRiskTrans.getAmount(), style);
//            createCell(row, columnCount++, vimoRiskTrans.getRulesDescription().stream()
//                    .map(n -> String.valueOf(n))
//                    .collect(Collectors.joining(",", "{", "}")), style);
//            createCell(row, columnCount++, vimoRiskTrans.getMethodName(), style);
//            createCell(row, columnCount++, vimoRiskTrans.getTransactionTime().toLocalDateTime().toString(), style);
//        }
//    }
//
//    @Override
//    public ByteArrayResource exportReport(List<VimoRiskTrans> vimoRiskTransList) {
//        try {
//            log.info("get file report");
//            writeHeaderLine();
//            writeDetailLines(vimoRiskTransList);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            try {
//                workbook.write(bos);
//            } finally {
//                bos.close();
//            }
//            byte[] excelFileAsBytes = bos.toByteArray();
//            ByteArrayResource resource = new ByteArrayResource(excelFileAsBytes);
//            workbook.close();
//            bos.close();
//            return resource;
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
//        return null;
//    }
//}
