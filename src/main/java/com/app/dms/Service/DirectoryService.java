package com.app.dms.Service;

import com.app.dms.Entity.Directory;
import com.app.dms.Repository.DirectoryRepository;
import com.app.dms.advice.DbExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class DirectoryService {

    @Autowired
    private DirectoryRepository directoryRepository;

    public Directory createDirectory(String name,String workspaceId, String parentId, String createdBy) {
        Directory directory = new Directory();
        Directory savedDirectory;
        directory.setName(name);
        directory.setCreatedBy(createdBy);

        if (parentId != null) {
            directory.setParentId(parentId);
            Directory parentDirectory = directoryRepository.findByIdOrWorkspaceId(parentId,parentId).get();
            directory.setPath(parentDirectory.getPath() + "\\" + name);
            new File(directory.getPath()).mkdirs();
            savedDirectory = directoryRepository.save(directory);
            parentDirectory.getChildDirectories().add(savedDirectory.getId());
            directoryRepository.save(parentDirectory);
        }
        else{
            directory.setWorkspaceId(workspaceId);
            directory.setPath("C:\\Users\\hassa\\OneDrive\\Desktop\\Db\\" + createdBy + "\\" + name);
            new File(directory.getPath()).mkdirs();
            savedDirectory = directoryRepository.save(directory);
        }

        return savedDirectory;
    }

    public Directory findDirectoryById(String id) {
        return directoryRepository.findById(id).orElseThrow(() -> new DbExceptions("Not Found"));
    }

    public List<Directory> getSubdirectories(String parentId) {
        return directoryRepository.findByParentId(parentId);
    }


}
