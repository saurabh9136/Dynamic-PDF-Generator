package com.assignment.generatePDF.Service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.assignment.generatePDF.Entity.PdfRequest;

@SpringBootTest
public class PdfGenerationServiceTest {

    @Autowired
    private PdfGenerationService pdfGenerationService;

    @MockBean
    private TemplateEngine templateEngine;

    @Test
    public void testGeneratePdf() throws Exception {
        PdfRequest pdfRequest = new PdfRequest(
                "XYZ Pvt. Ltd.",
                "29AABBCCDD121ZD",
                "New Delhi, India",
                "Vedant Computers",
                "29AABBCCDD131ZD",
                "New Delhi, India",
                Arrays.asList(new PdfRequest.Item("Product 1", "12 Nos", 123.00, 1476.00))
        );

        // Mocking Thymeleaf template engine behavior
        Mockito.when(templateEngine.process(Mockito.anyString(), Mockito.any(Context.class)))
                .thenReturn("<html><body>Test PDF</body></html>");

        // Call the service to generate the PDF
        File pdfFile = pdfGenerationService.generatePdf(pdfRequest);

        // Verify that the file was generated
        assertNotNull(pdfFile); // Check that the returned file is not null
        assertTrue(pdfFile.exists()); // Check that the file actually exists on disk
    }
}
