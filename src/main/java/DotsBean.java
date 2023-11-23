import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@ManagedBean
@SessionScoped
public class DotsBean implements Serializable {
    FacesContext context = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
    //private String owner = session.getId();
    private String owner = "owner";
    private int str = 0;
    //private boolean nextButton = false;
    //private boolean prevButton = false;
    private Dot newDot = new Dot();
    private final DatabaseBean databaseBean = new DatabaseBean();
    private List<Dot> dots = new LinkedList<>();
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public List<Dot> getDots(){
        return dots;
    }
    private List<Integer> pagBut = new ArrayList<>();
    public List<Integer> getPagBut(){
        return pagBut;
    }

    public void newPagBut(){
        pagBut.clear();
        int count = databaseBean.strCount(owner, str);
        if(count <= 5) {
            for (int i = 0; i < count; i++) {
                pagBut.add(i);
            }
        } else {
            pagBut.add(0);

            if(str < 4) {
                for(int i = 1; i <= str; i++){
                    pagBut.add(i);
                }
            } else {
                pagBut.add(-1);
                for(int i = str -2; i <= str; i++){
                    pagBut.add(i);
                }
            }

            if(str + 3 < count){
                for(int i = str; i <= str + 2; i++){
                    pagBut.add(i);
                }
                pagBut.add(-1);
            } else {
                for(int i = str; i < count - 1; i++){
                    pagBut.add(i);
                }
            }
            pagBut.add(count - 1);
//            if(str <= 4) {
//                for(int i = 0; i <= str + 2 && i <= count; i++){
//                    pagBut.add(i);
//                }
//                if(str + 2 + 1 < count) pagBut.add(-1);
//                pagBut.add(count);
//
//            }
//
//

//            pagBut.add(0);
//            if(0 < str - 2 - 1) pagBut.add(-1);
//            int i;
//            if(str < 2) {i =0;}
//                else {i = str - 2;}
//            while(i <= str + 2){
//                pagBut.add(i);
//                i++;
//            }
//
//            if(str + 2 + 1 < count) pagBut.add(-1);
//            pagBut.add(count);

//           pagBut.add(0);
//           for(int i = str - 2; (0 <= i) && (i <= str + 2) && (i <= count); i++){
//               pagBut.add(i);
//           }
//           pagBut.add(0);
          // pagBut.add(count);
            pagBut = new HashSet<>(pagBut).stream().sorted().collect(Collectors.toList());
        }


    }



    public String getPageClass(int page){
        if(page == -1) return "nonePag";
        if(page == str) return "currentPag";
        return "pagination";
    }

//    @PostConstruct
//    public void loadFromDatabase(){
////        this.dots = databaseBean.getDotsByOwner(owner);
//        str = 0;
//        this.dots = databaseBean.getNext20DotsByOwner(owner, str);
//        newPagBut();
//        if(databaseBean.getHasNext()) nextButton = true;
//
//       // if(databaseBean.getMoreThan20()) nextButton = true;
//
//    }

//    public void loadPrevFromDatabase(){
//        str--;
//        this.dots = databaseBean.getNext20DotsByOwner(owner, str);
//        prevButton = str > 0;
//        newPagBut();
//        nextButton = databaseBean.getHasNext();
//
//    }
//
//    public void loadNextFromDatabase(){
////        this.dots = databaseBean.getDotsByOwner(owner);
//        str++;
//        this.dots = databaseBean.getNext20DotsByOwner(owner, str);
//        prevButton = true;
//        newPagBut();
//        nextButton = databaseBean.getHasNext();
//    }

    @PostConstruct
    public void loadStartStr(){
        loadStr(0);
    }

    public void loadStr(int str){
        this.str = str;
        this.dots = databaseBean.getNext20DotsByOwner(owner, str);
        newPagBut();
       // nextButton = databaseBean.getHasNext();
        //prevButton = str > 0;
    }


//    public boolean getNextButton(){
//        return nextButton;
//    }
//    public boolean getPrevButton(){
//        return prevButton;
//    }

   /* @PostConstruct
    public void loadAllDotsFromDatabase(){
        this.dots = databaseBean.getAllDots();
        dots = dots.stream().sorted(Comparator.comparing(Dot::getTime).reversed()).collect(Collectors.toList());

        System.out.println("добавили точечки: " + dots.size() + " штуков");
    }

    */

    public void add(){
        newDot.setResult(isInArea(newDot));
        Date d = new Date();
        newDot.setTime(formatter.format(d));
        newDot.setOwner(owner);
       // newDot.setOwner("owner");
      //  this.time = formatter.format(d);
        this.addDot(newDot);
        //loadFromDatabase();
//        str = 0;
//        loadNextFromDatabase();
        newDot = new Dot(newDot.getX(), newDot.getY(), newDot.getR(), newDot.getOwner());
    }


    public void addDot(Dot dot){
        if(databaseBean.addDot(dot)) System.out.println("добавили точку в бд");
            else System.out.println(" не добавили точку в бд");
        this.dots.add(0, dot);
        newPagBut();
        str = 0;
        loadStr(str);
        //loadFromDatabase();
    }

    public void addFromCanvas(){
        final Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
       // params.values().forEach(System.out::println);
        try{
            double x = Double.parseDouble(params.get("x"));
            double y = Double.parseDouble(params.get("y"));
            double r = Double.parseDouble(params.get("r"));

            Dot dot = new Dot(x, y, r, owner);
            Date d = new Date();
            dot.setTime(formatter.format(d));
            dot.setResult(isInArea(dot));
            this.addDot(dot);
//
//            str = 0;
//            loadNextFromDatabase();
            //newPagBut();

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
