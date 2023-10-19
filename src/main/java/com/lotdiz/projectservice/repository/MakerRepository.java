package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.Maker;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MakerRepository extends JpaRepository<Maker, Long> {
  Optional<Maker> findByMemberId(@Param("memberId") Long memberId);
}
