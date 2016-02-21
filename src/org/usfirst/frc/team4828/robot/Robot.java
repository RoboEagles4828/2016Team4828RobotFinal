package org.usfirst.frc.team4828.robot;

import org.usfirst.frc.team4828.robot.WorldChampionDrive.Direction;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//when deploying grip, put this as JVM arguments: -Xmx50m -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError
//it stops error about main class = null
public class Robot extends IterativeRobot {
	private WorldChampionDrive rd;
	private Joystick cameraStick, driveStick;
	private CameraMotors camera;
	private AnalogGyro gyro;
	private Shooter shooter;
	private Loader loader;

	private ShooterHoldThread shooterHold;

	private SendableChooser positionChooser;
	private SendableChooser obstacleChooser;

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

	public void robotInit() {// inits the robot
		cameraStick = new Joystick(1);
		driveStick = new Joystick(0);
		rd = new WorldChampionDrive(3, 4, 1, 2);
		gyro = new AnalogGyro(0);
		SmartDashboard.putBoolean("Camera Tracking: ", false);
		shooter = new Shooter(5, 6, 16, 7, 3, 1);
		camera = new CameraMotors(shooter.getLeftRightMotor(), shooter.getUpDownMotor());
		loader = new Loader(9, 5);

		shooterHold = new ShooterHoldThread(shooter.getUpDownMotor(), driveStick, 9, 10);
		shooterHold.start();
	}

	private boolean hasRun = false;

	public void autonomousInit() {
		hasRun = false;

		positionChooser = new SendableChooser();
		positionChooser.addDefault("1", AutoPosition.ONE);
		positionChooser.addObject("2", AutoPosition.TWO);
		positionChooser.addObject("3", AutoPosition.THREE);
		positionChooser.addObject("4", AutoPosition.FOUR);
		positionChooser.addObject("5", AutoPosition.FIVE);

		obstacleChooser = new SendableChooser();
		obstacleChooser.addDefault("Low Bar", AutoObstacle.LOW_BAR);
		obstacleChooser.addObject("Portcullis", AutoObstacle.PORTCULLIS);
		obstacleChooser.addObject("Cheval De Frise", AutoObstacle.CHEVAL_DE_FRISE);
		obstacleChooser.addObject("Moat", AutoObstacle.MOAT);
		obstacleChooser.addObject("Ramparts", AutoObstacle.RAMPARTS);
		obstacleChooser.addObject("Drawbridge", AutoObstacle.DRAWBRIDGE);
		obstacleChooser.addObject("Sallyport", AutoObstacle.SALLYPORT);
		obstacleChooser.addObject("Rock Wall", AutoObstacle.ROCK_WALL);
		obstacleChooser.addObject("Rough Terrain", AutoObstacle.ROUGH_TERRAIN);

		SmartDashboard.putData("Autonomous Position", positionChooser);
		SmartDashboard.putData("Autonomous Obstacle", obstacleChooser);
	}

	public void autonomousPeriodic() {
		AutoPosition position = (AutoPosition) positionChooser.getSelected();
		AutoObstacle obstacle = (AutoObstacle) obstacleChooser.getSelected();
		System.out.println(position.toString());
		System.out.println(obstacle.toString());

		if (!hasRun) {
			rd.move(Direction.FORWARD, 0.25 /* ,52 */);
			// defeat the obstacle
			switch (obstacle) {
			case CHEVAL_DE_FRISE:
				break;
			case DRAWBRIDGE:
				break;
			case LOW_BAR:
				rd.move(Direction.FORWARD, 0.2 /* , 84 */);
				break;
			case MOAT:
				break;
			case PORTCULLIS:
				break;
			case RAMPARTS:
				break;
			case ROCK_WALL:
				break;
			case ROUGH_TERRAIN:
				break;
			case SALLYPORT:
				break;
			default:
				System.out.println("Something went wrong in autonomous Error: 1");
				break;
			}
			rd.stop();
			rd.rotateToAngle(0, gyro);
			// Move based on the obstacle position
			switch (position) {
			case ONE:
				rd.move(Direction.FORWARD, 0.25/* , 77 */);
				rd.rotateToAngle(60, gyro);
				camera.enableAutoAim();
				rd.move(Direction.FORWARD, 0.25/* , 59 */);
				Timer.delay(2);
				shooter.shoot();
				break;
			case TWO:
				break;
			case THREE:
				break;
			case FOUR:
				break;
			case FIVE:
				break;
			default:
				System.out.println("Something went wrong in autonomous Error: 2");
				break;
			}
			hasRun = true; // so auto doesn't repeat
		}
	}

	public void teleopInit() {
		camera.disableAutoAim();
	}

	public void teleopPeriodic() {
		SmartDashboard.putNumber("Gyro: ", gyro.getAngle());
		SmartDashboard.putBoolean("Shooter Hall Effect: ", shooter.getHallEffect());
		SmartDashboard.putNumber("Shooter UpDown Motor Encoder: ", shooter.getUpDownEncPosition());
		// SmartDashboard.putNumber("Loader Motor Encoder: ",
		// loader.getEncPosition());
		// loader up/down
		if (driveStick.getRawButton(11))
			loader.flipUp();
		else if (driveStick.getRawButton(12))
			loader.flipDown();
		else
			loader.flipStop();

		if (driveStick.getRawButton(7))
			shooter.rotateLeft();
		else if (driveStick.getRawButton(8))
			shooter.rotateRight();
		else
			shooter.rotateStop();

		if (driveStick.getRawButton(9))
			shooter.flipUp();
		else if (driveStick.getRawButton(10))
			shooter.flipDown();
		else
			shooter.flipStop();

		// loader/shooter intake wheel control
		if (driveStick.getRawButton(5)) {
			loader.rollIn();
			shooter.shooterIntake();
		} else if (driveStick.getRawButton(3)) {
			shooter.stopShooter();
			loader.rollStop();
		}

		if (driveStick.getRawButton(6))
			shooter.pushServo();
		else if (driveStick.getRawButton(4))
			shooter.retractServo();

		if (driveStick.getTrigger()) {
			shooter.shoot();
		}

		rd.arcadeDrive(driveStick);

		// camera.servoDebugOutput();
		// if (cameraStick.getTrigger())
		// // camera.stickAim(cameraStick);
		// rd.arcadeDrive(driveStick);
		// if (cameraStick.getRawButton(11))
		// camera.enableAutoAim();
		// else if (cameraStick.getRawButton(12))
		// camera.disableAutoAim();
		// else if (cameraStick.getRawButton(10))
		// camera.setToBasePosition();
		// else if (driveStick.getRawButton(3))
		// gyro.reset();
		// else if (driveStick.getRawButton(4))
		// rd.rotateToAngle(0, gyro);

		Timer.delay(0.01);
	}

	public void testInit() {

	}

	public double pos = 0;
	public void testPeriodic() {
		System.out.println("Gyro: " + gyro.getAngle());
		SmartDashboard.putNumber("Gyro: ", gyro.getAngle());
		if(driveStick.getRawButton(11))
			
		rd.arcadeDrive(driveStick);
	}
}
