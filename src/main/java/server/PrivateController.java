package server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import server.db.MusicDB;
import server.models.Music;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/private")
public class PrivateController {

    @RequestMapping("/*")
    public ModelAndView handlePrivateRequests(HttpServletRequest request, Model model) {
        String servlet = request.getServletPath();
        ModelAndView mv = new ModelAndView();
        HttpSession session = request.getSession();

        boolean isLoggedIn = (boolean) session.getAttribute("loggedin");
        if (isLoggedIn) {
            mv.setViewName("secret");
            String username = (String) session.getAttribute("username");

// ========================= Previous music by username on log in ========================
            mv.addObject("musicObjectQueue", MusicDB.getMusicByUserName(username));
            for (Music music : MusicDB.getMusicByUserName(username)) {
                System.out.println("artist = " + music.artist);
                System.out.println("song = " + music.song);
            }
// ============================= end of previous music logic ============================

        } else {
            //setting username to null if the session is not logged in
            model.addAttribute("username", null);
            mv.setViewName("accessdenied");
        }

        //I wanted a more meaningful and easily read console log to follow the program through
        System.out.println("From Private Controller:"
                +"\n" + "Logged In = " + session.getAttribute("loggedin") + "\n");

        return mv;
    }
}