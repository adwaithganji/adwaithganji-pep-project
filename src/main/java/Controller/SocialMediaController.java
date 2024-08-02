package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import DAO.AccountDAO;
import DAO.MessageDAO;

import java.util.Collections;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;
    
    
    public SocialMediaController() {
        // Initialize DAO objects
        AccountDAO accountDAO = new AccountDAO();
        MessageDAO messageDAO = new MessageDAO();
        
        // Initialize Service objects with DAO dependencies
        this.accountService = new AccountService(accountDAO);
        this.messageService = new MessageService(messageDAO);
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessageText);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserId);

        return app;
    }

    
    
    private void registerUser(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account registeredAccount = accountService.registerAccount(account);

        if (registeredAccount != null) {
            context.json(registeredAccount);
        } else {
            context.status(400);
        }
    }

    private void loginUser(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());

        if (loggedInAccount != null) {
            context.json(loggedInAccount);
        } else {
            context.status(401);
        }
    }

    private void createMessage(Context context) {
        Message message = context.bodyAsClass(Message.class);
        Message createdMessage = messageService.addMessage(message);

        if (createdMessage != null) {
            context.json(createdMessage);
        } else {
            context.status(400);
        }
    }

    
    private void getAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void getMessageById(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            context.json(message);
        } else {
            context.status(200).result("");
        }
    }

    private void deleteMessage(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
    
        if (deletedMessage != null) {
            context.json(deletedMessage); 
        } else {
            context.status(200).result(""); 
        }
    }


    private void updateMessageText(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        String newText = context.bodyAsClass(Message.class).getMessage_text();

        Message updatedMessage = messageService.updateMessageText(messageId, newText);

        if (updatedMessage != null) {
            context.json(updatedMessage);
        } else {
            context.status(400);
        }
    }

    private void getMessagesByUserId(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUserId(accountId);

        if (messages != null && !messages.isEmpty()) {
            context.json(messages);
        } else {
            context.status(200).json(Collections.emptyList());
        }
    }


}