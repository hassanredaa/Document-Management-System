package com.app.dms.Service;

import com.app.dms.Entity.Directory;
import com.app.dms.Entity.Document;
import com.app.dms.Entity.Metadata;
import com.app.dms.Repository.DirectoryRepository;
import com.app.dms.Repository.DocumentRepository;
import com.app.dms.Repository.MetadataRepository;
import com.app.dms.advice.DbExceptions;
import com.app.dms.advice.UnauthorizedException;
import com.app.dms.response.DocumentPreviewResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private MetadataService metadataService;

    public Document upload(MultipartFile file, String directoryId, String ownerId, String firstname, String lastname) throws IOException {
        String directoryPath;
        Document document = new Document();
        Optional<Directory> directory = directoryRepository.findById(directoryId);

        if (directory.isPresent()) {
            if(!directory.get().getCreatedBy().equals(ownerId)) {
                throw new UnauthorizedException("Not authorized");
            }
            for(String doc : directory.get().getFiles()){
                Optional<Document> docTemp = documentRepository.findById(doc);
                if (docTemp.isPresent() && docTemp.get().getName().equals(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.')))) {
                    throw new UnauthorizedException("Cannot have 2 documents with same filename");
                }
            }
            document.setName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.')));
            document.setDirectoryId(directoryId);
            document.setOwnerId(ownerId);
            document.setDeleted(false);
            directoryPath = directory.get().getPath();
            Path uploadPath = Paths.get(directoryPath);
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            document.setPath(filePath.toString());
            file.transferTo(filePath);
            document = documentRepository.save(document);
            Metadata metadata = new Metadata();
            metadata.setDocumentId(document.getId());
            metadata.setOwnerFirstName(firstname);
            metadata.setOwnerLastName(lastname);
            metadata.setVersion(1);
            metadata.setTag("Document");
            metadata.setDocumentType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            metadataService.createMetadata(metadata);
            directory.get().addFile(document.getId());
            directoryRepository.save(directory.get());
        }else {
            throw new DbExceptions("No directory found");
        }
        return document;
    }

    public ResponseEntity<Resource> download(String documentId, String ownerId) throws IOException {
        Optional<Document> document = documentRepository.findById(documentId);
        if (document.isPresent()) {
            if(!document.get().getOwnerId().equals(ownerId)) {
                throw new UnauthorizedException("Not authorized");
            }
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
        else
        {
            throw new DbExceptions("No document found");
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity softDelete(String documentId,String id)  {
        Optional<Document> document = documentRepository.findById(documentId);
        if (document.isPresent()) {
            if(!document.get().getOwnerId().equals(id)) {
                throw new UnauthorizedException("Not authorized");
            }
            document.get().setDeleted(true);
            documentRepository.save(document.get());
            return ResponseEntity.ok().build();
        }
        else
        {
            throw new DbExceptions("No document found");
        }
    }

    public DocumentPreviewResponse preview(String documentId,String ownerId) throws IOException {
        Optional<Document> document = documentRepository.findById(documentId);
        String base64Encoded = null;
        if (document.isPresent()) {
            if (!document.get().getOwnerId().equals(ownerId)) {
                throw new UnauthorizedException("Not authorized");
            }
            String filePath = document.get().getPath();
            Path path = Paths.get(filePath);
            byte[] fileContent = Files.readAllBytes(path);
            base64Encoded = Base64.getEncoder().encodeToString(fileContent);
        }
        else {
            throw new DbExceptions("No document found");
        }
        return new DocumentPreviewResponse(base64Encoded);
    }

    public List<Document> getWorkspaceDocuments(String workspaceId,String ownerId) {
        Directory directory = directoryRepository.findByWorkspaceId(workspaceId);
        if(directory == null) {
            throw new DbExceptions("No directory found");
        }
        if(!directory.getCreatedBy().equals(ownerId)) {
            throw new UnauthorizedException("Not authorized");
        }
        return getDocuments(directory.getId());
    }

    public List<Document> getDocuments(String directoryId){
        List<Document> documents = new ArrayList<>();
        Optional<Directory> directory = directoryRepository.findById(directoryId);
        if(directory.isPresent()) {
            for (String fileId : directory.get().getFiles()) {
                Optional<Document> documentOpt = documentRepository.findById(fileId);
                if(documentOpt.isPresent() && !documentOpt.get().isDeleted()){
                    documents.add(documentOpt.get());
                }
            }
            for (String childDirectoryId : directory.get().getChildDirectories()) {
                documents.addAll(getDocuments(childDirectoryId));
            }
        }
        return documents;
    }

    public List<Document> search(String ownerId, String name, String type){
        List<Document> documents = documentRepository.findByOwnerId(ownerId);
        List<Document> results = new ArrayList<>();
        log.info(documents.toString());
        if(documents.isEmpty()) {
            return results;
        }
        for(Document document : documents) {
            Metadata metadata = metadataService.getMetadata(document.getId());
            if(type == null && document.getName().toLowerCase().contains(name.toLowerCase()) ){
                log.info("type is null");
                results.add(document);
            } else if (name == null && metadata.getDocumentType().toLowerCase().contains(type.toLowerCase())) {
                results.add(document);
            }
            else if(name!=null && type!=null){
                if(document.getName().toLowerCase().contains(name.toLowerCase()) || metadata.getDocumentType().toLowerCase().contains(type.toLowerCase())){
                    results.add(document);

                }
            }
        }
        return results;
    }
}
