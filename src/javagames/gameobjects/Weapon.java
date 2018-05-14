package javagames.gameobjects;


import javagames.util.NumberUtils;

public class Weapon {
	
	float time;
    int capacity;
    int charge;
    float rof;
    float spread;
    public float barrelAngle;
    public float barrelLoc;
    
    NumberUtils numberUtils;

    public Weapon(int c, float r, float s){
        capacity = c;
        rof = r;
        spread = s;
        numberUtils = new NumberUtils();
        time = 0.0f;
        barrelAngle = (float)Math.toRadians(22);
        barrelLoc = 0.5f;

    }

    /**
     *
     * returns total capacity of weapon
     * @return
     */
    public int getCapacity(){return capacity;}

    /**
     *
     * returns current ammo in weapon
     * @return
     */
    public int getCharge(){return charge;}

    /**
     *
     * returns weapon rate of fire
     * @return
     */
    public float getRof(){return rof;}

    /**
     *
     * returns spread value of weapon
     * @return
     */
    public float getSpread(){return spread;}

    /**
     *
     * creates a random value within the spread value of weapon
     * @param angle
     * @return
     */
    public float createSpread(float angle){
        return numberUtils.randomFloat(angle-(float)Math.toRadians(spread), angle+(float)Math.toRadians(spread));
    }
    public void reset(){
        charge = 0;
    }

    public String toString(){
        return "weapon";
    }

    /**
     *
     * useful for bullet creation location
     * @return
     */
    public float barrelAngle(){
        return barrelAngle;
    }

    /**
     *
     * useful for bullet creation location
     * @return
     */
    public float barrelLoc(){
        return barrelLoc;
    }
    
    public void updateWeapon(float delta){
    	time += delta;
    	if(charge > 0){
    		if(time > 1.0f){
    			charge--;
    			time = 0.0f;
    		}
    	}
    	
    }

}
