package com.example.tarek.inventoreyapp.database.product;

public class Product {


    private final String productName;
    private final float productPrice;
    private final int productQuantity;
    private final String supplierName;
    private final String supplierPhone;

    public Product(String productName ,float productPrice ,int productQuantity ,String supplierName ,String supplierPhone ){
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.supplierName = supplierName;
        this.supplierPhone = supplierPhone;
    }

    public String getProductName() {
        return productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public float getProductPrice() {
        return productPrice;
    }

}
