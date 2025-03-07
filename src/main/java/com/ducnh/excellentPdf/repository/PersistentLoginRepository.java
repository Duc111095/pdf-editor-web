package com.ducnh.excellentPdf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ducnh.excellentPdf.model.PersistentLogin;

@Repository
public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, String> {
	void deleteByUsername(String username);

}
