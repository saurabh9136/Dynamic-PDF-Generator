package com.assignment.generatePDF.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.assignment.generatePDF.Entity.PdfRequest;
import com.assignment.generatePDF.Service.PdfGenerationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class PdfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PdfGenerationService pdfGenerationService;

    @Test
    public void testGenerateNewPdf() throws Exception {
        PdfRequest pdfRequest = new PdfRequest(
                "XYZ Pvt. Ltd.",
                "29AABBCCDD121ZD",
                "New Delhi, India",
                "Vedant Computers",
                "29AABBCCDD131ZD",
                "New Delhi, India",
                Arrays.asList(new PdfRequest.Item("Product 1", "12 Nos", 123.00, 1476.00))
        );

        String generatedFilePath = "generated_pdfs/new_file.pdf";

        // Mocking behavior of the service for a new file generation
        Mockito.when(pdfGenerationService.generatePdf(Mockito.any(PdfRequest.class)))
                .thenReturn(new File(generatedFilePath));

        Mockito.when(pdfGenerationService.generateFileName(Mockito.any(PdfRequest.class)))
                .thenReturn("new_file.pdf");

        File newFile = new File(generatedFilePath);
        if (newFile.exists()) {
            newFile.delete(); // Ensure the file doesn't exist to simulate a new generation
        }

        // Perform the API request and expect a successful response
        mockMvc.perform(post("/api/pdf/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(pdfRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("New PDF file created successfully at: " + generatedFilePath));
    }

    @Test
    public void testGenerateExistingPdf() throws Exception {
        PdfRequest pdfRequest = new PdfRequest(
                "XYZ Pvt. Ltd.",
                "29AABBCCDD121ZD",
                "New Delhi, India",
                "Vedant Computers",
                "29AABBCCDD131ZD",
                "New Delhi, India",
                Arrays.asList(new PdfRequest.Item("Product 1", "12 Nos", 123.00, 1476.00))
        );

        String existingFilePath = "generated_pdfs/existing_file.pdf";

        // Mocking behavior of the service for an existing file
        Mockito.when(pdfGenerationService.generatePdf(Mockito.any(PdfRequest.class)))
                .thenReturn(new File(existingFilePath));

        Mockito.when(pdfGenerationService.generateFileName(Mockito.any(PdfRequest.class)))
                .thenReturn("existing_file.pdf");

        // Simulating that the file already exists
        File existingFile = new File(existingFilePath);
        existingFile.createNewFile();

        // Perform the API request and expect a response saying the file already exists
        mockMvc.perform(post("/api/pdf/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(pdfRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("File already exists. Path: " + existingFilePath));
    }

    // Helper method to convert request objects to JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
