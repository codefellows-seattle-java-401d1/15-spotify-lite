package Server;

import Server.models.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import Server.db.UserDB;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/auth")
@SessionAttributes("username")
public class Login {
    @PostMapping("/register")
    public ModelAndView register(@RequestParam String username, @RequestParam String password, @RequestParam String bio) {
        System.out.println("hit the /register route");
        ModelAndView mv = new ModelAndView();
//        if (UserDB.getUserByName(username) != null) {
//            mv.setViewName("loginerror");
//            mv.addObject("error", "Sorry, that username already exists. Choose another.");
//        } else {
//            UserDB.createUser(username, password);
//            mv.setViewName("loggedin");
//            mv.addObject("username", username);
//        }
        return mv;
    }

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
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("loggedin", false);
        return new ModelAndView("loggedout");
    }

}
