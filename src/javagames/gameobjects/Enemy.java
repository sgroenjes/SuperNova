package javagames.gameobjects;

import javagames.main.SuperNova.Actor;
import javagames.util.BoundingCircle;
import javagames.util.Matrix3x3f;
import javagames.util.NumberUtils;
import javagames.util.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class Enemy extends SpriteObject {
    public boolean isWalking;
    public boolean isShooting;
    public boolean canShoot;
    public boolean inRange;
    
    protected BufferedImage feetSprite;
    protected BufferedImage[] feetidle;
    protected BufferedImage[] feetwalk;
    protected Weapon equippedWeapon;
    
    public Actor p;
    float attackTime;
    float time;
    float frameDelay;
    float moveSpeed;
    public int enemyFrame;    
    int footFrame;
    float aggroRangex;
    float aggroRangey;
    public float shotCooldown;
    public int health;
    public float root2 = (float) Math.sqrt(2);


    protected BoundingCircle boundingCircle;

    NumberUtils numberUtils;

    public Enemy(Vector2f p){
        super();
        this.p = Actor.MINION;
        numberUtils = new NumberUtils();
        position = p;
        isShooting = false;
        canShoot = true;
        isWalking = false;
        inRange = false;
        time = 0.0f;
        attackTime = 0.0f;
        frameDelay = 0.04f;
        enemyFrame = 0;
        aggroRangex = 7.0f;
        aggroRangey = 4.0f;
        shotCooldown = 1.0f;
        health = 4;
        
        feetidle = new BufferedImage[1];
        feetwalk = new BufferedImage[20];

        moveSpeed = numberUtils.randomFloat(100.0f,120.0f);
        createSprite();
        createlaserAnims();
        equippedWeapon = new Weapon(5,1.0f,3.5f);
        boundingCircle = new BoundingCircle(new Vector2f(0.0f, 0.5f), position);
    }

    /**
    *
    * creates player foot sprites
    */
   private void createSprite(){
       //create feetidle animations
       for(int i=0;i<feetidle.length;i++){
           try {
               feetidle[i] = ImageIO.read(this.getClass().getResource("/actors/feet/idle/survivor-idle_" +i+".png"));
               feetidle[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, feetidle[i],4);
           }
           catch (IOException e){
               e.printStackTrace();
               feetidle[i] = null;
           }
       }

       //create feetwalk animations
       for(int i=0;i<feetwalk.length;i++){
           try {
               feetwalk[i] = ImageIO.read(this.getClass().getResource("/actors/feet/walk/survivor-walk_" +i+".png"));
               feetwalk[i] = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, feetwalk[i],4);
           }
           catch (IOException e){
               e.printStackTrace();
               feetwalk[i] = null;
           }
       }
       feetSprite = feetidle[0];
   }

   /**
    *
    * Creates laser specific animations
    */
   private void createlaserAnims(){
       try {
           sprite = ImageIO.read(this.getClass().getResource("/actors/enemy.png"));
           sprite = scaleWithGraphics(RenderingHints.VALUE_INTERPOLATION_BILINEAR, sprite,4);
       }
       catch (IOException e){
           e.printStackTrace();
           sprite = null;
       }
   }

   /**
   *
   * initiates enemy shooting animation
   */
  public boolean shoot(){
  	if(equippedWeapon.getCharge()<equippedWeapon.getCapacity()){
  		equippedWeapon.charge++;
  		enemyFrame = 0;
  		isShooting = true;
  		canShoot = false;
  	}
  	return isShooting;
  }
  
  public void reset(Vector2f p){
      health = 4;
      position = p;
	  
  }
  
  /**
  *
  * returns equipped weapon
  * @return
  */
 public Weapon getEquippedWeapon(){
     return equippedWeapon;
 }



    public void updateWorld(float delta, Vector2f player) {
        super.updateWorld(delta);
        equippedWeapon.updateWeapon(delta);
        
        //moves toward player
        if(Math.abs(player.x - position.x) < aggroRangex && Math.abs(player.y - position.y) < aggroRangey){
            if (player.x < position.x && player.y < position.y) {
                velocity = new Vector2f(-moveSpeed / root2 * delta, -moveSpeed / root2 * delta);
                isWalking = true;
            } else if (player.x < position.x && player.y > position.y) {
                velocity = new Vector2f(-moveSpeed / root2 * delta, moveSpeed / root2 * delta);
                isWalking = true;
            } else if (player.x > position.x && player.y < position.y) {
                velocity = new Vector2f(moveSpeed / root2 * delta, -moveSpeed / root2 * delta);
                isWalking = true;
            } else if (player.x > position.x && player.y > position.y) {
                velocity = new Vector2f(moveSpeed / root2 * delta, moveSpeed / root2 * delta);
                isWalking = true;
            } else {

                if (player.x < position.x) {
                    velocity.x = -moveSpeed * delta;
                    isWalking = true;
                } else if (player.x > position.x) {
                    velocity.x = moveSpeed * delta;
                    isWalking = true;
                }

                if (player.y < position.y) {
                    velocity.y = -moveSpeed * delta;
                    isWalking = true;
                } else if (player.y > position.y) {
                    velocity.y = moveSpeed * delta;
                    isWalking = true;
                }
            }
            
            if(Math.abs(player.x - position.x) < aggroRangex-1.0f && Math.abs(player.y - position.y) < aggroRangey-1.0f){
            	inRange = true;
            }
            else{
            	inRange = false;
            }
            
            
        }
        else{    	
        	velocity.y = velocity.x = 0.0f;
        	isWalking = false;
        }
        //idle state
        if(velocity.x == 0.0f && velocity.y == 0.0f){
            isWalking = false;
        }
        boundingCircle.updateCenter(position);
        time+=delta;

      //set upper body animation
        if(time > frameDelay) {
            enemyFrame++;
            if (isShooting) {
                if (enemyFrame > 3) {
                	isShooting = false;
                    enemyFrame = 0;
                }
            }
            else if(isWalking){
            	
                if (enemyFrame > 19) {
                	enemyFrame = 0;
                }

            }
            else{
            	
                if (enemyFrame > 19) {
                	enemyFrame = 0;
                }
            }

            //walking animation
            if(isWalking){
                //animates feet
                if (footFrame > 19) {
                    footFrame = 0;
                }
                feetSprite = feetwalk[footFrame++];
            }
            else{
                feetSprite = feetidle[0];
            }
            time=0.0f;
            
        }

        if(!canShoot){
        	shotCooldown -= delta;
        }
        if(shotCooldown <= 0){
        	shotCooldown = 1.0f;
        	canShoot = true;
        }

        //rotates zombie to face player
        float estAngle = (float) Math.toDegrees(Math.atan2(player.x - position.x, player.y - position.y));

        if (Math.toDegrees(angle) > estAngle - 1 && Math.toDegrees(angle) < estAngle + 1) {
            //do nothing, avoids jittery sprite
        } else if (estAngle > 90 && estAngle <= 180 && Math.toDegrees(angle) < -90) {
            angle -= rotation * delta;
            if (Math.toDegrees(angle) < -180) {
                angle = angle + (float) Math.toRadians(360);
            }
        } else if (estAngle < -90 && Math.toDegrees(angle) > 90) {
            angle += rotation * delta;
            if (Math.toDegrees(angle) > 180) {
                angle = angle - (float) Math.toRadians(360);
            }
        } else if (Math.toDegrees(angle) < estAngle) {
            angle += rotation * delta;

        } else if (Math.toDegrees(angle) > estAngle) {
            angle -= rotation * delta;
        }
    }

    public void render(Graphics g, Matrix3x3f viewport){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform transform = createTransform(position, angle, viewport, sprite);
        AffineTransform ftransform = createTransform(position, angle, viewport, feetSprite);
        g2d.drawImage(feetSprite, ftransform, null);
        g2d.drawImage(sprite, transform, null);
        
        Vector2f screenPos = viewport.mul(position);
        g.setColor(Color.black);
        if(this instanceof Boss)
        	g.drawRect((int)screenPos.x-40, (int)screenPos.y-50, 200, 10);
        else
        	g.drawRect((int)screenPos.x-20, (int)screenPos.y-30, 40, 10);
        g.setColor(Color.RED);
        for(int i=0;i<this.health;++i){
        	 if(this instanceof Boss)
        		 g.fillRect((int)screenPos.x-40+i*10, (int)screenPos.y-50, 10, 10);
        	 else
        		 g.fillRect((int)screenPos.x-20+i*10, (int)screenPos.y-30, 10, 10);
        }
    }

    public AffineTransform createTransform(Vector2f position, float angle, Matrix3x3f viewport, BufferedImage sp){
        Vector2f screen = viewport.mul(position);
        AffineTransform transform = AffineTransform.getTranslateInstance(screen.x, screen.y);
        transform.rotate(angle);
        transform.translate(-sp.getWidth()/2, -sp.getHeight()/2);
        return transform;
    }

    public BoundingCircle getBoundingCircle(){
        return boundingCircle;
    }

}
