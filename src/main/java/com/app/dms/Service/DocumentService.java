package com.app.dms.Service;

import com.app.dms.Entity.Directory;
import com.app.dms.Entity.Document;
import com.app.dms.Repository.DirectoryRepository;
import com.app.dms.Repository.DocumentRepository;
import com.app.dms.advice.DbExceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DirectoryRepository directoryRepository;

    public Document upload(MultipartFile file, String directoryId, String ownerId) throws IOException {
        String directoryPath;
        Document document = new Document();
        Optional<Directory> directory = directoryRepository.findById(directoryId);
        if (directory.isPresent()) {
            document.setName(file.getOriginalFilename());
            document.setType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            document.setDirectoryId(directoryId);
            document.setOwnerId(ownerId);

            directoryPath = directory.get().getPath();
            Path uploadPath = Paths.get(directoryPath);
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            document.setPath(filePath.toString());
            file.transferTo(filePath);

        }else {
            throw new DbExceptions("Error");
        }
        return documentRepository.save(document);
    }

    public ResponseEntity<Resource> download(String documentId) throws IOException {
        Optional<Document> document = documentRepository.findById(documentId);
        if (document.isPresent()) {
            Path path = Paths.get(document.get().getPath());
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                String contentType = Files.probeContentType(path);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : "application/octet-stream")
                        .body(resource);


            }
    }

        return ResponseEntity.notFound().build();
    }
}
