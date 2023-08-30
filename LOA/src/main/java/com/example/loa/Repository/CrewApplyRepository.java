package com.example.loa.Repository;

import com.example.loa.Entity.Crew;
import com.example.loa.Entity.CrewApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewApplyRepository extends JpaRepository<CrewApply, Integer> {
    List<CrewApply> findAllByCrew(Crew crew);
}
