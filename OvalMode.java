import java.awt.event.MouseEvent;
import java.awt.*;

public class OvalMode implements Mode {
    private static int ovalCount = 1;
    private Point startPoint = null;
    private OvalObject previewOval = null;
    @Override
    public void mousePressed(MouseEvent e, Canvas canvas){
        // startPoint = e.getPoint();

        // previewOval = new OvalObject(startPoint.x, startPoint.y,1,1);
        // previewOval.setLabel("UseCase" + ovalCount);
        // previewOval.setFillColor(Color.BLACK);
        // canvas.setPreviewObject(previewOval);
    }

    @Override
    public void mouseDragged(MouseEvent e, Canvas canvas) {
        // if(startPoint == null || previewOval == null) return;

        // Point current = e.getPoint();

        // int x = Math.min(startPoint.x, current.x);
        // int y = Math.min(startPoint.y, current.y);
        // int width = Math.abs(current.x - startPoint.x);
        // int height = Math.abs(current.y - startPoint.y);

        // previewOval = new OvalObject(x, y, width, height);
        // previewOval.setLabel("UseCase"+ovalCount);
        // previewOval.setFillColor(Color.BLACK);
        // canvas.setPreviewObject(previewOval);
    }

    @Override
    public void mouseReleased(MouseEvent e, Canvas canvas) {
    //     if(startPoint == null || previewOval == null) return;

    //     Rectangle bounds = previewOval.getBounds();
    //     if(bounds.width >= 5 && bounds.height >= 5){
    //         previewOval.setFillColor(Color.WHITE);
    //         canvas.addObject(previewOval);
    //         ovalCount++;
    //     }

        
    //     previewOval = null;
    //     startPoint = null;
    //     canvas.clearPreviewObject();
    //     canvas.setCurrentMode((new SelectMode()));
    } 
}
