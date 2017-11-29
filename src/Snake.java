import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashMap;

public class Snake {
    public ArrayList<Double> cord;
    public ArrayList<Integer> bodyx, bodyy;
    public Double head_x, head_y, current_x, current_y, diameter, stepsize, lastangle, limit, locx, locy;
    private Color paint1, paint2;
    private Boolean gradient, boosting;
    private int snakeid;
    public Deque<SnakeBodyParts> body;

    public Snake(Integer id, Double x, Double y, Color p1, Color p2, LinkedHashMap bodys){
        cord = new ArrayList<>();
        snakeid = id;
        bodyx = new ArrayList<>();
        bodyy = new ArrayList<>();
        stepsize = 5.0;
        for(int i = 0; i < 200; i++){
            cord.add(0.0);
            bodyx.add((int)(x/1));
            bodyy.add((int)(y + (199-i) * stepsize));
            bodys.put(bodyx.get(i) * 2 * 3000 + bodyy.get(i), snakeid);
        }
        head_x = 400.0;
        head_y = 240.0;
        paint1 = p1;
        paint2 = p2;
        diameter = 30.0;
        stepsize = 5.0;
        locx = x;
        locy = y;
        lastangle = 0.0;
        limit = 0.19;
        gradient = false;
        boosting = false;

    }

    public void draw_snake(Graphics2D g2d, Double map_x, Double map_y){
        head_x = locx - map_x;
        head_y = locy - map_y;
        g2d.setPaint(paint1);
        g2d.fill(new Ellipse2D.Double(head_x, head_y, diameter, diameter));
        if (gradient)
            g2d.setPaint(new GradientPaint(5, 30, paint1, 35, 100, paint2, true));
            //set gradient
        if (boosting){
            g2d.setPaint(new GradientPaint(5, 30, paint1, 35, 100, Color.YELLOW, true));
        }
        current_x = head_x;
        current_y = head_y;
        for(int i = cord.size()-1; i > 0; i --){
            current_y += stepsize * Math.cos(cord.get(i));
            current_x += stepsize * Math.sin(cord.get(i));
            if (i %2 == 1)
                g2d.fill(new Ellipse2D.Double(current_x, current_y, diameter, diameter));
        }
        g2d.setPaint(Color.BLACK);
        current_x = head_x;
        current_y = head_y;
        current_y += diameter * Math.cos(lastangle+1.57)/4 + 3*diameter/8;
        current_x += diameter * Math.sin(lastangle+1.57)/4 + 3*diameter/8;
        g2d.fill(new Ellipse2D.Double(current_x, current_y, diameter/4, diameter/4));
        current_x = head_x;
        current_y = head_y;
        current_y += diameter * Math.cos(lastangle-1.57)/4 + 3*diameter/8;
        current_x += diameter * Math.sin(lastangle-1.57)/4 + 3*diameter/8;
        g2d.fill(new Ellipse2D.Double(current_x, current_y, diameter/4, diameter/4));

        //g2d.fill(new Ellipse2D.Double(head_x, head_y, diameter, diameter));
    }

    public Boolean update(Double angle, LinkedHashMap<Integer, Integer> bodys){

        cord.remove(0);
        if ((Math.abs(angle - lastangle) < limit) || (6.283 - Math.abs(angle - lastangle) < limit))
            lastangle = angle;
        else if (Math.abs(angle - lastangle) > 3.1415)
            lastangle += -limit * Math.signum(angle-lastangle);
        else
            lastangle += limit * Math.signum(angle-lastangle);
        cord.add(lastangle);
        locx -= stepsize * Math.sin(lastangle);
        locy -= stepsize * Math.cos(lastangle);

        if(boosting){
            cord.remove(0);
            cord.add(lastangle);
            locx -= stepsize * Math.sin(lastangle);
            locy -= stepsize * Math.cos(lastangle);
        }
        for (int i = (int)(locx-20); i < (int)(locx+20); i++){
            for (int j = (int)(locy-20); j < (int)(locy+20); j++){
                if ((bodys.get(i * 6000 + j) != null)&&(!(bodys.get(i * 6000 + j) == snakeid))){
                    cord = new ArrayList<>();
                    cord.add(0.0);
                }
            }
        }
        bodyx.add((int)(locx/1));
        bodyy.add((int)(locy/1));
        bodys.put((int)(locx/1) * 2 * 3000 + (int)(locy/1), snakeid);
        bodys.remove(bodyx.get(0) * 6000 + bodyy.get(0));
        bodyx.remove(0);
        bodyy.remove(0);
        return true;
    }

    public void set_color(Color c1, Color c2){
        paint1 = c1;
        paint2 = c2;
    }

    public void set_gradient(){
        gradient = !gradient;
    }

    public void set_head_loc(Double x, Double y){
        head_x = x - diameter/2;
        head_y = y - diameter/2;
    }

    public void set_boosting(Boolean bool){
        boosting = bool;
        if (boosting)
            limit = 0.12;
        else
            limit = 0.19;
    }

    public void set_loc(Double x, Double y) {
        locx = x;
        locy = y;
    }
}
