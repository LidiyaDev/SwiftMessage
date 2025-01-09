package com.swiftmessage.swiftmessage.dto;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import lombok.Data;

@Entity
@NamedNativeQuery(name = "findAllAccounts", query = "select (count(tbl_incoming_record.dashen_branch) AS Total,tbl_incoming_record.dashen_branch) from swift_transaction INNER JOIN tbl_incoming_record ON swift_transaction.message = tbl_incoming_record.id GROUP BY tbl_incoming_record.dashen_branch")
@SqlResultSetMapping(name = "findAllAccountsMapping", classes = @ConstructorResult(targetClass = TransactionReport.class, columns = {
        @ColumnResult(name = "Total"),
        @ColumnResult(name = "dashen_branch"),
}))
public class TransactionReport {

    @Id
    @Column(name="employee_id")
    private Long employeeId;

    @Column(name="Total")
    private int total;

    @Column(name="dashen_branch")
    private String dashen_branch;

    public TransactionReport(int total, String dashen_branch) {
        this.total = total;
        this.dashen_branch = dashen_branch;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        total = total;
    }

    public String getDashen_branch() {
        return dashen_branch;
    }

    public void setDashen_branch(String dashen_branch) {
        this.dashen_branch = dashen_branch;
    }

}