package app.Controllers;

import app.Entities.User;
import app.JpaRepository.AuthorityRepository;
import app.JpaRepository.UserRepository;
import app.Vo.LoginForm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
@AllArgsConstructor
public class LoginController {

    UserRepository userRepository;
    AuthorityRepository authorityRepository;

    @GetMapping("/d")
    public String index() {
        return "index";
    }

    //G6/O0: Log in to marketplace
    @PostMapping("/login")
    @ResponseBody
    public Object login(@ModelAttribute LoginForm login) {
        Map<String, Object> map = new HashMap<>();
        User user = userRepository.findByUsername(login.getUsername());
        if (user == null || !user.getPassword().equals(login.getPassword())) {
            map.put("IsNull?", user);
            map.put("status", "fail");
            return map + "<meta http-equiv=\"refresh\" content=\"1; url=/loginWrong/\">";
        }
        map.put("status", "success");

        String type;
        if(user.isAdmin().equals("true")) type = "owner";
        else type = "User";

        return map + "<meta http-equiv=\"refresh\" content=\"1; url=/" + type + "/" + user.getId() + "\">";
    }

    //G6/O0: Log in to marketplace
    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("wrong", "");
        model.addAttribute("lf", new app.Vo.LoginForm());
        return "Login";
    }

    //G6/O0: Log in to marketplace, if wrong username/password detected
    @GetMapping("/loginWrong")
    public String wrongLogin(Model model) {
        model.addAttribute("wrong", "Wrong username or password");
        model.addAttribute("lf", new app.Vo.LoginForm());
        return "Login";
    }
}


/**
 * URL: localhost:8080/login
 * METHOD: POST
 * BODY: {
 *     "username": "User's username",
 *     "password": 123
 * }
 *
 * RESPONSE: {
 *     "status": "fail"
 * } OR : {
 *     "status": "success"
 * }
 *
 */

