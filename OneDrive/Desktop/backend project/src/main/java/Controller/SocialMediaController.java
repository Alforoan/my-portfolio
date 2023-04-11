package Controller;

import Model.Message;
import Service.AccountService;
import DAO.MessageDAO;
import Model.Account;
import DAO.AccountDAO;
import Service.MessageService;


import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.*;


public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

public SocialMediaController (){
    this.accountService = new AccountService();
    this.messageService = new MessageService(null);
}


    

    public Javalin startAPI() {

        Javalin app = Javalin.create();
        app.post("/posts/create", this::createPostHandler); 
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.post("/login", this::loginHandler);
        app.post("/register", this::registerHandler);
        app.patch("/messages/{id}", this::updateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        app.get("/messages/{message_id}", context -> {
            //parse the message_id from the path parameter
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            
            //retrieve the message by its ID using the MessageDAO
            MessageDAO messageDAO = new MessageDAO();
            Message message = messageDAO.getMessageById(message_id);
            
            //return the message as JSON in the response body
            context.json(message);
        });

        app.get("/accounts/{account_id}/messages", ctx -> {
            // Get the account ID from the request path parameter
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            
            // Retrieve all messages written by the specified account ID
            MessageDAO messageDAO = new MessageDAO();
            List<Message> messages = messageDAO.getMessagesByAccountId(accountId);
            
            // Return the list of messages as a JSON response
            ctx.json(messages);
        });

        app.start(8080);
        return app;
    }



    private void registerHandler(Context context) throws SQLException {
        // parse the request body to get the necessary information
        String username = context.formParam("username");
        String password = context.formParam("password");

        if(username==null|| password==null)
        {
            context.status(400);
            return;
        }
        // check if the account exists in the database
        AccountDAO accountDAO = new AccountDAO();
        Account account = accountDAO.getAccountByUsernameAndPassword(username, password);
        if (account == null) {
            //Create new account
            //the registration will be successful
            // if and only if the username is not blank, the password is at least 4 characters
            assert username != null;assert password != null;
            if (!username.isEmpty() && password.length()>=4){
                //create new user
                account=accountDAO.insertAccount(new Account(username,password));
                // return the account information in the response body
                context.json(account);
            }else{
                context.status(400);
            }


        } else {
            // return 401 unauthorized status
            context.status(401);
        }
    }

    //post handler
    private void createPostHandler(Context context){
       //parse the request body to get the necessary information
    int postedBy = Integer.parseInt(context.formParam("posted_by"));
    String messageText = context.formParam("message_text");
    long timePostedEpoch = System.currentTimeMillis() / 1000L; //current time in seconds since epoch

    //create the post using the Message model and insert it into the database
    Message newMessage = new Message(postedBy, messageText, timePostedEpoch);
    MessageDAO messageDAO = new MessageDAO();
    messageDAO.insertMessage(newMessage);

    //return the newly created message object as JSON
    context.json(newMessage);
    context.status(200);
    }

    private void loginHandler(Context context) throws SQLException {
        // parse the request body to get the necessary information
        String username = context.formParam("username");
        String password = context.formParam("password");
    
        // check if the account exists in the database
        AccountDAO accountDAO = new AccountDAO();
        Account account = accountDAO.getAccountByUsernameAndPassword(username, password);
        if (account != null) {
            // return the account information in the response body
            context.json(account);
        } else {
            // return 401 unauthorized status
            context.status(401);
        }
    }
   
    private void createMessageHandler(Context context) throws SQLException {
        // Get message data from request body
        int postedBy = Integer.parseInt(context.formParam("posted_by"));
        String messageText = context.formParam("message_text");
        long timePostedEpoch = System.currentTimeMillis() / 1000L;
    
        // Check if message text is valid
        if (messageText == null || messageText.isBlank() || messageText.length() > 255) {
            context.status(400).json("Invalid message text");
            return;
        }
    
        // Check if user exists
        AccountDAO accountDAO = new AccountDAO();
        if (accountDAO.getAccountById(postedBy) == null) {
            context.status(400).json("User does not exist");
            return;
        }
    
        // Create message object and insert into database
        Message newMessage = new Message(postedBy, messageText, timePostedEpoch);
        MessageDAO messageDAO = new MessageDAO();
        messageDAO.insertMessage(newMessage);
    
        // Return success response
        context.json(newMessage);
    }
    private void getAllMessagesHandler(Context context) {
        MessageDAO messageDAO = new MessageDAO();
        List<Message> messages = messageDAO.getAllMessages();
        context.json(messages);
    }

    private void deleteMessageHandler(Context context) {
        // Get the message ID from the request path parameter
        int messageId = Integer.parseInt(context.pathParam("message_id"));
    
        // Attempt to delete the message from the database
        MessageDAO messageDAO = new MessageDAO();
        Message deletedMessage = messageDAO.deleteMessageById(messageId);
    
        if (deletedMessage != null) {
            // If the message was successfully deleted, return it in the response body
            context.json(deletedMessage);
        } else {
            // If the message was not found, return an empty response body
            context.status(200);
        }
    }
    private void updateMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("id"));
        MessageDAO messageDAO = new MessageDAO();
        Message messageToUpdate = messageDAO.getMessageById(messageId);
    
        if (messageToUpdate == null) {
            context.status(400).result("Message not found.");
            return;
        }
    
        String newMessageText = context.body();
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            context.status(400).result("Invalid message text.");
            return;
        }
    
        messageToUpdate.setMessage_text(newMessageText);
        boolean success = messageDAO.updateMessage(messageId, messageToUpdate);
    
        if (success) {
            context.json(messageToUpdate);
        } else {
            context.status(400).result("Failed to update message.");
        }
    } 
}