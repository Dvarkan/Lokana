package com.dv.Lokana.web;

import com.dv.Lokana.dto.CommentDto;
import com.dv.Lokana.mapper.CommentMap;
import com.dv.Lokana.payload.response.MessageResponse;
import com.dv.Lokana.service.CommentService;
import com.dv.Lokana.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;
    private final CommentMap commentMap;
    private final ResponseErrorValidation errorValidation;

    @GetMapping("/{id}/all")
    public ResponseEntity<List<CommentDto>> getAllCommentForPost(@PathVariable("id") String id) {
        var comments = commentService.getAllCommentFromProst(Long.getLong(id)).stream()
                .map(commentMap::map)
                .toList();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/{id}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                                    @PathVariable("id") String postId,
                                                    Principal principal,
                                                    BindingResult bindingResult) {
        var error = errorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(error)) return error;

        var comment = commentService.saveComment(commentDto, Long.parseLong(postId), principal);
        var createdComment = commentMap.map(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("{id}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("id") String id) {
        commentService.removeComment(Long.getLong(id));
        return new ResponseEntity<>(new MessageResponse("Comment deleted"), HttpStatus.OK);
    }
}
