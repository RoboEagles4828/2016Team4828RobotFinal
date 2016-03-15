package org.usfirst.frc.team4828.robot;

import org.usfirst.frc.team4828.robot.WorldChampionDrive.Direction;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//when deploying grip, put this as JVM arguments: -Xmx50m -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError
//it stops error about main class = null
public class Robot extends IterativeRobot {
	private WorldChampionDrive rd;
	private Joystick driveStick2, driveStick;
	private CameraMotors camera;
	private AnalogGyro gyro;
	private Shooter shooter;
	private Loader loader;

	private SendableChooser positionChooser;
	private SendableChooser obstacleChooser;

	public void robotInit() {// inits the robot
		driveStick2 = new Joystick(Ports.driveStick2);
		driveStick = new Joystick(Ports.driveStick);
		rd = new WorldChampionDrive(Ports.driveFrontLeft, Ports.driveRearLeft, Ports.driveFrontRight,
				Ports.driveRearRight);
		gyro = new AnalogGyro(Ports.gyro);
		SmartDashboard.putBoolean("Camera Tracking: ", false);
		shooter = new Shooter(Ports.shooterMotor1, Ports.shooterMotor2, Ports.shooterUpDownMotor,
				Ports.shooterLeftRightMotor, Ports.shooterServo1, Ports.shooterServo2, Ports.shooterHallEffect);
		camera = new CameraMotors(shooter);
		loader = new Loader(Ports.loaderUpDownMotor, Ports.loaderIntakeMotor);

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
		PORTCULLIS("Portcullis"), CHEVAL_DE_FRISE("Cheval De Frise"), MOAT("Moat"), RAMPARTS("Ramparts"), DRAWBRIDGE(
				"Drawbridge"), SALLYPORT("Sallyport"), ROCK_WALL("Rock Wall"), ROUGH_TERRAIN("Rough Terrain"), LOW_BAR(
						"Low Bar");
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

	public void autonomousInit() {
		hasRun = false;
	}

	public void autonomousPeriodic() {
		AutoObstacle obstacle = (AutoObstacle) obstacleChooser.getSelected();
		
		if (!hasRun && isAutonomous()) {
			rd.move(Direction.FORWARD, 0.25, 12);
			if(obstacle == AutoObstacle.LOW_BAR){
				loader.reset();
				shooter.reset();
			}
			rd.autoHack();
			Timer.delay(2.8);
			rd.stop();
			hasRun = true;
		}
		// AutoPosition position = (AutoPosition) positionChooser.getSelected();
		
		// System.out.println(position.toString());
		// System.out.println(obstacle.toString());
		//
		// if (!hasRun) {
		// rd.move(Direction.FORWARD, 0.25, 12);
		// loader.reset();
		// shooter.reset();
		// rd.autoHack();
		// //rd.move(Direction.FORWARD, 0.4 , 24);
		// // defeat the obstacle
		// switch (obstacle) {
		// case CHEVAL_DE_FRISE:
		// break;
		// case DRAWBRIDGE:
		// break;
		// case LOW_BAR:
		// //rd.move(Direction.FORWARD, 0.2 , 90 );
		// break;
		// case MOAT:
		// break;
		// case PORTCULLIS:
		// break;
		// case RAMPARTS:
		// break;
		// case ROCK_WALL:
		// break;
		// case ROUGH_TERRAIN:
		// break;
		// case SALLYPORT:
		// break;
		// default:
		// System.out.println("Something went wrong in autonomous Error: 1");
		// break;
		// }
		// //rd.stop();
		// //rd.rotateToAngle(0, gyro);
		// // Move based on the obstacle position
		// switch (position) {
		// case ONE:
		// //rd.move(Direction.FORWARD, .25, 100);
		// //rd.move(Direction.FORWARD, .25, 106.85); // originally 160.85
		// //rd.rotateToAngle(60, gyro); //original 60, rotated for shooting
		// //shooter.setPosition(50000); //tentative rotation rating
		// //camera.enableAutoAim();
		// //rd.move(Direction.FORWARD, .25, 59);
		// //Timer.delay(2);
		// //shooter.shoot();
		// break;
		// case TWO:
		// rd.move(Direction.FORWARD, .25, 100);
		// rd.rotateToAngle(0, gyro);
		// rd.move(Direction.FORWARD, .25, 189.714);
		// rd.rotateToAngle(60, gyro);
		// camera.enableAutoAim();
		// rd.move(Direction.FORWARD, .25, 59);
		// Timer.delay(2);
		// shooter.shoot();
		// break;
		// case THREE:
		// rd.move(Direction.FORWARD, .25, 100);
		// rd.rotateToAngle(0, gyro);
		// rd.rotateToAngle(27.65, gyro);
		// rd.move(Direction.FORWARD, .25, 100.60);
		// rd.rotateToAngle(0, gyro);
		// camera.enableAutoAim();
		// Timer.delay(2);
		// shooter.shoot();
		// break;
		// case FOUR:
		// rd.move(Direction.FORWARD, .25, 100);
		// rd.rotateToAngle(0, gyro);
		// camera.enableAutoAim();
		// rd.move(Direction.FORWARD, .25, 94.5);
		// Timer.delay(2);
		// shooter.shoot();
		// case FIVE:
		// rd.move(Direction.FORWARD, .25, 100);
		// rd.rotateToAngle(0, gyro);
		// rd.move(Direction.FORWARD, .25, 183.074);
		// rd.rotateToAngle(-60, gyro);
		// camera.enableAutoAim();
		// rd.move(Direction.FORWARD, .25, 14.844);
		// Timer.delay(2);
		// shooter.shoot();
		// break;
		// default:
		// System.out.println("Something went wrong in autonomous Error: 2");
		// break;
		// }
		// hasRun = true; // so auto doesn't repeat
		// }
		System.out.println("finish auto");
	}

	public void teleopInit() {
		camera.disableAutoAim();
	}

	private boolean checkLoader = false; // needed for PID; don't touch
	private boolean checkShooter = false; // needed for PID; don't touch
	private boolean checkCamera = false;

	public void teleopPeriodic() {
		SmartDashboard.putNumber("Gyro: ", gyro.getAngle());
		SmartDashboard.putBoolean("Shooter Hall Effect: ", shooter.getHallEffect());
		SmartDashboard.putNumber("Shooter UpDown Motor Enc: ", shooter.getUpDownEncPosition());
		SmartDashboard.putNumber("shooter LeftRight Enc: ", shooter.getLeftRightEncPosition());
		SmartDashboard.putNumber("Loader upDownMotor Enc: ", loader.getEncPosition());
		SmartDashboard.putNumber("Drive RL Enc: ", rd.getRLEnc());
		SmartDashboard.putBoolean("Loader limit down: ", loader.getLimitDown());
		SmartDashboard.putBoolean("Inverse Controls: ", rd.inverseControls);

		SmartDashboard.putNumber("Ideal Shooter Position: ", 235000);
		SmartDashboard.putNumber("Ideal Loader Position: ", 0);

		if (driveStick.getRawButton(ButtonMappings.loaderUp) || driveStick2.getRawButton(ButtonMappings.loaderUp)) {
			checkLoader = true;
			loader.flipUp();
		} else if (driveStick.getRawButton(ButtonMappings.loaderDown)
				|| driveStick2.getRawButton(ButtonMappings.loaderDown)) {
			checkLoader = true;
			loader.flipDown();
		} else {
			if (checkLoader) {
				loader.flipStop();
				loader.lockPosition();
				checkLoader = false;
			}
		}

		if (driveStick.getRawButton(ButtonMappings.shooterRotateLeft)
				|| driveStick2.getRawButton(ButtonMappings.shooterRotateLeft))
			shooter.rotateLeft();
		else if (driveStick.getRawButton(ButtonMappings.shooterRotateRight)
				|| driveStick2.getRawButton(ButtonMappings.shooterRotateRight))
			shooter.rotateRight();
		else
			shooter.rotateStop();

		if (driveStick.getRawButton(ButtonMappings.shooterFlipUp)
				|| driveStick2.getRawButton(ButtonMappings.shooterFlipUp) ) {
			checkShooter = true;
			shooter.flipUp();
		} else if (driveStick.getRawButton(ButtonMappings.shooterFlipDown)
				|| driveStick2.getRawButton(ButtonMappings.shooterFlipDown)) {
			checkShooter = true;
			shooter.flipDown();
		} else {
			if (checkShooter) {
				checkShooter = false;
				shooter.flipStop();
				shooter.lockPosition();
			}
		}

		// loader/shooter intake wheel control
		if (driveStick.getRawButton(ButtonMappings.loaderIntakeOn)) {
			loader.rollIn();
			shooter.shooterIntake();
		} else if (driveStick.getRawButton(ButtonMappings.loaderIntakeOff)) {
			shooter.stopShooter();
			loader.rollStop();
		}

		if (driveStick.getRawButton(ButtonMappings.shooterMoveToHeight)) {
			shooter.setPosition(-43500);
		}
		if (driveStick.getRawButton(ButtonMappings.shooterShoot)) {
			shooter.shooterIntake(); //roll in to make sure ball is not in wheels
			Timer.delay(0.7);
			shooter.shoot();
			//shooter.pushServo();
			//Timer.delay(2);
			//shooter.stopShooter();
			//shooter.retractServo();
		}

		if (driveStick.getRawButton(ButtonMappings.inverseControls)) {
			rd.inverseControls = true;
		}
		if (driveStick.getRawButton(ButtonMappings.undoInverseControls)) {
			rd.inverseControls = false;
		}

		// hold to aim cam
		if (driveStick2.getRawButton(ButtonMappings.aimCamera)) {
			camera.aimCamera();
			checkCamera = true;
		} else {
			if (checkCamera) {
				checkShooter = true;
			}
		}
		// enable passive autoaim
		if (driveStick2.getRawButton(ButtonMappings.enableAutoAim)) {
			camera.enableAutoAim();
		}
		if (driveStick2.getRawButton(ButtonMappings.disableAutoAim)) {
			camera.disableAutoAim();
		}
		if (driveStick2.getRawButton(ButtonMappings.loaderIntakeOut)) {
			loader.rollOut();
		}
		rd.arcadeDrive(driveStick);

		Timer.delay(0.01);
	}

	public void testInit() {
		
	}

	public void testPeriodic() {
		AutoObstacle obstacle = (AutoObstacle) obstacleChooser.getSelected();
		System.out.println(obstacle.stringVal); 
		System.out.println("You're in test mode, by the way.");
	}
}
