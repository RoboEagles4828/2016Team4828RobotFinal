package org.usfirst.frc.team4828.robot;

import org.usfirst.frc.team4828.robot.WorldChampionDrive.Direction;

import edu.wpi.first.wpilibj.AnalogGyro;
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
		AutoPosition position = (AutoPosition) obstacleChooser.getSelected();
		
		switch(position){
		case ONE:
			//setup robot with back edge of robot on auto line
			rd.move(Direction.FORWARD, .25, 18, this);
			loader.reset(this);
			shooter.reset(this);
			rd.move(Direction.FORWARD,.25, 18, this); //move from auto line to defense
			rd.rotateToAngle(0, gyro); //reorient
			rd.move(Direction.FORWARD,.25, 98, this); //defeat defense, move 1 foot + robot length past it for buffer
			rd.rotateToAngle(0, gyro); //reorient
			rd.move(Direction.FORWARD,.25, 81.85, this); //drive forward and place midpoint of robot on turn point
			rd.rotateToAngle(240, gyro); //rotate 60 degrees clockwise +  180 to turn robot around backward, back now points at goal
			camera.enableAutoAim();
			//robot is now 136.31 inches from goal (14 ft 9.93 inches)
			//rd.move(Direction.BACK,.25, shootingrange); //move into optimal shooting range
			Timer.delay(2);
			shooter.shoot();
			break;
		case TWO:
			//setup robot with back edge of robot on auto line
			rd.move(Direction.FORWARD,.25, 36, this); //move front of robot to defense
			rd.rotateToAngle(0, gyro); //reorient
			rd.move(Direction.FORWARD,.25, 98, this); //defeat defense, move 1 ft + robot length past it for buffer
			rd.rotateToAngle(0, gyro); //reorient
			rd.move(Direction.FORWARD,.25, 110.71, this); //drive forward to turn point
			rd.rotateToAngle(240, gyro); //rotate 60 degrees clockwise from forward to point at goal
			camera.enableAutoAim();
			//robot is now 78.57 inches from goal
			//rd.move(Direction.BACK,.25, shootingrange); //move into optimal shooting range
			Timer.delay(2);
	    	shooter.shoot();
			break;
		case THREE:
			//setup robot with back edge of robot on auto line
			rd.move(Direction.FORWARD,.25, 36, this); //move front of robot to defense
			rd.rotateToAngle(0, gyro); //reorient
			rd.move(Direction.FORWARD,.25, 98, this); //defeat defense, move 1 ft + robot length past it for buffer
			rd.rotateToAngle(0, gyro); //reorient
			rd.move(Direction.FORWARD,.25, 27.75, this); //drive forward to turn point 1
			rd.rotateToAngle(90, gyro); //rotate 90 degrees clockwise
			rd.move(Direction.FORWARD,.25, 40, this); //drive forward to turn point 2
			rd.rotateToAngle(180, gyro); //point back end at goal
			camera.enableAutoAim();
			//robot is now 108.75 inches from goal
			// rd.move(Direction.BACK,.25, shootingrange); //move into optimal shooting range
			Timer.delay(2);
			shooter.shoot();
			break;
		case FOUR:
			//setup robot with back edge of robot on auto line
			rd.move(Direction.FORWARD,.25, 36, this); //move front of robot to defense
			rd.rotateToAngle(0, gyro); //reorient
			rd.move(Direction.FORWARD,.25, 98, this); //defeat defense, move 1 ft + robot length past it for buffer
			rd.rotateToAngle(0, gyro); //reorient
			rd.move(Direction.FORWARD,.25, 27.75, this); //drive forward to turn point 1
			//rd.rotateToAngle(-90, gyro); //rotate 90 degrees clockwise
			//rd.move(Direction.FORWARD,.25, 8); //drive forward to turn point 2
			rd.rotateToAngle(180, gyro); //point back end at goal
			camera.enableAutoAim();
			//robot is now 108.75 inches from goal
			//rd.move(Direction.BACK,.25, shootingrange); //move into optimal shooting range
			Timer.delay(2);
			shooter.shoot();
			break;
		case FIVE:
			//setup robot with back edge of robot on auto line
			rd.move(Direction.FORWARD,.25, 36, this); //move front of robot to defense
			rd.rotateToAngle(0, gyro); //reorient
			rd.move(Direction.FORWARD,.25, 98, this); //defeat defense, move 1 ft + robot length past it for buffer
			rd.rotateToAngle(0, gyro); //reorient
			rd.move(Direction.FORWARD,.25, 27.75, this); //drive forward to turn point 1
			rd.rotateToAngle(-90, gyro); //rotate 90 degrees clockwise
			rd.move(Direction.FORWARD,.25, 63, this); //drive forward to turn point 2
			rd.rotateToAngle(180, gyro); //point back end at goal
			camera.enableAutoAim();
			//robot is now 108.75 inches from goal
			//rd.move(Direction.BACK,.25, shootingrange); //move into optimal shooting range
			Timer.delay(2);
			shooter.shoot();
			break;
		}
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
		//SmartDashboard.putNumber("Drive RL Enc: ", rd.getRLEnc());
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

		if ((driveStick.getRawButton(ButtonMappings.shooterFlipUp)
				|| driveStick2.getRawButton(ButtonMappings.shooterFlipUp)) && shooter.getUpDownEncPosition() < -6000 ) {
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
			if(shooter.getUpDownEncPosition() > -42500)
				shooter.flipEnc(-9000);
			else if (shooter.getUpDownEncPosition() < -45500)
				shooter.flipEnc(9000);
			else 
				shooter.flipStop();
		}
		
		if(driveStick2.getRawButton(ButtonMappings.shooterMoveToLoad)){
			if(shooter.getUpDownEncPosition() > -236000)
				shooter.flipEnc(-9000);
			else if(shooter.getUpDownEncPosition() < -244000)
				shooter.flipEnc(9000);
			else
				shooter.flipStop();
		}
		
		if(driveStick2.getRawButton(ButtonMappings.loaderMoveToLoad)){
			if(loader.getEncPosition() > -2900)
				loader.flipDown();
			else if (loader.getEncPosition() < -3200)
				loader.flipUp();
			else
				loader.flipStop();
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
				checkCamera = false;
				checkShooter = true;
			}
		}
		rd.arcadeDrive(driveStick);

		Timer.delay(0.01);
	}

	public void testInit() {
		System.out.print("Hello! Hello! Hello!\nYou're in test mode by the way!\n");
	}

	public void testPeriodic() {
//		}
	}
}
