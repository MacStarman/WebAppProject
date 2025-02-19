package app.Entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue //(strategy = GenerationType.IDENTITY)
    private int id;

    public User() {}

    public User(String name, String password, String fname, String lname, String phone, String email) {
        this.username = name;
        this.password = password;
        this.firstname = fname;
        this.surname = lname;
        this.phone = phone;
        this.address = email;
    }

    @Column
    private String username;

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Timestamp getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(Timestamp lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String isAdmin() {
		return isAdmin;
	}

	public void setAdmin(String admin) {
		isAdmin = admin;
	}

	@Column
    private String password;

    @Column
    private String firstname;

    @Column
    private String surname;

    @Column
    private String phone;

	@Column
	private String ownerId;

    @Column
    private String address;

	@Column
	private String isAdmin;

    @Column
    private Timestamp lastLoginAt;

    @Column
    private Timestamp createdAt;

	@Column
    private Timestamp updatedAt;

    public String getName() {
        return username;
    }
}