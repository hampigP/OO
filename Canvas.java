import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.util.List;


public class Canvas extends JPanel {
    private ArrayList<GraphObject> objects;
    private Mode currentMode;

    private Point previewStart = null;
    private Point previewEnd = null;
    private boolean showPreviewLine = false;

    private Point selectionStart = null;
    private Point selectionEnd = null;
    private boolean showSelectionBox = false;
    // private GraphObject previewObject = null;

    private String draggingToolType = null;
    private Point draggingPreviewPoint = null;
    private boolean toolbarDragging = false;
    private static final int TOOL_WIDTH = 100;
    private static final int TOOL_HEIGHT = 80;

  


    public Canvas(){
        setBackground(Color.WHITE);

        objects = new ArrayList<>();
        currentMode = new RectMode();
        
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                if(toolbarDragging) return;
                if(currentMode != null){
                    currentMode.mousePressed(e, Canvas.this);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
                if(toolbarDragging) return;
                if(currentMode != null){
                    currentMode.mouseReleased(e, Canvas.this);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                if(toolbarDragging) return;
                if(currentMode != null){
                    currentMode.mouseDragged(e, Canvas.this);
                }
            }
        });
    }
    public void addObject(GraphObject obj){
        objects.add(obj);
        repaint();
    }
    public void setCurrentMode(Mode mode){
        this.currentMode = mode;
        clearPreviewLine();
        clearSelectionBox();
        repaint();
    }
    public Mode getCurrentMode(){
        return currentMode;
    }
    public void clearSelection(){
        for(GraphObject obj : objects){
            obj.setSelected(false);
        }
        repaint();
    }
    public GraphObject findObjectAt(Point p){
        for(int i = objects.size() - 1; i>=0; i--){
            GraphObject obj = objects.get(i);
            if(obj.contains(p)){
                return obj;
            }
        }
        return null;
    }
    public Port findPortAt(Point p){
        for(int i = objects.size() - 1; i>=0; i--){
            GraphObject obj = objects.get(i);

            for(Port port : obj.getPorts()){
                if(port.contains(p)){
                    return port;
                }
            }
        }
        return null;
    }
    public ArrayList<GraphObject> getObjects(){
        return objects;
    }

    public void setPreviewLine(Point start, Point end){
        previewStart = start;
        previewEnd = end;
        showPreviewLine = true;
        repaint();
    }

    public void clearPreviewLine(){
        previewStart = null;
        previewEnd = null;
        showPreviewLine = false;
        repaint();
    }

    public void setSelectionBox(Point start, Point end){
        selectionStart = start;
        selectionEnd = end;
        showSelectionBox = true;
        repaint();
    }

    public void clearSelectionBox(){
        selectionStart = null;
        selectionEnd = null;
        showSelectionBox = false;
        repaint();
    }

    public Rectangle getSelectionRectangle(){
        if(selectionStart == null || selectionEnd == null){
            return null;
        }
        int x = Math.min(selectionStart.x, selectionEnd.x);
        int y = Math.min(selectionStart.y, selectionEnd.y);
        int width = Math.abs(selectionStart.x - selectionEnd.x);
        int height = Math.abs(selectionStart.y - selectionEnd.y);
        return new Rectangle(x,y,width,height);
    }

    public ArrayList<GraphObject> getSelectedObjects(){
         ArrayList<GraphObject> selectedObjects = new ArrayList<>();

        for(GraphObject obj : objects){
            if(obj.isSelected()){
                selectedObjects.add(obj);
            }
        }
        return selectedObjects;
    }

    public void removeObjects(List<GraphObject> objectsToRemove){
        objects.removeAll(objectsToRemove);
        repaint();
    }

    public void groupSelectedObjects(){
        ArrayList<GraphObject> selectedObjects = getSelectedObjects();
        ArrayList<GraphObject> groupTargets = new ArrayList<>();
        for(GraphObject obj : selectedObjects){
            if(!(obj instanceof LinkObject)){
                groupTargets.add(obj);
            }
        }

        if(groupTargets.size()<2){
            return;
        }
        
        for(GraphObject obj : groupTargets){
            obj.setSelected(false);
        }

        CompositeObject group = new CompositeObject(groupTargets);
        group.setSelected(true);

        objects.removeAll(groupTargets);
        objects.add(group);
        repaint();
    }

    public void ungroupSelectedObject(){
        ArrayList<GraphObject> selectedObjects = getSelectedObjects();
        if(selectedObjects.size() != 1){
            return;
        }

        GraphObject selected = selectedObjects.get(0);
        if(!(selected instanceof CompositeObject)){
            return;
        }

        CompositeObject composite = (CompositeObject) selected;
        List<GraphObject> children = composite.getChildren();

        objects.remove(composite);
        for(GraphObject child : children){
            child.setSelected(true);
            objects.add(child);
        }
        repaint();
    }

    public GraphObject getSingleSelectedObject(){
        ArrayList<GraphObject> selectedObjects = getSelectedObjects();

        if(selectedObjects.size() == 1){
            return selectedObjects.get(0);
        }
        return null;
    }

    public void editSelectedObjectLabel(String newLabel){
        GraphObject selectedObject = getSingleSelectedObject();
        if(selectedObject == null){
            return;
        }
        if(selectedObject instanceof LinkObject){
            return;
        }
        if(selectedObject instanceof CompositeObject){
            return;
        }
        selectedObject.setLabel(newLabel);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        for(GraphObject obj : objects){
            obj.draw(g);
        }

        if(showPreviewLine && previewStart != null && previewEnd != null){
            g.drawLine(previewStart.x, previewStart.y, previewEnd.x, previewEnd.y);
        }

        if(showSelectionBox && selectionStart != null && selectionEnd != null){
            Rectangle rect = getSelectionRectangle();
            if(rect != null){
                g.drawRect(rect.x, rect.y, rect.width, rect.height);
            }
        }
     
        if(toolbarDragging && draggingToolType != null && draggingPreviewPoint != null){
            int x = draggingPreviewPoint.x - TOOL_WIDTH/2;
            int y = draggingPreviewPoint.y - TOOL_HEIGHT/2;

            g.setColor(Color.BLACK);

            if("RECT".equals(draggingToolType)){
                g.fillRect(x, y, TOOL_WIDTH, TOOL_HEIGHT);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, TOOL_WIDTH, TOOL_HEIGHT);
            }else if("OVAL".equals(draggingToolType)){
                g.fillOval(x, y, TOOL_WIDTH, TOOL_HEIGHT);
                g.setColor(Color.BLACK);
                g.drawOval(x, y, TOOL_WIDTH, TOOL_HEIGHT);
            }
        }
    }
    public void startToolbarDrag(String toolType){
        draggingToolType = toolType;
        draggingPreviewPoint = null;
        toolbarDragging = true;
        clearSelection();
        repaint();
    }

    public void updateToolbarDragPreview(Point canvasPoint){
        if(!toolbarDragging) return;
        draggingPreviewPoint = canvasPoint;
        repaint();
    }

    public void finishToolbarDrag(Point canvasPoint){
        if(!toolbarDragging) return;

        if(canvasPoint != null && isPointInCanvas(canvasPoint)){
            int x = canvasPoint.x - TOOL_WIDTH/2;
            int y = canvasPoint.y - TOOL_HEIGHT/2;

            if("RECT".equals(draggingToolType)){
                RectObject rect = new RectObject(x, y, TOOL_WIDTH, TOOL_HEIGHT);
                
                rect.setFillColor(Color.WHITE);
                addObject(rect);
            }else if("OVAL".equals(draggingToolType)){
                OvalObject oval = new OvalObject(x, y, TOOL_WIDTH, TOOL_HEIGHT);
                
                oval.setFillColor(Color.WHITE);
                addObject(oval);
            }
        }
        draggingToolType = null;
        draggingPreviewPoint = null;
        repaint();
    }

    public boolean isPointInCanvas(Point p){
        return p.x >= 0 && p.x <= getWidth() && p.y >= 0 && p.y <= getHeight();
    }

    public void cancelToolbarDrag(){
        draggingToolType = null;
        draggingPreviewPoint = null;
        toolbarDragging = false;
        repaint();
    }
}