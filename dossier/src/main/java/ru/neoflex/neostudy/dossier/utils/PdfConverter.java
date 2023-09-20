package ru.neoflex.neostudy.dossier.utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.neoflex.neostudy.dossier.model.dto.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PdfConverter {

    @Value("${document.output-path}")
    private String documentPath;

    private final TemplateEngine templateEngine;

    public void execute(String htmlName, ApplicationDTO applicationInfo, Long applicationId) throws IOException {
        String process = substituteInTemplate(htmlName, applicationInfo);
        Document xhtml = getXhtml(process);
        generatePdf(htmlName, xhtml, applicationId);
    }

    private void generatePdf(String htmlName, Document xhtml, Long applicationId) throws IOException {
        String pdfOutput = String.format(documentPath, htmlName, applicationId);
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
        EmploymentDTO employment = client.getEmployment();
        CreditDTO credit = applicationInfo.getCredit();
        context.setVariable("firstname", client.getFirstName());
        context.setVariable("lastname", client.getLastName());
        context.setVariable("middlename", client.getMiddleName());
        context.setVariable("gender", client.getGender()
                                            .getText());
        context.setVariable("passport_series", passport.getSeries());
        context.setVariable("passport_number", passport.getNumber());
        context.setVariable("issue_branch", passport.getIssueBranch());
        context.setVariable("issue_date", passport.getIssueDate());
        context.setVariable("email", client.getEmail());
        context.setVariable("marital_status", client.getMaritalStatus()
                                                    .getText());
        context.setVariable("dependent_amount", client.getDependentAmount());
        context.setVariable("employment_status", employment.getStatus()
                                                           .getText());
        context.setVariable("employer_inn", employment.getEmployerInn());
        context.setVariable("position", employment.getPosition()
                                                  .getText());
        context.setVariable("salary", employment.getSalary());
        context.setVariable("work_experience_total", employment.getWorkExperienceTotal());
        context.setVariable("work_experience_current", employment.getWorkExperienceCurrent());

        context.setVariable("amount", credit.getAmount());
        context.setVariable("term", credit.getTerm());
        context.setVariable("rate", credit.getRate());
        context.setVariable("psk", credit.getPsk());
        context.setVariable("monthly_payment", credit.getMonthlyPayment());
        context.setVariable("insurance_enable", credit.getIsInsuranceEnabled());
        context.setVariable("salary_client", credit.getIsSalaryClient());

        context.setVariable("payments", credit.getPaymentSchedule());
        context.setVariable("date", LocalDate.now());

        return templateEngine.process(htmlName, context);
    }
}
