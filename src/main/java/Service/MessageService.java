package Service;

import DAO.MessageDAO;
import Model.Message;


import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    
    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    
    public Message addMessage(Message message) {
       
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

   
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

   
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    
    public Message deleteMessage(int messageId) {
        Message message = messageDAO.getMessageById(messageId);
        if (message != null) {
            messageDAO.deleteMessage(messageId);
        }
        return message;
    }


    
    public Message updateMessageText(int messageId, String newText) {
        if (newText == null || newText.isEmpty() || newText.length() > 255) {
            return null;
        }
    
     
        return messageDAO.updateMessageText(messageId, newText);
    }
    

    
    public List<Message> getMessagesByUserId(int accountId) {
        return messageDAO.getMessagesByUserId(accountId);
    }
}