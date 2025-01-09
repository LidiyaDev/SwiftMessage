package com.swiftmessage.swiftmessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountInfoModel {
    private String branchCode;
    private String branchName;
    private String accountNumber;
    private String customerName;
}
