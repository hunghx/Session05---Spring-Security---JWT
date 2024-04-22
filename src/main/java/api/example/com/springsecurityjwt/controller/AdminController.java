package api.example.com.springsecurityjwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
@GetMapping("/list")
    public ResponseEntity<List<String>> home(){
    return new ResponseEntity<>(Arrays.asList("hh","ll","dd"), HttpStatus.OK);
}
}
