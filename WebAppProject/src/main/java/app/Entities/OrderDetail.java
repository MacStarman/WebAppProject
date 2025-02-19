package app.Entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class OrderDetail {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column
    private Integer orderId;

    @Column
    private Integer productId;

    @Column
    private Integer qtt;

    @Column
    private Double price;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;

}
