package com.swiftmessage.swiftmessage.controller;

import net.sf.jasperreports.engine.*;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import net.sf.jasperreports.engine.design.JasperDesign;

import net.sf.jasperreports.engine.export.JRPdfExporter;

import net.sf.jasperreports.engine.util.JRLoader;

import net.sf.jasperreports.engine.util.JRSaver;

import net.sf.jasperreports.engine.xml.JRXmlLoader;

import net.sf.jasperreports.export.SimpleExporterInput;

import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import net.sf.jasperreports.export.SimplePdfReportConfiguration;

import org.primefaces.event.SelectEvent;

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

import com.swiftmessage.swiftmessage.dto.AccountInfoModel;

import com.swiftmessage.swiftmessage.entity.SwiftTransaction;

import com.swiftmessage.swiftmessage.service.AccountInfoService;

import com.swiftmessage.swiftmessage.service.AccountRestClient;

import com.swiftmessage.swiftmessage.service.BranchService;

import org.springframework.context.annotation.ScopedProxyMode;

import org.springframework.core.io.Resource;

import org.springframework.core.io.ResourceLoader;

import java.io.File;

import java.io.IOException;

import java.io.InputStream;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.time.LocalDate;

import java.time.ZoneId;

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

@RestController

public class JasperReportController {

	private static final String WebClient = null;

	@Autowired

	private DataSource dataSource;

	@Autowired

	private BranchService branchService;

	@Autowired

	private ResourceLoader resourceLoader;

	@Autowired

	AccountInfoService accountInfoService;

	private Date from;

	private double sendamount;

	private double usedRate;

	private SwiftTransaction items;

	public void prepareSelect(SwiftTransaction item) {

		if (item != null) {

			System.out.println("test item"
					+ item);

			items = item;

		}

	}

	public double getSendamount() {

		return sendamount;

	}

	public void setSendamount(double sendamount) {

		this.sendamount = sendamount;

	}

	public double getUsedRate() {

		return usedRate;

	}

	public void setUsedRate(double usedRate) {

		this.usedRate = usedRate;

	}

	public void onRowSelect(SelectEvent event) {

		System.out.println(items.getId());

	}

	@GetMapping("/Transaction/FINC/advice")

	public ResponseEntity<byte[]> getEmployeeRecordReport() throws JRException, SQLException,
			IOException {

		System.out.println("test"
				+ items);

		System.out.println(items.getMessage().getAccountType()
				+ "type");

		// create employee data

		if (items.getMessage().getBankReference().contains("RETA")

				&& (items.getMessage().getCurrencyId().equalsIgnoreCase(items.getMessage().getAccountNumberCurrency())

						||
						items.getMessage().getCurrencyId().equalsIgnoreCase(items.getOtherAccountCurrency()))) {

			System.out.println("Here Ret");

			Map<String, Object> empParams = new HashMap<String, Object>();

			empParams.put("invoice_id",
					items.getMessage().getReference());

			// empParams.put("usedRate",items.getFcyRate());

			String branchName = branchService.requestTransactionBranchCheck(items.getMessage().getDashenBranch())

					.getBRANCH_NAME();

			empParams.put("branchname",
					branchName);

			if (items.getOtherAccountNumber() != null)

			{

				AccountInfoModel accountHolderName = accountInfoService.getAccountInfo(items.getOtherAccountNumber());

				empParams.put("beneficiaryName",
						accountHolderName.getCustomerName());

			}

			else

			{

				AccountInfoModel accountHolderName = accountInfoService
						.getAccountInfo(items.getMessage().getAccountNumber());

				empParams.put("beneficiaryName",
						accountHolderName.getCustomerName());

			}

			System.out.println("test"
					+ empParams);

			InputStream reportStream = getClass().getResourceAsStream("/RETFull.jasper");

			if (reportStream == null) {

				throw new JRException("Report file not found");

			}

			// Load the compiled report

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

			JasperPrint empReport = new JasperPrint();

			// Fill the report

			empReport = JasperFillManager.fillReport(jasperReport,
					empParams, dataSource.getConnection());

			HttpHeaders headers = new HttpHeaders();

			// set the PDF format

			headers.setContentType(MediaType.APPLICATION_PDF);

			headers.setContentDispositionFormData("filename",

					"Credit_Advice.pdf");

			// create the report in PDF format

			return new ResponseEntity<byte[]>

			(JasperExportManager.exportReportToPdf(empReport),

					headers,
					HttpStatus.OK);

		}

		if (items.getMessage().getBankReference().contains("RETA")

				&& (!items.getMessage().getCurrencyId().equalsIgnoreCase(items.getMessage().getAccountNumberCurrency())

						||
						!items.getMessage().getCurrencyId().equalsIgnoreCase(items.getOtherAccountCurrency()))) {

			System.out.println("Here Ret Cross");

			Map<String, Object> empParams = new HashMap<String, Object>();

			empParams.put("invoice_id",
					items.getMessage().getReference());

			// empParams.put("usedRate",items.getFcyRate());

			String branchName = branchService.requestTransactionBranchCheck(items.getMessage().getDashenBranch())

					.getBRANCH_NAME();

			empParams.put("branchname",
					branchName);

			if (items.getOtherAccountNumber() != null)

			{

				AccountInfoModel accountHolderName = accountInfoService.getAccountInfo(items.getOtherAccountNumber());

				empParams.put("beneficiaryName",
						accountHolderName.getCustomerName());

			}

			else

			{

				AccountInfoModel accountHolderName = accountInfoService
						.getAccountInfo(items.getMessage().getAccountNumber());

				empParams.put("beneficiaryName",
						accountHolderName.getCustomerName());

			}

			System.out.println("test"
					+ empParams);

			InputStream reportStream = null;

			reportStream = getClass().getResourceAsStream("/RETCROSSFull.jasper");

			if (reportStream == null) {

				throw new JRException("Report file not found");

			}

			// Load the compiled report

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

			JasperPrint empReport = new JasperPrint();

			// Fill the report

			empReport = JasperFillManager.fillReport(jasperReport,
					empParams, dataSource.getConnection());

			HttpHeaders headers = new HttpHeaders();

			// set the PDF format

			headers.setContentType(MediaType.APPLICATION_PDF);

			headers.setContentDispositionFormData("filename",

					"Credit_Advice.pdf");

			// create the report in PDF format

			return new ResponseEntity<byte[]>

			(JasperExportManager.exportReportToPdf(empReport),

					headers,
					HttpStatus.OK);

		}

		if (items.getMessage().getBankReference().contains("FINC")

				&& (!items.getMessage().getCurrencyId().equalsIgnoreCase(items.getMessage().getAccountNumberCurrency())

						&& (!items.getMessage().getAccountNumberCurrency().equalsIgnoreCase("ETB")))) {

			System.out.println("Here FINC");

			Map<String, Object> empParams = new HashMap<String, Object>();

			empParams.put("invoice_id",
					items.getMessage().getReference());

			// empParams.put("usedRate",items.getFcyRate());

			String branchName = branchService.requestTransactionBranchCheck(items.getMessage().getDashenBranch())

					.getBRANCH_NAME();

			empParams.put("branchname",
					branchName);

			if (items.getOtherAccountNumber() != null)

			{

				AccountInfoModel accountHolderName = accountInfoService.getAccountInfo(items.getOtherAccountNumber());

				empParams.put("beneficiaryName",
						accountHolderName.getCustomerName());

			}

			else

			{

				AccountInfoModel accountHolderName = accountInfoService
						.getAccountInfo(items.getMessage().getAccountNumber());

				empParams.put("beneficiaryName",
						accountHolderName.getCustomerName());

			}

			System.out.println("test"
					+ empParams);

			InputStream reportStream = getClass().getResourceAsStream("/FINCCROSSFull.jasper");

			if (reportStream == null) {

				throw new JRException("Report file not found");

			}

			// Load the compiled report

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

			JasperPrint empReport = new JasperPrint();

			// Fill the report

			empReport = JasperFillManager.fillReport(jasperReport,
					empParams, dataSource.getConnection());

			HttpHeaders headers = new HttpHeaders();

			// set the PDF format

			headers.setContentType(MediaType.APPLICATION_PDF);

			headers.setContentDispositionFormData("filename",

					"Credit_Advice.pdf");

			// create the report in PDF format

			return new ResponseEntity<byte[]>

			(JasperExportManager.exportReportToPdf(empReport),

					headers,
					HttpStatus.OK);

		}

		if (items.getMessage().getBankReference().contains("FINC")

				&& (items.getMessage().getCurrencyId().equalsIgnoreCase(items.getMessage().getAccountNumberCurrency())

				)) {

			System.out.println("here true");

			Map<String, Object> empParams = new HashMap<String, Object>();

			empParams.put("invoice_id",
					items.getMessage().getReference());

			// empParams.put("usedRate",items.getFcyRate());

			String branchName = branchService.requestTransactionBranchCheck(items.getMessage().getDashenBranch())

					.getBRANCH_NAME();

			empParams.put("branchname",
					branchName);

			if (items.getOtherAccountNumber() != null)

			{

				AccountInfoModel accountHolderName = accountInfoService.getAccountInfo(items.getOtherAccountNumber());

				empParams.put("beneficiaryName",
						accountHolderName.getCustomerName());

			}

			else

			{

				AccountInfoModel accountHolderName = accountInfoService
						.getAccountInfo(items.getMessage().getAccountNumber());

				empParams.put("beneficiaryName",
						accountHolderName.getCustomerName());

			}

			System.out.println("test"
					+ empParams);

			InputStream reportStream = getClass().getResourceAsStream("/FINCLCYFull.jasper");

			if (reportStream == null) {

				throw new JRException("Report file not found");

			}

			// Load the compiled report

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

			JasperPrint empReport = new JasperPrint();

			// Fill the report

			empReport = JasperFillManager.fillReport(jasperReport,
					empParams, dataSource.getConnection());

			HttpHeaders headers = new HttpHeaders();

			// set the PDF format

			headers.setContentType(MediaType.APPLICATION_PDF);

			headers.setContentDispositionFormData("filename",

					"Credit_Advice.pdf");

			// create the report in PDF format

			return new ResponseEntity<byte[]>

			(JasperExportManager.exportReportToPdf(empReport),

					headers,
					HttpStatus.OK);

		}

		else

		{

			System.out.println("here Another test");

			Map<String, Object> empParams = new HashMap<String, Object>();

			empParams.put("invoice_id",
					items.getMessage().getReference());

			// empParams.put("usedRate",items.getFcyRate());

			String branchName = branchService.requestTransactionBranchCheck(items.getMessage().getDashenBranch())

					.getBRANCH_NAME();

			empParams.put("branchname",
					branchName);

			if (items.getOtherAccountNumber() != null)

			{

				AccountInfoModel accountHolderName = accountInfoService.getAccountInfo(items.getOtherAccountNumber());

				empParams.put("beneficiaryName",
						accountHolderName.getCustomerName());

			}

			else

			{

				AccountInfoModel accountHolderName = accountInfoService
						.getAccountInfo(items.getMessage().getAccountNumber());

				empParams.put("beneficiaryName",
						accountHolderName.getCustomerName());

			}

			System.out.println("test"
					+ empParams);

			InputStream reportStream = getClass().getResourceAsStream("/FINCLCYFull.jasper");

			if (reportStream == null) {

				throw new JRException("Report file not found");

			}

			// Load the compiled report

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

			JasperPrint empReport = new JasperPrint();

			// Fill the report

			empReport = JasperFillManager.fillReport(jasperReport,
					empParams, dataSource.getConnection());

			HttpHeaders headers = new HttpHeaders();

			// set the PDF format

			headers.setContentType(MediaType.APPLICATION_PDF);

			headers.setContentDispositionFormData("filename",

					"Credit_Advice.pdf");

			// create the report in PDF format

			return new ResponseEntity<byte[]>

			(JasperExportManager.exportReportToPdf(empReport),

					headers,
					HttpStatus.OK);

		}

		// dynamic parameters required for report

	}

	public Date getFrom() {

		return from;

	}

	public void setFrom(Date from) {

		this.from = from;

	}

	/**
	 * 
	 * @return DataSource return the dataSource
	 * 
	 */

	public DataSource getDataSource() {

		return dataSource;

	}

	/**
	 * 
	 * @param
	 * dataSource        the dataSource to set
	 * 
	 */

	public void setDataSource(DataSource dataSource) {

		this.dataSource = dataSource;

	}

	/**
	 * 
	 * @return SwiftTransaction return the items
	 * 
	 */

	public SwiftTransaction getItems() {

		return items;

	}

	/**
	 * 
	 * @param
	 * items        the items to set
	 * 
	 */

	public void setItems(SwiftTransaction items) {

		this.items = items;

	}

}
