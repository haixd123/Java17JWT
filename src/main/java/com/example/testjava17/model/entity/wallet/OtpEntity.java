package com.example.testjava17.model.entity.wallet;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "OTP")
public class OtpEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_OTP")
    @SequenceGenerator(sequenceName = "S_OTP", allocationSize = 1, name = "S_OTP")
    @Id

    private Long id;
    @Column(name = "WALLET_USER_ID")
    private Long walletUserId;
    @Column(name = "MSISDN")
    private String msisdn;
    @Column(name = "ADDITIONAL_INFO")
    private String additionalInfo;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "TYPE")
    private Long type;
    @Column(name = "CREATE_DATE")
    private Date createDate;
    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "WALLET_USER_TEMP_ID")
    private Long walletUserTempId;
    @Column(name = "TRANSACTION_ID")
    private Long transactionId;
    @Column(name = "COUNT")
    private Long count;
}
