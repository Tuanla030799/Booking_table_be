package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.tbl_News;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepo extends CrudRepository<tbl_News, Long> {
        List<tbl_News> findByNewsStatus(int status);

        tbl_News findByNewsId(Long id);
}

