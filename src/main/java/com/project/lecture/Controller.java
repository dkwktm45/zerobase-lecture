package com.project.lecture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @GetMapping("/test")
  public String message(){
    return "hello spring";
  }
}
