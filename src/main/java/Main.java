import DB.DBConnector;
import model.Comment;
import model.Course;
import model.Mark;
import model.User;
import server.Server;


/**
 * Created by Анатолий on 06.03.2016.
 */
public class Main {

    public static void main(String[] args) {
        DBConnector connector = new DBConnector();
        Server server = new Server(connector);
    }
}
