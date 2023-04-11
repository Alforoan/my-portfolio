import Controller.SocialMediaController;
import DAO.AccountDAO;
import Model.Account;
import Util.ConnectionUtil;
import io.javalin.Javalin;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
         SocialMediaController controller = new SocialMediaController();
         controller.startAPI();
    }
}
