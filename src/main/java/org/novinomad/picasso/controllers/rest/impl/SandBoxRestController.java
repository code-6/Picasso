package org.novinomad.picasso.controllers.rest.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/sand-box")
@RequiredArgsConstructor
public class SandBoxRestController {

    @GetMapping("/1")
    public void one() {
        throw new NoSuchElementException("test NoSuchElementException");
    }

    @GetMapping("/2")
    public void two() {
        throw new IllegalStateException("test IllegalStateException");
    }

    @GetMapping("/3")
    public void three() {
        throw new IllegalArgumentException("test IllegalArgumentException");
    }

    @GetMapping("/4")
    public void four() throws Exception {
        throw new Exception("test Exception");
    }
}
