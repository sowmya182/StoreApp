package com.kosuri.stores.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOTPRepository extends JpaRepository<UserOTPEntity, String> {

	Optional<UserOTPEntity> findByUserEmailAndEmailOtp(String email, String emailOtp);

	Optional<UserOTPEntity> findByUserPhoneNumberAndPhoneOtp(String phoneNumber,String phoneOtp);

    Optional<UserOTPEntity> findByUserEmail(String email);

	Optional<UserOTPEntity> findByUserPhoneNumber(String phoneNumber);
}
