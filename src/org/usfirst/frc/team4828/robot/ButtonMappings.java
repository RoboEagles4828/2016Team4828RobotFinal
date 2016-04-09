package org.usfirst.frc.team4828.robot;

public class ButtonMappings {
	private ButtonMappings(){
		throw new AssertionError("This is our town scrub...\nYeah, beat it!\n(Assertion Error 'cause you instantiated class ButtonMappings.");
	}
	
	//shared between joysticks
	public static final int loaderIntakeOn = 5;
	public static final int loaderIntakeOff = 3;
	
	public static final int shooterRotateLeft = 7;
	public static final int shooterRotateRight = 8;
	public static final int shooterFlipUp = 12;
	public static final int shooterFlipDown = 11;
	
	//exclusive to first joystick
	public static final int shooterShoot = 1; //shoots; 1 = trigger
	public static final int inverseControls = 4;
	public static final int undoInverseControls = 6;
	public static final int shooterMoveToHeight = 2;
	public static final int shooterFlipUpSlow = 10;
	public static final int shooterFlipDownSlow = 9;
	
	//exclusive to second joystick
	public static final int aimCamera = 1; //hold to aim cam; 1 = trigger
	public static final int shooterLoadSet = 2;
	public static final int shooterCenter = 6;
	public static final int loaderUp = 10;
	public static final int loaderDown = 9;
	
	
	//climb stick is a special snowflake
	public static final int climberRightUp = 5;
	public static final int climberLeftUp = 6;
	public static final int climberRightDown = 3;
	public static final int climberLeftDown = 4;
	
	public static final int climberStableLeft = 11;
	public static final int climberStableRight = 12;
	public static final int climberStableLeftDown = 7;
	public static final int climberStableRightDown = 8;
	
	public static final int climberSetup = 1;
	public static final int loaderSet = 2;
}
