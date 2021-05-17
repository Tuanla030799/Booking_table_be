package com.nuce.duantp.sunshine.repository;
import com.nuce.duantp.sunshine.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends JpaRepository<Image,Integer> {
    Image findByName(String name);
}
