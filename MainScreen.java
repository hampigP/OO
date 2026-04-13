import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainScreen extends JFrame {
    private Canvas canvas;
    private JButton selectButton;
    private JButton rectButton;
    private JButton ovalButton;
    private JButton associationButton;
    private JButton generalizationButton;
    private JButton compositionButton;
    
    

    private Color defaultButtonColor;
    private Color activeButtonColor = Color.DARK_GRAY;



    public MainScreen() {
        setTitle("UML editor");
        setSize(1000,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        canvas = new Canvas();
        add(canvas, BorderLayout.CENTER);
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenuItem labelMenuItem = new JMenuItem("Label");
        JMenuItem groupMenuItem = new JMenuItem("Group");
        JMenuItem ungroupMenuItem = new JMenuItem("Ungroup");
        editMenu.add(labelMenuItem);
        editMenu.add(groupMenuItem);
        editMenu.add(ungroupMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);

        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new GridLayout(6,1,5,5));
        selectButton = new JButton("Select");
        rectButton = new JButton("Rect");
        ovalButton = new JButton("Oval");
        associationButton = new JButton("Association");
        generalizationButton = new JButton("Generalization");
        compositionButton = new JButton("Composition");
        
        

        defaultButtonColor = selectButton.getBackground();
        
        selectButton.addActionListener(e -> {
            canvas.cancelToolbarDrag();
            canvas.setCurrentMode(new SelectMode());
            highlightToolButton(selectButton);
        });

        labelMenuItem.addActionListener(e -> {
            GraphObject selectedObject = canvas.getSingleSelectedObject();
            if(selectedObject == null){
                JOptionPane.showMessageDialog(this, "請先選取一個物件");
                return;
            }
            if(selectedObject instanceof LinkObject || selectedObject instanceof CompositeObject){
                JOptionPane.showMessageDialog(this, "只有基本圖形可以修改標籤或顏色");
                return;
            }

            JTextField nameField = new JTextField(selectedObject.getLabel(),12);

            JButton colorButton = new JButton("選擇顏色");
            JPanel colorPreview = new JPanel();
            colorPreview.setPreferredSize(new Dimension(40,20));
            colorPreview.setBackground(selectedObject.getFillColor());
            final Color[] selectedColor = {selectedObject.getFillColor()};
            colorButton.addActionListener(ev -> {
                Color chosen = JColorChooser.showDialog(
                    this,
                    "選擇物件顏色",
                    selectedColor[0]
                );
                if(chosen != null){
                    selectedColor[0] = chosen;
                    colorPreview.setBackground(chosen);
                }
            }); 

            JPanel panel = new JPanel(new GridLayout(2,2,10,10));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(colorButton);
            panel.add(colorPreview);
            int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Customize Label Style",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if(result == JOptionPane.OK_OPTION){
                String newLabel = nameField.getText().trim();
                selectedObject.setLabel(newLabel);
                selectedObject.setFillColor(selectedColor[0]);
                canvas.repaint();
            }
        });

        rectButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                canvas.startToolbarDrag("RECT");
                highlightToolButton(rectButton);
            }

            @Override
            public void mouseReleased(MouseEvent e){
                Point screenPoint = e.getLocationOnScreen();
                Point canvasPoint = new Point(screenPoint);
                SwingUtilities.convertPointFromScreen(canvasPoint, canvas);

                if(canvas.contains(canvasPoint)){
                    canvas.finishToolbarDrag(canvasPoint);
                }else{
                    canvas.cancelToolbarDrag();
                }
            }
        });

        rectButton.addMouseMotionListener(new MouseAdapter() {
          @Override
           public void mouseDragged(MouseEvent e){
                Point screenPoint = e.getLocationOnScreen();
                Point canvasPoint = new Point(screenPoint);
                SwingUtilities.convertPointFromScreen(canvasPoint, canvas);

                canvas.updateToolbarDragPreview(canvasPoint);
           }   
        });
           

        ovalButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                canvas.startToolbarDrag("OVAL");
                highlightToolButton(ovalButton);
            }

            @Override
            public void mouseReleased(MouseEvent e){
                Point screenPoint = e.getLocationOnScreen();
                Point canvasPoint = new Point(screenPoint);
                SwingUtilities.convertPointFromScreen(canvasPoint, canvas);

                if(canvas.contains(canvasPoint)){
                    canvas.finishToolbarDrag(canvasPoint);
                }else{
                    canvas.cancelToolbarDrag();
                }
            }
        });

        ovalButton.addMouseMotionListener(new MouseAdapter() {
           @Override
           public void mouseDragged(MouseEvent e){
            Point screePoint = e.getLocationOnScreen();
            Point canvasPoint = new Point(screePoint);
            SwingUtilities.convertPointFromScreen(canvasPoint, canvas);

            canvas.updateToolbarDragPreview(canvasPoint);
           } 
        });

        
        
        associationButton.addActionListener(e -> {
            canvas.cancelToolbarDrag();
            canvas.setCurrentMode(new AssociationMode());
            highlightToolButton(associationButton);
        });
        generalizationButton.addActionListener(e -> {
            canvas.cancelToolbarDrag();
            canvas.setCurrentMode(new GeneralizationMode());
            highlightToolButton(generalizationButton);
        });
        compositionButton.addActionListener(e -> {
            canvas.cancelToolbarDrag();
            canvas.setCurrentMode(new CompositionMode());
            highlightToolButton(compositionButton);
        });
        groupMenuItem.addActionListener(e-> canvas.groupSelectedObjects());
        ungroupMenuItem.addActionListener(e-> canvas.ungroupSelectedObject());

        
        toolPanel.add(selectButton);
        toolPanel.add(rectButton);
        toolPanel.add(ovalButton);
        toolPanel.add(associationButton);
        toolPanel.add(generalizationButton);
        toolPanel.add(compositionButton);

        
        add(toolPanel, BorderLayout.WEST);
        highlightToolButton(selectButton);
        setVisible(true);
    }

    public void highlightToolButton(JButton activeButton){
        JButton[] buttons = {
            selectButton, rectButton, ovalButton, associationButton, generalizationButton, compositionButton
        };
        for(JButton button : buttons){
            button.setBackground(defaultButtonColor);
            button.setForeground(Color.BLACK);
            button.setOpaque(true);
            button.setBorderPainted(true);
        }

        activeButton.setBackground(activeButtonColor);
        activeButton.setForeground(Color.WHITE);
        activeButton.setOpaque(true);
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(MainScreen::new);
    }
    
    
}