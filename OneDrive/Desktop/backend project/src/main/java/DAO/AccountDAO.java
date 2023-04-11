package DAO;
import java.util.*;
import java.sql.*;

import Util.ConnectionUtil;
import Model.Account;




public class AccountDAO {

    public List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account";

            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("account_id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Account account = new Account(id, username, password);
                accounts.add(account);
            }

        return accounts;
    }


    public Account getAccountById(int id) throws SQLException{
        String sql = "SELECT * FROM account WHERE account_id = ?";

            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String usernameFromDb = rs.getString("username");
                String passwordFromDb = rs.getString("password");

                return new Account(id, usernameFromDb, passwordFromDb);
            } else {
                return null;
            }


    }

    public Account insertAccount(Account account) throws SQLException {
        String sql = "INSERT INTO account(username, password) VALUES (?, ?)";

            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
             statement.executeUpdate();
            //statement.
            return getAccountByUsernameAndPassword(account.getUsername(),account.getPassword());

    }

    public void updateAccount(int id, Account account) throws SQLException {
        String sql = "UPDATE account SET username=?, password=? WHERE account_id=?";

            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setInt(3, id);
             statement.executeUpdate();

    }
    public Account getAccountByUsernameAndPassword(String username, String password) throws SQLException{
        String query = "SELECT * FROM  account WHERE username = ? AND password = ?";


            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);


                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    int accountId = rs.getInt("account_id");
                    String storedUsername = rs.getString("username");
                    String storedPassword = rs.getString("password");

                    return new Account(accountId, storedUsername, storedPassword);
                } else {
                    return null;
                }


    }
}