import java.awt.*;
import java.awt.event.MouseEvent;

public class AssociationMode implements Mode {
    private Port startPort = null;

    @Override
    public void mousePressed(MouseEvent e, Canvas canvas){
        startPort = canvas.findPortAt(e.getPoint());

        if(startPort != null){
            Point startPoint = startPort.getPosition();
            canvas.setPreviewLine(startPoint, startPoint);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e, Canvas canvas){
        if(startPort != null){
            canvas.setPreviewLine(startPort.getPosition(), e.getPoint());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e, Canvas canvas){
        Port endPort = canvas.findPortAt(e.getPoint());
        if(startPort != null && endPort != null && startPort != endPort){
            AssociationLine line = new AssociationLine(startPort, endPort);
            canvas.addObject(line);
        }
        startPort = null;
        canvas.clearPreviewLine();
    }
    
}
