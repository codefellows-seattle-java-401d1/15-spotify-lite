package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import server.db.MusicDB;
import server.db.UserDB;
import server.models.Music;
import server.storage.FileSystemStorageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final FileSystemStorageService storageService;

    @Autowired
    public FileUploadController(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }


    //This isn't being hit at all and I cannot figure out what needs to be called.
    @GetMapping("/index")
    public String listUploadedMusic(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        System.out.println("From List Uploaded Music: \n" + "User Name = ");
        return "secret";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Resource file = storageService.loadAsResource(filename);
            System.out.println("server file function");
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
//                                   @RequestParam("username") String username,
                                   @RequestParam("artist") String artist,
                                   @RequestParam("song") String song,
                                   Model model, RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) {


/*
        @RequestParam("username") String username,
        @RequestParam("uploadlocation") String uploadlocation,
*/
        try {
            HttpSession session = request.getSession();
            String username = (String) session.getAttribute("username");
            String filepath = storageService.store(file);
            filepath = filepath.split("public")[1];
            System.out.println("From Handle File Upload: " + filepath);
            System.out.println("From Handle File Upload: " + artist);
            System.out.println("From Handle File Upload: " + song);

            Music mp3 = new Music();
            mp3.username = username;
            mp3.artist = artist;
            mp3.song = song;
            mp3.uploadlocation = filepath;

            MusicDB.createMusic(username, artist, song, filepath);
            MusicDB.songs.add(mp3);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/secret";
    }
}