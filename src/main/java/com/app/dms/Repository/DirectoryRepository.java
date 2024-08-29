package com.app.dms.Repository;

import com.app.dms.Entity.Directory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DirectoryRepository extends MongoRepository<Directory, String> {
    Optional<Directory> findByNameAndParentId(String name, String parentId);
    Optional<Directory> findByIdOrWorkspaceId(String id, String workspaceId);
    List<Directory> findByParentId(String parentId);


}
