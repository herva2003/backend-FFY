package com.puccampinas.backendp5noname.services;

import com.puccampinas.backendp5noname.domain.Message;
import com.puccampinas.backendp5noname.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessagesByTopicId(String topicId) {
        return messageRepository.findByTopicId(topicId);
    }

    public Message createMessage(String topicId, Message message) {
        message.setTopicId(topicId);
        message.setCreatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }
}