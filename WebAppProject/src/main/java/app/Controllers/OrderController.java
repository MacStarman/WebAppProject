package app.Controllers;

import app.Entities.*;
import app.JpaRepository.*;
import app.Vo.OrderVo;
import app.Vo.PutOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class OrderController {
    @Autowired
    DatasetRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrdersRepository ordersRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    //G4: Remove a selected dataset from the shopping cart
    @RequestMapping(path = "/User/removeFromCart", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String removeFromCart (@RequestParam int id){
        Orders order = ordersRepository.getById(id);
        int userId = order.getUserId();

        ordersRepository.deleteById(id);

        return "<meta http-equiv=\"refresh\" content=\"1; url=/User/" + userId + "/shoppingcart\">";
    }

    //G2: Change the number of data points in the shopping cart
    @GetMapping("/User/addSub")
    @ResponseBody
    public String addSub(@RequestParam int id, @RequestParam int moreOrLess) {
        Orders order = ordersRepository.getById(id);
        Dataset dataset = productRepository.getById(order.getDataset());
        int newCount = order.getCount() + moreOrLess;
        order.setCount(newCount);
        order.setPrice(newCount * (double) dataset.getPricePerDataPoint());
        ordersRepository.save(order);

        int userId = order.getUserId();

        return "<meta http-equiv=\"refresh\" content=\"1; url=/User/" + userId + "/shoppingcart\">";
    }

    @ResponseBody
    @PostMapping("/orders/{id}/{orderId}")
    public String setStatus(@PathVariable("orderId") Integer orderId,
                                         @PathVariable Integer id,
                                         PutOrderVo status) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (status.getOrderStatus() < 0 || status.getOrderStatus() > 2)
                throw new IllegalArgumentException("Invalid status range.");
            if (!ordersRepository.existsById(orderId))
                throw new IllegalArgumentException("Order with ID = " + orderId + "does not exist.");
            ordersRepository.setStatusById(orderId, status.getOrderStatus(), new Timestamp(System.currentTimeMillis()));
            map.put("status", "success");
            map.put("msg", "Order status changed.");
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("msg", e.getMessage());
        }
        return map + "<meta http-equiv=\"refresh\" content=\"1; url=/owner/" + id + "/orders\">";
    }

    @GetMapping("/orders/all")
    @ResponseBody
    public Object showAllOrders(){
        return ordersRepository.findAll();
    }

    @ResponseBody
    @GetMapping("owner/delete/{id}/{userId}")
    public Object deleteOrder(@PathVariable("id") Integer id, @PathVariable int userId) {

        Map<String, Object> map = new HashMap<>();

        try {
            if (!ordersRepository.existsById(id))
                throw new IllegalArgumentException("Order with ID = " + id + " does not exist.");
            ordersRepository.deleteById(id);
            orderDetailRepository.deleteByOrderId(id);
            map.put("status", "success");
            map.put("data", id);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("msg", e.getMessage());
        }

        return map + "<meta http-equiv=\"refresh\" content=\"1; url=/owner/" + userId + "/orders\">";
    }

    // C2: Method to show a customer their orders
    @GetMapping("/User/{id}/orders")
    public Object getCustomerOrder(@PathVariable("id") Integer userId, Model model) {
        Map<String, Object> map = new HashMap<>();
        List<OrderVo> result = new ArrayList<>();
        try {
            if (!userRepository.existsById(userId))
                throw new IllegalArgumentException("User with ID = " + userId + " doesn't exist.");
            for (Orders orders : ordersRepository.findByUserId(userId)) {
                Integer ordersId = orders.getId();
                OrderVo order = new OrderVo();
                order.setOrderNumber(orders.getOrderNumber());
                order.setPrice(String.format("%.2f", orders.getPrice()));
                order.setCreatedAt(orders.getCreatedAt());
                order.setId(orders.getId());
                order.setUpdatedAt(orders.getUpdatedAt());
                order.setDatasetName(orders.getDatasetName());
                if(orders.getStatus() == 1) order.setStatus("Bought");
                else if(orders.getStatus() == 0) order.setStatus("In Cart");
                else order.setStatus("Refunded");
                order.setUserId(orders.getUserId());
                for (OrderDetail detail : orderDetailRepository.findOrderDetailByOrderId(ordersId)) {
                    order.getDetail().add(detail);
                }
                result.add(order);
            }
            map.put("status", "success");
            map.put("data", result);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("msg", e.getMessage());
            //e.printStackTrace();
        }

        User u = userRepository.getById(userId);

        model.addAttribute("id", userId);
        model.addAttribute("name", u.getName());
        model.addAttribute("orders", result);
        return "Orders";
    }

    // O3: Method to show an owner all the orders for their products.
    @GetMapping("/owner/{id}/orders")
    public String getOwnerOrders(@PathVariable("id") Integer ownerId, Model model) {
        /*
            1.  To view all order history

         */
        List<Orders> orders = ordersRepository.findAll();
        List<OrderVo> resultOrders = new ArrayList<>();

        for (Orders o : orders) {
            OrderVo ov = new OrderVo();
            Dataset product = productRepository.getById(o.getDataset());
            if (product != null) {
                if (product.getOwnerId() == (ownerId)) {
                    ov.setId(o.getId());
                    ov.setCreatedAt(o.getCreatedAt());
                    ov.setOrderNumber(o.getOrderNumber());
                    ov.setPrice(String.format("%.2f", o.getPrice()));
                    ov.setDatasetName(product.getName());
                    if (o.getStatus() == 1) ov.setStatus("Bought");
                    else if(o.getStatus() == 0) ov.setStatus("In Cart");
                    else ov.setStatus("Refunded");
                    ov.setUpdatedAt(o.getUpdatedAt());
                    ov.setUserId(o.getUserId());
                    resultOrders.add(ov);
                }
            }
        }

        model.addAttribute("id", ownerId);
        model.addAttribute("status", new PutOrderVo());
        model.addAttribute("size", resultOrders.size());
        model.addAttribute("orders", resultOrders);

        return "OwnerOrders";
    }
}