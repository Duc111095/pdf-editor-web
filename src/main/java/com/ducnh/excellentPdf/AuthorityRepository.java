package com.ducnh.excellentPdf;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ducnh.excellentPdf.model.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Set<Authority> findByUser_UserName(String username);
	
	Authority findByUserId(long user_id);
}
