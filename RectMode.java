import java.awt.event.MouseEvent;
import java.awt.*;

public class RectMode implements Mode{
    private static int rectCount = 1;
    private Point startPoint = null;
    private RectObject previewRect = null;
    @Override
    public void mousePressed(MouseEvent e, Canvas canvas){
        // startPoint = e.getPoint();

        // previewRect = new RectObject(startPoint.x, startPoint.y,1,1);
        // previewRect.setLabel("Class" + rectCount);
        // previewRect.setFillColor(Color.BLACK);
        // canvas.setPreviewObject(previewRect);
    }

    @Override
    public void mouseDragged(MouseEvent e, Canvas canvas){
        // if(startPoint == null || previewRect == null) return;

        // Point current = e.getPoint();

        // int x = Math.min(startPoint.x, current.x);
        // int y = Math.min(startPoint.y, current.y);
        // int width = Math.abs(current.x - startPoint.x);
        // int height = Math.abs(current.y - startPoint.y);

        // previewRect = new RectObject(x, y, width, height);
        // previewRect.setLabel("Class"+rectCount);
        // previewRect.setFillColor(Color.BLACK);
        // canvas.setPreviewObject(previewRect);
    }
    @Override
    public void mouseReleased(MouseEvent e, Canvas canvas){
        // if(startPoint == null || previewRect == null) return;

        // Rectangle bounds = previewRect.getBounds();
        // if(bounds.width >= 5 && bounds.height >=5){
        //     previewRect.setFillColor(Color.WHITE);
        //     canvas.addObject(previewRect);
        //     rectCount++;
        // }

        
        // previewRect = null;
        // startPoint = null;
        // canvas.clearPreviewObject();
        // canvas.setCurrentMode((new SelectMode()));
    }
}
