package com.app.dms.Service;
import com.app.dms.Entity.Workspace;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface WorkspaceService {

    ResponseEntity<Workspace> getWorkspaceById(String id);
    ResponseEntity<Workspace> createWorkspace(Workspace workspace);
    ResponseEntity<Workspace> updateWorkspace(Workspace workspace);
    ResponseEntity deleteWorkspace(String id);
}

