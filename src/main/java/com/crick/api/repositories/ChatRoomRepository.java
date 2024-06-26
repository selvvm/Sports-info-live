// ChatRoomRepository.java
package com.crick.api.repositories;

import com.crick.api.entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByMatchId(Long matchId);
}
