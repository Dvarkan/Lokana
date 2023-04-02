package com.dv.Lokana.service;

import com.dv.Lokana.dto.PostDto;
import com.dv.Lokana.entitys.Image;
import com.dv.Lokana.entitys.Post;
import com.dv.Lokana.entitys.User;
import com.dv.Lokana.exceptions.PostNotFoundException;
import com.dv.Lokana.repository.ImageRepository;
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
public class PostService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public Post createPost(PostDto postDto, Principal principal) {
        User user = findUserByPrincipal(principal);
        Post post = Post.builder()
                .user(user)
                .caption(postDto.getCaption())
                .location(postDto.getLocation())
                .title(postDto.getTitle())
                .likes(0)
                .build();
        user.getPosts().add(post);
        LOG.info("Create new post for {}", user.getEmail());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public Post getPostsbyId(Long postId, Principal principal) {
        User user = findUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
    }

    public List<Post> getAllPostsFromUser(Principal principal) {
        User user = findUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public void removePost(Long id , Principal principal) {
        User user = findUserByPrincipal(principal);
        Post post = postRepository.findPostByIdAndUser(id, user)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        Optional<Image> image = imageRepository.findByPostId(id);
        image.ifPresent(imageRepository::delete);
        postRepository.delete(post);
    }

    public Post addAndRemoveLike(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        var userLiked = post.getLikedUsers().stream()
                .filter(u -> u.equals(username))
                .findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }
        postRepository.save(post);
        return post;
    }




    private User findUserByPrincipal(Principal principal) {
        return userRepository.findUserByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + principal.getName()));
    }
}
