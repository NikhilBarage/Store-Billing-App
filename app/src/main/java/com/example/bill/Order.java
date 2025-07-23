package com.example.bill;

public class Order {
    private int orderid;
    private String ordername;
    private double orderprice;
    private double orderquantity;
    private String orderdate;

    public Order() {
        // Default constructor
    }

    public Order(int orderid, String ordername, double orderprice, double orderquantity, String orderdate) {
        this.orderid = orderid;
        this.ordername = ordername;
        this.orderprice = orderprice;
        this.orderquantity = orderquantity;
        this.orderdate = orderdate;
    }

    //Getters & Setters

    public int getIdd() {
        return orderid;
    }

    public void setIdd(int orderid) {
        this.orderid = orderid;
    }

    public String getNamee() {
        return ordername;
    }

    public void setNamee(String ordername) {
        this.ordername = ordername;
    }

    public double getPricee() {
        return orderprice;
    }

    public void setPricee(double orderprice) {
        this.orderprice = orderprice;
    }

    public double getQuantityy() {
        return orderquantity;
    }

    public void setQuantityy(double orderquantity) { this.orderquantity = orderquantity; }

    public String getDate() {
        return orderdate;
    }

    public void setDate(String orderdate) {
        this.orderdate = orderdate;
    }
}
