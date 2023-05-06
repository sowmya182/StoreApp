package com.kosuri.stores.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerLoyaltyRepository extends JpaRepository<CustomerLoyaltyEntity, Integer> {
    @Query("select e from CustomerLoyaltyEntity e where e.customerName = ?1 OR e.customerPhone = ?2 order by e.discountedDate limit 1")
    public Optional<CustomerLoyaltyEntity> findByCustomerNameOrCustomerPhoneAndFirstByOrderByDiscountedDateDsc(String customerName, String customerPhone);

    @Query("select e from CustomerLoyaltyEntity e where e.customerName = ?1 AND e.customerPhone = ?2 order by e.discountedDate limit 1")
    public Optional<CustomerLoyaltyEntity> findByCustomerNameAndCustomerPhoneAndFirstByOrderByDiscountedDateDsc(String customerName, String customerPhone);
}
