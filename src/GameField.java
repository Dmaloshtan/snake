import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.PublicKey;
import java.util.Random;

// Игровое поле
public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;  //размер поля
    private final int DOT_SIZE = 16; // размер в пикселях одной ячейки (или яблочка) 16х16 пикселей.
    private final int ALL_DOTS = 400; // сколько всего игровых единиц может поместиться на поле
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY; // позиция яблока

    private int[] x = new int[ALL_DOTS];  // змейка
    private int[] y = new int[ALL_DOTS];
    private int dots;  // сколько точек занимает змейка
    private Timer timer;

// поля отвечающие за текущее положение змейки
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

 // конструктор
    public GameField(){
        setBackground(Color.BLACK);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);  // фокус на игровом поле, чтобы клавиши коннектились с ним

    }

    // метод инициилизирующий начало игры

    public void initGame(){

        // положение змейки в начале игры

        dots = 3;
        for (int i = 0; i<dots; i++){
            x[i] = 48 - i*DOT_SIZE;
            y[i] = 48;

        }
        timer = new Timer(250,this); // частота, с которой будет двигаться змейка
        timer.start();
        createApple();
    }

    // метод создания яблока

    public void createApple(){
        appleX = new Random().nextInt(20)*DOT_SIZE; // 20 позиций может поместиться на игровое поле
        appleY = new Random().nextInt(20)*DOT_SIZE;
    }

// метод для загрузки картинок
    public void loadImages(){
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("snake.png");
        dot = iid.getImage();


    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){                            // Здесь мы отрисовываемся когда состяние в игре
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i],y[i],this);
            }
        } else { // Если игра закончена
           String str = "ПОТРАЧЕНО";
           Font f = new Font("Arial",62,Font.BOLD);
           g.setColor(Color.RED);
//           g.setFont(f);
           g.drawString(str,125,SIZE/2);

        }
    }

    // перемещение змейки
    public void move(){
        for (int i = dots; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(left){
            x[0] -= DOT_SIZE;
        }
        if(right){
            x[0] += DOT_SIZE;
        }
        if(up){
            y[0] -= DOT_SIZE;
        }
        if(down){
            y[0] += DOT_SIZE;
        }
    }

    // метод проверяющий, не встретили ли мы яблоко

    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){ // если голова змейки в яблоке
            dots++;   // добавляем одну ячейку к змейке
            createApple();  // создаем яблоко в другом месте

        }
    }

    // метод проверяющий не столкнулась ли змея сама с собой или со стеной

    public void checkCollisions(){
        for (int i = dots; i > 0 ; i--) {
            // это сама с собой
            if(i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        // это со стеной
        if (x[0]> SIZE){
            inGame = false;
        }
        if (x[0]< 0){
            inGame = false;
        }
        if (y[0]> SIZE){
            inGame = false;
        }
        if (y[0]< 0){
            inGame = false;
        }
    }



    // двигаем змейку
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame){
            checkApple();
            checkCollisions();
            move();

        }
        repaint();  // перерисовка поля
    }

    // Управление змейкой

    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode(); // узнаем какая клавиша была нажата
            if (key == KeyEvent.VK_LEFT && !right){  // Если была нажата клавиша влево и я не двигаюсь в право (т.к. нельзя сразу развернуться справа налево)
               left = true;
               up = false;
               down = false;
            }

            if (key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_UP && !down){  // Если была нажата клавиша влево и я не двигаюсь в право (т.к. нельзя сразу развернуться справа налево)
                up = true;
                right = false;
                left = false;
            }

            if (key == KeyEvent.VK_DOWN && !up){  // Если была нажата клавиша влево и я не двигаюсь в право (т.к. нельзя сразу развернуться справа налево)
                down = true;
                right = false;
                left = false;
            }

        }
    }


}
