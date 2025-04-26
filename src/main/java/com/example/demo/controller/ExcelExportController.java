package com.example.demo.controller;

import com.example.demo.dto.ScrapDto;
import com.example.demo.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExcelExportController {
    @Autowired
    ExcelExportService excelExportService;

    @PostMapping("/xlsx")
    public ResponseEntity<byte[]> exportToExcel(@RequestBody List<ScrapDto> scraps) throws IOException {
        byte[] excelBytes = excelExportService.generateExcel(scraps);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "scraps.xlsx");

        return ResponseEntity.ok().headers(headers).body(excelBytes);
    }
}
