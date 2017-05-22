package fr.mgargadennec.blossom.ui.web.content.filemanager;

import fr.mgargadennec.blossom.module.filemanager.FileDTO;
import fr.mgargadennec.blossom.module.filemanager.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Maël Gargadennnec on 22/05/2017.
 */
@Controller
@RequestMapping("/files")
public class FileController {
  private final FileService fileService;

  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @GetMapping("/{id}")
  public void serve(@PathVariable("id") Long fileId, HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException {
    FileDTO fileDTO = fileService.getOne(fileId);
    if (fileDTO != null) {
      res.setHeader(HttpHeaders.CONTENT_TYPE, fileDTO.getContentType());
      res.setHeader(HttpHeaders.CONTENT_LENGTH, fileDTO.getSize() + "");
      res.setHeader(HttpHeaders.CONTENT_DISPOSITION, "Content-Disposition: inline; filename=\"" + fileDTO.getName() + "\"");
      StreamUtils.copy(fileService.download(fileId), res.getOutputStream());
      return;
    }

    res.setStatus(HttpStatus.NOT_FOUND.value());
  }
}
