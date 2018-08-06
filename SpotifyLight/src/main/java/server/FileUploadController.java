package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import server.db.SongDB;
import server.models.Song;
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

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
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
    public String handleFileUpload(

            @RequestParam("artist") String artist,
            @RequestParam("song") String song,
            @RequestParam("file") MultipartFile file)
//            RedirectAttributes redirectAttributes,
//            HttpServletRequest request)
    {
//
//        ModelAndView mv = new ModelAndView();
        try {
            String filepath = storageService.store(file);
            filepath = filepath.split("static")[1];
            System.out.println(filepath);

            Song mp3 = new Song();
            mp3.artist = artist;
            mp3.title = song;
            mp3.path = filepath;

            SongDB.songs.add(mp3);
        } catch (IOException e) {

        }
        return "redirect:/songs";
    }
}
