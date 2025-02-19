package app;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;


@Entity
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //    @NotBlank(message = "Cannot be left blank")
    private String fname;
    //    @NotBlank(message = "Cannot be left blank")
    private String lname;
    private String company;
    //    @NotBlank(message = "Cannot be left blank")
    private String address;
    //    @NotBlank(message = "Cannot be left blank")
    private String city;
    //    @NotBlank(message = "Cannot be left blank")
    private String country;
    private int phone;

    private String note;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    private Integer userId;

    //Something extra
    private String discount;
    private boolean expressShipping;
    private int rating;
    private String review;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Checkout() {}

    public Checkout( String fname,
                     String lname,
                     String company,
                     String address,
                     String city,
                     String country,
                     int phone,
                     String discount,
                     boolean expressShipping) {
        this.fname = fname;
        this.lname = lname;
        this.company = company;
        this.address = address;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.discount = discount;
        this.expressShipping = expressShipping;
    }

    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public int getPhone() {
        return phone;
    }
    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getDiscount() {
        return discount;
    }
    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public boolean isExpressShipping() {
        return expressShipping;
    }
    public void setExpressShipping(boolean expressShipping) {
        this.expressShipping = expressShipping;
    }

    public String getReview() {return review;}
    public void setReview(String review) { this.review = review;}

    public int getRating() {return rating;}
    public void setRating(int rating) {this.rating = rating;}

    public String getNote() {return note;}
    public void setNote(String note) {this.note = note;}

    @Override
    public String toString() {
        return "Checkout{" +
                "Firstname " + fname + '\'' +
                ", Lastname " + lname + '\'' +
                ", Company " + company + '\'' +
                ", Address " + address + '\'' +
                ", City " + city + '\'' +
                ", Country " + country + '\'' +
                ", Phone " + phone +
                ", Discount " + discount + '\'' +
                ", Express Shipping " + expressShipping +
                ", Rating " + rating + '\'' + ", Review '" + review +
                "'}";
    }
}