package demo.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class AttachmentsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AttachmentsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ArrayList<String> getUsers(String room){
        ArrayList<Attachment> attachments = (ArrayList<Attachment>) jdbcTemplate.query("SELECT * FROM attachments", BeanPropertyRowMapper.newInstance(Attachment.class));

        ArrayList<String> users = new ArrayList<>();

        for(Attachment attachment : attachments){
            if(attachment.getRoom().equals(room)){
                users.add(attachment.getUsername());
            }
        }

        return users;
    }

    public boolean findAttachment(String username){
        ArrayList<Attachment> attachments = (ArrayList<Attachment>) jdbcTemplate.query("SELECT * FROM attachments", BeanPropertyRowMapper.newInstance(Attachment.class));

        for(Attachment attachment : attachments){
            if(attachment.getUsername().equals(username))
                return true;
        }

        return false;
    }

    public void addAttachment(Attachment attachment){
        jdbcTemplate.update("INSERT INTO attachments (username, room) VALUES(?,?)", attachment.getUsername(), attachment.getRoom());
    }

    public void updateAttachment(Attachment attachment){
        jdbcTemplate.update("UPDATE attachments SET room=? WHERE username=?", attachment.getRoom(), attachment.getUsername());
    }

    public void deleteAttachment(String username){
        jdbcTemplate.update("DELETE FROM attachments WHERE username=?", username);
    }

    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM attachments");
    }

}
