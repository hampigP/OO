import java.awt.*;

public class GeneralizationLine extends LinkObject {
    public GeneralizationLine(Port startPort, Port endPort) {
        super(startPort, endPort);
    }

    @Override
    public void draw(Graphics g) {
        Point start = startPort.getPosition();
        Point end = endPort.getPosition();
        drawLineWithTriangle(g,start,end);
    }
    private void drawLineWithTriangle(Graphics g, Point start, Point end){
        Graphics2D g2 = (Graphics2D) g;

        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double len = Math.sqrt(dx * dx + dy * dy);
        if(len == 0) return;

        double ux = dx / len;
        double uy = dy / len;

        int arrowLength = 20;
        int arrowWidth = 10;
        Point tip = end;
        Point baseCenter = new Point(
            (int)(end.x - ux * arrowLength),
            (int)(end.y - uy * arrowLength)
        );
        Point left = new Point(
            (int)(baseCenter.x - uy * arrowWidth),
            (int)(baseCenter.y + ux * arrowWidth)
        );
        Point right = new Point(
            (int)(baseCenter.x + uy * arrowWidth),
            (int)(baseCenter.y - ux * arrowWidth)
        );
        g2.drawLine(start.x, start.y, baseCenter.x, baseCenter.y);
        int[] xs = {tip.x, left.x, right.x};
        int[] ys = {tip.y, left.y, right.y};
        g2.drawPolygon(xs,ys,3);
    }
    
}
