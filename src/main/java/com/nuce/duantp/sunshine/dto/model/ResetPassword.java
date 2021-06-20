package com.nuce.duantp.sunshine.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ResetPassword")
@EntityListeners(AuditingEntityListener.class)
public class ResetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "count")
    private int count;

    @Column(name = "status")
    private boolean status;

    public ResetPassword(String email) {
        this.email = email;
        this.count = 5;
        this.status = true;
    }
}
