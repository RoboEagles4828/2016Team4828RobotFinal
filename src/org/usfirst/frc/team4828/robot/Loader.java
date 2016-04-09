package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Loader {
	CANTalon upDownMotor;
	Victor intake;

	private final DigitalInput limitLoaderDown = new DigitalInput(Ports.loaderLimitLoaderDown);

	private static final double rollSpeed = -1;
	private static final double flipUpSpeed = 0.4;
	private static final double flipDownSpeed = -0.175;

	public Loader(int port1, int port2) {
		upDownMotor = new CANTalon(port1);
		intake = new Victor(port2);

		upDownMotor.setPID(.4, 0, 20, 0, 0, 0, 0);
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	}

	public CANTalon getUpDownMotor() {
		return upDownMotor;
	}

	public void lockPosition() {
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		upDownMotor.set(upDownMotor.getEncPosition());
		System.out.println("setting loader " + upDownMotor.getEncPosition());
	}
	
	public void setPosition(int pos){
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		upDownMotor.set(pos);
	}

	public boolean getLimitDown() {
		return limitLoaderDown.get();
	}

	public void pause() {
		intake.set(0);
	}

	public void rollIn() {
		intake.set(rollSpeed);
	}

	public void rollOut() {
		intake.set(-rollSpeed);
	}

	public void rollStop() {
		intake.set(0);
	}

	public void reset() {
		while (upDownMotor.getEncPosition() > -3400) {
			flipDown();
		}
		flipStop();
		System.out.println("loader up down reset hit limit");
	}

	//auto version
	public void reset(Robot r) {
		while (upDownMotor.getEncPosition() > -3000 && r.isAutonomous()) {
			flipDown();
		}
		flipStop();
		//System.out.println("loader up down reset hit limit");
	}

	public int getEncPosition() {
		return upDownMotor.getEncPosition();
	}

	public void flipUp() {
		if (upDownMotor.getEncPosition() < -900) {
			upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			upDownMotor.set(flipUpSpeed);
		} else {
			flipStop();
		}
	}

	public void flipDown() {
		// if(!limitLoaderDown.get())
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		upDownMotor.set(flipDownSpeed);
		// else
		// flipStop();
	}

	public void flipStop() {
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		upDownMotor.set(0);
	}
}
