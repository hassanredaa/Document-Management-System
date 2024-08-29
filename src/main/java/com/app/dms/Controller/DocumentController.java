package com.app.dms.Controller;


import com.app.dms.Requests.DocumentRequest;
import com.app.dms.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("${app.config.document.base-uri}")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping(value = "" , consumes = "multipart/form-data")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("directory") String directoryId, Authentication auth) throws IOException {
        return new ResponseEntity(documentService.upload(file, directoryId, (String) auth.getPrincipal()), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Resource> downloadFile(@RequestBody DocumentRequest request) throws IOException {
        return documentService.download(request.documentId);

    }
}
