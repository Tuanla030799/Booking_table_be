package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.tbl_Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends CrudRepository<tbl_Customer, Long> {
    Optional<tbl_Customer> findById(Long id);
    Optional<tbl_Customer> findByEmail(String email);
//    tbl_Customer findByEmail(String email);
    Boolean existsByEmail(String email);
    @Query("select c from tbl_Customer c where c.email=?1")
    tbl_Customer findCustomerByEmail(String email);

    @Query(nativeQuery = true, value = "exec pr_test @text = :text")
    String test(@Param("text") String text);

}

