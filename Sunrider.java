package Informatica;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;

public class Sunrider extends AdvancedRobot {
    
	int moveDirection=1;	//which way to move 1=forward -1=backward
	
    public void run() {
        setAdjustRadarForRobotTurn(true);//keep the radar still while we turn
		setColors(Color.black, Color.orange ,Color.yellow);	//sets the colours of the robot
        setBulletColor(Color.blue); 	//sets the colours of the bullets
        setAdjustGunForRobotTurn(true); // Keep the gun still when we turn
   		
        // Check for new targets.
    	turnRadarRightRadians(Double.POSITIVE_INFINITY);
    	while (true) {
        scan();
    	}
	}		

    public void onScannedRobot(ScannedRobotEvent e) {
        
		double absBearing = e.getBearingRadians() + getHeadingRadians() ; //enemies absolute bearing
        double latVel = e.getVelocity() * Math.sin(e.getHeadingRadians() - absBearing); //enemies later velocity
        double gunTurnAmt; //amount to turn our gun
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians()); //lock on the radar
        
		//randomly change speed
		if(Math.random() < 0.9){
            setMaxVelocity((20*Math.random())+20); //If the speed is too slow
        } else { setMaxVelocity((10*Math.random())); } //if the speed is faster than earlier
		
        if (e.getDistance() > 200) { //if distance is greater than 200
            gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+latVel/22); //amount to turn our gun, lead just a little bit
            setTurnGunRightRadians(gunTurnAmt); //turn our gun
            	setTurnRightRadians(Utils.normalRelativeAngle(absBearing-getHeadingRadians()+latVel/getVelocity())); //drive towards the enemies predicted future location
            setAhead((e.getDistance() - 150)*moveDirection) ;//move forward
            setFire(2); //fire
        } else{
			if (e.getDistance() > 100) { //if distance is greater than 100
            	gunTurnAmt = Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+latVel/18); //amount to turn our gun, lead just a little bit
            	setTurnGunRightRadians(gunTurnAmt); //turn our gun
            	setTurnRightRadians(Utils.normalRelativeAngle(absBearing-getHeadingRadians()+latVel/getVelocity())); //drive towards the enemies predicted future location
            	setAhead((e.getDistance() - 100)*moveDirection); //move forward
            	setFire(4); //fire
        	} else{//if we are close enough...
           		gunTurnAmt = Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+latVel/15);
            	setTurnGunRightRadians(gunTurnAmt);	//turn our gun
            	setTurnLeft(-90-e.getBearing()); //turn perpendicular to the enemy
            	setAhead((e.getDistance() - 10)*moveDirection); //move forward
            	setFire(6); //fire
        	}
    	}
	}
    
	public void onHitWall(HitWallEvent e){
        moveDirection=-moveDirection;	//reverse direction upon hitting a wall
    }
}