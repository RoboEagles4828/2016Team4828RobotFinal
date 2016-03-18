package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.GenericHID;

public class WorldChampionDrive {
	public enum Direction {
		FORWARD, BACKWARD, LEFT, RIGHT, SPINLEFT, SPINRIGHT;
	}

	public enum Rotation {
		LEFT90, RIGHT90;
	}

	public boolean inverseControls = false;

	public CANTalon frontLeft;
	public CANTalon rearLeft;
	public CANTalon frontRight;
	public CANTalon rearRight;

	protected static final int kMaxNumberOfMotors = 4;

	public WorldChampionDrive(int flport, int rlport, int frport, int rrport) {
		frontLeft = new CANTalon(flport);
		rearLeft = new CANTalon(rlport);
		frontRight = new CANTalon(frport);
		rearRight = new CANTalon(rrport);
	}

//	public double getRLEnc() {
//		return rearLeft.getEncPosition();
//	}

	// public void printEncOutput() {
	// System.out.println("CAN1: " + rearRight.getEncPosition() + " " +
	// rearRight.getAnalogInVelocity());
	// System.out.println("CAN2: " + frontRight.getEncPosition() + " "+ " " +
	// frontRight.getEncVelocity());
	// System.out.println("CAN3: " + frontLeft.getEncPosition() + " " +
	// frontLeft.getAnalogInVelocity());
	// System.out.println("CAN4: " + rearLeft.getEncPosition() + " " + " " +
	// rearLeft.getEncVelocity() + "\n\n");
	// }

	protected static double limit(double num) {
		if (num > 1.0) {
			return 1.0;
		}
		if (num < -1.0) {
			return -1.0;
		}
		return num;
	}

	public void stop() {
		frontLeft.set(0);
		rearLeft.set(0);
		frontRight.set(0);
		rearRight.set(0);
	}

	public static class MotorType {
		public final int value;
		static final int kFrontLeft_val = 0;
		static final int kFrontRight_val = 1;
		static final int kRearLeft_val = 2;
		static final int kRearRight_val = 3;

		public static final MotorType kFrontLeft = new MotorType(kFrontLeft_val);
		public static final MotorType kFrontRight = new MotorType(kFrontRight_val);
		public static final MotorType kRearLeft = new MotorType(kRearLeft_val);
		public static final MotorType kRearRight = new MotorType(kRearRight_val);

		private MotorType(int value) {
			this.value = value;
		}
	}

	protected static void normalize(double wheelSpeeds[]) {
		double maxMagnitude = Math.abs(wheelSpeeds[0]);
		int i;
		for (i = 1; i < kMaxNumberOfMotors; i++) {
			double temp = Math.abs(wheelSpeeds[i]);
			if (maxMagnitude < temp)
				maxMagnitude = temp;
		}
		if (maxMagnitude > 1.0) {
			for (i = 0; i < kMaxNumberOfMotors; i++) {
				wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
			}
		}
	}

	public void tankDrive(GenericHID leftStick, GenericHID rightStick) {
		if (leftStick == null || rightStick == null) {
			throw new NullPointerException("Null HID provided");
		}
		tankDrive(leftStick.getY(), rightStick.getY(), true);
	}

	public void tankDrive(double leftValue, double rightValue, boolean squaredInputs) {
		leftValue = limit(leftValue);
		rightValue = limit(rightValue);
		if (squaredInputs) {
			if (leftValue >= 0.0) {
				leftValue = (leftValue * leftValue);
			} else {
				leftValue = -(leftValue * leftValue);
			}
			if (rightValue >= 0.0) {
				rightValue = (rightValue * rightValue);
			} else {
				rightValue = -(rightValue * rightValue);
			}
		}
		setLeftRightMotorOutputs(leftValue, rightValue);
	}

	public void setLeftRightMotorOutputs(double leftOutput, double rightOutput) {
		if (rearLeft == null || rearRight == null) {
			throw new NullPointerException("Null motor provided");
		}
		if (frontLeft != null) {
			frontLeft.set(limit(leftOutput));
		}
		rearLeft.set(limit(leftOutput));
		if (frontRight != null) {
			frontRight.set(-limit(rightOutput));
		}
		rearRight.set(-limit(rightOutput));
	}

	public void arcadeDrive(GenericHID stick) {
		this.arcadeDrive(stick, true);
	}

	public void arcadeDrive(GenericHID stick, boolean squaredInputs) {
		arcadeDrive(stick.getY(), stick.getX(), squaredInputs);
	}

	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) {
		double leftMotorSpeed;
		double rightMotorSpeed;
		moveValue = limit(moveValue);
		rotateValue = limit(rotateValue);

		if (inverseControls) {
			moveValue = -moveValue;
		}

		if (squaredInputs) {
			if (moveValue >= 0.0) {
				moveValue = (moveValue * moveValue);
			} else {
				moveValue = -(moveValue * moveValue);
			}
			if (rotateValue >= 0.0) {
				rotateValue = (rotateValue * rotateValue);
			} else {
				rotateValue = -(rotateValue * rotateValue);
			}
		}
		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				leftMotorSpeed = Math.max(moveValue, -rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			}
		} else {
			if (rotateValue > 0.0) {
				leftMotorSpeed = -Math.max(-moveValue, rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			} else {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
			}
		}
		setLeftRightMotorOutputs(leftMotorSpeed, rightMotorSpeed);
	}

	public void mecanumDrive_Polar(double magnitude, double direction, double rotation) {
		magnitude = limit(magnitude) * Math.sqrt(2.0);
		double dirInRad = (direction + 45.0) * Math.PI / 180.0;
		double cosD = Math.cos(dirInRad);
		double sinD = Math.sin(dirInRad);
		double wheelSpeeds[] = new double[kMaxNumberOfMotors];
		wheelSpeeds[MotorType.kFrontLeft_val] = (sinD * magnitude + rotation);
		wheelSpeeds[MotorType.kFrontRight_val] = (cosD * magnitude - rotation);
		wheelSpeeds[MotorType.kRearLeft_val] = (cosD * magnitude + rotation);
		wheelSpeeds[MotorType.kRearRight_val] = (sinD * magnitude - rotation);
		normalize(wheelSpeeds);
		frontLeft.set(wheelSpeeds[MotorType.kFrontLeft_val]);
		frontRight.set(wheelSpeeds[MotorType.kFrontRight_val]);
		rearLeft.set(wheelSpeeds[MotorType.kRearLeft_val]);
		rearRight.set(wheelSpeeds[MotorType.kRearRight_val]);
	}

	public void rotateToAngle(double angle, AnalogGyro gyro) {
		if (gyro.getAngle() % 360 > angle) {
			while (gyro.getAngle() % 360 > angle)
				move(Direction.SPINLEFT, 0.3);
		} else if (gyro.getAngle() % 360 < angle) {
			while (gyro.getAngle() % 360 < angle)
				move(Direction.SPINRIGHT, 0.3);
		}
		stop();
	}

	// public void driveRotations(int rotations, double speed) {
	// int encStart = rearLeft.getEncPosition();
	// double encIncNeeded = rotations * 1440;
	// while (rearLeft.getEncPosition() < encStart + encIncNeeded
	// && rearLeft.getEncPosition() > encStart - encIncNeeded) {
	// move(Direction.FORWARD, speed);
	// System.out.println(rearLeft.getEncPosition());
	// }
	// stop();
	// }
	//
	// // encoders = 1440 per rotation
	// private final static double PULSE_PER_90 = 1.0D;
	//
	// public void rotate(Rotation rotation, double speed) {
	// int encStart = rearLeft.getEncPosition();
	// double encIncNeeded = PULSE_PER_90;
	// if (rotation == Rotation.LEFT90) {
	// while (frontRight.getEncPosition() < encStart + encIncNeeded)
	// move(Direction.SPINLEFT, speed);
	// stop();
	// } else if (rotation == Rotation.RIGHT90) {
	// while (rearLeft.getEncPosition() < encStart + encIncNeeded)
	// move(Direction.SPINRIGHT, speed);
	// stop();
	// }
	// }
	//
	private final static double FUDGE_FACTOR = 288;
	private final static double PULSE_PER_INCH = 45.830D;

	public void move(Direction direction, double speed, double inches, Robot r) {
		int encStart = rearLeft.getEncPosition();
		if (inches <= 0)
			System.out.println("Distance requested < 0 inches; put in a valid parameter");
		else {
			double encIncNeeded = PULSE_PER_INCH * inches;
			while (rearLeft.getEncPosition() < encStart + encIncNeeded - FUDGE_FACTOR
					&& rearLeft.getEncPosition() > encStart - encIncNeeded + FUDGE_FACTOR && r.isAutonomous())
				move(direction, speed);
			stop();
		}
	}

	public void autoHack() {
		frontLeft.set(0.55);
		frontRight.set(-0.55);
		rearLeft.set(0.55);
		rearRight.set(-0.55);
	}

	public void move(Direction direction, double speed) {
		if (direction == Direction.BACKWARD) {
			frontLeft.set(-speed);
			frontRight.set(-speed);
			rearLeft.set(-speed);
			rearRight.set(-speed);
		} else if (direction == Direction.FORWARD) {
			frontLeft.set(speed);
			frontRight.set(-speed);
			rearLeft.set(speed);
			rearRight.set(-speed);
		} else if (direction == Direction.LEFT) {
			frontLeft.set(speed);
			frontRight.set(speed);
			rearLeft.set(-speed);
			rearRight.set(-speed);
		} else if (direction == Direction.RIGHT) {
			frontLeft.set(-speed);
			frontRight.set(-speed);
			rearLeft.set(speed);
			rearRight.set(speed);
		} else if (direction == Direction.SPINRIGHT) {
			frontLeft.set(-speed);
			frontRight.set(-speed);
			rearLeft.set(-speed);
			rearRight.set(-speed);
		} else if (direction == Direction.SPINLEFT) {
			frontLeft.set(speed);
			frontRight.set(speed);
			rearLeft.set(speed);
			rearRight.set(speed);
		}
	}

}
