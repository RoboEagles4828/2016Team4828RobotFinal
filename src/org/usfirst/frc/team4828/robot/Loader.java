package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;

public class Loader {//don't forget about limit switches
	CANTalon rotate;//moves the loader in an up-down motion. look into CANTalon control modes
	Victor intake;//a bar that spins to obtain the ball
	
	private final DigitalInput limitLoaderDown = new DigitalInput(3);
	private final DigitalInput limitLoaderUp = new DigitalInput(2);
	
	
	private static final double rollSpeed = 0.8;
	private static final double flipUpSpeed = 1;
	private static final double flipDownSpeed = -0.2;

	public Loader(int port1, int port2){
		rotate = new CANTalon(port1);
		intake = new Victor(port2);
	}
	public void pause(){
		intake.set(0);
	}
	public void rollIn(){
		intake.set(rollSpeed);
	}
	public void rollOut(){
		intake.set(-rollSpeed);
	}
	public void rollStop(){
		intake.set(0);
	}
	
	public int getEncPosition(){
		return rotate.getEncPosition();
	}
	
	public void flipUp(){
//		if(!limitLoaderUp.get())
			rotate.set(flipUpSpeed);
//		else
//			flipStop();
	}
	public void flipDown(){
//		if(!limitLoaderDown.get())
			rotate.set(flipDownSpeed);
//		else
//			flipStop();
	}
	public void flipStop(){
		rotate.set(0);
	}
}
