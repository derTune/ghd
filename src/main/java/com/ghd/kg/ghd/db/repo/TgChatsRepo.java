package com.ghd.kg.ghd.db.repo;

import com.ghd.kg.ghd.db.entity.TgChats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TgChatsRepo extends JpaRepository<TgChats, Long> {
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update TgChats set isMain = true where id = :chatId")
    void setChatAsMain(Long chatId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update TgChats set isMain = false where id not in (:chatId)")
    void setChatsAsNotMain(Long chatId);

    @Query("select t from TgChats t where t.isMain = true")
    TgChats getMainChat();
}
