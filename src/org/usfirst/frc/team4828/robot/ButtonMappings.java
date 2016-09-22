package org.usfirst.frc.team4828.robot;

public class ButtonMappings {
	private ButtonMappings(){
		throw new AssertionError("This is our town scrub...\nYeah, beat it!\n(Assertion Error 'cause you instantiated class ButtonMappings.");
	}
	
	//exclusive to first joystick
	public static final int blockerUp = 5;
	public static final int blockerDown = 6;
	public static final int inverseControls = 2;
	public static final int undoInverseControls = 6;
	public static final int climberRightUp = 5;
	public static final int climberLeftUp = 6;
	public static final int climberRightDown = 3;
	public static final int climberLeftDown = 4;
	public static final int climberStableLeft = 8;
	public static final int climberStableRight = 7;
	public static final int climberStableLeftDown = 10;
	public static final int climberStableRightDown = 9;
	
	//exclusive to second joystick (shoot stick)
	public static final int shoot = 1;
	public static final int aimCamera = 2;

	public static final int loaderIntakeOn = 5;
	public static final int loaderIntakeOff = 3;
	public static final int loaderUp = 12;
	public static final int loaderDown = 11;
	
	public static final int shooterMoveToHeight = 6;
	public static final int shooterRotateLeft = 7;
	public static final int shooterRotateRight = 8;
	public static final int shooterFlipUp = 10;
	public static final int shooterFlipDown = 9;
	public static final int shooterCenter = 4;

	
	
	
//	public static final int climberSetup = 1;
//	public static final int loaderSet = 2;
}
