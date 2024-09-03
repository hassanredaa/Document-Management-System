package com.app.dms.Repository;

import com.app.dms.Entity.Metadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MetadataRepository extends MongoRepository<Metadata, String> {

}
