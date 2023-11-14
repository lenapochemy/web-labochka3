import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean
@SessionScoped
public class DotsBean implements Serializable {
    FacesContext context = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
    String owner = session.getId();
    private Dot newDot = new Dot();
    private final DatabaseBean databaseBean = new DatabaseBean();
    private List<Dot> dots = new LinkedList<>();
    public List<Dot> getDots(){
        return dots;
    }

    @PostConstruct
    public void loadFromDatabase(){
        this.dots = databaseBean.getDotsByOwner(owner);
    }


   /* @PostConstruct
    public void loadAllDotsFromDatabase(){
        this.dots = databaseBean.getAllDots();
        dots = dots.stream().sorted(Comparator.comparing(Dot::getTime).reversed()).collect(Collectors.toList());

        System.out.println("добавили точечки: " + dots.size() + " штуков");
    }

    */

    public void add(){
        newDot.setResult(isInArea(newDot));
        this.addDot(newDot);
        newDot = new Dot(newDot.getX(), newDot.getY(), newDot.getR(), newDot.getOwner());
    }


    public void addDot(Dot dot){
        if(databaseBean.addDot(dot)) System.out.println("добавили точку в бд");
            else System.out.println(" не добавили точку в бд");
        this.dots.add(0, dot);
    }

    public void addFromCanvas(){
        final Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
       // params.values().forEach(System.out::println);

        try{
            double x = Double.parseDouble(params.get("x"));
            double y = Double.parseDouble(params.get("y"));
            double r = Double.parseDouble(params.get("r"));

            Dot dot = new Dot(x, y, r, owner);
            dot.setResult(isInArea(dot));
            this.addDot(dot);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }

    public void setNewDot(Dot newDot){
        this.newDot = newDot;
        this.newDot.setOwner(owner);
        this.newDot.setResult(isInArea(this.newDot));

    }

    public void setDots(List<Dot> dots){
        this.dots = dots;
    }
    public Dot getNewDot(){
        return newDot;
    }

    private boolean isInArea(Dot dot){
        Double x = dot.getX();
        Double y = dot.getY();
        Double r = dot.getR();

        return (-r/2 <= x && x <= 0 && -r <= y && y <= 0) ||
                (y >= (x/2) - (r/2) && x >= 0 && y <= 0) ||
                ((x * x + y * y <= (r/2) * (r/2)) && x >= 0 && y >= 0);

    }
}
