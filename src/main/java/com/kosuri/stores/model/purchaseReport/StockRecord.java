package com.kosuri.stores.model.purchaseReport;

public class StockRecord {
    private String vendorName;
    private String date;
    private String productType;
    private String storeId;
    private String batchNo;
    private String expiryDate;
    private String mfgDate;
    private Double mrp;
    private Double discount;
    private Double gst;
    private Double purchasePrice;
    private Double qtyInPack;
    private Double qtyInLoose;
    private Double amountAtPurchasePrice;

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(String mfgDate) {
        this.mfgDate = mfgDate;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getGst() {
        return gst;
    }

    public void setGst(Double gst) {
        this.gst = gst;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getQtyInPack() {
        return qtyInPack;
    }

    public void setQtyInPack(Double qtyInPack) {
        this.qtyInPack = qtyInPack;
    }

    public Double getQtyInLoose() {
        return qtyInLoose;
    }

    public void setQtyInLoose(Double qtyInLoose) {
        this.qtyInLoose = qtyInLoose;
    }

    public Double getAmountAtPurchasePrice() {
        return amountAtPurchasePrice;
    }

    public void setAmountAtPurchasePrice(Double amountAtPurchasePrice) {
        this.amountAtPurchasePrice = amountAtPurchasePrice;
    }
}
