package com.swiftmessage.swiftmessage.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.component.piechart.PieChart;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.optionconfig.animation.Animation;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.polar.PolarAreaChartDataSet;
import org.primefaces.model.charts.polar.PolarAreaChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.swiftmessage.swiftmessage.dto.Branch;
import com.swiftmessage.swiftmessage.dto.TransactionReport;
import com.swiftmessage.swiftmessage.entity.SwiftTransaction;
import com.swiftmessage.swiftmessage.service.AccountInfoService;
import com.swiftmessage.swiftmessage.service.AccountRestClient;
import com.swiftmessage.swiftmessage.service.BranchService;
import com.swiftmessage.swiftmessage.service.MessageIncomingService;
import com.swiftmessage.swiftmessage.service.SwiftTransactionService;
import com.swiftmessage.swiftmessage.service.TblCorrespondentBankService;
import com.swiftmessage.swiftmessage.service.TransactionReportService;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.ScopedProxyMode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Controller
public class ReportController {

    private String downloadFileName = "pfe-rocks.pdf";
    private StreamedContent content;

    private StreamedContent file;

    @Autowired
    SwiftTransactionService swifttransactionservice;

    @Autowired
    TransactionReportService transactionservice;

    @Autowired
    MessageIncomingService incomingService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BranchService branchService;

    @Autowired
    AccountInfoService accountInfoService;

    @Autowired
    TblCorrespondentBankService tblCorrespondentBankService;

    private WebClient webclient2 = WebClient.create("http://localhost:6070/getByAccountNo/");

    AccountRestClient accountRestClient = new AccountRestClient(webclient2);

    private SwiftTransaction selected;

    private List<SwiftTransaction> retaitems;
    private List<SwiftTransaction> nretaitems;
    private List<SwiftTransaction> mulselect;
    private List<Branch> branchList;

    private int liquidated;
    private int pending;
    private int cancelled;
    private int otherBank;

    private Date to;
    private Date from;
    private boolean dataRnd = false;
    private String fileName;
    private String fincName;

    private BarChartModel barModel2;
    private BarChartModel currencyModel;
    private DonutChartModel donutModel;
    private PieChartModel pieModel;
    private PolarAreaChartModel polarAreaModel;

    private List<SwiftTransaction> items;
    private List<TransactionReport> chartitems;
    private String status;
    private String branchCode;
    private String reportBy;


    private String birrAccount = new String();
    private String fcyAccount = new String();
    private Double totalBirrAmount;
    private String branch;
    private String district;
    private String corrBranch;

    private String product;
    private String corrAccName;
    private String corrAccNo;
    private String corrAccccy;
    private Double corrLcyAmt;

    


    

    

    public String getCorrBranch(SwiftTransaction item) {
        if (tblCorrespondentBankService.findByName(item.getMessage().getCorrespondentBank()).size()>0) {
            String correAcc = tblCorrespondentBankService.findByName(item.getMessage().getCorrespondentBank()).get(0).getBankAccount();
            corrBranch = accountRestClient.getAccountInfo(correAcc).getBRANCH_CODE();
        }
       
        return corrBranch;
    }

    public void setCorrBranch(String corrBranch) {
        this.corrBranch = corrBranch;
    }

    public String getDistrict(SwiftTransaction item) {

        if (accountRestClient.getDistrictName(item.getMessage().getDashenBranch())!=null) {
            district = accountRestClient.getDistrictName(item.getMessage().getDashenBranch()).getDESTRICT_NAME();
        
            return district;
    
        }
        
        else
        {
            return "No";
        }
        
       
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Double getCorrLcyAmt(SwiftTransaction item) {
        DecimalFormat df = new DecimalFormat("0.00");
        corrLcyAmt= item.getMessage().getFcyAmount() * item.getMessage().getRate();
        corrLcyAmt = Double.parseDouble(df.format(corrLcyAmt));
        return corrLcyAmt;
    }

    public void setCorrLcyAmt(Double corrLcyAmt) {
       
        this.corrLcyAmt = corrLcyAmt;
        
    }

    public String getProduct(SwiftTransaction item) {
        if (item.getMessage().getBankReference().contains("FINC")) {
            product = "FINC";
        }
        if (item.getMessage().getBankReference().contains("RETA")) {
            product = "RETA";
        }
        
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCorrAccName(SwiftTransaction item) {

        if (tblCorrespondentBankService.findByName(item.getMessage().getCorrespondentBank()).size()>0) {
            String correAcc = tblCorrespondentBankService.findByName(item.getMessage().getCorrespondentBank()).get(0).getBankAccount();
            corrAccName = accountRestClient.getAccountInfo(correAcc).getAC_DESC();
        }
       
        
        return corrAccName;
    }

    public void setCorrAccName(String corrAccName) {
        this.corrAccName = corrAccName;
    }

    public String getCorrAccNo(SwiftTransaction item) {
        if (tblCorrespondentBankService.findByName(item.getMessage().getCorrespondentBank()).size()>0) {
        corrAccNo = tblCorrespondentBankService.findByName(item.getMessage().getCorrespondentBank()).get(0).getBankAccount();
        }
        return corrAccNo;
    }

    public void setCorrAccNo(String corrAccNo) {
        this.corrAccNo = corrAccNo;
    }

    public String getCorrAccccy(SwiftTransaction item) {
        if (tblCorrespondentBankService.findByName(item.getMessage().getCorrespondentBank()).size()>0) {
        String correAcc = tblCorrespondentBankService.findByName(item.getMessage().getCorrespondentBank()).get(0).getBankAccount();
        corrAccccy = accountRestClient.getAccountInfo(correAcc).getCCY();
        }
        return corrAccccy;
    }

    public void setCorrAccccy(String corrAccccy) {
        this.corrAccccy = corrAccccy;
    }

    public String getBranch(SwiftTransaction item) {

        String branch = branchService.requestTransactionBranchCheck(item.getMessage().getDashenBranch()).getBRANCH_NAME();
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Double getTotalBirrAmount(SwiftTransaction item) {

        DecimalFormat df = new DecimalFormat("0.00");
        if (item.getMessage().getFcyAmount()!=null) {
            totalBirrAmount = item.getMessage().getFcyAmount() * item.getMessage().getRate();
            totalBirrAmount = Double.parseDouble(df.format(totalBirrAmount));
        }
        return totalBirrAmount;
    }

    public void setTotalBirrAmount(Double totalBirrAmount) {
        this.totalBirrAmount = totalBirrAmount;
    }

    public String getBirrAccount(SwiftTransaction item) {
        birrAccount=new String();
        if(item.getRetaBirrAccount()!=null)
        {
            birrAccount=item.getRetaBirrAccount();
        }
        if (item.getOtherAccountNumber()!=null) {
            if (item.getOtherAccountCurrency().equalsIgnoreCase("ETB")) {
                birrAccount=item.getOtherAccountNumber();
            }
        }
        if (item.getOtherAccountNumber()==null) {
            if (item.getMessage().getAccountNumberCurrency().equalsIgnoreCase("ETB")) {
                birrAccount=item.getMessage().getAccountNumber();
            }
        }
        
        return birrAccount;
    }

    public void setBirrAccount(String birrAccount) {
        this.birrAccount = birrAccount;
    }

    public String getFcyAccount(SwiftTransaction item) {
        fcyAccount = new String();
        if (item.getOtherAccountNumber()!=null) {
            if (!item.getOtherAccountCurrency().equalsIgnoreCase("ETB")) {
                fcyAccount=item.getOtherAccountNumber();
            }
        }
        if (item.getOtherAccountNumber()==null) {
            if (!item.getMessage().getAccountNumberCurrency().equalsIgnoreCase("ETB")) {
                fcyAccount=item.getMessage().getAccountNumber();
            }
        }
        
        return fcyAccount;
    }

    public void setFcyAccount(String fcyAccount) {
        this.fcyAccount = fcyAccount;
    }

    public StreamedContent getFile() {
        file = DefaultStreamedContent.builder()
                .name("employeeReport.pdf")
                .contentType("application/pdf")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext()
                        .getResourceAsStream("employeeReport.pdf"))
                .build();
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public StreamedContent getContent() {
        return content;
    }

    public void setContent(StreamedContent content) {
        this.content = content;
    }

    @RequestMapping("/Report/dashboard")
    public String report() {
        // chartitems = transactionservice.findAllc();

        // System.out.println(chartitems + " Chart Data");
        return "/home.xhtml";
    }

    @RequestMapping("/Report/Birr")
    public String reportbirr() {
        // chartitems = transactionservice.findAllc();

        // System.out.println(chartitems + " Chart Data");
        return "/Report/SwiftInBirr.xhtml";
    }

    @RequestMapping("/Report/audit")
    public String reportTableAudit() {
        System.out.println("test");

        // nretaitems = swifttransactionservice.findByDateAll(from, to);

        // System.out.println(nretaitems + "Items");
        return "forward:/Report/AuditReport.xhtml";
    }

    // @RequestMapping("/Report/general")
    // public String reportTableGeneral() {
    // System.out.println("test");

    // System.out.println(from);
    // nretaitems = swifttransactionservice.findByDateAll(from, to);

    // System.out.println(nretaitems + "Items");
    // return "forward:/Report/SwiftInBirr.xhtml";
    // }

    @RequestMapping("/Report/table")
    public String reportTable() {
        System.out.println("test");

        System.out.println("Date--------------" + from);
        LocalDate changedDate = LocalDate.now();
        LocalDate firstDayOfMonth = changedDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = changedDate.withDayOfMonth(changedDate.lengthOfMonth());
        Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        System.out.println(startDate + "-------start");
        System.out.println(endDate + "-------end");
        nretaitems = swifttransactionservice.findByBnDates(startDate, endDate);

        System.out.println(nretaitems + "Items");
        return "forward:/Report/index.xhtml";
    }

    @RequestMapping("/Report/retatable")
    public String reportTableReta() {
        System.out.println("test");

        System.out.println("Date--------------" + from);
        LocalDate changedDate = LocalDate.now();
        LocalDate firstDayOfMonth = changedDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = changedDate.withDayOfMonth(changedDate.lengthOfMonth());
        Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        System.out.println(startDate + "-------start");
        System.out.println(endDate + "-------end");
        retaitems = swifttransactionservice.findByBnDatesReta(startDate, endDate);

        System.out.println(retaitems + "Items");
        return "forward:/Report/retaIndex.xhtml";
    }

    /**
     * 
     */
    public void getByMonthReta() {
        System.out.println("status" + status);

        System.out.println("Date--------------" + from);
        dataRnd = true;
        LocalDate changedDate = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate firstDayOfMonth = changedDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = changedDate.withDayOfMonth(changedDate.lengthOfMonth());
        Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        System.out.println(startDate + "-------start");
        System.out.println(endDate + "-------end");

        retaitems = swifttransactionservice.findByBnDatesReta(startDate, endDate);
    }

    public void getByMonth() {
        System.out.println("status" + status);
        dataRnd = true;
        System.out.println("Date--------------" + dataRnd);
        LocalDate changedDate = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate firstDayOfMonth = changedDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = changedDate.withDayOfMonth(changedDate.lengthOfMonth());
        Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        System.out.println(startDate + "-------start");
        System.out.println(endDate + "-------end");

        nretaitems = swifttransactionservice.findByBnDates(startDate, endDate);
        System.out.println(nretaitems.toString() + "-------------items");
    }

    public void getByMonthAudit() {

        dataRnd = true;
        System.out.println("Date--------------" + dataRnd);

        nretaitems = swifttransactionservice.findByDateAll(from, to);
        System.out.println(nretaitems.toString() + "-------------items");
    }

    public void getByMonthAdvice() {

        dataRnd = true;
        System.out.println("Date--------------" + to);

        nretaitems = swifttransactionservice.findByBnDateSingle(to);
        System.out.println(nretaitems.toString() + "-------------items");
    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", DataSet Index:" + event.getDataSetIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public BarChartModel getBarModel2() {

        barModel2 = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("FINC");
        barDataSet.setBackgroundColor("rgba(255, 99, 132, 0.2)");
        barDataSet.setBorderColor("rgb(255, 99, 132)");
        barDataSet.setBorderWidth(1);
        List<Number> values = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        cal.clear();
        cal.set(Calendar.YEAR, year);

        for (int currentMonth = 0; currentMonth < 12; currentMonth++) {

            cal.set(Calendar.MONTH, currentMonth);

            // first day :
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date firstDay = cal.getTime();

            // last day
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date lastDay = cal.getTime();

            int total = swifttransactionservice.findByBnDates(firstDay, lastDay).size();
            System.out.println(total + "---------------total");
            values.add(total);
            barDataSet.setData(values);

        }

        BarChartDataSet barDataSet2 = new BarChartDataSet();
        barDataSet2.setLabel("RETA");
        barDataSet2.setBackgroundColor("rgba(255, 159, 64, 0.2)");
        barDataSet2.setBorderColor("rgb(255, 159, 64)");
        barDataSet2.setBorderWidth(1);
        List<Number> values2 = new ArrayList<>();
        for (int currentMonth = 0; currentMonth < 12; currentMonth++) {

            cal.set(Calendar.MONTH, currentMonth);

            // first day :
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date firstDay = cal.getTime();

            // last day
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date lastDay = cal.getTime();

            int total = swifttransactionservice.findByBnDatesReta(firstDay, lastDay).size();
            System.out.println(total + "---------------total");
            values2.add(total);
            barDataSet2.setData(values2);

        }

        data.addChartDataSet(barDataSet);
        data.addChartDataSet(barDataSet2);

        List<String> labels = new ArrayList<>();

        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");
        data.setLabels(labels);
        barModel2.setData(data);

        // Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        linearAxes.setBeginAtZero(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Bar Chart");
        options.setTitle(title);

        barModel2.setOptions(options);
        return barModel2;
    }

    public void setBarModel2(BarChartModel barModel2) {
        this.barModel2 = barModel2;
    }

    /**
     * @return
     */
    public DonutChartModel getDonutModel() {
        donutModel = new DonutChartModel();
        ChartData data = new ChartData();

        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> values = new ArrayList<>();
        System.out.println("--------------number------" + incomingService.findTopBranch());
        List<Object[]> topListObject = incomingService.findTopBranch();
        BigDecimal amount = BigDecimal.ZERO;
        for (Object[] obj : topListObject) {
            amount = (BigDecimal) obj[0];
            System.out.println(amount + "------------------");
            values.add(amount);
            dataSet.setData(values);
        }

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");
        bgColors.add("rgb(255, 205, 86)");
        bgColors.add("rgb(153, 102, 255)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        String label = null;
        for (Object[] obj : topListObject) {
            if ((String) obj[1] == null) {
                label = "Other Bank";
            } else {
                String branchName = branchService.requestTransactionBranchCheck((String) obj[1]).getBRANCH_NAME();
                label = branchName;
            }

            System.out.println(label + "------------------");
            labels.add(label);
            data.setLabels(labels);
        }
 
        donutModel.setData(data);
        return donutModel;
    }

    public PieChartModel getPieModel() {
        pieModel = new PieChartModel();
        ChartData data = new ChartData();
        List<Object[]> topListObject = incomingService.findAccountType();
        System.out.println(topListObject.size() + "data");
        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();
        for (Object[] obj : topListObject) {

            values.add((BigDecimal) obj[1]);

        }
        dataSet.setData(values);
        data.addChartDataSet(dataSet);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgba(255, 99, 132)");
        bgColors.add("rgba(255, 159, 64)");
        bgColors.add("rgba(255, 205, 86)");
        bgColors.add("rgba(75, 192, 192)");
        bgColors.add("rgba(54, 162, 235)");
        bgColors.add("rgba(153, 102, 255)");
        bgColors.add("rgba(201, 203, 207)");
        dataSet.setBackgroundColor(bgColors);


        List<String> labels = new ArrayList<>();
        for (Object[] obj : topListObject) {

            labels.add((String) obj[0]);

        }
        data.setLabels(labels);
        System.out.println(data.toString() + "data");
        pieModel.setData(data);

        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    public BarChartModel getCurrencyModel() {
        currencyModel = new BarChartModel();
        ChartData data = new ChartData();

        List<Object[]> topListObject = incomingService.findCurrencyAmount();
        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Currency");
        BigDecimal amount = BigDecimal.ZERO;
        List<Number> values = new ArrayList<>();
        for (Object[] obj : topListObject) {
            amount = (BigDecimal) obj[1];
            System.out.println(amount + "------------------");
            values.add(amount);

        }
        barDataSet.setData(values);

        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
        barDataSet.setBackgroundColor(bgColor);

        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
        barDataSet.setBorderColor(borderColor);
        barDataSet.setBorderWidth(1);

        data.addChartDataSet(barDataSet);

        List<String> labels = new ArrayList<>();
        for (Object[] obj : topListObject) {

            System.out.println(amount + "------------------");
            labels.add((String) obj[0]);

        }

        data.setLabels(labels);
        // data.addChartDataSet(barDataSet);
        currencyModel.setData(data);

        return currencyModel;
    }

    

    public PolarAreaChartModel getPolarAreaModel() {
        polarAreaModel = new PolarAreaChartModel();
        ChartData data = new ChartData();
        List<Object[]> topListObject = incomingService.findAccountType();
        PolarAreaChartDataSet dataSet = new PolarAreaChartDataSet();
        List<Number> values = new ArrayList<>();
       for (Object[] obj : topListObject) {

            //System.out.println(amount + "------------------");
            values.add((BigDecimal) obj[1]);

        }
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(75, 192, 192)");
        bgColors.add("rgb(255, 205, 86)");
        bgColors.add("rgb(201, 203, 207)");
        bgColors.add("rgb(54, 162, 235)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        for (Object[] obj : topListObject) {

            System.out.println((String) obj[0] + "------------------");
            labels.add((String) obj[0]);

        }
        data.setLabels(labels);

        polarAreaModel.setData(data);
        return polarAreaModel;
    }


    @GetMapping("/Transaction/Country/advice")
	public ResponseEntity<byte[]> getEmployeeRecordReport() throws FileNotFoundException, JRException, SQLException {

		
		JasperPrint empReport;

		
		// create employee data
LocalDate changedDate = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate firstDayOfMonth = changedDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = changedDate.withDayOfMonth(changedDate.lengthOfMonth());
        Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
	
			Map<String, Object> empParams = new HashMap<String, Object>();

			empParams.put("startdate", startDate);
			empParams.put("enddate",endDate);
			

			System.out.println("test" + empParams);

			empReport =

					JasperFillManager.fillReport

					(

							JasperCompileManager.compileReport(

									ResourceUtils.getFile("classpath:BirrReport.jrxml")

											.getAbsolutePath()) // path of the jasper report

							, empParams // dynamic parameters

							, dataSource.getConnection()

					);
		
		// dynamic parameters required for report

		HttpHeaders headers = new HttpHeaders();

		// set the PDF format

		headers.setContentType(MediaType.APPLICATION_PDF);

		headers.setContentDispositionFormData("filename",
				"CountryBasedReport.pdf");

		// create the report in PDF format

		return new ResponseEntity<byte[]>

		(JasperExportManager.exportReportToPdf(empReport),
				headers, HttpStatus.OK);

	}

 

     public void exportToExcel() throws IOException {
       Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");

        // Add logo image
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logoPath = servletContext.getRealPath("/resources/img/dbLogo.jpg");
        InputStream inputStream = new FileInputStream(logoPath); // Replace with your logo path
        byte[] bytes = IOUtils.toByteArray(inputStream);
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        inputStream.close();
        
        CreationHelper helper = workbook.getCreationHelper();
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(0);
        anchor.setRow1(0);
        Picture picture = drawing.createPicture(anchor, pictureIdx);
        picture.resize(1, 3); // Adjust the size and position of the image

        // Add header titles
        Row titleRow1 = sheet.createRow(4); // Adjust row index as needed
        Cell titleCell1 = titleRow1.createCell(0);
        titleCell1.setCellValue("DASHEN BANK SC");
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell1.setCellStyle(titleStyle);
        
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 17)); // Merge cells for the title

        Row titleRow2 = sheet.createRow(5); // Adjust row index as needed
        Cell titleCell2 = titleRow2.createCell(0);
        titleCell2.setCellValue("IBD Report");
        CellStyle subtitleStyle = workbook.createCellStyle();
        Font subtitleFont = workbook.createFont();
        subtitleFont.setBold(true);
        subtitleFont.setFontHeightInPoints((short) 14);
        subtitleStyle.setFont(subtitleFont);
        subtitleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell2.setCellStyle(subtitleStyle);
        
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 17)); // Merge cells for the subtitle

 
        // Create header row
        String[] headers = {"District Name","Branch Code","Branch Name", "Transaction Date", "Message Reference", "Transaction Reference", 
                            "Beneficiary Customer", "FCY Account", "Birr Account", "Account Class", 
                            "Account Currency", "Customer Category", "Fcy Amount", "Transferred FCY Amount", 
                            "Transferred Birr Amount", "Ex. Rate", "Total Birr Amount", 
                            "Ordering Customer", "Payment Purpose", "Country"};
                            Row headerRow = sheet.createRow(7); // Adjust row index as needed
                            for (int i = 0; i < headers.length; i++) {
                                Cell cell = headerRow.createCell(i);
                                cell.setCellValue(headers[i]);
                                CellStyle headerStyle = workbook.createCellStyle();
                                Font headerFont = workbook.createFont();
                                headerFont.setBold(true);
                                headerStyle.setFont(headerFont);
                                cell.setCellStyle(headerStyle);
                            }
                    
                            // Fill data rows
                            int rowNum = 8;
        for (SwiftTransaction item : retaitems) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(getDistrict(item));
            row.createCell(1).setCellValue(item.getMessage().getDashenBranch());
            row.createCell(2).setCellValue(getBranch(item));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(item.getMessage().getRegistrationDate());
            row.createCell(3).setCellValue(formattedDate);
            row.createCell(4).setCellValue(item.getMessage().getReference());
            row.createCell(5).setCellValue(item.getTransactionNumber());
            row.createCell(6).setCellValue(item.getMessage().getBeneficiary());
            if (getFcyAccount(item)!=null) {
                row.createCell(7).setCellValue(getFcyAccount(item).toString());
            }
            if (getBirrAccount(item)!=null) {
                row.createCell(8).setCellValue(getBirrAccount(item).toString());
            }
            
            row.createCell(9).setCellValue(item.getMessage().getAccountType());
            row.createCell(10).setCellValue(item.getMessage().getAccountNumberCurrency());
            row.createCell(11).setCellValue(item.getCustomerType());
            if (item.getMessage().getFcyAmount()!=null) {
                row.createCell(12).setCellValue(item.getMessage().getFcyAmount());
            }
            if (item.getFcyAmount()!=null) {
                row.createCell(13).setCellValue(item.getFcyAmount());
            }
            if (item.getBirrAmount()!=null) {
                row.createCell(14).setCellValue(item.getBirrAmount());
            }
            
             
            row.createCell(15).setCellValue(item.getMessage().getRate().toString());
            if (getTotalBirrAmount(item)!=null) {
                row.createCell(16).setCellValue(getTotalBirrAmount(item));
            }
           
            row.createCell(17).setCellValue(item.getMessage().getOrderingCustomer());
            row.createCell(18).setCellValue(item.getMessage().getPaymentPurpose());
            row.createCell(19).setCellValue(item.getMessage().getCountryOfOrigin());
        }

        // Autosize columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        context.getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"report.xlsx\"");
        
        OutputStream out = context.getExternalContext().getResponseOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
        
        context.responseComplete();
    }


    public void exportToExcelCorr() throws IOException {
        Workbook workbook = new XSSFWorkbook();
         Sheet sheet = workbook.createSheet("Report");
 
         // Add logo image
                 ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
         String logoPath = servletContext.getRealPath("/resources/img/dbLogo.jpg");
         InputStream inputStream = new FileInputStream(logoPath); // Replace with your logo path
         byte[] bytes = IOUtils.toByteArray(inputStream);
         int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
         inputStream.close();
         
         CreationHelper helper = workbook.getCreationHelper();
         Drawing<?> drawing = sheet.createDrawingPatriarch();
         ClientAnchor anchor = helper.createClientAnchor();
         anchor.setCol1(0);
         anchor.setRow1(0);
         Picture picture = drawing.createPicture(anchor, pictureIdx);
         picture.resize(1, 3); // Adjust the size and position of the image
 
         // Add header titles
         Row titleRow1 = sheet.createRow(4); // Adjust row index as needed
         Cell titleCell1 = titleRow1.createCell(0);
         titleCell1.setCellValue("DASHEN BANK SC");
         CellStyle titleStyle = workbook.createCellStyle();
         Font titleFont = workbook.createFont();
         titleFont.setBold(true);
         titleFont.setFontHeightInPoints((short) 16);
         titleStyle.setFont(titleFont);
         titleStyle.setAlignment(HorizontalAlignment.CENTER);
         titleCell1.setCellStyle(titleStyle);
         
         sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 17)); // Merge cells for the title
 
         Row titleRow2 = sheet.createRow(5); // Adjust row index as needed
         Cell titleCell2 = titleRow2.createCell(0);
         titleCell2.setCellValue("IBD Report");
         CellStyle subtitleStyle = workbook.createCellStyle();
         Font subtitleFont = workbook.createFont();
         subtitleFont.setBold(true);
         subtitleFont.setFontHeightInPoints((short) 14);
         subtitleStyle.setFont(subtitleFont);
         subtitleStyle.setAlignment(HorizontalAlignment.CENTER);
         titleCell2.setCellStyle(subtitleStyle);
         
         sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 17)); // Merge cells for the subtitle
 
  
         // Create header row
       
         String[] headers = {"TRN_REF_NO","Branch Name", "Product", "Corr_ACC_Name", "Corr_AC_No", 
                             "Corr_AC_CCy", "TRN_DT", "VALUE_DT", "INSTRUMENT_CODE", 
                             "DRCR_NO", "FCY_AMOUNT", "EXCH_RATE", "LCY_AMOUNT"};
                             Row headerRow = sheet.createRow(7); // Adjust row index as needed
                             for (int i = 0; i < headers.length; i++) {
                                 Cell cell = headerRow.createCell(i);
                                 cell.setCellValue(headers[i]);
                                 CellStyle headerStyle = workbook.createCellStyle();
                                 Font headerFont = workbook.createFont();
                                 headerFont.setBold(true);
                                 headerStyle.setFont(headerFont);
                                 cell.setCellStyle(headerStyle);
                             }
                     
                             // Fill data rows
                             int rowNum = 8;
         for (SwiftTransaction item : retaitems) {
             Row row = sheet.createRow(rowNum++);
             row.createCell(0).setCellValue(item.getTransactionNumber());
             row.createCell(1).setCellValue(getCorrBranch(item));
             row.createCell(2).setCellValue(getProduct(item));
             row.createCell(3).setCellValue(getCorrAccName(item));
             row.createCell(4).setCellValue(getCorrAccNo(item).toString());
            
             row.createCell(5).setCellValue(getCorrAccccy(item));
             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
             String formattedDate = formatter.format(item.getMessage().getRegistrationDate());

                 row.createCell(6).setCellValue(formattedDate);
                 SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                 String formattedDate1 = formatter1.format(item.getMessage().getValueDate());
             
             row.createCell(7).setCellValue(formattedDate1);
             row.createCell(8).setCellValue(item.getMessage().getReference());
             row.createCell(9).setCellValue("DR");
             if (item.getMessage().getFcyAmount()!=null) {
                 row.createCell(10).setCellValue(item.getMessage().getFcyAmount());
             }
             
                 row.createCell(11).setCellValue(item.getMessage().getRate());
             
             if (item.getBirrAmount()!=null) {
                 row.createCell(12).setCellValue(getCorrLcyAmt(item));
             }
             
              
             
         }
 
         // Autosize columns
         for (int i = 0; i < headers.length; i++) {
             sheet.autoSizeColumn(i);
         }
 
         // Write the output to a file
         FacesContext context = FacesContext.getCurrentInstance();
         context.getExternalContext().setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
         context.getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"report.xlsx\"");
         
         OutputStream out = context.getExternalContext().getResponseOutputStream();
         workbook.write(out);
         out.flush();
         out.close();
         workbook.close();
         
         context.responseComplete();
     }


    public void setPolarAreaModel(PolarAreaChartModel polarAreaModel) {
        this.polarAreaModel = polarAreaModel;
    }

    public void setCurrencyModel(BarChartModel currencyModel) {
        this.currencyModel = currencyModel;
    }

    public void setDonutModel(DonutChartModel donutModel) {
        this.donutModel = donutModel;
    }

    public String getFincName() {
        fincName = from + "  FINC Report";
        return fincName;
    }

    public void setFincName(String fincName) {
        this.fincName = fincName;
    }

    public String getFileName() {
        fileName = from + "  RETA Report";
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isDataRnd() {
        return dataRnd;
    }

    public void setDataRnd(boolean dataRnd) {
        this.dataRnd = dataRnd;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public SwiftTransaction getSelected() {
        return selected;
    }

    public void setSelected(SwiftTransaction selected) {
        this.selected = selected;
    }

    public List<SwiftTransaction> getRetaitems() {
        return retaitems;
    }

    public void setRetaitems(List<SwiftTransaction> retaitems) {
        this.retaitems = retaitems;
    }

    public List<SwiftTransaction> getNretaitems() {
        return nretaitems;
    }

    public void setNretaitems(List<SwiftTransaction> nretaitems) {
        this.nretaitems = nretaitems;
    }

    public List<SwiftTransaction> getMulselect() {
        return mulselect;
    }

    public void setMulselect(List<SwiftTransaction> mulselect) {
        this.mulselect = mulselect;
    }

    public List<TransactionReport> getChartitems() {
        return chartitems;
    }

    public void setChartitems(List<TransactionReport> chartitems) {
        this.chartitems = chartitems;
    }

    public int getLiquidated() {
        items = swifttransactionservice.findByStatus("Liquidated");
        liquidated = items.size();
        return liquidated;
    }

    public void setLiquidated(int liquidated) {
        this.liquidated = liquidated;
    }

    public int getPending() {
        items = swifttransactionservice.findByStatus("Pending");
        pending = items.size();
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getCancelled() {

        items = swifttransactionservice.findByStatus("Cancelled");
        cancelled = items.size();
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public int getOtherBank() {
        otherBank = incomingService.findOtherBank().size();
        return otherBank;
    }

    public void setOtherBank(int otherBank) {
        this.otherBank = otherBank;
    }

    public List<SwiftTransaction> getItems() {
        return items;
    }

    public void setItems(List<SwiftTransaction> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    

    /**
     * @return DataSource return the dataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return List<Branch> return the branchList
     */
    public List<Branch> getBranchList() {

        return branchService.getBranchInfo();
    }

    /**
     * @param branchList the branchList to set
     */
    public void setBranchList(List<Branch> branchList) {
        this.branchList = branchList;
    }

    /**
     * @return BranchService return the branchService
     */
    public BranchService getBranchService() {
        return branchService;
    }

    /**
     * @param branchService the branchService to set
     */
    public void setBranchService(BranchService branchService) {
        this.branchService = branchService;
    }

    /**
     * @return String return the branchCode
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * @param branchCode the branchCode to set
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * @return String return the reportBy
     */
    public String getReportBy() {
        return reportBy;
    }

    /**
     * @param reportBy the reportBy to set
     */
    public void setReportBy(String reportBy) {
        this.reportBy = reportBy;
    }

}