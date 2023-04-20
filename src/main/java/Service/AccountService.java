package Service;
import DAO.AccountDAO;
import Model.Account;
import Model.Message;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) {
        if(account.getUsername().equals("")) {
            return null;
        }else if(account.getPassword().length() < 4)  {
            return null;
        }else {
            return accountDAO.createAccount(account);
        }
    }

    public Account getAccount(Account account) {
            return accountDAO.retreiveAccount(account);
    }

    public List<Message> getAllMessagesByAccountId(int account_id) {
        return accountDAO.getAllMessagesByAccountId(account_id);
    }
}
