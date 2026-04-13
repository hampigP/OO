import java.awt.event.MouseEvent;
import java.awt.*;

public class SelectMode implements Mode {
    private GraphObject selectedObject = null;
    private Point lastPoint = null;

    private boolean isDraggingObject = false;
    private boolean isSelectingArea = false;
    private Point selectionStartPoint = null;

    private PortPosition currentResizePort = null;
    private GraphObject resizeObject = null;
    private boolean isResizing = false;

    private PortPosition getFlippedPortPosition(GraphObject obj, Point mousePoint, PortPosition currentPort){
        Rectangle bounds = obj.getBounds();
        if(bounds == null){
            return currentPort;
        }
        int centerX = bounds.x + bounds.width /2;
        int centerY = bounds.y + bounds.height/2;

        boolean isLeft = mousePoint.x < centerX;
        boolean isTop = mousePoint.y < centerY;

        switch(currentPort){
            case LEFT:
            case RIGHT:
                return isLeft ? PortPosition.LEFT : PortPosition.RIGHT;
            case TOP:
            case BOTTOM:
                return isTop ? PortPosition.TOP : PortPosition.BOTTOM;
            case TOP_LEFT:
            case TOP_RIGHT:
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                if(isTop && isLeft) return PortPosition.TOP_LEFT;
                if(isTop) return PortPosition.TOP_RIGHT;
                if(isLeft) return PortPosition.BOTTOM_LEFT;
                return PortPosition.BOTTOM_RIGHT;
            default:
                return currentPort;
        }
    }

    @Override
    public void mousePressed(MouseEvent e, Canvas canvas){
        Point p = e.getPoint();

        currentResizePort = null;
        resizeObject = null;
        isResizing = false;

        for(GraphObject obj : canvas.getSelectedObjects()){
            for(Port port : obj.getPorts()){
                if(port.contains(p)){
                    currentResizePort = port.getPortPosition();
                    resizeObject = obj;
                    isResizing = true;
                    lastPoint = p;
                    return;
                }
            }
        }

        selectedObject = canvas.findObjectAt(p);
        if(selectedObject != null){
            canvas.clearSelection();
            selectedObject.setSelected(true);
            lastPoint = p;
            isDraggingObject = true;
            isSelectingArea = false;
            selectionStartPoint = null;
        }else{
            canvas.clearSelection();
            isDraggingObject = false;
            isSelectingArea = true;
            selectionStartPoint = p;
            canvas.setSelectionBox(p,p);
        }
        canvas.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e, Canvas canvas){
        Point currentPoint = e.getPoint();
        if(isResizing && resizeObject != null && currentResizePort != null && lastPoint != null){
            int dx = currentPoint.x - lastPoint.x;
            int dy = currentPoint.y - lastPoint.y;

            resizeObject.resize(currentResizePort, dx, dy);

            currentResizePort = getFlippedPortPosition(resizeObject, currentPoint, currentResizePort);
            lastPoint = currentPoint;
            canvas.repaint();
        }else if(isDraggingObject && selectedObject != null && lastPoint != null){
            
            int dx = currentPoint.x - lastPoint.x;
            int dy = currentPoint.y - lastPoint.y;

            selectedObject.move(dx,dy);
            lastPoint = currentPoint;
            canvas.repaint();
        }else if (isSelectingArea && selectionStartPoint != null){
            canvas.setSelectionBox(selectionStartPoint, currentPoint);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e, Canvas canvas){
        if(isSelectingArea){
            Rectangle selectionRect = canvas.getSelectionRectangle();

            if(selectionRect != null){
                for(GraphObject obj : canvas.getObjects()){
                    Rectangle bounds = obj.getBounds();

                    if(bounds != null && selectionRect.contains(bounds)){
                        obj.setSelected(true);
                    }
                }
            }
            canvas.clearSelectionBox();
            canvas.repaint();
        }
        selectedObject = null;
        lastPoint = null;
        isDraggingObject = false;
        isSelectingArea = false;
        selectionStartPoint = null;

        currentResizePort = null;
        resizeObject = null;
        isResizing = false;
    }
}
