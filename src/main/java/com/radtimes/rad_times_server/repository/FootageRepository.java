package com.radtimes.rad_times_server.repository;

import com.radtimes.rad_times_server.model.footage.ClipModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FootageRepository  extends JpaRepository<ClipModel, Long> {


}
