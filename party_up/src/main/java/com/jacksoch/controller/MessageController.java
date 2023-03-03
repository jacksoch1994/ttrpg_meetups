package com.jacksoch.controller;

import com.jacksoch.dao.MessageDao;
import com.jacksoch.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/messages")
public class MessageController {

    private MessageDao dao;

    public MessageController(MessageDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public List<Message> getAllMessages(@RequestParam(required = false) Integer senderId, @RequestParam(required = false) Integer receiverId) {
        List<Message> messages = new ArrayList<>();

        if (senderId != null && receiverId != null) {
            messages = dao.getMessagesBySenderReceiver(senderId, receiverId);
        } else if (senderId != null) {
            messages = dao.getMessagesBySender(senderId);
        } else if (receiverId != null) {
            messages = dao.getMessagesByReceiver(receiverId);
        }
        return messages;
    }

    @GetMapping("/{id}")
    public Message getMessage(@PathVariable int id) {
        Message message = dao.getMessage(id);
        // If message does not exist, throw error.
        if (message == null) throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown Message");

        return message;
    }

    //Todo: ADD VALIDATION
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Message create(@RequestBody Message message) {
        return dao.addMessage(message);
    }

    //Todo: ADD VALIDATION
    @PutMapping("/{id}")
    public Message update(@RequestBody Message message, @PathVariable int id) {
        //Handling message not existing
        if (getMessage(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown Message");
        }

        return dao.updateMessage(message, id);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable int id) {
        dao.deleteMessage(id);
    }

}
