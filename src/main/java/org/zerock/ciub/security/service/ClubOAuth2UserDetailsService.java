package org.zerock.ciub.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.ciub.entity.ClubMember;
import org.zerock.ciub.entity.ClubMemberRole;
import org.zerock.ciub.repository.ClubMemberRepository;
import org.zerock.ciub.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final ClubMemberRepository repository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        String clientName = userRequest.getClientRegistration().getClientName();
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = null;
        if(clientName.equals("Google")){
            email = oAuth2User.getAttribute("email");
        }
        log.info("EMAEL" + email);
//        ClubMember member = saveSocialMember(email);
//        return oAuth2User;
        ClubMember member = saveSocialMember(email);
        //entity to dto
        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_"+ role.name())).collect(Collectors.toSet()),
                oAuth2User.getAttributes()
        );
        clubAuthMember.setName(member.getName());
        return clubAuthMember;
    }
    private ClubMember saveSocialMember(String email){
        //????????? ????????? ???????????? ????????? ????????? ?????? ???????????? ?????????
        Optional<ClubMember> result = repository.findByEmail(email, true);
        if(result.isPresent()){
            return result.get();
        }
        //????????? ?????? ?????? ??????????????? 1111 ????????? ?????? ????????? ?????????
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();
        clubMember.addMemberRole(ClubMemberRole.USER);
        repository.save(clubMember);
        return clubMember;
    }
}
