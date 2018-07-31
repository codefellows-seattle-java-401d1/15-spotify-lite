package Server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class Routing {
    @GetMapping("/")
    public String goHome () {
        return "home";
    }

    @GetMapping("/auth/register")
    public String GoRegister(){
        return"register";
    }

    @GetMapping("/auth/login")
    public String GoLogin(){
        return"login";
    }

    @GetMapping("/loggedin")
    public String GoLoggedin(){
        return"loggedin";
    }

    @GetMapping("/loginerror")
    public String GoLoginerror(){
        return"loginerror";
    }

    @GetMapping("/upload")
    public String GoUpload(){
        return"upload";
    }

}
