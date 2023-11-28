
public class Dot {

    private Double x;
    private Double y;
    private Double r;
    private boolean result;
    private String owner;
    private String time;
    private int id;
//    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public Dot(Double x, Double y, Double r, String owner){
        this.x = x;
        this.y = y;
        this.r = r;
        this.owner = owner;
        //this.hit = isInArea();
//        Date d = new Date();
//        this.time = formatter.format(d);
    }

    public Dot(){
//        Date d = new Date();
//        this.time = formatter.format(d);
    }


    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getR() {
        return r;
    }
    public String getOwner(){
        return owner;
    }

    public String getTime() {
        return time;
    }

    public boolean getResult() {
        return result;
    }
    public int getId(){
        return id;
    }

    public String getResultString(){
        if(result) return "Точка попала";
        else return "Точка не попала";
    }

    public String getResultClass(){
        if(result) return "success";
        else return "fail";
    }
    public void setX(Double x){
        this.x = x;
    }
    public void setY(Double y){
        this.y = y;
    }
    public void setR(Double r){
        this.r = r;
    }
    public void setOwner(String owner){
        this.owner = owner;
    }
    public void setResult(boolean isInArea){
        this.result = isInArea;
    }
    public void setTime(String time){
        this.time = time;
    }

    public void setId(int id){
        this.id = id;
    }

}
