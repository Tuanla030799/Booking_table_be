package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_Booking;
import com.nuce.duantp.sunshine.dto.model.tbl_Customer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends CrudRepository<tbl_Customer, Long> {
    Optional<tbl_Customer> findById(Long id);

    Optional<tbl_Customer> findByEmail(String email);

    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phone);

    @Query("select c from tbl_Customer c where c.email=?1")

    tbl_Customer findCustomerByEmail(String email);

    tbl_Customer findByPhoneNumber(String phone);
    List<tbl_Customer> findAllByRole(String role);

    Object findAll(Sort colName);

    @Query("SELECT u FROM tbl_Customer u where u.email !='sunshine87lethanhnghi@gmail.com'  order by u.accStatus desc")
    List<tbl_Customer> getAllCustomer();
//    List<tbl_Customer> findAllBy
}

