package DAO;

import java.sql.Connection;
import java.util.*;
import Model.Message;
import java.sql.*;
import Util.ConnectionUtil;


public class MessageDAO{

    public Message insertMessage(Message message) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                message.setMessage_id(rs.getInt(1));
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateMessage(int message_id, Message message) {
        try   {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "UPDATE message SET message_text=?, time_posted_epoch=? WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message.getMessage_text());
            ps.setLong(2, message.getTime_posted_epoch());
            ps.setInt(3, message_id);
            int count = ps.executeUpdate();
            if (count == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message";
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Message getMessageById(int message_id) {
        try  {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteMessage(int message_id) {
        try  {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "DELETE FROM message WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            int count = ps.executeUpdate();
            if (count == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Message deleteMessageById(int messageId) {
        String sql = "DELETE FROM message WHERE message_id =?";
    try  {
        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, messageId);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 1) {
            return getMessageById(messageId);
        } else {
            return null;
        }
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    }
    public List<Message> getMessagesByAccountId(int accountId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by =?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int message_id = result.getInt("message_id");
                String message_text = result.getString("message_text");
                long time_posted_epoch = result.getLong("time_posted_epoch");
                Message message = new Message(message_id, accountId, message_text, time_posted_epoch);
                messages.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
}