package com.dv.Lokana.exceptions;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(String post_not_found) {
        super(post_not_found);
    }
}
