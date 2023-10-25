package com.tefo.api.documentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedocController {


    @GetMapping({"redoc", "redoc-qa"})
    public ResponseEntity<String> getRedocDev() {
        return ResponseEntity.ok(getHtml("qa"));
    }

    @GetMapping("redoc-dev")
    public ResponseEntity<String> getRedoc() {
        return ResponseEntity.ok(getHtml("dev"));
    }

    private String getHtml(String env) {
        return String.format("""
                <!DOCTYPE html>
                <html>
                  <head>
                    <title>Redoc</title>
                    <!-- needed for adaptive design -->
                    <meta charset="utf-8"/>
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <link href="https://fonts.googleapis.com/css?family=Montserrat:300,400,700|Roboto:300,400,700" rel="stylesheet">
                    <!--
                    Redoc doesn't change outer page styles
                    -->
                    <style>
                      body {
                        margin: 0;
                        padding: 0;
                      }
                    </style>
                  </head>
                  <body>
                    <redoc spec-url='api-docs-all/%s'></redoc>
                    <script src="https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js"> </script>
                  </body>
                </html>
                """, env);
    }

}
