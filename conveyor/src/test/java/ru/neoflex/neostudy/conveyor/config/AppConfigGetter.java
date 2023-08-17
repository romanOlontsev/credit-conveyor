package ru.neoflex.neostudy.conveyor.config;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.util.Properties;

public class AppConfigGetter {
    @SneakyThrows
    public static AppConfig getConfig() {
        String rootPath = Thread.currentThread()
                                .getContextClassLoader()
                                .getResource("")
                                .getPath();
        String appConfigPath = rootPath + "application.properties";
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));

        double baseRate = Double.parseDouble(appProps.getProperty("app.conveyor.base-rate"));
        int minAge = Integer.parseInt(appProps.getProperty("app.conveyor.credit-min-age"));
        int maxAge = Integer.parseInt(appProps.getProperty("app.conveyor.credit-max-age"));
        int experienceTotal = Integer.parseInt(appProps.getProperty("app.conveyor.min-work-experience-total"));
        int experienceCurrent = Integer.parseInt(appProps.getProperty("app.conveyor.min-work-experience-current"));
        int salaryCount = Integer.parseInt(appProps.getProperty("app.conveyor.salaries-less-loan-amount"));
        int womanMinAge = Integer.parseInt(appProps.getProperty("app.conveyor.woman-min-age-changing-rate"));
        int womanMaxAge = Integer.parseInt(appProps.getProperty("app.conveyor.woman-max-age-changing-rate"));
        int manMinAge = Integer.parseInt(appProps.getProperty("app.conveyor.man-min-age-changing-rate"));
        int manMaxAge = Integer.parseInt(appProps.getProperty("app.conveyor.man-max-age-changing-rate"));

        double isInsuranceEnabledRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.is-insurance-enabled-reduction"));
        double isSalaryClientRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.is-salary-client-reduction"));
        double selfEmployedRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.self-employed-increase"));
        double businessOwnerRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.business-owner-increase"));
        double middleManagerRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.middle-manager-reduction"));
        double topManagerRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.top-manager-reduction"));
        double marriedRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.married-reduction"));
        double divorcedRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.divorced-increase"));
        double dependentAmountRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.dependent-amount-increase"));
        double womanAgeRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.woman-age-reduction"));
        double manAgeRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.man-age-reduction"));
        double nonBinaryAgeRate = Double.parseDouble(appProps.getProperty("app.conveyor.rate.non-binary-age-increase"));
        return new AppConfig(baseRate, minAge, maxAge, experienceTotal, experienceCurrent, salaryCount, womanMinAge,
                womanMaxAge, manMinAge, manMaxAge,
                new AppConfig.Rate(isInsuranceEnabledRate, isSalaryClientRate, selfEmployedRate, businessOwnerRate,
                        middleManagerRate, topManagerRate, marriedRate, divorcedRate, dependentAmountRate,
                        womanAgeRate, manAgeRate, nonBinaryAgeRate));
    }
}
