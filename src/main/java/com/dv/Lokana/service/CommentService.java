package com.dv.Lokana.service;

import com.dv.Lokana.dto.CommentDto;
import com.dv.Lokana.entitys.Comment;
import com.dv.Lokana.entitys.Post;
import com.dv.Lokana.entitys.User;
import com.dv.Lokana.exceptions.PostNotFoundException;
import com.dv.Lokana.repository.CommentRepository;
import com.dv.Lokana.repository.PostRepository;
import com.dv.Lokana.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static final Logger LOG = LoggerFactory.getLogger(CommentService.class);


    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Comment> getAllCommentFromProst(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        return commentRepository.findAllByPost(post);
    }

    public Comment saveComment(CommentDto commentDto, Long postId, Principal principal) {
        User user = findUserByPrincipal(principal);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment comment =  Comment.builder()
                .post(post)
                .message(commentDto.getMessage())
                .username(user.getUsername())
                .userId(user.getId())
                .build();
        post.getComments().add(comment);
        LOG.info("Save comment {}", comment);
        return commentRepository.save(comment);
    }

    public void removeComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User findUserByPrincipal(Principal principal) {
        return userRepository.findUserByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + principal.getName()));
    }
}
