
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;

import java.io.Serializable;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@ManagedBean
@SessionScoped
public class DatabaseBean implements Serializable {
    // Class.forName("org.postgresql.Driver");

    private final String url = "jdbc:postgresql://localhost:4444/studs";
    private final String login = "s367519";
    private final String password = "Ph4YbHYFFztmodS5";
    private Connection connection;

    public DatabaseBean(){
        connectToDatabase();
    }

    public void connectToDatabase(){
        try {
            connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException e){
            System.out.println("problem with database((");
            //System.exit(-1);
        }
    }

    private static final String ADD_DOT = "INSERT INTO dots (x, y, r ,result, owner) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_DOTS_BY_OWNER = "SELECT * FROM dots WHERE owner = ?";
    private static final String GET_ALL_DOTS = "SELECT * FROM dots";


    public boolean addDot(Dot dot){
        try{
            connection.setAutoCommit(false);
            connection.setSavepoint();
            PreparedStatement addStatement = connection.prepareStatement(ADD_DOT);
            addStatement.setDouble(1, dot.getX());
            addStatement.setDouble(2, dot.getY());
            addStatement.setDouble(3, dot.getR());
            addStatement.setBoolean(4, dot.getResult());
            addStatement.setString(5, dot.getOwner());
            addStatement.executeUpdate();
            addStatement.close();

            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e){
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e1){
                return false;
            }
            return false;
        }
    }

    public List<Dot> getDotsByOwner(String owner) {
        List<Dot> dots = new LinkedList<>();
        try {
            PreparedStatement getStatement = connection.prepareStatement(GET_DOTS_BY_OWNER);
            getStatement.setString(1, owner);
            ResultSet resultSet = getStatement.executeQuery();

            dots = dotsFromResult(resultSet);

            //return dots;
        } catch (SQLException e){
            // return null;
        }
        return dots;
    }

    public List<Dot> getAllDots() {
        List<Dot> dots = new LinkedList<>();
        try {
            PreparedStatement getStatement = connection.prepareStatement(GET_ALL_DOTS);
            ResultSet resultSet = getStatement.executeQuery();
            dots = dotsFromResult(resultSet);
            //return dots;
        } catch (SQLException e){
            // return null;
        }
        return dots;
    }

    private  List<Dot> dotsFromResult(ResultSet resultSet){
        List<Dot> dots = new LinkedList<>();
        try {
            double x, y, r;
            boolean result;
            while (resultSet.next()) {
                x = resultSet.getDouble("x");
                y = resultSet.getDouble("y");
                r = resultSet.getDouble("r");
                result = resultSet.getBoolean("result");
                Dot dot = new Dot();
                dot.setX(x);
                dot.setY(y);
                dot.setR(r);
                dot.setResult(result);
                dots.add(dot);
            }
        } catch (SQLException e){

        }
        return dots;
    }

}
