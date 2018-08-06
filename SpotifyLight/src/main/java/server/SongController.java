package server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import server.db.SongDB;

@Controller
@RequestMapping("/songs")
public class SongController {
    @RequestMapping
    public String index(Model model) {
        model.addAttribute("songs", SongDB.songs);
        return "songs";
    }
}