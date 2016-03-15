package org.usfirst.frc.team4828.robot;

public class ButtonMappings {
	private ButtonMappings(){
		throw new AssertionError("This is our town scrub...\nYeah, beat it!\n(Assertion Error 'cause you instantiated class ButtonMappings.");
	}
	
	//shared between joysticks
	public static final int shooterMoveToHeight = 2;
	
	public static final int loaderUp = 12;
	public static final int loaderDown = 11;
	public static final int loaderIntakeOn = 5;
	public static final int loaderIntakeOff = 3;
	
	public static final int shooterRotateLeft = 7;
	public static final int shooterRotateRight = 8;
	public static final int shooterFlipUp = 10;
	public static final int shooterFlipDown = 9;
	
	//exclusive to first joystick
	public static final int shooterShoot = 1; //shoots; 1 = trigger
	public static final int inverseControls = 4;
	public static final int undoInverseControls = 6;
	
	//exclusive to second joystick
	public static final int aimCamera = 1; //hold to aim cam; 1 = trigger
	public static final int enableAutoAim = 4;
	public static final int disableAutoAim = 6;
	public static final int loaderIntakeOut = 2;
}
