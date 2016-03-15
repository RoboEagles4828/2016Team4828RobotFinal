package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;

public class LoaderHoldThread extends Thread {
	private CANTalon talon;
	private Joystick joystick;
	private int buttonUp, buttonDown;
	
	private boolean isAlive;
	
	private boolean hasMoved;
	private int position;
	
	private final static double[] upSpeeds = {0.13};
	private final static double[] downSpeeds = {0};

	public LoaderHoldThread(CANTalon t, Joystick j, int b, int b2) {
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
					talon.set(downSpeeds[0]);
				} else if (cPosition < position) {
						talon.set(upSpeeds[0]);
				} else {
					talon.set(0);
				}
			}
		}
	}
}
