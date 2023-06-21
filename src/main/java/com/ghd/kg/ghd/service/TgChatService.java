package com.ghd.kg.ghd.service;

import com.ghd.kg.ghd.db.repo.TgChatsRepo;
import com.ghd.kg.ghd.service.interfaces.ITgChatsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TgChatService implements ITgChatsService {
    final TgChatsRepo tgChatsRepo;
    @Override
    @Transactional
    public void setChatAsMain(Long chatId) {
        tgChatsRepo.setChatAsMain(chatId);
    }

    @Override
    @Transactional
    public void setChatsAsNotMain(Long chatId) {
        tgChatsRepo.setChatsAsNotMain(chatId);
    }
}
