package app.Controllers;

import app.Checkout;
import app.JpaRepository.*;
import app.Entities.*;
import app.Vo.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    private DatasetRepository datasetRepository;

    //C0: Go to checkout
    @GetMapping("/checkout")
    public String showCheckout(@RequestParam int id, @RequestParam String note, Model model, Checkout checkout) {
        User user = userRepository.getById(id);
        float price = 0;

        for(Orders o: orderRepository.findAll()){
            if(o.getUserId() == id && o.getStatus() == 0){
                price += o.getPrice();
            }
        }

        String type;

        if(user.isAdmin() == "TRUE") type = "owner";
        else type = "User";
        checkout.setNote(note);

        model.addAttribute("type", type);
        model.addAttribute("id", id);
        model.addAttribute("price", price);
        model.addAttribute("checkout", checkout);

        float sum = 0;
        List<Integer> ratings = new ArrayList<>();

        for(Review r: reviewRepository.findAll()){
            if(r != null) {
                if (r.getRating() != null) {
                    sum += r.getRating();
                    ratings.add(r.getRating());
                }
            }
        }

        float avg = sum/ratings.size();

        model.addAttribute("rating", avg);

        return "checkout";
    }

    /*@GetMapping("/backtocart")
    public String showBackToCart(Checkout checkout) {
        return "index";
    }*/

    //C1: Fake payment portal
    @PostMapping("/checkout/{id}")
    public String addCheckout(@Valid Checkout checkout, BindingResult result, Model model, @PathVariable Integer id){
        if (result.hasErrors()){
            return "checkout";
        }

        checkout.setUserId(id);

        Checkout newCheckout = checkoutRepository.save(checkout);

        model.addAttribute("id", id);
        model.addAttribute("checkId", newCheckout.getId());

        return "Payment";
    }

    @GetMapping("/index")
    public String showAllCheckout(Model model, Checkout checkout){
        model.addAttribute("checkout", checkoutRepository.findAll());
        return "index";
    }

    //Buying feature for C1
    @PostMapping("/User/{userId}/buy/{checkId}")
    public @ResponseBody String buy(@PathVariable int userId, @PathVariable int checkId){
        List<Orders> orders = new ArrayList<>();

        Checkout checkout = checkoutRepository.getById((long) checkId);
        for(Orders o: orderRepository.findAll()){
            if(o.getUserId() == checkout.getUserId() && o.getStatus() == 0){
                o.setStatus(1);
                orders.add(o);
            }
        }

        float price = 0;

        for(Orders o: orders){
            price += o.getPrice();
        }

        Review r = new Review();

        r.setRating(checkout.getRating());
        r.setReview(checkout.getReview());
        r.setSubmitter(userRepository.getById(userId).getName());

        reviewRepository.save(r);

        HashMap<Integer, Float> map = new HashMap<>();
        map.put(userId, price);

        return "<h3>Success!</h3>" + "<meta http-equiv=\"refresh\" content=\"1; url=/User/" + userId + "\">";
    }
//    @GetMapping("/edit/{id}")
//    public String showUpdateCheckout(@PathVariable("id") long id, Model model) {
//        Checkout checkout = checkoutRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
//
//        model.addAttribute("checkout", checkout);
//        return "update-checkout";
//    }
//
//    @PostMapping("/update/{id}")
//    public String updateUser(@PathVariable("id") long id, @Valid Checkout checkout,
//                             BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            checkout.setId(id);
//            return "update-checkout";
//        }
//
//        checkoutRepository.save(checkout);
//        return "redirect:/payment";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteCheckout(@PathVariable("id") long id, Model model) {
//        Checkout checkout = checkoutRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
//        checkoutRepository.delete(checkout);
//        return "redirect:/index";
//    }



//    @GetMapping("/checkout")
//    public String showCheckout(Model model) {
//        Checkout checkout = new Checkout();
//        model.addAttribute("details", checkout);
//        return "checkout.html";
//    }
//
//    @PostMapping("/checkout")
//    public String submitCheckout(@ModelAttribute("details") Checkout details){
//        System.out.println(details);
//        Map<String, Object> map = new HashMap<>(1);
//
//        if (details.getFname() == null || details.getLname() == null) {
//            return "checkout.html";
//        } else {
//            map.put("data", details);
//        }
//
//        return "payment.html";
//    }
}

