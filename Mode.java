import java.awt.event.MouseEvent;

public interface Mode {
    void mousePressed(MouseEvent e, Canvas canvas);
    void mouseDragged(MouseEvent e, Canvas canvas);
    void mouseReleased(MouseEvent e, Canvas canvas);
}
