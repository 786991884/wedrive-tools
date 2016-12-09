package com.terminal.service;

import com.terminal.entity.Message;

public interface MessageRepository {

    Iterable<Message> findAll();

    Message save(Message message);

    Message findMessage(Long id);

}
