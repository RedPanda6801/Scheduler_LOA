package com.example.loa.Repository;

import com.example.loa.Entity.Crew;
import com.example.loa.Entity.CrewMember;
import com.example.loa.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, Integer> {
    List<CrewMember> findAllByUser(User user);

    List<CrewMember> findAllByCrew(Crew crew);
}
