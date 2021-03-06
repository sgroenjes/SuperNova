package javagames.gameobjects;


import java.awt.RenderingHints;
import java.io.IOException;

import javax.imageio.ImageIO;
import javagames.util.Vector2f;

public class Background extends Sprite {

	private int scale;
	
	public Background(int level){
		super();
		init(level);
	}
	
	public int getScale(){ return scale;}
	
	public void init(int level){
		position = new Vector2f(26.8f,-30.32f);
		scale = 2;
		
		try {
			if(level == 1){
				sprite = ImageIO.read(this.getClass().getResource("/ground/world1.png"));
				sprite = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, sprite, 2);
			}else{
				System.out.println("Loaded boss room sprite");
				sprite = ImageIO.read(this.getClass().getResource("/ground/test.png"));
				sprite = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, sprite, 2);
			}
		}
		catch( IOException e){
			e.printStackTrace();
			sprite = null;
		}
	}
	
}
