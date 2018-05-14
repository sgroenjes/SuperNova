package javagames.gameobjects;

import javagames.main.SuperNova.Actor;
import javagames.util.BoundingCircle;
import javagames.util.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bullet extends SpriteObject {
     float speed;
     BoundingCircle boundingCircle;


    public Bullet(float angle, float x, float y, float ba, float bl, Actor p){
        super();
        init(p);
        this.angle = angle;
        speed = 5.0f;


        //creates position at tip of gun
        //using ba(barrel angle) and bl (barrel length) stored in weapon class
        position.x = x+(float)Math.sin(angle+ba)*bl;
        position.y = y+(float)Math.cos(angle+ba)*bl;
        boundingCircle = new BoundingCircle(new Vector2f(0.0f, 0.005f),position);
        velocity.x = speed*(float)Math.sin(this.angle);
        velocity.y = speed*(float)Math.cos(this.angle);




    }
    
    public void reset(float angle, float x, float y, float ba, float bl,Actor p){
    	this.angle = angle;
    	init(p);
    	   //creates position at tip of gun
        //using ba(barrel angle) and bl (barrel length) stored in weapon class
        position.x = x+(float)Math.sin(angle+ba)*bl;
        position.y = y+(float)Math.cos(angle+ba)*bl;
        boundingCircle = new BoundingCircle(new Vector2f(0.0f, 0.005f),position);
        velocity.x = speed*(float)Math.sin(this.angle);
        velocity.y = speed*(float)Math.cos(this.angle);
    }

    private void init(Actor p){
        try {
        	
           BufferedImage beams = ImageIO.read(this.getClass().getResource("/effects/beams.png"));
        	if( p == Actor.PLAYER )
        		sprite = beams.getSubimage(200, 310, 65, 85);
        	else if ( p == Actor.MINION )
        		sprite = beams.getSubimage(230, 214, 42, 80);
        	else
        		sprite = beams.getSubimage(162, 218, 42, 80);
           sprite = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, sprite, 3);
        }
        catch (IOException e){
            e.printStackTrace();
           sprite = null;
        }

    }

    public void updateWorld(float delta){
        position = position.add(velocity.mul(delta));
        boundingCircle.updateCenter(position);
    }

    public BoundingCircle getBoundingCircle(){
        return boundingCircle;
    }




}
