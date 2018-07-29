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
import server.storage.FileSystemStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
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
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        try {
            storageService.store(file);

            redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");

//            int words = CountingWords.countingWords(file.getInputStream());
//            int sentences = CountingWords.countingSentences(file.getInputStream());
//            int syllables = CountingWords.countingSyllables(file.getInputStream());

            //specifically cast int division as a double
//            float wordsAndSentences = (float) (0.39*((float)words/sentences));
//            float syllablesAndWords = (float) (11.8*((float) syllables/words)-15.59);
//            float readingLevelCalculations = wordsAndSentences + syllablesAndWords;
//
//            model.addAttribute("message", "Your uploaded file, " + file.getOriginalFilename() + ", has a Reading Level of ...");
//            model.addAttribute("files", file);
//            model.addAttribute("words", words);
//            model.addAttribute("sentences", sentences);
//            model.addAttribute("syllables", syllables);
//            model.addAttribute("readingLevelCalculations", readingLevelCalculations);
//            return "word-count";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}