package app.Entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private Double price;

    @Column
    private String img;

    @Column
    private String description;

    @Column
    private Integer onSale;

    @Column
    private Integer saleVolume;

    @Column
    private Integer ownerId;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;

}
