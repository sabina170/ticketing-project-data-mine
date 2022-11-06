package com.cydeo.repository;

import com.cydeo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("SELECT COUNT(task) FROM Task task WHERE task.project.projectCode=?1 AND  task.taskStatus <>  'COMPLETE' ")
    int totalNonCompletedTasks(String projectCode);

    @Query(value="SELECT COUNT(*) FROM tasks t " +
            "JOIN projects p ON t.project_id=p.id " +
            "WHERE p.project_code=?1 AND t.task_status='COMPLETE'", nativeQuery = true)
    int totalCompletedTasks(String projectCode);
}
