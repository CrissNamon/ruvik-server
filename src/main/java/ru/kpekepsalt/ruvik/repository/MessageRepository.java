package ru.kpekepsalt.ruvik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpekepsalt.ruvik.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdAndIdGreaterThan(Long conversationId, Long id);
}
