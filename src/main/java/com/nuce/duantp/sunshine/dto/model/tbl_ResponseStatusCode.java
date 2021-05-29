package com.nuce.duantp.sunshine.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_response_status_code")
public class tbl_ResponseStatusCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "responseStatusCode", unique = true)
    private String responseStatusCode;

    @Column(name = "responseStatusMessage")
    private String responseStatusMessage;

}
