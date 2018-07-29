package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //Added Model model to access username information and add to thymleaf on html
    @GetMapping("/")
    public String homepage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        //I wanted a more meaningful and easily read console log to follow the program through
        System.out.println("From Homepage: \n"
                + "Session ID: " + session.getId() + " for User " + "\"" + username + "\""
                + "\n" + "Logged In = " + session.getAttribute("loggedin") + "\n");

        //Only change the value of username to "user" if the logged in session is false/null
        if (session.getAttribute("loggedin") == null) {
            model.addAttribute("username", "user");
        }
        //If the session is null, the user name will be set to user. If the session above is not null, then
        //the username will persist from the previous session.
        if (username != null) {
            model.addAttribute("username", username);
        }
        return "index";
    }
}