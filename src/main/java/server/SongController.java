package server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import server.db.UserDB;

@Controller
public class SongController {

    @RequestMapping("secret")
    public String index(Model model) {
        model.addAttribute("songs", UserDB.songs);
        return "secret";
    }
}
