package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class ShooterHoldThread extends Thread {
	private CANTalon talon;
	private Joystick joystick;
	private int buttonUp, buttonDown;
	
	private boolean isAlive;
	
	private boolean hasMoved;
	private int position;
	
	private final static double[] upSpeeds = {0.15, 0.25, 0.25, 0.05, 0.05};
	private final static double[] downSpeeds = {-0.05, -0.05, 0, -0.23, -0.13};

	public ShooterHoldThread(CANTalon t, Joystick j, int b, int b2) {
		talon = t;
		joystick = j;
		buttonUp = b;
		buttonDown = b2;
		hasMoved = false;
		position = t.getEncPosition();

		isAlive = true;
	}

	public void kill() {
		isAlive = false;
	}

	@Override
	public void run() {
		int cPosition = 0;
		while (isAlive) {
			if (joystick.getRawButton(buttonUp) || joystick.getRawButton(buttonDown)) {
				hasMoved = true;
			} else if (hasMoved) { // set the new position
				position = talon.getEncPosition();
				hasMoved = false;
			} else { // maintain position
				cPosition = talon.getEncPosition();
				if (cPosition > position) {
					if(cPosition < 50000)
						talon.set(downSpeeds[0]);
					else if (cPosition < 90000)
						talon.set(downSpeeds[1]);
					else if (cPosition < 145000)
						talon.set(downSpeeds[2]);
					else if (cPosition< 221000)
						talon.set(downSpeeds[3]);
					else
						talon.set(downSpeeds[4]);
				} else if (cPosition < position) {
					if(cPosition < 50000)
						talon.set(upSpeeds[0]);
					else if (cPosition < 90000)
						talon.set(upSpeeds[1]);
					else if (cPosition < 145000)
						talon.set(upSpeeds[2]);
					else if (cPosition < 221000)
						talon.set(upSpeeds[3]);
					else 
						talon.set(upSpeeds[4]);
				} else {
					talon.set(0);
				}
			}
		}
	}
}
