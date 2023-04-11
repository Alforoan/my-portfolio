package Service;
import java.util.*;
import Model.Message;
import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }
    
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }
    
    public void createMessage(Message message) {
        messageDAO.insertMessage(message);
    }
    
    public void updateMessage(Message message) {
        messageDAO.updateMessage(1, message);
    }
    
    public void deleteMessage(int id) {
        messageDAO.deleteMessage(id);
    }
}
