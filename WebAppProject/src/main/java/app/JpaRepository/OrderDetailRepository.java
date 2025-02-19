package app.JpaRepository;

import app.Entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Transactional
    @Modifying
    @Query("delete from OrderDetail where orderId = :variable")
    void deleteByOrderId(@Param("variable") Integer orderId);

    @Query("select o from OrderDetail o where o.orderId = :variable")
    List<OrderDetail> findOrderDetailByOrderId(@Param("variable") Integer orderId);

    @Query("select o from OrderDetail o where o.productId = :ownerId order by o.orderId")
    List<OrderDetail> findByOwnerId(@Param("ownerId") Integer ownerId);

}
