package com.app.dms.Service.impl;

import com.app.dms.Entity.Workspace;
import com.app.dms.Repository.WorkSpaceRepository;
import com.app.dms.Service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkSpaceRepository workSpaceRepository;

    @Override
    public ResponseEntity<Workspace> getWorkspaceById(String id) {

        Optional<Workspace> workspace = workSpaceRepository.findById(id);
        return workspace.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Workspace> createWorkspace(Workspace workspace) {
        if(workSpaceRepository.findByUserIdAndName(workspace.getUserId() ,workspace.getName()).isPresent()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(workSpaceRepository.save(workspace), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Workspace> updateWorkspace(Workspace workspace) {
        return new ResponseEntity(workSpaceRepository.save(workspace), HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteWorkspace(String id) {
        if(workSpaceRepository.findById(id).isPresent()){
            workSpaceRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


}
