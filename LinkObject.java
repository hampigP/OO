import java.awt.*;

public abstract class LinkObject extends GraphObject{
    protected Port startPort;
    protected Port endPort;

    public LinkObject(Port startPort, Port endPort){
        this.startPort = startPort;
        this.endPort = endPort;
    }

    @Override
    public void move(int dx, int dy){

    }

    @Override
    public boolean contains(Point p){
        return false;
    }
}
