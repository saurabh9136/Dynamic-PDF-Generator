package com.assignment.generatePDF.Controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.generatePDF.Entity.PdfRequest;
import com.assignment.generatePDF.Service.PdfGenerationService;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {
    
    @Autowired
    private PdfGenerationService pdfGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<String> generatePdf(@RequestBody PdfRequest pdfRequest) {
        try {
            
            String fileName = pdfGenerationService.generateFileName(pdfRequest);
            File pdfFile = new File("generated_pdfs/" + fileName);

            boolean fileAlreadyExists = pdfFile.exists(); // Check existence before generation

            // Generate or retrieve the PDF
            pdfFile = pdfGenerationService.generatePdf(pdfRequest);

            String message;
            if (fileAlreadyExists) {
                message = "File already exists. Path: " + pdfFile.getAbsolutePath();
            } else {
                message = "New PDF file created successfully at: " + pdfFile.getAbsolutePath();
            }

            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to generate PDF", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

