package com.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloWorld {
    @RequestMapping(path = "/hi")
  public  String helloworld(){
      return  "helloworld";
  }
}
