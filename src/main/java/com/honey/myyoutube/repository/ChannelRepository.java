package com.honey.myyoutube.repository;

import com.honey.myyoutube.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, String > {
}
