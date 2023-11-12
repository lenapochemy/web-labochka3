import javax.annotation.ManagedBean;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@ManagedBean
@SessionScoped
public class DotsBean implements Serializable {
    //String owner = sessionid;
    private Dot newDot = new Dot();
    private final DatabaseBean databaseBean = new DatabaseBean();

    private List<Dot> dots = new LinkedList<Dot>();
    public List<Dot> getDots(){
        return dots;
    }

    /*public void loadFromDatabase(){
        this.dots = databaseBean.getDotsByOwner(owner);
    }

     */
    public void loadAllDotsFromDatabase(){
        this.dots = databaseBean.getAllDots();
    }

    public void add(){
        newDot.setResult(isInArea(newDot));
        this.addDot(newDot);
        newDot = new Dot(newDot.getX(), newDot.getY(), newDot.getR(), newDot.getOwner());
    }


    public void addDot(Dot dot){
        databaseBean.addDot(dot);
        this.dots.add(dot);
    }

    public void addFromJS(){

    }

    public void setNewDot(Dot newDot){
        this.newDot = newDot;
        this.newDot.setResult(isInArea(this.newDot));

    }

    public void setDots(List<Dot> dots){
        this.dots = dots;
    }
    public Dot getNewDot(){
        return newDot;
    }


    public void dotHandle(){
        databaseBean.addDot(newDot);
    }

    public DotsBean(){
        this.newDot = new Dot();
    }




    private boolean isInArea(Dot dot){
        Double x = dot.getX();
        Double y = dot.getY();
        Double r = dot.getR();

        return (-r/2 <= x && x <= 0 && -r <= y && y <= 0) ||
                (y >= (x/2) - (r/2) && x >= 0 && y <= 0) ||
                ((x * x + y * y <= r/2 * r/2) && x >= 0 && y >= 0);

    }
}
