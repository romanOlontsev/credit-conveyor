package ru.neoflex.neostudy.dossier.utils;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor
public class PdfConverter {

    private final TemplateEngine templateEngine;

    public void execute() throws IOException {
        Context context = new Context();
        context.setVariable("lastname", "GUS");
        String process = templateEngine.process("document", context);

        Document xhtml = Jsoup.parse(process, "UTF-8");
        xhtml.outputSettings()
             .syntax(Document.OutputSettings.Syntax.xml);

        String pdfOutput = "dossier/src/main/resources/document/document.pdf";
        File outputPdf = new File(pdfOutput);
        try (OutputStream outputStream = new FileOutputStream(outputPdf)) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            renderer.setDocumentFromString(xhtml.html());
            renderer.layout();
            renderer.createPDF(outputStream);
        }
//        try (OutputStream os = new FileOutputStream(outputPdf)) {
//            PdfRendererBuilder builder = new PdfRendererBuilder();
//            builder.withUri(pdfOutput);
//            builder.toStream(os);
//            builder.withW3cDocument(new W3CDom().fromJsoup(xhtml), "/");
//            builder.run();
//        }

    }
}
