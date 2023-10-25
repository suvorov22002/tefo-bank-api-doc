package com.tefo.api.documentation.controller;

import com.tefo.api.documentation.service.ApiUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiDocsController {

    private final ApiUtils apiUtils;

    public ApiDocsController(ApiUtils apiUtils) {
        this.apiUtils = apiUtils;
    }

    @GetMapping("/api-docs/{service}/{env}")
    public ResponseEntity<String> getDocs(@PathVariable String service,
                                          @PathVariable String env) {
        return ResponseEntity.ok(apiUtils.getAPI(service, env));
    }

    @GetMapping("/api-docs-all/{env}")
    public ResponseEntity<String> getCombinedDocs(@PathVariable String env) {
        return ResponseEntity.ok(apiUtils.getCombinedAPI(env));
    }
}
