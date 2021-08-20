package ru.kpekepsalt.ruvik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kpekepsalt.ruvik.model.Conversation;
import ru.kpekepsalt.ruvik.enums.ConversationStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findBySessionKey(String sessionKey);
    List<Conversation> findByReceiverId(Long id);
    List<Conversation> findBySenderId(Long id);
    List<Conversation> findByStatusAndReceiverId(ConversationStatus status, Long id);
    List<Conversation> findByStatusAndSenderId(ConversationStatus status, Long id);
    Optional<Conversation> findByReceiverIdAndSenderId(Long receiver, Long sender);

    /**
     * Session establishment
     * @param status New conversation status
     * @param id Conversation id
     */
    @Modifying
    @Transactional
    @Query("UPDATE Conversation c SET c.oneTimeKey = '', c.status = :status WHERE id = :id")
    void establishSession(@Param("status") ConversationStatus status, @Param("id") Long id);

}
