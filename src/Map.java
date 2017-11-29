import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class Map {
    public Double offset_x, offset_y, loc_x, loc_y, head_x, head_y, x_diff, y_diff, left_x, left_y, length, width;
    private BufferedImage background;
    private int size_x, size_y;
    public static Snake mysnake;
    private ArrayList<Snake> dummys;
    public LinkedHashMap<Integer, Food> foods = new LinkedHashMap<>();
    public LinkedHashMap<Integer, Snake> snakes = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, Integer> snakebodys = new LinkedHashMap<>();


    public Map(int xsize, int ysize){
        dummys = new ArrayList<>();
        mysnake = new Snake(0,0.0, 0.0, Color.BLUE, Color.GREEN, snakebodys);
        size_x = xsize;
        size_y = ysize;
        offset_x = 0.0;
        offset_y = 0.0;
        loc_x = 0.0;
        loc_y = 0.0;
        left_x = -400.0;
        left_y = -240.0;
        length = 800.0;
        width = 480.0;
        head_x = 400.0;
        head_y = 240.0;
        try {
            background = ImageIO.read(new File("/Users/Jason/Desktop/bg-1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 5; i++){
            dummys.add(new Snake(i+1,i*100.0+50, i*200.0 + 50, Color.BLUE, Color.GREEN, snakebodys));
            snakes.put(i, new Snake(i+1,i*100.0+50, i*200.0 + 50, Color.BLUE, Color.GREEN, snakebodys));
        }
        Random r = new Random();
        int Low = -xsize;
        int High = xsize;

        for (int i = 0; i < 2500; i++){
            add_food(r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low, 20.0);
        }
    }

    public void paint_background(Graphics2D g){
        g.drawImage(background, (int)Math.round(offset_x)-617, (int)Math.round(offset_y)-516, null);
        g.drawImage(background, (int)Math.round(offset_x)-607, (int)Math.round(offset_y)+221, null);
        g.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setPaint(Color.RED);
        g.draw(new Line2D.Double(head_x + size_x - loc_x, head_y + size_y - loc_y,
                head_x + size_x - loc_x, head_y - size_y - loc_y));
        g.draw(new Line2D.Double(head_x - size_x - loc_x, head_y + size_y - loc_y,
                head_x - size_x - loc_x, head_y - size_y - loc_y));
        g.draw(new Line2D.Double(head_x + size_x - loc_x, head_y + size_y - loc_y,
                head_x - size_x - loc_x, head_y + size_y - loc_y));
        g.draw(new Line2D.Double(head_x + size_x - loc_x, head_y - size_y - loc_y,
                head_x - size_x - loc_x, head_y - size_y - loc_y));
        foods.values().forEach(food -> {
            g.fillOval((int) (food.x - food.size / 2-left_x), (int) (food.y - food.size / 2-left_y), (int) (food.size / 1), (int) (food.size / 1));
        });


        mysnake.draw_snake(g, left_x, left_y);

        for (int i = 0; i < dummys.size() - 1; i++){
            dummys.get(i).draw_snake(g, left_x, left_y);
        }

    }

    public Boolean update(Double angle, JLabel mouseloc){
        mysnake.update(angle, snakebodys);
        for (int i = 0; i < dummys.size()-1; i++){
            dummys.get(i).update((Math.random()-0.5)*5, snakebodys);
        }
        x_diff = loc_x - mysnake.locx;
        y_diff = loc_y - mysnake.locy;
        offset_x = (offset_x +597.0+ x_diff)%597.0;
        offset_y = (offset_y +516.0+ y_diff)%516.0;
        loc_x -= x_diff;
        loc_y -= y_diff;
        for (int i = (int)(loc_x-30); i < (int)(loc_x+30); i++){
            for (int j = (int)(loc_y-30); j < (int)(loc_y+30); j++){
                if (foods.get(i * size_x * 2 + j) != null){
                    foods.remove(i * size_x * 2 + j);
                }
            }
        }
        left_x -= x_diff;
        left_y -= y_diff;
        mouseloc.setText("(" + loc_x + "," + loc_y + ")");
        if ((Math.abs(loc_x) > size_x)||(Math.abs(loc_y) > size_y)){
            return false;
        }
        return true;
    }

    public void set_head_loc(Double x, Double y){
        head_x = x;
        head_y = y;
        mysnake.set_head_loc(x, y);
    }

    public void set_loc(Double x, Double y){
        loc_x = x;
        loc_y = y;
        mysnake.set_loc(x, y);
    }

    public void add_food(int x, int y, Double size){
        foods.put(x*size_x*2 + y, new Food(x, y, size));
    }

    public void remove_food(int x, int y){
        foods.remove(x*size_x*2 + y);
    }

}
