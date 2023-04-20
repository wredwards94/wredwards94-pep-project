package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message createNewMessage(Message message) {
        return message.getMessage_text().length() <= 254 && !message.getMessage_text().isEmpty() ?
                messageDAO.createNewMessage(message) : null;
    }

    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public Message updateMessageById(int message_id, Message message) {
        if(messageDAO.getMessageById(message_id) == null || message.getMessage_text().length() > 254 ||
        message.getMessage_text().isEmpty()) {
            return null;
        }else {
            Message updatedMessage = messageDAO.updateMessageById(message_id, message);
            return updatedMessage;
        }
    }

    public Message deleteMessageById(int message_id, Message message) {
        if(getMessageById(message_id) != null) {
            return messageDAO.deleteMessageById(message_id, message);
        }else {
            return null;
        }
    }
}
