package app.Entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class Orders
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Orders() {}

    public Orders(int id, int user, int dataset, int count, double price, Integer status) {
        this.id = id;
        this.userId = user;
        this.dataset = dataset;
        this.count = count;
        this.price = price*count;
        this.status = status;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getDataset() {
		return dataset;
	}

	public void setDataset(int dataset) {
		this.dataset = dataset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public Integer getStatus() {
		return status;
	}

	@Column
    private String orderNumber;

    @Column
    private Integer userId;

    @Column
    private int dataset;

    @Column
    private int fromRow;

    @Column
    private int toRow;

    @Column
    private Integer status;
    
    @Column
    private int count;

    @Column
    private String statusString;

    @Column
    private Double price;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;

	@Column
	private String note;

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	private String datasetName;

    public int getUser() {
        return userId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
