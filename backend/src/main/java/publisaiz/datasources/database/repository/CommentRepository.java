/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publisaiz.datasources.database.repository;

import org.springframework.data.repository.CrudRepository;
import publisaiz.datasources.database.entities.Comment;

/**
 * @author michal
 */
public interface CommentRepository extends CrudRepository<Comment, Long> {

}
