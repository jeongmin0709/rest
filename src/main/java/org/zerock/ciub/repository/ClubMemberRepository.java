package org.zerock.ciub.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ciub.entity.ClubMember;

import javax.persistence.Entity;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select m from ClubMember m where m.fromSocial=:social and m.email=:email")
    Optional<ClubMember> findByEmail(String email, boolean social);
}
