import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public abstract class GraphObject {
    protected boolean selected = false;
    protected String label = "";
    protected Color fillColor = Color.WHITE;

    public abstract void draw(Graphics g);
    public abstract boolean contains(Point p);
    public abstract void move(int dx, int dy);


    
    public List<Port> getPorts(){
        return new ArrayList<>();
    }

    public Point getPortPoint(PortPosition position){
        return null;
    }

    public Rectangle getBounds(){
        return null;
    }

    public void resize(PortPosition portPosition, int dx, int dy){

    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public boolean isSelected(){
        return selected;
    }

    public String getLabel(){
        return label;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public void setFillColor(Color fillColor){
        this.fillColor = fillColor;
    }

    public Color getFillColor(){
        return fillColor;
    }
}
