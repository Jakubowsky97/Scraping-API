package com.example.demo.controller;

import com.example.demo.model.ScrapModel;
import com.example.demo.service.ScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scrap")
public class ScrapController {

    @Autowired
    public ScrapService scrapService;

    @GetMapping("/get-all")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<ScrapModel> scrapModelList = scrapService.getAll();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Scrap successfully");
        response.put("code", "200");
        response.put("data", scrapModelList);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/by-title")
    public ResponseEntity<Map<String, Object>> scrapByTitle(@RequestBody Map<String, String> body) {
        String query = body.get("query");

        List<String> elements = scrapService.getInfo(query);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Scrap successfully");
        response.put("code", "200");
        response.put("data", elements);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove-by-title")
    public ResponseEntity<Map<String, Object>> removeByTitle(@RequestBody Map<String, String> body) {
        String query = body.get("query");
        String DBresponse = scrapService.removeInDb(query);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Scrap successfully");
        response.put("code", "200");
        response.put("data", DBresponse);

        return ResponseEntity.ok(response);
    }
}
