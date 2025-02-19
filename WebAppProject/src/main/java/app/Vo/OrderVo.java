package app.Vo;

import app.Entities.OrderDetail;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderVo {

    private Integer id;
    private Timestamp createdAt;
    private String orderNumber;
    private String price;
    private String status;
    private Timestamp updatedAt;
    private Integer userId;
    private String datasetName;
    private List<OrderDetail> detail = new ArrayList<>();
}