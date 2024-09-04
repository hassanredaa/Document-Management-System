package com.app.dms.Controller;


import com.app.dms.Entity.Directory;
import com.app.dms.Requests.DirectoryRequest;
import com.app.dms.Service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${app.config.directory.base-uri}")
public class DirectoryController {

    @Autowired
    private DirectoryService directoryService;

    @PostMapping("${app.config.directory.api.create-directory}")
    public ResponseEntity<Directory> createDirectory(@RequestBody DirectoryRequest directoryRequest, Authentication authentication) {
        Directory newDirectory = directoryService.createDirectory(directoryRequest, (String) authentication.getPrincipal());
        return new ResponseEntity<Directory>(newDirectory,HttpStatus.CREATED);
    }

    @GetMapping("${app.config.directory.api.get-directory}")
    public ResponseEntity<Directory> getDirectory(@PathVariable String id) {
        return new ResponseEntity<Directory>(directoryService.findDirectoryById(id),HttpStatus.CREATED);
    }

    @GetMapping("${app.config.directory.api.get-subdirectories}")
    public ResponseEntity<List<Directory>> getSubdirectories(@PathVariable String parentId) {
        List<Directory> subdirectories = directoryService.getSubdirectories(parentId);
        return new ResponseEntity<List<Directory>>(subdirectories,HttpStatus.OK);
    }


}
