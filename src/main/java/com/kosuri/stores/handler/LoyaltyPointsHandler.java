package com.kosuri.stores.handler;

import com.kosuri.stores.dao.*;
import com.kosuri.stores.exception.APIException;
import com.kosuri.stores.model.request.ConfigureLoyaltyPointsRequest;
import com.kosuri.stores.model.request.CustomerLoyaltyRequest;
import com.kosuri.stores.model.request.RedeemLoyaltyPointsRequest;
import com.kosuri.stores.model.response.ConfigureLoyaltyPointsResponse;
import com.kosuri.stores.model.response.CustomerLoyaltyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Service
public class LoyaltyPointsHandler {

    @Autowired
    LoyaltyRepository loyaltyRepository;

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    CustomerLoyaltyRepository customerLoyaltyRepository;

    public ConfigureLoyaltyPointsResponse configureLoyaltyPoints(ConfigureLoyaltyPointsRequest request) throws Exception {
        LoyaltyEntity loyaltyEntity = new LoyaltyEntity();
        ConfigureLoyaltyPointsResponse response = new ConfigureLoyaltyPointsResponse();

        loyaltyEntity.setStoreId(request.getStoreId());
        loyaltyEntity.setSalesVolume(request.getSalesVolume());
        loyaltyEntity.setLoyaltyPoints(request.getLoyaltyPoints());
        loyaltyEntity.setFixedDiscountPercentage(request.getFixedDiscountPercentage());
        loyaltyEntity.setMinLoyaltyPoints(request.getMinLoyaltyPoints());

        Double totalSalesVolume = request.getMinLoyaltyPoints()*(request.getSalesVolume()/request.getLoyaltyPoints());
        Double totalDiscountPercentage = request.getFixedDiscountPercentage() * request.getMinLoyaltyPoints()/100D;
        Double totalDiscount = (totalSalesVolume * totalDiscountPercentage)/100;
        loyaltyEntity.setTotalSalesVolume(totalSalesVolume);

        loyaltyRepository.save(loyaltyEntity);

        response.setTotalSalesVolume(totalSalesVolume);
        response.setTotalDiscount(totalDiscount);

        return response;
    }

    public void redeemLoyaltyPointsForCustomer(RedeemLoyaltyPointsRequest request) throws Exception {
        CustomerLoyaltyEntity customerLoyaltyEntity = new CustomerLoyaltyEntity();
        if ((request.getFirstName() == null || request.getFirstName().isEmpty())
                && (request.getLastName() == null || request.getLastName().isEmpty())
                && (request.getCustomerPhone() == null || request.getCustomerPhone().isEmpty())) {
            throw new APIException("Please input customer name or phone no");
        }


        String name = null;
        if (request.getFirstName() == null && request.getLastName() != null) {
            name = request.getLastName().trim();
        } else if (request.getLastName() == null && request.getFirstName() != null) {
            name = request.getFirstName().trim();
        } else if (request.getLastName() != null && request.getFirstName() != null){
            name = request.getFirstName().trim() + " " + request.getLastName().trim();
        }

        customerLoyaltyEntity.setLoyaltyPoints(request.getLoyaltyPoints());
        customerLoyaltyEntity.setCustomerName(name);
        customerLoyaltyEntity.setCustomerPhone(request.getCustomerPhone());
        customerLoyaltyEntity.setTotalSales(request.getTotalSales());
        customerLoyaltyEntity.setDiscountAmount(request.getDiscountAmount());
        customerLoyaltyEntity.setDiscountedDate(request.getDateOfDiscount());
        customerLoyaltyEntity.setStoreId(request.getStoreId());

        customerLoyaltyRepository.save(customerLoyaltyEntity);
    }

    public CustomerLoyaltyResponse getDiscountForCustomer(CustomerLoyaltyRequest request) throws Exception {
        String name = null;

        if ((request.getFirstName() == null || request.getFirstName().isEmpty())
                && (request.getLastName() == null || request.getLastName().isEmpty())
                && (request.getCustomerPhone() == null || request.getCustomerPhone().isEmpty())) {
            throw new APIException("Please input customer name or phone no");
        }

        if (request.getFirstName() == null && request.getLastName() != null) {
            name = request.getLastName().trim();
        } else if (request.getLastName() == null && request.getFirstName() != null) {
            name = request.getFirstName().trim();
        } else if (request.getLastName() != null && request.getFirstName() != null){
            name = request.getFirstName().trim() + " " + request.getLastName().trim();
        }

        if(name.trim().isEmpty()){
            name = null;
        }

        if (request.getCustomerPhone().isEmpty()) {
            request.setCustomerPhone(null);
        }

        Optional<CustomerLoyaltyEntity> customerLoyaltyEntityOptional;
        if(request.getCustomerPhone() != null &&  name != null) {
            customerLoyaltyEntityOptional = customerLoyaltyRepository.findByCustomerNameAndCustomerPhoneAndFirstByOrderByDiscountedDateDsc(name, request.getCustomerPhone());
        }else{
            customerLoyaltyEntityOptional = customerLoyaltyRepository.findByCustomerNameOrCustomerPhoneAndFirstByOrderByDiscountedDateDsc(name, request.getCustomerPhone());
        }

        Date lastDiscountedDate;

        if (customerLoyaltyEntityOptional.isPresent()) {
            lastDiscountedDate = customerLoyaltyEntityOptional.get().getDiscountedDate();
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            String dateInString = "1-Jan-1800";
            lastDiscountedDate = formatter.parse(dateInString);
        }

        CustomerLoyaltyResponse response = new CustomerLoyaltyResponse();
        response.setFirstName(request.getFirstName());
        response.setLastName(request.getLastName());
        response.setPhoneNumber(request.getCustomerPhone());

        Double totalSaleAfterDate;
        if(request.getCustomerPhone() != null &&  name != null) {
            totalSaleAfterDate = saleRepository.findTotalSalesForCustomerPhoneAndNameAfterDate(request.getCustomerPhone(), name, lastDiscountedDate);
        }else{
            totalSaleAfterDate = saleRepository.findTotalSalesForCustomerPhoneOrNameAfterDate(request.getCustomerPhone(), name, lastDiscountedDate);
        }

        if (totalSaleAfterDate == null) {
            totalSaleAfterDate = 0D;
        }

        Optional<LoyaltyEntity> storeLoyaltyOptional = loyaltyRepository.findById(request.getStoreId());
        if (storeLoyaltyOptional.isEmpty()) {
            throw new APIException("No loyalty points configured for this store");
        }

        LoyaltyEntity storeLoyalty = storeLoyaltyOptional.get();

        Long pointsEarned = Math.round((totalSaleAfterDate / storeLoyalty.getSalesVolume()) * storeLoyalty.getLoyaltyPoints());
        Double totalDiscPercentage = 0D;
        Double totalDiscountAmount = 0D;

        if (pointsEarned > storeLoyalty.getMinLoyaltyPoints()) {
            totalDiscPercentage = storeLoyalty.getFixedDiscountPercentage() * (pointsEarned / 100);
            totalDiscountAmount = (totalDiscPercentage * totalSaleAfterDate)/100;
        }

        response.setLoyaltyPoints(pointsEarned.intValue());
        response.setDiscountEligible(totalDiscountAmount);
        response.setTotalSalesVolume(totalSaleAfterDate);


        return response;
    }
}
