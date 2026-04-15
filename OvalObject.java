import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class OvalObject extends GraphObject{
    private int x;
    private int y;
    private int width;
    private int height;
    private static final int MIN_SIZE = 30;

    public OvalObject(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = "";
    }
    
    @Override
    public void draw(Graphics g){
        g.setColor(fillColor);
        g.fillOval(x,y,width,height);
        g.setColor(Color.BLACK);
        g.drawOval(x,y,width, height);
        drawLabel(g);

        if(selected){
            for(Port port : getPorts()){
                port.draw(g);
            }
        }
    }

    private void drawLabel(Graphics g){
        if(label == null || label.isEmpty()){
            return;
        }
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        int textHeight = fm.getAscent();

        int textX = x+(width-textWidth)/2;
        int textY = y+(height+textHeight)/2 - 4;

        if(fillColor.equals(Color.BLACK) || fillColor.equals(Color.DARK_GRAY) || fillColor.equals(Color.BLUE)){
            g.setColor(Color.WHITE);
        }else{
            g.setColor(Color.BLACK);
        }
        g.drawString(label,textX,textY);

    }

    @Override
    public boolean contains(Point p){
        double centerX = x+ width/2.0;
        double centerY = y+ height/2.0;
        double radiusX = width/2.0;
        double radiusY = height/2.0;

        if(radiusX == 0 || radiusY == 0){
            return false;
        }

        double dx = (p.x - centerX) / radiusX;
        double dy = (p.y - centerY) / radiusY;
        return dx * dx + dy * dy <= 1.0;
    }

    @Override
    public void move(int dx, int dy){
        x += dx;
        y += dy;
    }

    @Override
    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }

    @Override
    public List<Port> getPorts(){
        List<Port> ports = new ArrayList<>();

        ports.add(new Port(this, PortPosition.TOP));
        ports.add(new Port(this, PortPosition.BOTTOM));
        ports.add(new Port(this, PortPosition.LEFT));
        ports.add(new Port(this, PortPosition.RIGHT));
        return ports;
    }

    @Override
    public Point getPortPoint(PortPosition position){
        switch (position){
            case TOP:
                return new Point(x+width/2,y);
            case BOTTOM:
                return new Point(x+width/2,y+height);
            case LEFT:
                return new Point(x,y+height/2);
            case RIGHT:
                return new Point(x+width,y+height/2);
            default:
                return new Point(x,y);
        }
    }

    @Override
    public void resize(PortPosition portPosition, int dx, int dy){
        int left = x;
        int right = x + width;
        int top = y;
        int bottom = y + height;

        switch (portPosition) {
            case LEFT:
                left += dx;
                break;
            case RIGHT:
                right += dx;
                break;
            case TOP:
                top += dy;
                break;
            case BOTTOM:
                bottom += dy;
                break;
        }

        if (Math.abs(right - left) < MIN_SIZE) {
            int center = (left + right) / 2;
            left = center - MIN_SIZE / 2;
            right = center + MIN_SIZE / 2;
        }

        if (Math.abs(bottom - top) < MIN_SIZE) {
            int center = (top + bottom) / 2;
            top = center - MIN_SIZE / 2;
            bottom = center + MIN_SIZE / 2;
        }

        x = Math.min(left, right);
        y = Math.min(top, bottom);
        width = Math.abs(right - left);
        height = Math.abs(bottom - top);
        }
}
