package org.usfirst.frc.team4828.robot;

public class Ports {
	private Ports(){
		throw new AssertionError("You have awakened me too soon, Executus!\n(Assertion Error 'cause you instantiated class Ports.\nWhat part of private don't you understand, Nikhil...\nSigh.\n");
	}
	
	public static final int driveStick = 0;
	public static final int driveStick2 = 1;
	public static final int climbStick = 2;
	
	// real robot: 3, 4, 1, 2
	//practice robot : 0, 1, 3, 2
	public static final int driveFrontLeft = 3;
	public static final int driveRearLeft = 4;
	public static final int driveFrontRight = 1;
	public static final int driveRearRight = 2;
	
	public static final int gyro = 0;
	public static final int ultrasonic = 3;
	
	public static final int shooterMotor1 = 5;
	public static final int shooterMotor2 = 6;
	public static final int shooterUpDownMotor = 16;
	public static final int shooterLeftRightMotor = 7;
	public static final int shooterServo1 = 6; //PWM
	public static final int shooterServo2 = 7; //PWM
	public static final int shooterHallEffect = 9;
	public static final int shooterLimitShooterDown = 4;
	public static final int shooterLimitShooterUp = 5;
	
	public static final int loaderUpDownMotor = 9; 
	public static final int loaderIntakeMotor = 9; //PWM
	public static final int loaderLimitLoaderDown = 5;	
	
	public static final int climberMotorRight = 8; 
	public static final int climberMotorLeft = 2; //PWM 
	public static final int stableMotorRight = 1; //PWM
	public static final int stableMotorLeft = 2; //PWM
	
}
