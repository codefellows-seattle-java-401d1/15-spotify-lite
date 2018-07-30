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
import server.db.UserDB;
import server.models.Music;
import server.storage.FileSystemStorageService;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final FileSystemStorageService storageService;

    @Autowired
    public FileUploadController(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/index")
    public String listUploadedMusic(@RequestParam("username") String username, Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        model.addAttribute(username);
        System.out.println("From List Uploaded Music: \n" + "User Name = " + username);
        return "secret";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Resource file = storageService.loadAsResource(filename);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("artist") String artist,
                                   @RequestParam("song") String song,
                                   Model model, RedirectAttributes redirectAttributes) {


/*
        @RequestParam("username") String username,
        @RequestParam("uploadlocation") String uploadlocation,
*/
        try {

            String filepath = storageService.store(file);
            filepath = filepath.split("public")[1];
            System.out.println("From Handle File Upload: " + filepath);
            System.out.println("From Handle File Upload: " + artist);
            System.out.println("From Handle File Upload: " + song);

            Music songs = new Music();
            songs.artist = artist;
            songs.song = song;
            songs.uploadlocation = filepath;

            UserDB.songs.add(songs);
            
            model.addAttribute("songs", songs);
            model.addAttribute("artist", artist);
            model.addAttribute("song", song);

//            model.addAttribute("uploadlocation", uploadlocation);
//            System.out.println(model.addAttribute("uploadlocation", uploadlocation));



        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}