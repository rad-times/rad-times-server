package com.radtimes.rad_times_server.repository;

import com.radtimes.rad_times_server.model.footage.ClipModel;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FootageRepository extends JpaRepository<ClipModel, Long> {

    Optional<Set<ClipModel>> findByCreateDateBeforeOrderByCreateDateDesc(Long startDateTime, Limit limit);

}
