package Service;
import java.sql.SQLException;
import java.util.*;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    
    public Account addAccount(Account account) throws SQLException {
        Account addedAccount = accountDAO.insertAccount(account);
        return addedAccount;
    }

   
    public Account updateAccount(int account_id, Account account) throws SQLException {

        Account existingAccount = accountDAO.getAccountById(account_id);
if (existingAccount != null) {
    
    existingAccount.setUsername(account.getUsername());
    existingAccount.setPassword(account.getPassword());
    
    accountDAO.updateAccount(account_id, existingAccount);
    
    return existingAccount;
} else {
    return null;
}
    }

    
    public List<Account> getAllAccounts() throws SQLException {
        return accountDAO.getAllAccounts();
    }

    
   
}
