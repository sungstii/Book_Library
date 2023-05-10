package server.book_library;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ServerTestController {
    @GetMapping
    public String serverTest(){
        return "서버켜짐";
    }
}
