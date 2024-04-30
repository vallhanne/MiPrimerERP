package org.example;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Customer", schema = "Chinook", catalog = "")
public class CustomerClass {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CustomerId", nullable = false)
    private int customerId;
    @Basic
    @Column(name = "FirstName", nullable = false, length = 40)
    private String firstName;
    @Basic
    @Column(name = "LastName", nullable = false, length = 20)
    private String lastName;
    @Basic
    @Column(name = "Company", nullable = true, length = 80)
    private String company;
    @Basic
    @Column(name = "Address", nullable = true, length = 70)
    private String address;
    @Basic
    @Column(name = "City", nullable = true, length = 40)
    private String city;
    @Basic
    @Column(name = "State", nullable = true, length = 40)
    private String state;
    @Basic
    @Column(name = "Country", nullable = true, length = 40)
    private String country;
    @Basic
    @Column(name = "PostalCode", nullable = true, length = 10)
    private String postalCode;
    @Basic
    @Column(name = "Phone", nullable = true, length = 24)
    private String phone;
    @Basic
    @Column(name = "Fax", nullable = true, length = 24)
    private String fax;
    @Basic
    @Column(name = "Email", nullable = false, length = 60)
    private String email;
    @Basic
    @Column(name = "SupportRepId", nullable = true)
    private Integer supportRepId;
    @Basic
    @Column(name = "EntryDate", nullable = true)
    private Timestamp entryDate;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSupportRepId() {
        return supportRepId;
    }

    public void setSupportRepId(Integer supportRepId) {
        this.supportRepId = supportRepId;
    }
    
    public Timestamp getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Timestamp entryDate) {
        this.entryDate = entryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerClass that = (CustomerClass) o;

        if (customerId != that.customerId) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (company != null ? !company.equals(that.company) : that.company != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (fax != null ? !fax.equals(that.fax) : that.fax != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (supportRepId != null ? !supportRepId.equals(that.supportRepId) : that.supportRepId != null) return false;
        if (entryDate != null ? !entryDate.equals(that.entryDate) : that.entryDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = customerId;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (fax != null ? fax.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (supportRepId != null ? supportRepId.hashCode() : 0);
        result = 31 * result + (entryDate != null ? entryDate.hashCode() : 0);
        return result;
    }
}