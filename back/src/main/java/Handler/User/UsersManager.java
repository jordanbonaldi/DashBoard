package Handler.User;

import DataBase.SingleDatabaseConnector;
import Main.ServerMain;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class UsersManager {

    private List<User> users;
    private Jedis jedis;

    public static UsersManager getManager() {
        return ServerMain.getMain().getUm();
    }

    public UsersManager() {
        this.users = new ArrayList<>();
        this.jedis = SingleDatabaseConnector.getDatabase();
        this.getAllDatas();
    }

    private void getAllDatas() {
        System.out.println("Scanning accounts");
        this.jedis.keys("user:*").forEach(e -> {
            String email = this.jedis.hgetAll(e).get("email");

            System.out.println("Adding : " + email);
            this.users.add(new User(email));
        });
    }

    public User getUserByEmail(String email) {
        return this.users.stream().filter(e -> e.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
    }

    public Object connectUser(String email, String password) {
        User user = this.users.stream().filter(e -> e.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
        if (user == null)
            return "Account doesn't exist";
        if (!user.isPasswordMatch(password))
            return "Incorrect password";
        return user;
    }

    public User getUser(String email, String password, String pseudo) {
        User user = this.getUserByEmail(email);
        if (user == null) {
            user = new User(email);
            user.setPassword(password);
            user.setPseudo(pseudo);
            this.users.add(user);
            return user;
        }
        return user;
    }

    public void performActionOnCachedUsers(Consumer<User> user) {
        this.users.forEach(user);
    }

}
