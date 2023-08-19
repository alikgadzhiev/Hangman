package demo.paths;

import demo.components.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

@RestController
public class Paths {

    private final UsersDao usersDao;
    private final AttachmentsDao attachmentsDao;

    private HashMap<String, String> words = new HashMap<>();

    @Autowired
    public Paths(UsersDao usersDao, AttachmentsDao attachmentsDao) {
        this.usersDao = usersDao;
        this.attachmentsDao = attachmentsDao;
    }





    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody User user){
        if(attachmentsDao.findAttachment(user.getUsername())){
            return "Already logged in";
        }
        String response = usersDao.findUser(user.getUsername());
        return (response != null && response.equals(user.getPassword()) ? "Ok" : "Wrong username or password");
    }





    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok().body(usersDao.getUsers());
    }

    @GetMapping("/findUser/{username}")
    public String findUser(@PathVariable("username") String username){
        return usersDao.findUser(username);
    }

    @PostMapping("/addUser")
    @ResponseBody
    public String addUser(@RequestBody User user){
        if(usersDao.addUser(user.getUsername(), user.getPassword()) == Response.ALREADY_EXISTS) {
            return "The user with such username already exists";
        } else {
            return "Successfully added";
        }
    }

    @PostMapping("/updateUser")
    @ResponseBody
    public String updateUser(@RequestBody User user){
        if(usersDao.update(user) == Response.NO_SUCH_USER)
            return "No such user";
        else
            return "Updated successfully";
    }

    @DeleteMapping("/deleteUser/{username}")
    public String deleteUser(@PathVariable("username") String username){
        if(usersDao.deleteUser(username) == Response.OK){
            return "Successfully deleted";
        } else {
            return "No such user";
        }
    }

    @DeleteMapping("/deleteAllUsers")
    public String deleteAllUsers(){
        usersDao.deleteAll();
        return "Deleted :|";
    }




    @GetMapping("/checkTheRoom/{room}")
    public String checkTheRoom(@PathVariable("room") String room){
        if(attachmentsDao.getUsers(room).size() < 2)
            return "Safe";
        else
            return "This room is already full, enter other one";
    }

    @PostMapping("/addAttachment")
    @ResponseBody
    public void addAttachment(@RequestBody Attachment attachment){
        attachmentsDao.addAttachment(attachment);
    }

    @PostMapping("/updateAttachment")
    @ResponseBody
    public void updateAttachment(@RequestBody Attachment attachment){
        attachmentsDao.updateAttachment(attachment);
    }

    @DeleteMapping("/deleteAttachment/{username}")
    public void deleteAttachment(@PathVariable("username") String username){
        attachmentsDao.deleteAttachment(username);
    }

    @DeleteMapping("/deleteAllAttachments")
    public void deleteAllAttachments(){
        attachmentsDao.deleteAll();
    }



    @PostMapping("/sendWord")
    @ResponseBody
    public String sendWord(@RequestBody GuessWord word){
        if(words.containsKey(word.getRoom()))
            return "already guessed";
        words.put(word.getRoom(), word.getWord());
        return "put";
    }

    @DeleteMapping("/deleteWord/{room}")
    public void deleteWord(@PathVariable("room") String room){
        if(!words.containsKey(room))
            return;
        words.remove(room);
    }

    @GetMapping("/getWord/{room}")
    public String getWord(@PathVariable("room") String room){
        if(!words.containsKey(room))
            return "dont exist";
        return words.get(room);
    }

}

