
import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class DatabaseManager {
    // Class.forName("org.postgresql.Driver");

    private final String url = "jdbc:postgresql://localhost:9999/studs";
    private final String login = "s367519";
    private final String password = "*";
    private Connection connection;


    public DatabaseManager(){
try {
    Class.forName("org.postgresql.Driver");
    connectToDatabase();
} catch (ClassNotFoundException e){
    System.out.println("class driver exception");
}
    }

    public void connectToDatabase(){
        try {
            connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException e){
            System.out.println("aaaaaaaaaaaaaaa problem with database((");
            e.printStackTrace();
            System.exit(-1);
        }
    }


    private static final String ADD_DOT = "INSERT INTO dots (x, y, r ,result, owner, time) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_DOTS_BY_OWNER = "SELECT * FROM dots WHERE owner = ?";
    private static final String GET_20_DOTS_BY_OWNER = "SELECT * FROM dots WHERE owner = ? ORDER BY time DESC LIMIT 20";
    private static final String GET_NEXT_20_DOTS_BY_OWNER = "SELECT * FROM dots WHERE owner = ? ORDER BY time DESC LIMIT 20 OFFSET ?";
    private static final String GET_ALL_DOTS = "SELECT * FROM dots";
    private static final String GET_COUNT = "SELECT count(dots_id) FROM dots WHERE owner = ?";
    private static final int limit = 20;


    public boolean addDot(Dot dot){
        try{
            PreparedStatement addStatement = connection.prepareStatement(ADD_DOT);
            addStatement.setDouble(1, dot.getX());
            addStatement.setDouble(2, dot.getY());
            addStatement.setDouble(3, dot.getR());
            addStatement.setBoolean(4, dot.getResult());
            addStatement.setString(5, dot.getOwner());
            //addStatement.setString(5,"owner");
            addStatement.setString(6, dot.getTime());
            addStatement.executeUpdate();
            addStatement.close();

            return true;
        } catch (SQLException e){
                e.printStackTrace();
            return false;
        }
    }
    //вернет количество страниц по 20 точек, для пагинации,
    public int strCount(String owner){
        try{
            PreparedStatement countStatement = connection.prepareStatement(GET_COUNT);
            countStatement.setString(1, owner);
            ResultSet resultSet = countStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return (int) Math.ceil(1.0 * count / limit);
        } catch (SQLException e){
            //
            return 0;
        }
    }



    //вернет список всех точек автора
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


    //вернет первые 20 точек автора,
    public List<Dot> get20DotsByOwner(String owner) {
        List<Dot> dots = new LinkedList<>();
        try {
            PreparedStatement getStatement = connection.prepareStatement(GET_20_DOTS_BY_OWNER);
            getStatement.setString(1, owner);
            ResultSet resultSet = getStatement.executeQuery();

            dots = dotsFromResult(resultSet);
           // System.out.println("size: " + dots.size());

        } catch (SQLException e){
            //
        }
        return dots;
    }

    //вернет 20 точек текущей страницы
    public List<Dot> getNext20DotsByOwner(String owner, int str) {
        List<Dot> dots = new LinkedList<>();
        try {
            PreparedStatement getStatement = connection.prepareStatement(GET_NEXT_20_DOTS_BY_OWNER);
            getStatement.setString(1, owner);
            getStatement.setInt(2, limit * str);
            ResultSet resultSet = getStatement.executeQuery();

            dots = dotsFromResult(resultSet);
        } catch (SQLException e){
            // return null;
        }
        return dots;
    }



    //вернет все точки из бд
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


    //переводит результ сет из бд в список точек
    private  List<Dot> dotsFromResult(ResultSet resultSet){
        List<Dot> dots = new LinkedList<>();
        try {
            double x, y, r;
            boolean result;
            String time, owner;
            int id;
            while (resultSet.next()) {
                x = resultSet.getDouble("x");
                y = resultSet.getDouble("y");
                r = resultSet.getDouble("r");
                result = resultSet.getBoolean("result");
                time = resultSet.getString("time");
                owner = resultSet.getString("owner");
                id = resultSet.getInt("dots_id");
                Dot dot = new Dot();
                dot.setX(x);
                dot.setY(y);
                dot.setR(r);
                dot.setResult(result);
                dot.setTime(time);
                dot.setOwner(owner);
                dot.setId(id);
                dots.add(dot);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return dots;
    }

}
