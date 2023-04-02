package com.dv.Lokana.service;

import com.dv.Lokana.entitys.Image;
import com.dv.Lokana.entitys.Post;
import com.dv.Lokana.entitys.User;
import com.dv.Lokana.exceptions.PostNotFoundException;
import com.dv.Lokana.repository.ImageRepository;
import com.dv.Lokana.repository.PostRepository;
import com.dv.Lokana.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Value("${app.image.bucket:/home/dvarkan/program/project/Lokana/image}")
    private String bucket;

    @SneakyThrows
    public void uploadImageToUser(MultipartFile file, Principal principal) {
        Path fullImagePath = Path.of(bucket, file.getOriginalFilename());
        User user = findUserByPrincipal(principal);
        Image userImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if(!ObjectUtils.isEmpty(userImage)) {
            imageRepository.delete(userImage);
        }

        Image image = Image.builder()
                .userId(user.getId())
                .imagePath(fullImagePath.toString())
                .name(file.getOriginalFilename())
                .build();
        var content = file.getInputStream();
        try(content) {
            Files.createDirectories(fullImagePath.getParent());
            Files.write(fullImagePath, content.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
        imageRepository.save(image);
    }

    @SneakyThrows
    public void uploadImageToPost(MultipartFile file, Long postId) {
        Path fullImagePath = Path.of(bucket, file.getOriginalFilename());
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        Image image = Image.builder()
                .postId(post.getId())
                .name(file.getOriginalFilename())
                .imagePath(fullImagePath.toString())
                .build();
        var content = file.getInputStream();
        try(content) {
            Files.createDirectories(fullImagePath.getParent());
            Files.write(fullImagePath, content.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }

        imageRepository.save(image);
    }

    public Optional<byte[]> getImageForPost(Long postId) {
        Image image = imageRepository.findByPostId(postId)
                .orElse(null);

        if (!ObjectUtils.isEmpty(image)) {
            return getImage(image.getImagePath());
        }
        return Optional.empty();
    }

    public Optional<byte[]> getImageForUser(Principal principal) {
        User user = findUserByPrincipal(principal);
        Image image = imageRepository.findByUserId(user.getId())
                .orElse(null);

        if (!ObjectUtils.isEmpty(image)) {
            return getImage(image.getImagePath());
        }
        return Optional.empty();
    }

    private User findUserByPrincipal(Principal principal) {
        return userRepository.findUserByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + principal.getName()));
    }

    @SneakyThrows
    private Optional<byte[]> getImage(String imagePath) {
        Path fullImagePath = Path.of(bucket, imagePath);
        return Files.exists(fullImagePath)
                ? Optional.of(Files.readAllBytes(fullImagePath))
                : Optional.empty();
    }


}
