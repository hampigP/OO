import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CompositeObject extends GraphObject{
    private List<GraphObject> children;

    public CompositeObject(List<GraphObject> children){
        this.children = new ArrayList<>(children);
    }

    public List<GraphObject> getChildren(){
        return children;
    }

    @Override
    public void draw(Graphics g){
        for(GraphObject child : children){
            child.draw(g);
        }

        if(selected){
            Rectangle bounds = getBounds();
            if(bounds != null){
                g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
    }

    @Override
    public boolean contains(Point p){
        Rectangle bounds = getBounds();
        return bounds != null && bounds.contains(p);
    }

    @Override
    public void move(int dx, int dy){
        for(GraphObject child : children){
            child.move(dx, dy);
        }
    }

    @Override
    public Rectangle getBounds(){
        if(children.isEmpty()){
            return null;
        }

        Rectangle bounds = children.get(0).getBounds();
        if(bounds == null){
            return null;
        }

        int minX = bounds.x;
        int minY = bounds.y;
        int maxX = bounds.x + bounds.width;
        int maxY = bounds.y + bounds.height;

        for(int i = 1; i<children.size(); i++){
            Rectangle childBounds = children.get(i).getBounds();
            if(childBounds == null){
                continue;
            }

            minX = Math.min(minX, childBounds.x);
            minY = Math.min(minY, childBounds.y);
            maxX = Math.max(maxX, childBounds.x + childBounds.width);
            maxY = Math.max(maxY, childBounds.y + childBounds.height);
        }
        return new Rectangle(minX,minY,maxX-minX,maxY-minY);
    }
}
