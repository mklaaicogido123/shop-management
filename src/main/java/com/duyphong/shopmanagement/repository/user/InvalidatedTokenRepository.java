package com.duyphong.shopmanagement.repository.user;

import com.duyphong.shopmanagement.entity.user.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}
