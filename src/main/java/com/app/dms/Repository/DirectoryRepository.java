package com.app.dms.Repository;

import com.app.dms.Entity.Directory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DirectoryRepository extends MongoRepository<Directory, String> {
    Optional<Directory> findByIdOrWorkspaceId(String id, String workspaceId);
    List<Directory> findByDirectoryParentId(String parentId);
    Directory findByWorkspaceId(String workspaceId);


}
