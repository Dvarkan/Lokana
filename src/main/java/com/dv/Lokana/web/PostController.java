package com.dv.Lokana.web;

import com.dv.Lokana.dto.PostDto;
import com.dv.Lokana.mapper.PostMap;
import com.dv.Lokana.payload.response.MessageResponse;
import com.dv.Lokana.service.PostService;
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
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final PostMap postMap;
    private final ResponseErrorValidation errorValidation;

    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDto>> getAllPostsForUser(Principal principal) {
         var posts = postService.getAllPostsFromUser(principal).stream()
                .map(postMap::map)
                .toList();
         return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        var posts = postService.getAllPosts().stream()
                .map(postMap::map)
                .toList();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto,
                                             BindingResult bindingResult,
                                             Principal principal) {
        var error = errorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(error)) return error;

        var post = postService.createPost(postDto, principal);
        var updatdPost = postMap.map(post);
        return new ResponseEntity<>(updatdPost, HttpStatus.OK);
    }

    @GetMapping("/{id}/{username}/like")
    public ResponseEntity<PostDto> likePost(@PathVariable("id") String id,
                         @PathVariable("username") String username) {

        var post = postService.addAndRemoveLike(Long.getLong(id), username);
        var likedPost = postMap.map(post);
        return new ResponseEntity<>(likedPost, HttpStatus.OK);
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<MessageResponse> removePost(@PathVariable("id") String id,
                                      Principal principal) {

        postService.removePost(Long.parseLong(id), principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK) ;
    }
}
