package com.app.dms.Repository;

import com.app.dms.Entity.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentRepository extends MongoRepository<Document, String> {
    List<Document> findByOwnerId(String ownerId);
    List<Document> findByName(String name);
}
