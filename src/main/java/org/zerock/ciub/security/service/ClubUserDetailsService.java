package org.zerock.ciub.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.ciub.entity.ClubMember;
import org.zerock.ciub.repository.ClubMemberRepository;
import org.zerock.ciub.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Username: "+username);
        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);
        if(result.isEmpty()){
            throw new UsernameNotFoundException("chek Username or Social");
        }
        ClubMember clubMember = result.get();
        log.info("-------------------------------");
        log.info(clubMember);

        // entity to DTO
        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream()
                        .map(role-> new SimpleGrantedAuthority("ROLE_"+role.name()))
                        .collect(Collectors.toList())
        );
        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.isFromSocial());

        return clubAuthMember;
    }
}
