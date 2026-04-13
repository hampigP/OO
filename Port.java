import java.awt.*;

public class Port {
    private static final int SIZE = 10;

    private GraphObject owner;
    private PortPosition position;

    public Port(GraphObject owner, PortPosition position){
        this.owner = owner;
        this.position = position;
    }

    public Point getPosition(){
        return owner.getPortPoint(position);
    }

    public void draw(Graphics g){
        Point p = getPosition();
        g.fillRect(p.x-SIZE/2, p.y-SIZE/2, SIZE, SIZE);
    }

    public boolean contains(Point point){
        Point p = getPosition();
        return point.x >= p.x - SIZE / 2 && point.x <= p.x + SIZE / 2 &&
                point.y >= p.y - SIZE / 2 && point.y <= p.y + SIZE / 2;
    }

    public GraphObject getOwner(){
        return owner;
    }
    public PortPosition getPortPosition(){
        return position;
    }

}
