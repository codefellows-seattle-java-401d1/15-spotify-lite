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
import server.models.Music;
import server.storage.FileSystemStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static server.db.MusicDB.getMusicById;
import static server.db.MusicDB.getMusicByUserName;

@Controller
public class FileUploadController {

    private final FileSystemStorageService storageService;

    @Autowired
    public FileUploadController(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/auth")
    public String listUploadedMusic(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

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
                                   Model model, RedirectAttributes redirectAttributes) {


/*
        @RequestParam("username") String username,
        @RequestParam("artist") String artist,
        @RequestParam("song") String song,
        @RequestParam("uploadlocation") String uploadlocation,
*/
        try {

            String filepath = storageService.store(file);
            filepath = filepath.split("public")[1];
            System.out.println("From Handle File Upload: " + filepath);

//            Music songs = new Music();
//            songs.artist = artist;
//            songs.song = song;
//            songs.uploadlocation = filepath;

//            MusicDB.XXXXX.add(songs);

//            List<Music> music = (List<Music>) getMusicByUserName(username);
//            model.addAttribute("music", music);
//
//            model.addAttribute("artist", artist);
//            model.addAttribute("song", song);
//            model.addAttribute("uploadlocation", uploadlocation);
//            System.out.println(model.addAttribute("artist", artist));
//            System.out.println(model.addAttribute("song", song));
//            System.out.println(model.addAttribute("uploadlocation", uploadlocation));



        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/uploadForm";
    }
}