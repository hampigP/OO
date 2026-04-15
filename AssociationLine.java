import java.awt.*;

public class AssociationLine extends LinkObject {
    public AssociationLine(Port startPort, Port endPort) {
        super(startPort, endPort);
    }

    @Override
    public void draw(Graphics g) {
        Point start = startPort.getPosition();
        Point end = endPort.getPosition();
        drawArrowLine(g,start,end);
    }
    
    private void drawArrowLine(Graphics g, Point start, Point end){
        Graphics2D g2 = (Graphics2D) g;

        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double len = Math.sqrt(dx * dx + dy * dy);
        if(len == 0) return;

        double ux = dx / len;
        double uy = dy / len;

        int arrowLength = 16;
        int arrowWidth = 8;

        g2.drawLine(start.x, start.y, end.x, end.y);

        Point left = new Point(
            (int) (end.x - ux * arrowLength - uy * arrowWidth),
            (int) (end.y - uy * arrowLength + ux * arrowWidth)
        );

        Point right = new Point(
            (int) (end.x - ux * arrowLength + uy * arrowWidth),
            (int) (end.y - uy * arrowLength - ux * arrowWidth)
        );

        g2.drawLine(end.x, end.y, left.x, left.y);
        g2.drawLine(end.x, end.y, right.x, right.y);
    }

    @Override
    public Point getPortPoint(PortPosition position){
        return null;
    }
}
