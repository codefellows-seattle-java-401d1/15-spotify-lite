package server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import server.db.UserDB;
import server.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
@SessionAttributes("username")
public class AuthController {

    //Register a first-time user
    @PostMapping("/register")
    public ModelAndView register(@RequestParam String username, @RequestParam String password, @RequestParam String bio) {
        ModelAndView mv = new ModelAndView();

        if (UserDB.getUserByName(username) != null) {
            mv.setViewName("loginerror");
            mv.addObject("error", "Sorry, that username already exists. Choose another.");
        } else {
            UserDB.createUser(username, password, bio);
            mv.setViewName("loggedin");
            mv.addObject("username", username);
        }
        return mv;
    }

    //Logs in a returning user
    @PostMapping("/login")
    public ModelAndView login(
            HttpServletRequest request,
            @RequestParam String username,
            @RequestParam String password
    ) {
        ModelAndView mv = new ModelAndView();

        User user = UserDB.getUserByName(username);
        if (user == null) {
            mv.setViewName("loginerror");
            mv.addObject("error", "Username not found. Choose another.");
        } else {
            boolean isCorrectPassword = user.checkPassword(password);
            if(isCorrectPassword) {
                mv.setViewName("loggedin");
                mv.addObject("username", username);

                HttpSession session = request.getSession();
                session.setAttribute("loggedin", true);
            } else {
                mv.setViewName("loginerror");
                mv.addObject("error", "Wrong password. Try again.");
            }
        }

        return mv;
    }

    @PostMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        session.setAttribute("loggedin", false);


        //Once a user has been logged out(as opposed to never having visited), the user name will be set to
        //"user" so it will work with the thymleaf template
        String username = (String) session.getAttribute("username");
        boolean isLoggedIn = (boolean) session.getAttribute("loggedin");
        if (!isLoggedIn) {
            username = "user";
            model.addAttribute("username", "user");
        }

        //Once the username is not null (as it would be if the visitor has never visited before)
        //the user name is set. Both the if statement above and this one are necessary for the thymleaf
        //to get the information it needs for proper user info
        if (username != null) {
            model.addAttribute("username", username);
        }

        //I wanted a more meaningful and easily read console log to follow the program through
        System.out.println("From Logout Page: \n"
                + "Session ID: " + session.getId() + " for User " + "\"" + username + "\""
                + "\n" + "Logged In = " + session.getAttribute("loggedin") + "\n");

        return new ModelAndView("loggedout");
    }
}