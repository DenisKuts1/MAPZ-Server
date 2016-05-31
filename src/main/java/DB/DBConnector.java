package DB;

import model.Comment;
import model.Course;
import model.Mark;
import model.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Анатолий on 18.03.2016.
 */
public class DBConnector {

    private Connection dbConnection;

    public DBConnector()
    {
         dbConnection = null;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println(1);
        }
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:mydb.db");
        } catch (SQLException e) {
            System.out.println(2);
        }
    }

    private boolean insert(String insertTableSQL){
        try{
            Statement statement = dbConnection.createStatement();
            statement.execute(insertTableSQL);
            return true;
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public boolean insertUser(User user){
        String insertTableSQL = "INSERT INTO Users"
                + "( userName, password, isModerator) " + "VALUES"
                + "('" + user.getUserName() + "','" + user.getPassword() + "','"
                + user.isModerator() + "');";
        System.out.println(insertTableSQL);
        return insert(insertTableSQL);
    }


    public User getUser(String userName){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Users WHERE userName = '" + userName + "'");
            User user = new User();
            user.setUserName(userName);
            user.setPassword(rs.getString("password"));
            user.setId(rs.getInt("id"));
            user.setModerator(rs.getBoolean("isModerator"));
            return user;
        }catch (SQLException e){
            return null;
        }
    }

    public boolean insertCourse(Course course){
        String insertTableSQL = "INSERT INTO Courses"
                + "(title, description, link) " + "VALUES"
                + "('" + course.getTitle() + "','" + course.getDescription() + "','"
                + course.getLink() + "');";
        System.out.println(insertTableSQL);
        return insert(insertTableSQL);
    }


    public Course getCourse(String name){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Courses WHERE title = '" + name + "'");
            Course course = new Course();
            course.setDescription("description");
            course.setTitle(rs.getString("title"));
            course.setId(rs.getInt("id"));
            course.setLink(rs.getString("link"));
            return course;
        }catch (SQLException e){
            return null;
        }
    }

    public ArrayList<String> getCourses(){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Courses");

            ArrayList<String> list = new ArrayList<String>();
            while (rs.next()) {
                list.add(rs.getString("title"));
            }
            return list;

        }catch (SQLException e){
            return null;
        }
    }

    public boolean insertMark(Mark mark){
        String insertTableSQL = "INSERT INTO Marks"
                + "(mark, userID, courseID) " + "VALUES"
                + "(" + mark.getMark() + "," + mark.getUser().getId() + "," + mark.getCourse().getId() + ");";
        System.out.println(insertTableSQL);
        return insert(insertTableSQL);
    }

    public Mark getMark(User user, Course course){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Marks WHERE userID = " + user.getId() + " AND courseID = " + course.getId());
            Mark mark = new Mark();
            mark.setCourse(course);
            mark.setUser(user);
            mark.setMark(rs.getInt("mark"));
            mark.setId(rs.getInt("id"));
            return mark;
        }catch (SQLException e){
            return null;
        }
    }



    public boolean insertComment(Comment comment){
        String insertTableSQL = "INSERT INTO Comments"
                + "( comment, userID, courseID) " + "VALUES"
                + "('" + comment.getComment() +  "'," + comment.getUser().getId() + "," + comment.getCourse().getId() + ");";
        return insert(insertTableSQL);
    }

    public Comment getComment(User user, Course course){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Comments WHERE userID = " + user.getId() + " AND courseID = " + course.getId());
            Comment comment = new Comment();
            comment.setCourse(course);
            comment.setUser(user);
            comment.setComment(rs.getString("comment"));
            comment.setId(rs.getInt("id"));
            return comment;
        }catch (SQLException e){
            return null;
        }
    }

/**

    public Topic getTopic(long id){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select description FROM topics WHERE id = " + id);
            Topic topic = new Topic();
            topic.setId(id);
            topic.setDescription(rs.getString("description"));
            return topic;
        }catch (SQLException e){
            return null;
        }
    }

    public Player getPlayer(String email){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM players WHERE email = '" + email + "'");
            Player player = new Player();
            player.setEmail(email);
            player.setLogin(rs.getString("login"));
            player.setId(rs.getLong("id"));
            player.setRank(rs.getInt("rank"));
            player.setAvatar(getAvatar(rs.getLong("avatarId")));
            return player;
        }catch (SQLException e){
            return null;
        }
    }

    public Player getPlayer(long id){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM players WHERE id = " + id + "");
            Player player = new Player();
            player.setEmail(rs.getString("email"));
            player.setLogin(rs.getString("login"));
            player.setId(id);
            player.setRank(rs.getInt("rank"));
            player.setAvatar(getAvatar(rs.getLong("avatarId")));
            return player;
        }catch (SQLException e){
            return null;
        }
    }

    public boolean addFriend(Player player1, Player player2){
        String insertTableSQL = "INSERT INTO friendList"
                + "(player1Id, player2Id) " + "VALUES"
                + "(" + player1.getId() + "," + player2.getId() + ");";
        return insert(insertTableSQL);
    }

    public ArrayList<Player> getFriends(Player player){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM friendList " +
                    "WHERE player1Id = " + player.getId() + " OR  player2Id =  " + player.getId());
            ArrayList<Player> list = new ArrayList<Player>();
            while (rs.next()) {
                long id = rs.getLong("player1Id");
                Player friend;
                if(id == player.getId()) {
                    friend = getPlayer(rs.getLong("player2Id"));
                } else {
                    friend = getPlayer(id);
                }
                list.add(friend);
            }
            return list;
        }catch (SQLException e){
            return null;
        }
    }

    public Question getQuestion(long id){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM questions WHERE id = " + id + "");
            Question question = new Question();
            question.setAnswer(rs.getByte("answer"));
            question.setAnswer1(rs.getString("answer1"));
            question.setAnswer2(rs.getString("answer2"));
            question.setAnswer3(rs.getString("answer3"));
            question.setAnswer4(rs.getString("answer4"));
            question.setDescription(rs.getString("description"));
            question.setId(id);
            question.setAvatar(getAvatar(rs.getLong("avatarId")));
            question.setTopic(getTopic(rs.getLong("topicId")));
            return question;
        }catch (SQLException e){
            return null;
        }
    }

    public ArrayList<Question> getQuestionFromTopic(Topic topic){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM questions " +
                    "WHERE topicId = " + topic.getId());
            ArrayList<Question> list = new ArrayList<Question>();
            while (rs.next()) {
                Question question = new Question();
                question.setAnswer(rs.getByte("answer"));
                question.setAnswer1(rs.getString("answer1"));
                question.setAnswer2(rs.getString("answer2"));
                question.setAnswer3(rs.getString("answer3"));
                question.setAnswer4(rs.getString("answer4"));
                question.setDescription(rs.getString("description"));
                question.setId(rs.getLong("id"));
                question.setAvatar(getAvatar(rs.getLong("avatarId")));
                question.setTopic(topic);
                list.add(question);
            }
            return list;
        }catch (SQLException e){
            return null;
        }
    }

    public ArrayList<Topic> getAllTopics(){
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM topics");
            ArrayList<Topic> list = new ArrayList<Topic>();
            while (rs.next()) {
                Topic topic = new Topic();
                topic.setId(rs.getLong("id"));
                topic.setDescription(rs.getString("description"));
                list.add(topic);
            }
            return list;
        }catch (SQLException e){
            return null;
        }
    }

    public boolean deleteQuestion(long id){
        try {
            Statement statement = dbConnection.createStatement();
            statement.executeQuery("DELETE * FROM questions WHERE id = " + id + "");
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    public boolean deleteTopic(long id){
        try {
            Statement statement = dbConnection.createStatement();
            statement.executeQuery("DELETE * FROM topics WHERE id = " + id + "");
            return true;
        } catch (SQLException e){
            return false;
        }
    }

 */
}
