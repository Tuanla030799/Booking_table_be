package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_ResponseStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseStatusCodeRepo extends JpaRepository<tbl_ResponseStatusCode,Long > {
    tbl_ResponseStatusCode findByResponseStatusCode(String code);

}
