package org.usfirst.frc.team4828.robot;

import java.io.IOException;

import org.usfirst.frc.team4828.robot.WorldChampionDrive.Direction;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//when deploying grip, put this as JVM arguments: -Xmx50m -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError
//it stops error about main class = null
public class Robot extends IterativeRobot {
	private WorldChampionDrive rd;
	private Joystick driveStick2, driveStick, climbStick;
	private CameraMotors camera;
	private AnalogGyro gyro;
	private Shooter shooter;
	private Loader loader;
	private AnalogInput ultrasonic;
	private Climber climber;

	private PowerDistributionPanel pdp;
	private BuiltInAccelerometer accelerometer;

	private SendableChooser positionChooser;
	private SendableChooser obstacleChooser;
	private static final double AUTOAIM_DELAY = 7;

	public void robotInit() {// inits the robot
		driveStick2 = new Joystick(Ports.driveStick2);
		driveStick = new Joystick(Ports.driveStick);
		climbStick = new Joystick(Ports.climbStick);
		rd = new WorldChampionDrive(Ports.driveFrontLeft, Ports.driveRearLeft, Ports.driveFrontRight,
				Ports.driveRearRight);
		gyro = new AnalogGyro(Ports.gyro);
		SmartDashboard.putBoolean("Camera Tracking: ", false);
		shooter = new Shooter(Ports.shooterMotor1, Ports.shooterMotor2, Ports.shooterUpDownMotor,
				Ports.shooterLeftRightMotor, Ports.shooterServo1, Ports.shooterServo2, Ports.shooterHallEffect);
		camera = new CameraMotors(shooter);
		loader = new Loader(Ports.loaderUpDownMotor, Ports.loaderIntakeMotor);
		climber = new Climber();

		ultrasonic = new AnalogInput(Ports.ultrasonic);
		pdp = new PowerDistributionPanel();
		accelerometer = new BuiltInAccelerometer();

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
		// AutoPosition position = (AutoPosition) positionChooser.getSelected();

		if (!hasRun) {
			// try {
			// new ProcessBuilder("/home/lvuser/grip").inheritIO().start();
			// SmartDashboard.putBoolean("Started GRIP: ", true);
			// } catch (Exception e) {
			// e.printStackTrace();
			// SmartDashboard.putBoolean("Started GRIP: ", false);
			// System.out.println("Couldn't start GRIP");
			// }
			gyro.reset();
//			rd.autoHack();
//			Timer.delay(2.5);
//			rd.stop();
			if(obstacle == AutoObstacle.LOW_BAR){
				rd.move(Direction.FORWARD, 40, 18, this);
				loader.reset();
				Timer.delay(.5);
				shooter.reset();
				Timer.delay(1);
			}
			rd.autoHack();
			Timer.delay(2);
			hasRun = true;
		}

		//
		// switch (position) {
		// case ONE:
		// // setup robot with back edge of robot on auto line
		// rd.move(Direction.BACKWARD, .65, 18, this);
		// System.out.println("drove 18");
		// loader.reset(this);
		// System.out.println("reset loader");
		// shooter.reset(this);
		// rd.move(Direction.BACKWARD, .65, 18, this);
		// System.out.println("drove 18 again");
		// rd.move(Direction.BACKWARD, .65, 98, this);
		// System.out.println("drove 98");
		// Timer.delay(0.3);
		// rd.rotateToAngle(0, gyro, this);
		// Timer.delay(0.3);
		// System.out.println("reset gyro");
		// rd.move(Direction.BACKWARD, .65, 81.85, this);
		// System.out.println("drove 81.85");
		// rd.rotateToAngle(60, gyro, this);
		// System.out.println("rotate to 60");
		// rd.move(Direction.BACKWARD, .3, 48, this);
		// if (isAutonomous())
		// loader.reset();
		// shooter.autoHack(this);
		// camera.enableAutoAim();
		// if (isAutonomous()) {
		// System.out.println("delaying for cam to aim");
		// Timer.delay(AUTOAIM_DELAY);
		// shooter.shoot();
		// }
		// break;
		// case TWO:
		// rd.move(Direction.BACKWARD, .65, 36, this);
		// rd.move(Direction.BACKWARD, .65, 104, this);
		// Timer.delay(0.3);
		// rd.rotateToAngle(0, gyro, this);
		// Timer.delay(0.3);
		// rd.move(Direction.BACKWARD, .65, 80.71, this);
		// rd.rotateToAngle(60, gyro, this);
		// loader.reset(this);
		// shooter.autoHack(this);
		// camera.enableAutoAim();
		// if (this.isAutonomous()) {
		// Timer.delay(AUTOAIM_DELAY);
		// shooter.shoot();
		// }
		// break;
		// case THREE:
		// // setup robot with back edge of robot on auto line
		// rd.move(Direction.BACKWARD, .65, 36, this);
		// rd.move(Direction.BACKWARD, .65, 104, this);
		// Timer.delay(0.3);
		// rd.rotateToAngle(0, gyro, this); // reorient
		// Timer.delay(0.3);
		// rd.move(Direction.BACKWARD, .65, 45.75, this);
		// rd.rotateToAngle(90, gyro, this);
		// rd.move(Direction.BACKWARD, .3, 40, this);
		// rd.rotateToAngle(0, gyro, this); // point back end at goal
		// loader.reset(this);
		// shooter.autoHack(this);
		// camera.enableAutoAim();
		// if (this.isAutonomous()) {
		// Timer.delay(AUTOAIM_DELAY);
		// shooter.shoot();
		// }
		// break;
		// case FOUR:
		// // setup robot with back edge of robot on auto line
		// rd.move(Direction.BACKWARD, .65, 36, this);
		// rd.move(Direction.BACKWARD, .65, 104, this); // defeat obstacle
		// Timer.delay(0.3);
		// rd.rotateToAngle(0, gyro, this);
		// Timer.delay(0.3);
		// rd.move(Direction.BACKWARD, .65, 29.75, this);
		// loader.reset(this);
		// shooter.autoHack(this);
		// camera.enableAutoAim();
		// if (this.isAutonomous()) {
		// Timer.delay(AUTOAIM_DELAY);
		// shooter.shoot();
		// }
		// break;
		// case FIVE:
		// // setup robot with back edge of robot on auto line
		// rd.move(Direction.BACKWARD, .65, 36, this);
		// rd.rotateToAngle(0, gyro, this); // reorient
		// rd.move(Direction.BACKWARD, .65, 104, this);
		// Timer.delay(0.3);
		// rd.rotateToAngle(0, gyro, this);
		// Timer.delay(0.3);
		// rd.move(Direction.BACKWARD, .65, 21.75, this);
		// rd.rotateToAngle(-90, gyro, this);
		// rd.move(Direction.FORWARD, .65, 63, this);
		// rd.rotateToAngle(180, gyro, this);
		// loader.reset(this);
		// shooter.autoHack(this);
		// camera.enableAutoAim();
		// if (this.isAutonomous()) {
		// Timer.delay(AUTOAIM_DELAY);
		// shooter.shoot();
		// }
		// break;
		// }
		// }
		hasRun = true;
		// }
		System.out.println("finish auto");
	}

	public void teleopInit() {
		camera.disableAutoAim();
	}

	private boolean checkLoader = false; // needed for PID; don't touch
	private boolean checkShooter = false; // needed for PID; don't touch
	private boolean checkCamera = false;

	private double ultraV = 0;
	private int ultraSamplingCounter = 0;

	public void teleopPeriodic() {
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

		if (driveStick.getRawButton(ButtonMappings.shooterRotateLeft)
				|| driveStick2.getRawButton(ButtonMappings.shooterRotateLeft))
			shooter.rotateLeft();
		else if (driveStick.getRawButton(ButtonMappings.shooterRotateRight)
				|| driveStick2.getRawButton(ButtonMappings.shooterRotateRight))
			shooter.rotateRight();
		else
			shooter.rotateStop();

		if ((driveStick.getRawButton(ButtonMappings.shooterFlipUp)
				|| driveStick2.getRawButton(ButtonMappings.shooterFlipUp))) {
			checkShooter = true;
			shooter.flipUp();
		} else if (driveStick.getRawButton(ButtonMappings.shooterFlipDown)
				|| driveStick2.getRawButton(ButtonMappings.shooterFlipDown)) {
			checkShooter = true;
			shooter.flipDown();
		} else if (driveStick.getRawButton(ButtonMappings.shooterFlipUpSlow)) {
			//checkShooter = true;
			//shooter.flipUpSlow();
		} else if (driveStick.getRawButton(ButtonMappings.shooterFlipDownSlow)) {
			//checkShooter = true;
			//shooter.flipDownSlow();
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
			shooter.setPosition(-66000); //-66000 HIGH
		}
		
		if(driveStick2.getRawButton(ButtonMappings.shooterLoadSet)){
		//	shooter.setPosition(-288000);
		}

		if (driveStick2.getRawButton(ButtonMappings.shooterCenter)) {
			shooter.center();
		}

		if (driveStick.getRawButton(ButtonMappings.shooterShoot)) {
			shooter.shoot(); // locks robot into the shooting sequence
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
		
		if(climbStick.getRawButton(ButtonMappings.loaderSet)){
			loader.setPosition(-2900);
		}

		rd.arcadeDriveRamp(driveStick);
		//rd.tankDrive(driveStick, climbStick);
		
		if (climbStick.getRawButton(ButtonMappings.climberSetup)) {
			//climber.setup();
		}
		if (climbStick.getRawButton(ButtonMappings.climberLeftUp)) {
			climber.leftClimberUp();
		} else if (climbStick.getRawButton(ButtonMappings.climberLeftDown)) {
			climber.leftClimberDown();
		} else {
			climber.leftClimberStop();
		}

		if (climbStick.getRawButton(ButtonMappings.climberRightUp)) {
			climber.rightClimberUp();
		} else if (climbStick.getRawButton(ButtonMappings.climberRightDown)) {
			climber.rightClimberDown();
		} else {
			climber.rightClimberStop();
		}

		if (climbStick.getRawButton(ButtonMappings.climberStableLeft)) {
			climber.leftStableUp();
		} else if (climbStick.getRawButton(ButtonMappings.climberStableLeftDown)) {
			climber.leftStableDown();
		} else {
			climber.leftStableStop();
		}

		if (climbStick.getRawButton(ButtonMappings.climberStableRight)) {
			climber.rightStableUp();
		} else if (climbStick.getRawButton(ButtonMappings.climberStableRightDown)) {
			climber.rightStableDown();
		} else {
			climber.rightStableStop();
		}

		Timer.delay(0.01);
	}

	double accelerometerOutputCounter = 0;
	double accX = 0, accY = 0, accZ = 0;

	public void testInit() {
		System.out.print("Hello! Hello! Hello!\nYou're in test mode by the way!\n");
		rd.rotateToAngle(240, gyro, this);
	}

	public void testPeriodic() {
		Timer.delay(.1);
		if (driveStick.getRawButton(1)) {
			rd.rotateToAngle(180, gyro, this);
		} else {
			rd.rotateToAngle(0, gyro, this);
		}
	}
}