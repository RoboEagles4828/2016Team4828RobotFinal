package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;

public class Shooter {
	private CANTalon shooterMotor1;// left wheel of shooter wheel
	private CANTalon shooterMotor2;
	private CANTalon upDownMotor;// up-down from loading position to shooting
									// position
	private CANTalon leftRightMotor;// left-right shooter of the shooter to aim.
	private Servo pusherServo, pusherServo2;// pushes the ball to be shot by the
											// left and right wheel
	private DigitalInput hall_effect; // used for centering

	private static final int midEncPos = -97000; //Formerly used to know when to switch speeds when flipping.

	private static final int encPulseChangePerTick = 8000; //Modifying this changes the speed at which the PID loop attempts to move the shooter. 
	private static final int encPulseChangePerTickSlow = 6250;
	
	//Formerly used as speeds to move the shooter in PercentVBus (voltage) mode
	private static final double flipUpSpeed = 0.4;
	private static final double flipUpSpeedInv = 0.2;
	private static final double flipDownSpeed = -0.2;
	private static final double flipDownSpeedInv = -0.4;

	public static final double flipUpSpeedSlow = 0.2;
	public static final double flipDownSpeedSlow = -0.1;

	private static final double leftRightMotorSpeed = 0.15;
	private static final double intakeSpeed = 0.6;
	private static final double shootSpeed = 1;

	// private final DigitalInput limitShooterDown = new
	// DigitalInput(Ports.shooterLimitShooterDown);
	// private final DigitalInput limitShooterUp = new
	// DigitalInput(Ports.shooterLimitShooterUp);

	public Shooter(int shooterMotor1Port, int shooterMotor2Port, int upDownMotorPort, int leftRightMotorPort,
			int servoPort, int servoPort2, int hallEffectPort) {
		shooterMotor1 = new CANTalon(shooterMotor1Port);
		shooterMotor2 = new CANTalon(shooterMotor2Port);
		upDownMotor = new CANTalon(upDownMotorPort);
		leftRightMotor = new CANTalon(leftRightMotorPort);
		pusherServo = new Servo(servoPort);
		pusherServo2 = new Servo(servoPort2);
		hall_effect = new DigitalInput(hallEffectPort);

		upDownMotor.setPID(0.45, 0, 14, 0, 0, 0, 0);
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.Position);
	}

	public void setPosition(int encPos) {
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		upDownMotor.set(encPos);
	}

	/**
	 * Locks the motor on the current encoder position.
	 */
	public void lockPosition() {
		// System.out.println("debug: locking Shooter @ " + upDownMotor.getEncPosition());
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		upDownMotor.set(upDownMotor.getEncPosition());
	}

	/**
	 * Used to flip up or down by a specific number of encoder pulses.
	 * 
	 * @param encChange
	 *            unit in encoder pulses to increase/decrease by
	 */
	public void changePosition(int pos) {
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		upDownMotor.set(upDownMotor.getEncPosition() + pos);
	}

	public CANTalon getUpDownMotor() {
		return upDownMotor;
	}

	public CANTalon getLeftRightMotor() {
		return leftRightMotor;
	}

	public int getLeftRightEncPosition() {
		return leftRightMotor.getEncPosition();
	}

	public int getUpDownEncPosition() {
		return upDownMotor.getEncPosition();
	}

	public void center() {
		if (leftRightMotor.getEncPosition() > 0) {
			rotateLeft();
		} else if (leftRightMotor.getEncPosition() < 0) {
			rotateRight();
		} else
			rotateStop();
	}

	public boolean getHallEffect() {
		return !hall_effect.get();
	}

	public void reset() {
		while (upDownMotor.getEncPosition() > -190000) {
			flipDown();
		}
		lockPosition();
		upDownMotor.set(0);
		System.out.println("passed up down enc pos -190000");
	}

	public void reset(Robot r) {
		while (upDownMotor.getEncPosition() > -190000 && r.isAutonomous()) {
			flipDown();
		}
		upDownMotor.set(0);
		System.out.println("passed up down enc pos -190000");
	}

	public void rotateLeft(double speed) {
		if (leftRightMotor.getEncPosition() > -75000) {
			leftRightMotor.set(-speed);
		} else {
			leftRightMotor.set(0);
		}
	}

	public void rotateRight(double speed) {
		if (leftRightMotor.getEncPosition() < 75000) {
			leftRightMotor.set(speed);
		} else {
			leftRightMotor.set(0);
		}
	}

	public void rotateLeft() {
		if (leftRightMotor.getEncPosition() > -75000) {
			leftRightMotor.set(-leftRightMotorSpeed);
		} else {
			leftRightMotor.set(0);
		}
	}

	public void rotateRight() {
		if (leftRightMotor.getEncPosition() < 75000) {
			leftRightMotor.set(leftRightMotorSpeed);
		} else {
			leftRightMotor.set(0);
		}
	}

	public void rotateStop() {
		leftRightMotor.set(0);
	}

	public void flipUp() {
		/*
		 * upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		 * if (upDownMotor.getEncPosition() > midEncPos)
		 * upDownMotor.set(flipUpSpeedInv); else upDownMotor.set(flipUpSpeed);
		 */
		changePosition(encPulseChangePerTick);
	}

	public void flipSpeed(double speed) {
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		upDownMotor.set(speed);
	}

	public void flipDown() {
		/*
		 * upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		 * if (upDownMotor.getEncPosition() > midEncPos)
		 * upDownMotor.set(flipDownSpeedInv); else
		 * upDownMotor.set(flipDownSpeed);
		 */
		changePosition(-encPulseChangePerTick);
	}

	public void flipDownSlow() {
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		changePosition(-encPulseChangePerTickSlow);
	}

	public void flipUpSlow() {
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		upDownMotor.set(encPulseChangePerTickSlow);
	}

	public void flipStop() {
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		upDownMotor.set(0);
	}

	/**
	 * Causes the shooter wheels to turn inwards; allowing for ball intake.
	 */
	public void shooterIntake() {
		shooterMotor1.set(-intakeSpeed);
		shooterMotor2.set(intakeSpeed);
	}
	/**
	 * Turns off the shooter wheels.
	 */
	public void stopShooter() {
		shooterMotor1.set(0);
		shooterMotor2.set(0);
	}

	/**
	 * Turns on the shooter wheels.
	 */
	public void startShooter() {
		shooterMotor1.set(shootSpeed);
		shooterMotor2.set(-shootSpeed);
	}

	/**
	 * Activates the servos, which pushes the boulder into the shooter's wheels and fires a shot.
	 */
	public void pushServo() {
		pusherServo.set(.4);// .55
		pusherServo2.set(.4); // .55
	}

	/**
	 * Retracts the servos to their original position; ready for ball intake.
	 */
	public void retractServo() {
		pusherServo.set(.86); // 1
		pusherServo2.set(.86); // 1
	}

	/**
	 * Debug method for testing servos.
	 * @param pos
	 * position to set the servos to
	 */
	public void setServos(double pos) {
		pusherServo.set(pos);
		pusherServo2.set(pos);
	}

	/**
	 * Locks the robot into the shooting sequence, this involves: Locking the
	 * shooter position, activating shooter intake for 0.4 seconds, starting the
	 * shooter wheels for 1.6 seconds, pushing the shooter servos to move the
	 * ball, stop the shooter and retract servos after .75 seconds.
	 */
	public void shoot() {
		this.lockPosition();
		shooterIntake();
		Timer.delay(0.4);
		startShooter();
		Timer.delay(1.6);
		pushServo();
		Timer.delay(0.75);
		stopShooter();
		retractServo();
	}

	public void autoHack(Robot r) {
		while (getUpDownEncPosition() > -42500 && getUpDownEncPosition() < -45500 && r.isAutonomous()) {
			if (getUpDownEncPosition() > -42500) {
				changePosition(-9000);
			} else if (getUpDownEncPosition() < -45500) {
				changePosition(9000);
			}
		}
		this.lockPosition();
		this.flipStop();
	}
}
