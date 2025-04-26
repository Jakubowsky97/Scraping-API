package com.example.demo.service;

import com.example.demo.dto.ScrapDto;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {
    public byte[] generateExcel(List scraps) throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("News");
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Tytuł");
        header.createCell(1).setCellValue("Źródło");
        header.createCell(2).setCellValue("Link");

        for(int i = 0; i < scraps.size(); i++) {

            ScrapDto scrap = (ScrapDto) scraps.get(i);

            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(scrap.getTitle());
            row.createCell(1).setCellValue(scrap.getSource());
            row.createCell(2).setCellValue(scrap.getLink());
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        wb.write(output);

        return output.toByteArray();
    }
}
