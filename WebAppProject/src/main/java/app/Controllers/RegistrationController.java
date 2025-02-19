package app.Controllers;

import app.Entities.User;
import app.JpaRepository.UserRepository;
import app.Vo.RegisterVo;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class RegistrationController {
    UserRepository userRepository;
    
    @GetMapping("/testing")
    @ResponseBody
    public Map<String, Object> testing(){
        Map<String, Object> map = new HashMap();
        
        map.put("nice", 34);
        
        return map;
    }

    //G5: Create customer account - Register API
    @ResponseBody
    @PostMapping(path="/register")
    public Object register(@Valid @ModelAttribute RegisterVo regUser) {
        System.out.println(regUser);

        User user = userRepository.findByUsername(regUser.getUsername());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Map<String, Object> map = new HashMap<>(3);

        if (user == null) {
            user = new User();
            user.setUsername(regUser.getUsername());
            user.setPassword(regUser.getPassword());
            user.setFirstname(regUser.getFirstname());
            user.setSurname(regUser.getSurname());
            user.setPhone(regUser.getPhone());
            user.setAddress(regUser.getAddress());
            user.setAdmin(regUser.getIsAdmin());

            user.setCreatedAt(now);
            user.setUpdatedAt(now);
            user.setLastLoginAt(now);
            userRepository.save(user);

            User saveSuccess = userRepository.findByUsername(user.getUsername());
            if (saveSuccess == null) {
                map.put("status", "fail");
                map.put("msg", "User registration failed.");
            } else {
                map.put("status", "success");
                map.put("data", saveSuccess);
            }
        } else {
            map.put("status", "fail");
            map.put("msg", "The username has been registered.");
        }

        if (map.get("status").equals("success")) {
            User user1 = userRepository.findByUsername(user.getUsername());

            String type = "owner";
            if(user.getIsAdmin().equals("false")) type = "User";

            return map + "<meta http-equiv=\"refresh\" content=\"1; url=/" + type + "/" + user1.getId() + "\">";
        }
        else return map;
    }

    //G5: Create customer account page
    @GetMapping("/register")
    public String inputRegister(Model model) {
        model.addAttribute("rv", new app.Vo.RegisterVo());
        return "CreateCustomerAccount";
    }
}

/**
 * URL: localhost:8080/register
 * METHOD: POST
 * BODY: {
 *     "username" : "user's name";
 *     "password" : 123...;
 *     "firstname" : "user's firstname";
 *     "surname" : "user's surname";
 *     "phone" : "user's phone number";
 *     "address" "user's address";
 *
 * }
 *
 * RESPONSE: {
 *     "status": "fail"
 *     "msg": "User registration failed."
 * } OR : {
 *     "status": "success"
 *     "data", "saveSuccess"
 * } OR : {
 *      "status": "fail"
 *  *    "msg": "The username has been registered."
 *
 */