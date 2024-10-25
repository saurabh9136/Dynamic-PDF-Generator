package com.assignment.generatePDF.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.assignment.generatePDF.Entity.PdfRequest;

@Service
public class PdfGenerationService {
    
    @Autowired
    private TemplateEngine templateEngine;

    private final String pdfStoragePath = "generated_pdfs/";
    // steps - generate a unique filename,  check the file is available with same details and return accordingly
    public File generatePdf(PdfRequest pdfRequest) throws Exception {
        Files.createDirectories(Paths.get(pdfStoragePath));
        String fileName = generateFileName(pdfRequest);
        File pdfFile = new File(pdfStoragePath + fileName);

        if (pdfFile.exists()) {
            System.out.println("Returning the existing file: " + pdfFile.getAbsolutePath());
            return pdfFile; // Return existing PDF
        }

        // Generate new PDF
        Context context = new Context();
        context.setVariable("pdfRequest", pdfRequest);

        String htmlContent = templateEngine.process("invoice", context);

        try (OutputStream os = new FileOutputStream(pdfFile)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);
        }

        System.out.println("New PDF generated: " + pdfFile.getAbsolutePath());
        return pdfFile;
    }

    public String generateFileName(PdfRequest pdfRequest) throws Exception {
        String inputData = pdfRequest.getSeller() + pdfRequest.getBuyer() + pdfRequest.getItems().toString();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(inputData.getBytes("UTF-8"));
        return Base64.getUrlEncoder().encodeToString(hash) + ".pdf";
    }
}
