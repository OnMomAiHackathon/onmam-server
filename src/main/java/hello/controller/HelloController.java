package hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("hello")
    public void hello(){
        System.out.println("hello!");
    }

    @GetMapping("bye")
    public void bye(){
        System.out.println("bye~");
    }

    @GetMapping("responseTest")
    public String prompt(@RequestParam("text") String text){
        return "입력한문장:"+text;
    }
}
