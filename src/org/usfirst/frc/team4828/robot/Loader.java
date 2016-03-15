package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Loader {//don't forget about limit switches
	CANTalon upDownMotor;//moves the loader in an up-down motion. look into CANTalon control modes
	Victor intake;//a bar that spins to obtain the ball
		
	private final DigitalInput limitLoaderDown = new DigitalInput(Ports.loaderLimitLoaderDown);
	
	private static final double rollSpeed = 0.8;
	private static final double flipUpSpeed = 0.55;
	private static final double flipDownSpeed = -0.2;

	public Loader(int port1, int port2){
		upDownMotor = new CANTalon(port1);
		intake = new Victor(port2);
		
		upDownMotor.setPID(.2, 0, 0, 0, 0, 0, 0);
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	}
	
	public CANTalon getUpDownMotor(){
		return upDownMotor;
	}
	
	public void lockPosition(){
		Timer.delay(0.2);
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		upDownMotor.set(upDownMotor.getEncPosition());
		System.out.println("setting loader " + upDownMotor.getEncPosition());
	}
	
	public boolean getLimitDown(){
		return limitLoaderDown.get();
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
	
	public void reset(){
		while(upDownMotor.getEncPosition()>-3400){
			flipDown();
		}
		flipStop();
		System.out.println("loader up down reset hit limit");
	}
	
	public int getEncPosition(){
		return upDownMotor.getEncPosition();
	}
	
	public void flipUp(){
//		if(!limitLoaderUp.get())
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			upDownMotor.set(flipUpSpeed);
//		else
//			flipStop();
	}
	public void flipDown(){
//		if(!limitLoaderDown.get())
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			upDownMotor.set(flipDownSpeed);
//		else
//			flipStop();
	}
	public void flipStop(){
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		upDownMotor.set(0);
	}
	public int debugEncoder(){
		return upDownMotor.getEncPosition();
	}
}
