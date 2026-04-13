import java.awt.*;

public class AssociationLine extends LinkObject {
    public AssociationLine(Port startPort, Port endPort) {
        super(startPort, endPort);
    }

    @Override
    public void draw(Graphics g) {
        Point start = startPort.getPosition();
        Point end = endPort.getPosition();
        g.drawLine(start.x, start.y, end.x, end.y);
    }
    
    @Override
    public Point getPortPoint(PortPosition position){
        return null;
    }
}
