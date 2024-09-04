package com.app.dms.Service;

import com.app.dms.Entity.Workspace;
import com.app.dms.Repository.WorkSpaceRepository;
import com.app.dms.Requests.DirectoryRequest;
import com.app.dms.advice.DbExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkspaceService {

    @Autowired
    private WorkSpaceRepository workSpaceRepository;

    @Autowired
    private DirectoryService directoryService;


    public Workspace getWorkspaceById(String id) {

        Optional<Workspace> workspace = workSpaceRepository.findById(id);
        if (workspace.isEmpty()) {
            throw new DbExceptions("Workspace not found");
        }
        return workspace.get();
    }

    public ResponseEntity<List<Workspace>> getAllWorkspaces(String id) {
        return new ResponseEntity<>(workSpaceRepository.findAllByUserId(id), HttpStatus.OK);
    }

    public ResponseEntity<Workspace> createWorkspace(String name, String id) {
        if(workSpaceRepository.findByUserIdAndName(id ,name.toLowerCase()).isPresent()){
                throw new DbExceptions("Workspace name already exists");
        }
        Workspace workspace = new Workspace(id, name.toLowerCase());
        workspace = workSpaceRepository.save(workspace);
        DirectoryRequest directoryRequest = new DirectoryRequest(name,workspace.getId(),null);
        directoryService.createDirectory(directoryRequest,id);
        return new ResponseEntity<>(workspace, HttpStatus.CREATED);
    }

    public ResponseEntity<Workspace> updateWorkspace(String name,String workspace_id ,String user_id) {
        Optional<Workspace> workspace = workSpaceRepository.findById(workspace_id);
        if (workspace.isEmpty()) {
            throw new DbExceptions("Workspace not found");
        }
        if(workspace.get().getUserId().equals(user_id)){
            workspace.get().setName(name);
            return new ResponseEntity(workSpaceRepository.save(workspace.get()), HttpStatus.OK);
        }
        else {
            throw new DbExceptions("Workspace doesn't belong to user");
        }
    }

    public ResponseEntity deleteWorkspace(String id ,String user_id) {
        if(workSpaceRepository.findByIdAndUserId(id,user_id).isPresent()){
            workSpaceRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        throw new DbExceptions("Workspace not found");
    }


}
