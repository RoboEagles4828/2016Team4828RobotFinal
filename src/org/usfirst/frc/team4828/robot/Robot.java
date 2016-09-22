package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	// Declares main robot components
	private WorldChampionDrive rd;
	private Joystick driveStick2, driveStick;
	private CameraMotors camera;
	private AnalogGyro gyro;
	private Shooter shooter;
	private Loader loader;
	private AnalogInput ultrasonic;
	private Climber climber;
	//private Vision vision;
	private NetworkTable nt;

	private PowerDistributionPanel pdp;
	private BuiltInAccelerometer accelerometer;

	private Victor blocker;
	private double throttle = 0;
	
	private SendableChooser positionChooser;
	private SendableChooser obstacleChooser;
	
	//private Sendable throttleValue = 0;
	
	private DigitalInput blockHall;
	
	private DigitalInput autoSwitch1 = new DigitalInput(12), autoSwitch2 = new DigitalInput(13);

	public void robotInit() {
		// Initializes main robot components
		driveStick2 = new Joystick(Ports.driveStick2);
		driveStick = new Joystick(Ports.driveStick);
		rd = new WorldChampionDrive(Ports.driveFrontLeft, Ports.driveRearLeft, Ports.driveFrontRight, Ports.driveRearRight);
		gyro = new AnalogGyro(Ports.gyro);
		SmartDashboard.putBoolean("Camera Tracking: ", false);
		shooter = new Shooter(Ports.shooterMotor1, Ports.shooterMotor2, Ports.shooterUpDownMotor, Ports.shooterLeftRightMotor, Ports.shooterServo1, Ports.shooterServo2, Ports.shooterHallEffect);
		camera = new CameraMotors(shooter);
		loader = new Loader(Ports.loaderUpDownMotor, Ports.loaderIntakeMotor);
		climber = new Climber();
		blockHall = new DigitalInput(Ports.blockerHallEffect);
		// nt = NetworkTable.getTable("GRIP");
		// vision = new Vision(nt);

		blocker = new Victor(4);
		
		ultrasonic = new AnalogInput(Ports.ultrasonic);
		pdp = new PowerDistributionPanel();
		accelerometer = new BuiltInAccelerometer();

		//vision.startGrip();
		
		positionChooser = new SendableChooser();
		positionChooser.addDefault("1", AutoPosition.ONE);
		positionChooser.addObject("2", AutoPosition.TWO);
		positionChooser.addObject("3", AutoPosition.THREE);
		positionChooser.addObject("4", AutoPosition.FOUR);
		positionChooser.addObject("5", AutoPosition.FIVE);

		obstacleChooser = new SendableChooser();
		obstacleChooser.addObject("Low Bar", AutoObstacle.LOW_BAR);
		obstacleChooser.addObject("Portcullis", AutoObstacle.PORTCULLIS);
		obstacleChooser.addObject("Cheval De Frise", AutoObstacle.CHEVAL_DE_FRISE);
		obstacleChooser.addObject("Moat", AutoObstacle.MOAT);
		obstacleChooser.addObject("Ramparts", AutoObstacle.RAMPARTS);
		obstacleChooser.addObject("Drawbridge", AutoObstacle.DRAWBRIDGE);
		obstacleChooser.addObject("Sallyport", AutoObstacle.SALLYPORT);
		obstacleChooser.addDefault("Rock Wall", AutoObstacle.ROCK_WALL);
		obstacleChooser.addObject("Rough Terrain", AutoObstacle.ROUGH_TERRAIN);

		SmartDashboard.putData("Autonomous Position", positionChooser);
		SmartDashboard.putData("Autonomous Obstacle", obstacleChooser);

	}

	private enum AutoPosition {
		ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5");
		private final String stringVal;

		AutoPosition(String v) {
			stringVal = v;
		}

		@Override
		public String toString() {
			return stringVal;
		}
	}

	private enum AutoObstacle {
		PORTCULLIS("Portcullis"), CHEVAL_DE_FRISE("Cheval De Frise"), MOAT("Moat"), RAMPARTS("Ramparts"), DRAWBRIDGE("Drawbridge"), SALLYPORT("Sallyport"), ROCK_WALL("Rock Wall"), ROUGH_TERRAIN("Rough Terrain"), LOW_BAR("Low Bar");
		private final String stringVal;

		AutoObstacle(String v) {
			stringVal = v;
		}

		@Override
		public String toString() {
			return stringVal;
		}
	}

	private boolean hasRun = false;
	private boolean lowBar = false;
	private boolean spyShot = false;
	private boolean terrain = false;
	
	public void autonomousInit() {
		hasRun = false;
	}

	public void autonomousPeriodic() {
		SmartDashboard.putBoolean("Auto DI 1: ", autoSwitch1.get());
		SmartDashboard.putBoolean("Auto DI 2: ", autoSwitch2.get());
		boolean auto1 = autoSwitch1.get();
		boolean auto2 = autoSwitch2.get();

		if (!hasRun) {
//			if (auto1 == false && auto2 == false) { // 00
//				SmartDashboard.putString("Auto: ", "00 Low Bar");
//				lowBar = true;
//			} else if (auto1 == true && auto2 == false) { // 10
//				SmartDashboard.putString("Auto: ", "10 Terrain");
//				terrain = true;
//			} else if (auto1 == false && auto2 == true) { // 01
//				SmartDashboard.putString("Auto: ", "01 Spy Shot");
//				spyShot = true;
//			} else { // 11
//				SmartDashboard.putString("Auto: ", "11 Do Nothing");
//			}
//			if (!spyShot) {
//				if (lowBar) {
//					rd.move(Direction.BACKWARD, 30, 18, this);
//					loader.reset(this);
//					Timer.delay(.5);
//					shooter.reset(this);
//					Timer.delay(.5);
//					rd.autoHack();
//					Timer.delay(2.25);
//					rd.stop();
//				} else if (terrain){
//					rd.autoHack();
//					Timer.delay(2.7);
//					rd.stop();
//				}
//			} else if (spyShot){
//				shooter.shoot();
//			}
			//blocker.set(0);
			//Timer.delay(.5);
			//blocker.set(1);
			//Timer.delay(2);
			
			
			//spyshot
			//shooter.shootAuto();
			//terrain
			rd.autoHack();
			Timer.delay(2.6);
			rd.stop();
			Timer.delay(6);
			shooter.dropBall();
			System.out.println("Finished Autonomous!");
			hasRun = true;
//			
//			//This is highgoal
//			loader.flipDown();
//			Timer.delay(.5);
//			shooter.flipDownSlow();
//			Timer.delay(1);
//			//This is lowgoal
//			shooter.shootAuto();
			hasRun=true;
		}

	}

	public void teleopInit() {
	
	}

	double accelerometerOutputCounter = 0;
	double accX = 0, accY = 0, accZ = 0;

	private boolean checkLoader = false; // needed for PID; don't touch
	private boolean checkShooter = false; // needed for PID; don't touch
	private boolean checkCamera = false;

	private double ultraV = 0;
	private int ultraSamplingCounter = 0;

	public void teleopPeriodic() {
		//vision.getValues();
		if (driveStick.getPOV() == 90) {
			SmartDashboard.putNumber("RL Enc: ", rd.rearLeft.getEncPosition());
			SmartDashboard.putNumber("Gyro: ", gyro.getAngle());
			SmartDashboard.putBoolean("Shooter Hall Effect: ", shooter.getHallEffect());
			SmartDashboard.putNumber("Shooter up down enc: ", shooter.getUpDownEncPosition());
			SmartDashboard.putNumber("shooter LeftRight Enc: ", shooter.getLeftRightEncPosition());
			SmartDashboard.putNumber("Loader upDownMotor Enc: ", loader.getEncPosition());
			SmartDashboard.putBoolean("Loader limit down: ", loader.getLimitDown());
			SmartDashboard.putBoolean("Inverse Controls: ", rd.inverseControls);

			if (ultraSamplingCounter < 100) {
				ultraSamplingCounter++;
				ultraV += ultrasonic.getVoltage();
			} else {
				ultraSamplingCounter = 0;
				ultraV = ultraV / 100;
				SmartDashboard.putNumber("Raw Ultra : ", ultraV);
				SmartDashboard.putNumber("Meters    : ", ultraV * 1024 / 1000);
				SmartDashboard.putNumber("Feet      : ", ultraV * 1024 / 1000 * 3.28);
				ultraV = 0;
			}

			if (accelerometerOutputCounter < 10) {
				accelerometerOutputCounter++;
				accX += accelerometer.getX();
				accY += accelerometer.getY();
				accZ += accelerometer.getZ();
			} else {
				accelerometerOutputCounter = 0;
				SmartDashboard.putNumber("Acc X: ", accX / 10);
				SmartDashboard.putNumber("Acc Y: ", accY / 10);
				SmartDashboard.putNumber("Acc Z: ", accZ / 10);
				accX = 0;
				accY = 0;
				accZ = 0;
			}
		}

		if (driveStick2.getRawButton(ButtonMappings.loaderUp)) {
			checkLoader = true;
			loader.flipUp();
		} else if (driveStick2.getRawButton(ButtonMappings.loaderDown)) {
			checkLoader = true;
			loader.flipDown();
		} else {
			if (checkLoader) {
				loader.flipStop();
				loader.lockPosition();
				checkLoader = false;
			}
		}

		// if (driveStick.getRawButton(ButtonMappings.shooterFlipUpSlow)) {
		// shooter.flipUpSlow();
		// } else if
		// (driveStick.getRawButton(ButtonMappings.shooterFlipDownSlow)) {
		// shooter.flipDownSlow();
		// } else{
		// shooter.flipStop();
		// shooter.lockPosition();
		// }

		if (driveStick2.getRawButton(ButtonMappings.shooterRotateLeft))
			shooter.rotateLeft();
		else if (driveStick2.getRawButton(ButtonMappings.shooterRotateRight))
			shooter.rotateRight();
		else
			shooter.rotateStop();

		// For moving shooter with joystick
		
		if (driveStick2.getMagnitude() > 0.8) {
			System.out.println("X: " + driveStick2.getX() + "Y: " + driveStick2.getY());
			if(driveStick2.getX() > 0.8) shooter.rotateRight();
			if(driveStick2.getX() < -0.8) shooter.rotateLeft();
			if(driveStick2.getY() < -0.8) shooter.flipUp();
			if(driveStick2.getY() > 0.8) shooter.flipDown();
			else {
				if (checkShooter) {
					checkShooter = false;
					shooter.lockPosition();
				}
				shooter.rotateStop();
			}
		}
		
		if ((driveStick2.getRawButton(ButtonMappings.shooterFlipUp))) {
			checkShooter = true;
			shooter.flipUp();
		} else if (driveStick2.getRawButton(ButtonMappings.shooterFlipDown)) {
			checkShooter = true;
			shooter.flipDown();
		} else {
			if (checkShooter) {
				checkShooter = false;
				//shooter.flipStop();
				shooter.lockPosition();
			}
		}

		// loader/shooter intake wheel control
		if (driveStick2.getRawButton(ButtonMappings.loaderIntakeOn)) {
			loader.rollIn();
			shooter.shooterIntake();
		} else if (driveStick2.getRawButton(ButtonMappings.loaderIntakeOff)) {
			shooter.stopShooter();
			loader.rollStop();
		}

		if (driveStick2.getRawButton(ButtonMappings.shooterMoveToHeight)) {
			shooter.setPosition(-66000); // -66000 HIGH
		}

		if (driveStick2.getRawButton(ButtonMappings.shooterCenter)) {
			shooter.center();
		}

		if (driveStick2.getRawButton(ButtonMappings.shoot)) {
			loader.rollStop();
			rd.stop();
			shooter.shoot(); // locks robot into the shooting sequencef
		}

		if (driveStick.getRawButton(ButtonMappings.inverseControls)) {
			rd.inverseControls = true;
		}
		if (driveStick.getRawButton(ButtonMappings.undoInverseControls)) {
			rd.inverseControls = false;
		}

		// hold to aim camera
		if (driveStick2.getRawButton(ButtonMappings.aimCamera)) {
			camera.aimCamera();
			checkCamera = true;
		} else {
			if (checkCamera) {
				checkCamera = false;
				checkShooter = true;
			}
		}

		if (driveStick.getRawButton(2)) {
			blocker.set(0);
		}
		else if (driveStick2.getRawButton(2)) {
			blocker.set(1);
		}

		rd.arcadeDriveRamp(driveStick);

		if (driveStick.getTrigger()) {
			shooter.dropBall();
		}
//		if (driveStick.getRawButton(ButtonMappings.climberLeftUp)) {  
//			climber.leftClimberUp();
//		} else if (driveStick.getRawButton(ButtonMappings.climberLeftDown)) {
//			climber.leftClimberDown();
//		} else {
//			climber.leftClimberStop();
//		}

		if (driveStick.getRawButton(ButtonMappings.blockerUp) && blockHall.get()) {
			blocker.set(1);
		} else if (driveStick.getRawButton(ButtonMappings.blockerDown)) {
			blocker.set(-1);
		} else {
			blocker.set(0);
		}

//		if (driveStick.getRawButton(ButtonMappings.climberStableLeft)) {
//			climber.leftStableUp();
//		} else if (driveStick.getRawButton(ButtonMappings.climberStableLeftDown)) {
//			climber.leftStableDown();
//		} else {
//			climber.leftStableStop();
//		}

//		if (driveStick.getRawButton(ButtonMappings.climberStableRight)) {
//			climber.rightStableUp();
//		} else if (driveStick.getRawButton(ButtonMappings.climberStableRightDown)) {
//			climber.rightStableDown();
//		} else {
//			climber.rightStableStop();
//		}
		
		Timer.delay(0.01);
		
		camera.printDebugCenters();
		
		//vision debug
		camera.printDebugCenters();

	}

	public void testInit() {
		System.out.print("Hello! Hello! Hello!\nYou're in test mode by the way!\n");
		blockHall = new DigitalInput(Ports.blockerHallEffect);
	}

	private double ultraFeet = 10;
	public void testPeriodic() {
		System.out.println(blockHall.get());
	}
}