package com.kosuri.stores.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOTPRepository extends JpaRepository<UserOTPEntity, String> {

	Optional<UserOTPEntity> findByEmailOtp(@Valid String emailOtp);

	Optional<UserOTPEntity> findByPhoneOtp(@Valid String phoneOtp);

}
