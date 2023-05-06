package com.kosuri.stores.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerLoyaltyResponse extends GenericResponse {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Double totalSalesVolume;

    private Integer loyaltyPoints;
    private Double discountEligible;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getTotalSalesVolume() {
        return totalSalesVolume;
    }

    public void setTotalSalesVolume(Double totalSalesVolume) {
        this.totalSalesVolume = totalSalesVolume;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public Double getDiscountEligible() {
        return discountEligible;
    }

    public void setDiscountEligible(Double discountEligible) {
        this.discountEligible = discountEligible;
    }
}
