package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.model.TokenLiving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenLiveRepo extends JpaRepository<TokenLiving,Long> {
    TokenLiving findByEmail(String email);

}
