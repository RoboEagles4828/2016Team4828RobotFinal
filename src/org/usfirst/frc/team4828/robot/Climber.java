package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Victor;

public class Climber {
	public CANTalon rightClimb;
	public Victor leftStable, rightStable, leftClimb;
	private static final double climberUpSpeed = .8;
	private static final double climberDownSpeed = -.4;

	public Climber() {
		leftStable = new Victor(Ports.stableMotorLeft);
		rightStable = new Victor(Ports.stableMotorRight); 
		rightClimb = new CANTalon(Ports.climberMotorRight);
		leftClimb = new Victor(Ports.climberMotorLeft);
	}

	public void setup() {
		// TODO Auto-generated method stub

	}

	public void leftClimberUp() {
		leftClimb.set(-climberUpSpeed);

	}

	public void leftClimberDown() {
		leftClimb.set(-climberDownSpeed);

	}

	public void leftClimberStop() {
		leftClimb.set(0);
	}
	
	public void rightClimberUp() {
		rightClimb.set(climberUpSpeed);

	}

	public void rightClimberDown() {
		rightClimb.set(climberDownSpeed);

	}

	public void rightClimberStop() {
		rightClimb.set(0);
	}
	public void rightStableUp() {
		rightStable.set(climberUpSpeed);

	}

	public void rightStableDown() {
		rightStable.set(climberDownSpeed);

	}

	public void rightStableStop() {
		rightStable.set(0);
	}
	public void leftStableUp() {
		leftStable.set(climberUpSpeed);

	}

	public void leftStableDown() {
		leftStable.set(climberDownSpeed);

	}

	public void leftStableStop() {
		leftStable.set(0);
	}
	
}
