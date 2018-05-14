package javagames.gameobjects;

import java.awt.RenderingHints;
import java.io.IOException;

import javax.imageio.ImageIO;

import javagames.main.SuperNova.Actor;
import javagames.util.BoundingCircle;
import javagames.util.Vector2f;

public class Boss extends Enemy{
	
	public enum Attack{ TORNADO, BIGBULLET, HOMINGLASER }
	public Attack attack;
	
	public Boss(Vector2f p){
		super(p);
		this.p = Actor.BOSS;
		this.shotCooldown = 0.1f;
		health = 20;
		createLaserAnims();
		boundingCircle = new BoundingCircle(new Vector2f(0.0f, 0.5f), position);
		attack = Attack.TORNADO; 
		equippedWeapon.barrelLoc = 5.0f;
		equippedWeapon.barrelAngle = (float)Math.toRadians(45);
		aggroRangex = 14;
		aggroRangey = 8;
		moveSpeed = 200;
	}
	
	private void createLaserAnims(){
		try {
	           sprite = ImageIO.read(this.getClass().getResource("/actors/boss.png"));
	           sprite = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, sprite,4);
	       }
	       catch (IOException e){
	           e.printStackTrace();
	           sprite = null;
	       }
	}
	
	public void updateWorld(float delta, Vector2f player) {
		
		
		if( attack == Attack.TORNADO){

			equippedWeapon = new Weapon(0,0,0);
			frameDelay = 10;
			shotCooldown = 0;
			isShooting = true;
			//update angle
			angle += 30 * delta;
			if( time > 3 )
			{
				//attack = Attack.BIGBULLET;
				time = 0;
			}
			
		}
		if( attack == Attack.BIGBULLET ){
			equippedWeapon = new Weapon(5,1.0f,5.0f);
			frameDelay = 10.0f;
			shotCooldown = 2;
		}
		super.updateWorld(delta, player);
	}
}
