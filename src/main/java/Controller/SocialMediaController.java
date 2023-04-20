package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postNewAccountHandler);
        app.post("/login", this::postRetreiveAccountHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountId);

        return app;
    }

    /**
     * Account Handlers
     * */

    private void postNewAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account createNewAccount = accountService.addAccount(account);
        if(createNewAccount != null) {
            ctx.json(mapper.writeValueAsString(createNewAccount));
        }else {
            ctx.status(400);
        }
    }

    private void postRetreiveAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account retreiveAccount = accountService.getAccount(account);
        if (retreiveAccount != null) {
            ctx.json(mapper.writeValueAsString(retreiveAccount));
        } else {
            ctx.status(401);
        }
    }

    private void getAllMessagesByAccountId(Context ctx) {
        Integer account_id = Integer.parseInt(ctx.pathParam("account_id"));
        try{
            List<Message> messages = accountService.getAllMessagesByAccountId(account_id);
            if(messages != null) {
                ctx.json(messages).status();
            }else {
                ctx.status();
            }
        }catch(NumberFormatException e) {
            ctx.status(422);
        }
    }

    /**
     * Messages Handlers
     * */
    private void postNewMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createNewMessage = messageService.createNewMessage(message);
        if ((createNewMessage != null)) {
            ctx.json(mapper.writeValueAsString(createNewMessage));
        } else {
            ctx.status(400);
        }
    }
    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageById(Context ctx) {
        Integer message_id = Integer.parseInt(ctx.pathParam("message_id"));

        try {
            Message message = messageService.getMessageById(message_id);
            if (message != null) {
                ctx.json(message).status();
            } else {
                ctx.status();
            }
        }catch (NumberFormatException e){
            ctx.status(422);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Integer message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageById(message_id, message);

        if(updatedMessage == null) {
            ctx.status(400);
        }else {
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        Integer message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);

        try {
            Message deletedMessage = messageService.deleteMessageById(message_id, message);
            if (message != null) {
                ctx.json(deletedMessage);
                ctx.status();
            } else {
                ctx.status();
            }
        }catch(NumberFormatException e) {
            ctx.status(422);
        }
    }
}