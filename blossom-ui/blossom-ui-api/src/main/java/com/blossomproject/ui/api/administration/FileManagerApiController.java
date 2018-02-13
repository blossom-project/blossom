package com.blossomproject.ui.api.administration;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.module.filemanager.FileDTO;
import com.blossomproject.module.filemanager.FileService;
import com.blossomproject.ui.stereotype.BlossomApiController;
import java.io.IOException;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
@BlossomApiController
@RequestMapping("/content/filemanager")
public class FileManagerApiController {

  private static final Logger logger = LoggerFactory.getLogger(FileManagerApiController.class);
  private final FileService service;
  private final SearchEngineImpl<FileDTO> searchEngine;

  public FileManagerApiController(FileService service, SearchEngineImpl<FileDTO> searchEngine) {
    this.service = service;
    this.searchEngine = searchEngine;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('content:filemanager:read')")
  public Page<FileDTO> list(
    @RequestParam(value = "q", defaultValue = "", required = false) String q,
    @PageableDefault(size = 20) Pageable pageable) {
    if (Strings.isNullOrEmpty(q)) {
      return this.service.getAll(pageable);
    }
    return this.searchEngine.search(q, pageable).getPage();
  }


  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('content:filemanager:read')")
  public ResponseEntity<FileDTO> get(@PathVariable("id") Long id) {
    Preconditions.checkArgument(id != null);
    FileDTO file = service.getOne(id);
    if (file == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(file, HttpStatus.OK);
    }
  }

  @PostMapping(consumes = "multipart/form-data")
  @PreAuthorize("hasAuthority('content:filemanager:create')")
  public ResponseEntity<FileDTO> fileUpload(
    @RequestParam("file") MultipartFile uploadedFile) {
    Preconditions.checkArgument(uploadedFile != null);
    if (uploadedFile.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    try {
      return new ResponseEntity<>(service.upload(uploadedFile), HttpStatus.CREATED);
    } catch (IOException | SQLException e) {
      logger.error("Cannot save multipart file !", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('content:filemanager:read')")
  public ResponseEntity<InputStreamResource> serve(@PathVariable("id") Long fileId)
    throws SQLException, IOException {
    Preconditions.checkArgument(fileId !=null);
    FileDTO fileDTO = service.getOne(fileId);
    if (fileDTO != null) {
     return ResponseEntity
        .ok()
        .header(HttpHeaders.CONTENT_TYPE, fileDTO.getContentType())
        .header(HttpHeaders.CONTENT_LENGTH, fileDTO.getSize() + "")
        .header(HttpHeaders.CONTENT_DISPOSITION,
          "Content-Disposition: inline; filename=\"" + fileDTO.getName() + "\"")
        .body(new InputStreamResource(service.download(fileId)));
    }

    return ResponseEntity.notFound().build();
  }

}
