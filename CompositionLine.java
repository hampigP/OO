import java.awt.*;

public class CompositionLine extends LinkObject {
    public CompositionLine(Port startPort, Port endPort){
        super(startPort, endPort);
    }

    @Override
    public void draw(Graphics g) {
        Point start = startPort.getPosition();
        Point end = endPort.getPosition();
        drawLineWithDiamond(g,start,end);
    }
    
    private void drawLineWithDiamond(Graphics g, Point start, Point end){
        Graphics2D g2 = (Graphics2D) g;

        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double len = Math.sqrt(dx * dx + dy * dy);
        if(len == 0) return;

        double ux = dx / len;
        double uy = dy / len;

        int diamondLength = 20;
        int diamondWidth = 10;
        Point tip = end;
        Point center = new Point(
            (int)(end.x - ux * diamondLength),
            (int)(end.y - uy * diamondLength)
        );
        Point rear = new Point(
            (int)(end.x - ux * diamondLength * 2),
            (int)(end.y - uy * diamondLength * 2)
        );
        Point left = new Point(
            (int)(center.x - uy * diamondWidth),
            (int)(center.y + ux * diamondWidth)
        );
        Point right = new Point(
            (int)(center.x + uy * diamondWidth),
            (int)(center.y - ux * diamondWidth)
        );
        g2.drawLine(start.x, start.y, rear.x, rear.y);
        int[] xs = {tip.x, left.x, rear.x, right.x};
        int[] ys = {tip.y, left.y, rear.y, right.y};
        g2.drawPolygon(xs,ys,4);
    }
}
