package com.kosuri.stores.model.response;
import java.text.DecimalFormat;

public class ConfigureLoyaltyPointsResponse extends GenericResponse{
    private Double totalSalesVolume;
    private Double totalDiscount;
    private static final DecimalFormat df = new DecimalFormat("0.00");


    public Double getTotalSalesVolume() {
        return this.totalSalesVolume;
    }

    public Double getTotalDiscount() {
        return this.totalDiscount;
    }

    public void setTotalSalesVolume(Double totalSalesVolume) {
        this.totalSalesVolume =  Double.parseDouble(df.format(totalSalesVolume));
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = Double.parseDouble(df.format(totalDiscount));
    }
}
