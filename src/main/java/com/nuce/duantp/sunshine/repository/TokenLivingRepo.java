package com.nuce.duantp.sunshine.repository;

import com.nuce.duantp.sunshine.dto.model.TokenLiving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TokenLivingRepo extends JpaRepository<TokenLiving,Long> {
    TokenLiving findByEmail(String email);
    TokenLiving findByToken(String token);

}
