package com.dv.Lokana.repository;

import com.dv.Lokana.entitys.Comment;
import com.dv.Lokana.entitys.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    Comment findByIdAndUserId(Long commentId, Long userId);
}
