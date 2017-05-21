package fr.mgargadennec.blossom.ui.web.content.filemanager;

import fr.mgargadennec.blossom.module.filemanager.FileService;
import fr.mgargadennec.blossom.module.filemanager.store.Folder;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
@BlossomController("/content/filemanager")
public class FileManagerController {
    private static final Logger logger = LoggerFactory.getLogger(FileManagerController.class);

    private final FileService fileService;

    public FileManagerController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public ModelAndView getFilesPage(Model model) {
        model.addAttribute("folder", fileService.folderTree());
        return new ModelAndView("content/filemanager/filemanager", model.asMap());
    }

    @GetMapping("/files")
    public ModelAndView files(Model model, @RequestParam(value = "path", required = false) Optional<String> path) {
        Folder folder = fileService.folder(path.orElse(""));
        if (folder != null) {
            model.addAttribute("folder", folder);
            model.addAttribute("files", fileService.getAll(folder));
            return new ModelAndView("content/filemanager/filelist", model.asMap());
        }
        return null;
    }

}
