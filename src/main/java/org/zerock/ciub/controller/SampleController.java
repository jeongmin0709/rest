package org.zerock.ciub.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.ciub.security.dto.ClubAuthMemberDTO;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {
    @PreAuthorize("permitAll()") // 어노테이션으로 접근권한 지정
    @GetMapping("/all")
    public void exAll(){
        log.info("exAll..........");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember){
        log.info("exMember..........");

        log.info("-------------------------------");
        log.info(clubAuthMember);
    }

    @PreAuthorize("hasRole('ADIMN')")
    @GetMapping("/admin")
    public void exAdmin(){
        log.info("exAdmin..........");
    }

    //user95인 사용자만 접근하게 하고싶을떄 사용
    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.username eq \'user95@zerock.org\'")
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember){
        log.info("exMemberOnly.......................");
        log.info(clubAuthMember);
        return "/sample/admin";
    }
}
