package models.entities;

import java.sql.Timestamp;

public class Import {
    public String productid,expirationdate;
    public int quantity,supplierid,unit,employeeid;
    public float price;
    public Timestamp date;

    public Import(){
        productid = "";
        quantity = 0;
        supplierid = 0;
        unit = 0;
        employeeid = 0 ;
        price = 0;
        expirationdate = null;
        date = null;
    }

    public Import(String productid, int quantity, int supplierid, int unit, int employeeid, float price, String expirationdate, Timestamp date) {
        this.productid = productid;
        this.quantity = quantity;
        this.supplierid = supplierid;
        this.unit = unit;
        this.employeeid = employeeid;
        this.price = price;
        this.expirationdate = expirationdate;
        this.date = date;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(int supplierid) {
        this.supplierid = supplierid;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(int employeeid) {
        this.employeeid = employeeid;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getExpirationdate() {
        return expirationdate;
    }

    public void setExpirationdate(String expirationdate) {
        this.expirationdate = expirationdate;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
