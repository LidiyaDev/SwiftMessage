package com.swiftmessage.swiftmessage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swiftmessage.swiftmessage.dto.TransactionReport;
import com.swiftmessage.swiftmessage.repository.TransactionReportRepository;

@Service
public class TransactionReportService {
    @Autowired
    TransactionReportRepository swiftTransactinJpa1;

    public List<TransactionReport> findAllc() {
		return swiftTransactinJpa1.getAllAccounts();
	}
}