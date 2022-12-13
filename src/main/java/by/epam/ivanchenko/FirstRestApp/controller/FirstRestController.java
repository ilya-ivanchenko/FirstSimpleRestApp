package by.epam.ivanchenko.FirstRestApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController                                                             // Значит, что каждый метод будет возвращать данные (чтобы не помечать аннотацией @ResponseBody каждый метод)
@RequestMapping("/api")
public class FirstRestController {

    @GetMapping("/sayHello")
    public String sayHello() {
        return "Hello world!";
    }
}
