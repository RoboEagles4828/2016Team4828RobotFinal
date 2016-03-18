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

	private static final int midEncPos = -97000;

	private static final double flipUpSpeed = 0.5;
	private static final double flipUpSpeedInv = 0.2;
	private static final double flipDownSpeed = -0.2;
	private static final double flipDownSpeedInv = -0.5;

	private static final double leftRightMotorSpeed = 0.15;
	private static final double intakeSpeed = 0.4;
	private static final double shootSpeed = 1;

	// private final DigitalInput limitShooterDown = new
	// DigitalInput(Ports.shooterLimitShooterDown);
	// private final DigitalInput limitShooterUp = new
	// DigitalInput(Ports.shooterLimitShooterUp);

	private int lrZeroPos = 0;

	public Shooter(int shooterMotor1Port, int shooterMotor2Port, int upDownMotorPort, int leftRightMotorPort,
			int servoPort, int servoPort2, int hallEffectPort) {
		shooterMotor1 = new CANTalon(shooterMotor1Port);
		shooterMotor2 = new CANTalon(shooterMotor2Port);
		upDownMotor = new CANTalon(upDownMotorPort);
		leftRightMotor = new CANTalon(leftRightMotorPort);
		pusherServo = new Servo(servoPort);
		pusherServo2 = new Servo(servoPort2);
		hall_effect = new DigitalInput(hallEffectPort);

		upDownMotor.setPID(0.45, 0, 22.5, 0, 0, 0, 0);
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	}

	public void setPosition(int encPos) {
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		upDownMotor.set(encPos);
	}

	public void lockPosition() {
		Timer.delay(0.2);
		//System.out.println("locking Shooter @ " + upDownMotor.getEncPosition());
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		upDownMotor.set(upDownMotor.getEncPosition());
	}

	public void lockPosition(int pos) {
		//Timer.delay(0.1);
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
		while (leftRightMotor.getEncPosition() > 0) {
			rotateRight();
		}
		rotateStop();
		while (leftRightMotor.getEncPosition() < 0) {
			rotateLeft();
		}
		rotateStop();
	}

	public boolean getHallEffect() {
		return !hall_effect.get();
	}
	
	public void reset() {
		while (upDownMotor.getEncPosition() > -190000) {
			flipDown();
		}
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
		leftRightMotor.set(-speed);
	}

	public void rotateRight(double speed) {
		leftRightMotor.set(speed);
	}

	public void rotateLeft() {
		leftRightMotor.set(-leftRightMotorSpeed);
	}

	public void rotateRight() {
		leftRightMotor.set(leftRightMotorSpeed);
	}

	public void rotateStop() {
		leftRightMotor.set(0);
	}

	public void flipUp() {
//		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
//		if (upDownMotor.getEncPosition() > midEncPos)
//			upDownMotor.set(flipUpSpeedInv);
//		else
//			upDownMotor.set(flipUpSpeed);
		lockPosition(16000);
	}

	public void flipSpeed(double speed) {
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		upDownMotor.set(speed);
	}

	public void flipDown() {
//		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
//		if (upDownMotor.getEncPosition() > midEncPos)
//			upDownMotor.set(flipDownSpeedInv);
//		else
//			upDownMotor.set(flipDownSpeed);
		lockPosition(-16000);
	}
	
	public void flipEnc(int encChange){
		lockPosition(encChange);
	}

	public void flipStop() {
		upDownMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		upDownMotor.set(0);
	}

	public void shooterIntake() {
		shooterMotor1.set(-intakeSpeed);
		shooterMotor2.set(intakeSpeed);
	}

	public void stopShooter() {
		shooterMotor1.set(0);
		shooterMotor2.set(0);
	}

	public void startShooter() {
		shooterMotor1.set(shootSpeed);
		shooterMotor2.set(-shootSpeed);
	}

	public void pushServo() {
		pusherServo.set(.4);// .55
		pusherServo2.set(.4); // .55
	}

	public void retractServo() {
		pusherServo.set(.86); // 1
		pusherServo2.set(.86); // 1
	}

	public void setServos(double pos) {
		pusherServo.set(pos);
		pusherServo2.set(pos);
	}

	public void setShooterAngle(double angle) {
		upDownMotor.getEncPosition();
		System.out.println("Hi");
	}

	public void shoot() {
		// Timer.delay(10);
		shooterIntake();
		Timer.delay(0.4);
		startShooter();
		// Delay change may be necessary
		Timer.delay(1.6);
		pushServo();
		Timer.delay(0.75);
		stopShooter();
		retractServo();
	}
}
