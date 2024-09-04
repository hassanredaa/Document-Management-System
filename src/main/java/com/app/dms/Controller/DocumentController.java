package com.app.dms.Controller;

import com.app.dms.Entity.Document;
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
import java.util.List;

@RestController
@RequestMapping("${app.config.document.base-uri}")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping(value = "" , consumes = "multipart/form-data")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file,
                                     @RequestParam("directory") String directoryId, Authentication auth) throws IOException {
        return new ResponseEntity(documentService.upload(file, auth ,directoryId), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Resource> downloadFile(@RequestBody DocumentRequest request, Authentication auth) throws IOException {
        return documentService.download(request.documentId,(String) auth.getPrincipal());
    }

    @PutMapping("${app.config.document.api.soft-delete}")
    public ResponseEntity deleteDocument(@RequestParam("documentId") String documentId, Authentication auth) {
        return documentService.softDelete(documentId, (String) auth.getPrincipal());
    }

    @GetMapping("${app.config.document.api.preview}")
    public ResponseEntity previewDocument(@PathVariable String id, Authentication auth) throws IOException {
        return new ResponseEntity<>(documentService.preview(id,(String) auth.getPrincipal()),HttpStatus.OK);
    }

    @GetMapping("${app.config.document.api.workspace-documents}")
    public ResponseEntity<List<Document>> getworkspacedocuments(@PathVariable String workspaceId, Authentication auth) {
        return new ResponseEntity<List<Document>>(documentService.getWorkspaceDocuments(workspaceId, (String) auth.getPrincipal()), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Document>> searchDocument(@RequestParam("search") String searchTerm, Authentication auth) {
        return new ResponseEntity<>(documentService.search((String) auth.getPrincipal(),searchTerm), HttpStatus.OK);
    }



}
