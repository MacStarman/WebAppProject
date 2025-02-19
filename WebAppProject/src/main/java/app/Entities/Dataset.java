package app.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Dataset {
	@Id
	private int id;
	private int numberOfDataPoints;
	private float pricePerDataPoint;
	private boolean hidden = false;
	private String description;
	private String name;

    @Column
    private Integer ownerId;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Dataset() {}

    public static boolean isEmpty(String title) {
        return true;
    }
	
	public Dataset(int id, int points, float price, boolean hidden, String desc, String name, int ownerId) {
		this.id = id;
		this.numberOfDataPoints = points;
		this.pricePerDataPoint = price;
		this.hidden = hidden;
		this.description = desc;
		this.name = name;
        this.ownerId = ownerId;
	}
	
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfDataPoints() {
        return numberOfDataPoints;
    }

    public void setNumberOfDataPoints(int numberOfDataPoints) {
        this.numberOfDataPoints = numberOfDataPoints;
    }

    public float getPricePerDataPoint() {
        return pricePerDataPoint;
    }

    public void setPricePerDataPoint(float pricePerDataPoint) {
        this.pricePerDataPoint = pricePerDataPoint;
    }

    public boolean isHidden() {
    	return hidden;
    }
    
    public void toggleHidden() {
    	hidden = !hidden;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }

    public Integer getOwnerId() {
        return ownerId;
    }
}
