package com.swiftmessage.swiftmessage.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import javax.sql.DataSource;

public class JasperReportService {

    @Autowired
    private DataSource dataSource;
    
    public JasperPrint generateReport(Map<String, Object> parameters) throws JRException, SQLException
    {
          
        InputStream employeeReportStream = getClass().getResourceAsStream("/BirrReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
            JRSaver.saveObject(jasperReport, "BirrReport.jasper");


            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parameters, dataSource.getConnection());

                    return jasperPrint;

    }
    private Path getUploadPath(String fileFormat, JasperPrint jasperpring,String fileName) throws IOException, JRException
    {
        String uploadDir = ("./generated-reports");
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath))
        {
            Files.createDirectories(uploadPath);
        }

        if(fileFormat.equalsIgnoreCase("pdf"))
        {
            JasperExportManager.exportReportToPdfFile(jasperpring,uploadPath+fileName);
        }

        return uploadPath;
    }

    private String getPdfFileLink(String uploadPath )
    {
        return uploadPath+"/"+"products.pdf";
    }
}