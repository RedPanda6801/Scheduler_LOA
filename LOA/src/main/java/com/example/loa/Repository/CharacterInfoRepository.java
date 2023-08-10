package com.example.loa.Repository;


import com.example.loa.Entity.CharacterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterInfoRepository extends JpaRepository<CharacterInfo, Integer>{
    List<CharacterInfo> findAllByUserId(Integer id);
}
