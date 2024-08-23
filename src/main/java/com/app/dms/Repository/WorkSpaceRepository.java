package com.app.dms.Repository;

import com.app.dms.Entity.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;



public interface WorkSpaceRepository extends MongoRepository<Workspace,String> {

    void delete(Workspace workspace);
    Optional<Workspace> findById(String s);
    Optional<Workspace> findByUserIdAndName(String userid, String name);
}

