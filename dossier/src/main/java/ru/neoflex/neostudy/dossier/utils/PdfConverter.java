package ru.neoflex.neostudy.dossier.utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.neoflex.neostudy.dossier.model.dto.ApplicationDTO;
import ru.neoflex.neostudy.dossier.model.dto.ClientDTO;
import ru.neoflex.neostudy.dossier.model.dto.PassportDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PdfConverter {

    private final TemplateEngine templateEngine;

    public void execute(String htmlName, ApplicationDTO applicationInfo) throws IOException {
        String process = substituteInTemplate(htmlName, applicationInfo);
        Document xhtml = getXhtml(process);
        generatePdf(htmlName, xhtml);

//        try (OutputStream outputStream = new FileOutputStream(outputPdf)) {
//            ITextRenderer renderer = new ITextRenderer();
//            SharedContext sharedContext = renderer.getSharedContext();
//            sharedContext.setPrint(true);
//            sharedContext.setInteractive(false);
//            renderer.setDocumentFromString(xhtml.html());
//            renderer.layout();
//            renderer.createPDF(outputStream);
//        }
    }

    private void generatePdf(String htmlName, Document xhtml) throws IOException {
        String pdfOutput = "dossier/src/main/resources/document/" + htmlName + ".pdf";
        File outputPdf = new File(pdfOutput);
        try (OutputStream os = new FileOutputStream(outputPdf)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri(pdfOutput);
            builder.toStream(os);
            builder.withW3cDocument(new W3CDom().fromJsoup(xhtml), "/");
            builder.run();
        }
    }

    private Document getXhtml(String process) {
        Document xhtml = Jsoup.parse(process, "UTF-8");
        xhtml.outputSettings()
             .syntax(Document.OutputSettings.Syntax.xml);
        return xhtml;
    }

    private String substituteInTemplate(String htmlName, ApplicationDTO applicationInfo) {
        Context context = new Context();
        ClientDTO client = applicationInfo.getClient();
        PassportDTO passport = client.getPassport();
        context.setVariable("firstname", client.getFirstName());
        context.setVariable("lastname", client.getLastName());
        context.setVariable("middlename", client.getMiddleName());
        context.setVariable("gender", client.getGender().getText());
        context.setVariable("passport_series", passport.getSeries());
        context.setVariable("passport_number", passport.getNumber());
        return templateEngine.process(htmlName, context);
    }

    private String capitalizeFirstLetter(String string) {
        return Pattern.compile("^.")
                      .matcher(string)
                      .replaceFirst(m -> m.group()
                                          .toUpperCase());
    }
}
