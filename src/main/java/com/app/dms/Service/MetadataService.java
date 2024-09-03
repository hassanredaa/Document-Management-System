package com.app.dms.Service;

import com.app.dms.Entity.Metadata;
import com.app.dms.Repository.MetadataRepository;
import com.app.dms.advice.DbExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MetadataService {

    @Autowired
    private MetadataRepository metadataRepository;

    public Metadata createMetadata(Metadata metadata) {
        return metadataRepository.save(metadata);
    }

    public Metadata getMetadata(String metadataid) {
        Optional<Metadata> metadata =  metadataRepository.findById(metadataid);
        if (metadata.isPresent()) {
            return metadata.get();
        }
        else{
            throw new DbExceptions("No metadata found");
        }
    }
}
