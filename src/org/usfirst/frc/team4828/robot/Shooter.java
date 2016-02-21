package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;

public class Shooter {
	private CANTalon shooterMotor1;// left wheel of shooter wheel
	private CANTalon shooterMotor2;
	private CANTalon upDownMotor;// up-down from loading position to shooting position
	private CANTalon leftRightMotor;// left-right shooter of the shooter to aim.
	private Servo pusherServo;// pushes the ball to be shot by the left and right wheel
	private DigitalInput hall_effect; // used for centering

	private static final double flipUpSpeed = 0.5;
	private static final double flipDownSpeed = -0.05;
	private static final double inverseFlipUpSpeed = 0.1;
	private static final double inverseFlipDownSpeed = -0.4;
	
	private static final double centerUpDownEncoderPosition = 145384;
	
	private static final double leftRightMotorSpeed = 0.15;
	private static final double intakeSpeed = 0.4;
	private static final double shootSpeed = 1;

	public Shooter(int shooterMotor1Port, int shooterMotor2Port, int upDownMotorPort, int leftRightMotorPort,
			int servoPort, int hallEffectPort) {
		shooterMotor1 = new CANTalon(shooterMotor1Port);
		shooterMotor2 = new CANTalon(shooterMotor2Port);
		upDownMotor = new CANTalon(upDownMotorPort);
		leftRightMotor = new CANTalon(leftRightMotorPort);
		pusherServo = new Servo(servoPort);
		hall_effect = new DigitalInput(hallEffectPort);
	}
	
	public CANTalon getUpDownMotor(){
		return upDownMotor;
	}
	
	public CANTalon getLeftRightMotor(){
		return leftRightMotor;
	}
	
	public int getUpDownEncPosition(){
		return upDownMotor.getEncPosition();
	}
	
	public boolean getHallEffect(){
		return hall_effect.get();
	}

	public void center() {
		// TODO: this
	}

	public void rotateLeft() {
		leftRightMotor.set(-leftRightMotorSpeed);// check direction
	}

	public void rotateRight() {
		leftRightMotor.set(leftRightMotorSpeed);
	}
	
	public void rotateStop(){
		leftRightMotor.set(0);
	}

	public void flipUp() {
		if(upDownMotor.getEncPosition() < centerUpDownEncoderPosition)
			upDownMotor.set(flipUpSpeed);
		else
			upDownMotor.set(inverseFlipUpSpeed);
	}

	public void flipDown() {
		if(upDownMotor.getEncPosition() < centerUpDownEncoderPosition)
			upDownMotor.set(flipDownSpeed);
		else
			upDownMotor.set(inverseFlipDownSpeed);
	}
	
	public void flipStop(){
		upDownMotor.set(0);
	}
	
	public void shooterIntake(){
		shooterMotor1.set(-intakeSpeed);
		shooterMotor2.set(intakeSpeed);
	}

	public void stopShooter() {
		shooterMotor1.set(0);
		shooterMotor2.set(0);
	}

	private void startShooter() {
		shooterMotor1.set(shootSpeed);
		shooterMotor2.set(-shootSpeed);
	}

	public void pushServo() {
		pusherServo.set(0);
	}

	public void retractServo() {
		pusherServo.set(-1);
	}

	public void shoot() {
		startShooter();
		Timer.delay(1.5);
		pushServo();
		Timer.delay(2);
		stopShooter();
		retractServo();
	}
}
