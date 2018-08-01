package server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import server.db.MusicDB;

@Controller
public class SongController {

    @RequestMapping("secret")
    public String index(Model model) {
        model.addAttribute("songs", MusicDB.songs);
        return "secret";
    }
}
