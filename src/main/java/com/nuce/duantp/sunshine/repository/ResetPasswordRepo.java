package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.ResetPassword;
import com.nuce.duantp.sunshine.dto.model.tbl_Bill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordRepo extends CrudRepository<ResetPassword, Long> {
       ResetPassword findByEmail(String email);
}

