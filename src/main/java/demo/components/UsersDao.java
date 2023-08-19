package demo.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class UsersDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ArrayList<User> getUsers(){
        return (ArrayList<User>) jdbcTemplate.query("SELECT * FROM users", BeanPropertyRowMapper.newInstance(User.class));
    }

    public Response addUser(String username, String password){
        if(findUser(username) != null){
            return Response.ALREADY_EXISTS;
        }

        jdbcTemplate.update("INSERT INTO users (username, password) VALUES(?,?)",
                username, password);

        return Response.OK;
    }

    public String findUser(String username){
        ArrayList<User> users = (ArrayList<User>) jdbcTemplate.query("SELECT * FROM users", BeanPropertyRowMapper.newInstance(User.class));

        for(User user : users){
            if(user.getUsername().equals(username))
                return user.getPassword();
        }

        return null;
    }

    public Response update(User user){
        if(findUser(user.getUsername()) == null)
            return Response.NO_SUCH_USER;
        jdbcTemplate.update("UPDATE users SET password=? WHERE username=?", user.getPassword(), user.getUsername());
        return Response.OK;
    }

    public Response deleteUser(String username){
        if(findUser(username) == null){
            return Response.NO_SUCH_USER;
        }

        jdbcTemplate.update("DELETE FROM users WHERE username=?", username);
        return Response.OK;
    }

    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM users");
    }
}
