package com.app.dms.Service;

import com.app.dms.Entity.Directory;
import com.app.dms.Repository.DirectoryRepository;
import com.app.dms.Requests.DirectoryRequest;
import com.app.dms.advice.DbExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class DirectoryService {

    @Autowired
    private DirectoryRepository directoryRepository;

    @Value("${database.storage}")
    private String dbPath;

    public Directory createDirectory(DirectoryRequest directoryRequest, String createdBy) {
        Directory directory = new Directory();
        Directory savedDirectory;
        directory.setName(directoryRequest.name);
        directory.setCreatedBy(createdBy);

        if (directoryRequest.parentId != null) {
            directory.setDirectoryParentId(directoryRequest.parentId);
            Directory parentDirectory = directoryRepository.findByIdOrWorkspaceId(directoryRequest.parentId,directoryRequest.parentId).get();
            directory.setPath(parentDirectory.getPath() + "\\" + directoryRequest.name);
            new File(directory.getPath()).mkdirs();
            savedDirectory = directoryRepository.save(directory);
            parentDirectory.getChildDirectories().add(savedDirectory.getId());
            directoryRepository.save(parentDirectory);
        }
        else{
            directory.setWorkspaceId(directoryRequest.workspaceId);
            directory.setPath(createdBy + "\\" + directoryRequest.name);
            new File(dbPath + createdBy + "\\" + directoryRequest.name).mkdirs();
            savedDirectory = directoryRepository.save(directory);
        }

        return savedDirectory;
    }

    public Directory findDirectorybyWorkspaceId(String id) {
        return directoryRepository.findByWorkspaceId(id);
    }

    public Directory findDirectoryById(String id) {
        return directoryRepository.findById(id).orElseThrow(() -> new DbExceptions("Not Found"));
    }

    public List<Directory> getSubdirectories(String parentId) {
        return directoryRepository.findByDirectoryParentId(parentId);
    }


}
