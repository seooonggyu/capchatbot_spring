package com.thc.capchatbot.controller.page;

import com.thc.capchatbot.security.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("")
@Controller
public class DefaultPageController {
/*

    @RequestMapping("/index")
    public String index(){
        System.out.println("DefaultPageController.index()");
        return "index";
    }
*/
    @GetMapping({"/", "/index"}) // 루트 경로로 들어오면
    public String index(@AuthenticationPrincipal PrincipalDetails principal) {
        if (principal == null) {
            return "redirect:/user/login"; // 로그인 안 했으면 로그인 창으로
        }
        return "redirect:/space/list"; // 로그인 했으면 스페이스 목록으로
    }

    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }

}