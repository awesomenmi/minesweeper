import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.apple.eawt.Application;
import sweeper.Box;
import sweeper.Coord;
import sweeper.Game;
import sweeper.Ranges;

public class JavaSweeper extends JFrame /*чтобы программа запускалась в виде окна*/ {

    private Game game;
    private JPanel panel; //добавляем форму панель, чтобы на ней можно было потом рисовать
    private JLabel label;
    private final int COLS = 9;
    private final int ROWS = 9;
    private final int BOMBS = 10;
    private final int IMAGE_SIZE = 50;
    private boolean isMousePressed;


    public static void main(String[] args) {
        new JavaSweeper();
    }

    private JavaSweeper(){ /*конструктор*/
        game = new Game (COLS, ROWS, BOMBS); //'фасадный класс// '
        game.start();
        //Ranges.setSize (new Coord (COLS, ROWS));
        setImages();
        initLabel();
        initPanel();
        initFrame();
    }
    private void initLabel(){
        label = new JLabel("Welcome!");
        add (label, BorderLayout.SOUTH);
    }

    private void initPanel ()
    {
        panel = new JPanel()// инициализация панели
        {//анонимный класс
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //Coord coord = new Coord (box.ordinal() * IMAGE_SIZE, 0);
                for (Coord coord : Ranges.getAllCoords())
                    g.drawImage((Image) game.getBox(coord).image, coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE, this); //отображаем картинки
            }
        };

        panel .addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isMousePressed = true;
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord (x,y);
                if (e.getButton()== MouseEvent.BUTTON1)
                    game.pressLeftButton(coord);
                if (e.getButton()== MouseEvent.BUTTON3)
                    game.pressRightButton(coord);
                //if (e.getButton()== MouseEvent.BUTTON2)
                //    game.start();
                label.setText (getMessage());
                panel.repaint(); //перерисовываем форму, чтобы были видны изменения
            }
            public void mouseReleased(MouseEvent e){
                isMousePressed = false;
            }
        });


        panel.setPreferredSize(new Dimension(Ranges.getSize().x * IMAGE_SIZE,Ranges.getSize().y *IMAGE_SIZE));
        add(panel);
    }

    private String getMessage() {
        switch (game.getState())
        {
            case PLAYED: return "Think twice!";
            case BOMBED: return "You LOSE!";
            case WINNER: return "CONGRATS!";
            default    : return "Welcome!";
        }
    }

    private void initFrame () {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//действие по умолчанию при закрытии программы
        setTitle("Сапер");
        setResizable(false);
        setVisible(true);
        pack();//меняет размер формы чтобы там все поместилось
        setLocationRelativeTo(null); // размещаем окошко по центру

        //Application.getApplication().setDocIconImage(getImage("icon"));
    }

    private void setImages()
    {
        for (Box box : Box.values()){
            box.image = getImage(box.name().toLowerCase()); //подгружаем картинки
        }
    }

    private Image getImage (String name){
        String filename = "img/" + name+ ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }


}
