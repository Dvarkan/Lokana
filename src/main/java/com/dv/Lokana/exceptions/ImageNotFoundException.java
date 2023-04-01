package com.dv.Lokana.exceptions;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException(String image_not_found) {
        super(image_not_found);
    }
}
