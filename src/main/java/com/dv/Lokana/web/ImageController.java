package com.dv.Lokana.web;

import com.dv.Lokana.entitys.Image;
import com.dv.Lokana.payload.response.MessageResponse;
import com.dv.Lokana.service.ImageService;
import com.dv.Lokana.validations.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("api/image")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageForUser(@RequestParam("file") MultipartFile image,
                                                              Principal principal) {
        imageService.uploadImageToUser(image, principal);
        return new ResponseEntity<>(new MessageResponse("image upload successfully"), HttpStatus.OK);
    }

    @PostMapping("{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageForPost(@RequestParam("file") MultipartFile image,
                                   @PathVariable("postId") String postId) {
        imageService.uploadImageToPost(image, Long.parseLong(postId));
        return new ResponseEntity<>(new MessageResponse("image upload successfully"), HttpStatus.OK);
    }

    public ResponseEntity<byte[]> getImageToPost(@PathVariable("id") String id) {
        var image = imageService.getImageForPost(Long.getLong(id)).orElse(null);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> getImageToUser(Principal principal) {
      var image = imageService.getImageForUser(principal).orElse(null);
      return new ResponseEntity<>(image, HttpStatus.OK);

    }

}
