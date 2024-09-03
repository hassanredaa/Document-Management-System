package com.app.dms.Controller;

import com.app.dms.Entity.Metadata;
import com.app.dms.Service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.config.metadata.base-uri}")
public class MetadataController {

    @Autowired
    private MetadataService metadataService;

    @GetMapping("${app.config.metadata.api.get-metadata}")
    public ResponseEntity<Metadata> getMetadata(@PathVariable String metadataId) {
        return new ResponseEntity<Metadata>(metadataService.getMetadata(metadataId) , HttpStatus.OK);
    }
}
