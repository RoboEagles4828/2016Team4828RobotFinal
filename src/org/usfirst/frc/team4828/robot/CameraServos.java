package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CameraServos {
	Servo servoH;
	Servo servoV;
	NetworkTable table;
	AimThread aim;

	// DON'T MESS WITH THIS THREAD THANKS -Ben 					Wow threads, gj Ben. -Joseph
	public class AimThread extends Thread {
		private boolean enabled = false;
		private boolean alive = true;
		private double delay = 0.02;

		public AimThread(double delay) {
			this.delay = delay;
			enabled = false;
			alive = true;
		}

		public void kill() {
			alive = false;
		}

		public void disable() {
			enabled = false;
		}

		public void enable() {
			enabled = true;
		}

		public void run() {
			while (alive) {
				// System.out.println("AimThread enabled: " + enabled);
				if (enabled) {
					aimCamera();
					Timer.delay(delay);
				}
			}
		}
	}

	public void enableAutoAim() {
		aim.enable();
		SmartDashboard.putBoolean("Camera Tracking: ", true);
		setToBasePosition();
	}

	public void disableAutoAim() {
		aim.disable();
		SmartDashboard.putBoolean("Camera Tracking: ", false);
		setToBasePosition();
	}

	private final double defaultH = 0.4;
	private final double defaultV = 0.55;

	public CameraServos(int port1, int port2) {
		servoH = new Servo(port1);
		servoV = new Servo(port2);
		table = NetworkTable.getTable("GRIP/myContoursReport");
		// servoH.setAngle(80); // default servo settings
		// servoV.setAngle(120);
		setToBasePosition();
		aim = new AimThread(0.02);
		aim.disable();
		aim.start();
	}

	public void set(double h, double v) {
		servoH.set(h);
		servoV.set(v);
	}

	public void setAngle(double hA, double vA) {
		servoH.setAngle(hA);
		servoV.setAngle(vA);
	}

	public void setToBasePosition() {
		servoH.set(defaultH);
		servoV.set(defaultV);
	}

	public String arrOutput(double[] a) { // array output
		String s = "";
		for (int i = 0; i < a.length; i++) {
			s += a[i];
			s += "   ";
		}
		if (s == "")
			s = "nonexistent";
		return s;
	}

	private final static double DEGREE = .0015; // base speed
	private final static int DEADZONE = 10; // degree of error
	private final static int CAMERA_X_CENTER = 160; // center of camera x coord
	private final static int CAMERA_Y_CENTER = 120; // center of camera y coord

	public void aimCamera() { // CAMERAS WILL BE MOTORS IN THE REAL DEAL,
								// COPYPASTA THIS SECTION AND REPLACE WITH MOTOR
								// FUNCTIONALITY
		double[] defaultValue = { 0 };
		double[] centerX = table.getNumberArray("centerX", defaultValue);
		double[] centerY = table.getNumberArray("centerY", defaultValue);
		if (centerX[0] > 0) {
			// System.out.println("contour found");
			int contourUsed = parseContourMap(table.getNumberArray("width", defaultValue));
			double xval = centerX[contourUsed];
			double yval = centerY[contourUsed];
			if (centerX.length > 0) {
				double xmult = Math.abs(xval - CAMERA_X_CENTER) / (CAMERA_X_CENTER / 8);
				double ymult = Math.abs(yval - CAMERA_Y_CENTER) / (CAMERA_Y_CENTER / 8);
				if (xval > CAMERA_X_CENTER + DEADZONE) {
					servoH.set(servoH.get() + DEGREE * xmult);
					// System.out.println("too far left");
				} else if (xval < CAMERA_X_CENTER - DEADZONE) {
					servoH.set(servoH.get() - DEGREE * xmult);
					// System.out.println("too far right");
				}
				if (yval > CAMERA_Y_CENTER + DEADZONE) {
					servoV.set(servoV.get() + DEGREE * ymult / 2);
					// System.out.println("too far up");
				} else if (yval < CAMERA_Y_CENTER - DEADZONE) {
					servoV.set(servoV.get() - DEGREE * ymult / 2);
					// System.out.println("too far down");
				}
			}
		}
	}

	public int parseContourMap(double[] map) { // returns the index of longest
												// countour
		double longest = 0;
		int num = 0;
		for (int i = 0; i < map.length; i++) {
			if (map[i] > longest) {
				num = i;
				longest = map[i];
			}
		}
		return num;
	}

	public void aimCameraMotors(RobotDrive drive) {
		double[] defaultValue = { 0 };
		double[] centerX = table.getNumberArray("centerX", defaultValue);
		double[] centerY = table.getNumberArray("centerY", defaultValue);
		if (centerX.length > 0) {
			// System.out.println("contour found");
			int contourUsed = parseContourMap(table.getNumberArray("width", defaultValue));
			double xval = centerX[contourUsed];
			double yval = centerY[contourUsed];
			if (centerX.length > 0) {
				double xmult = Math.abs(xval - CAMERA_X_CENTER) / (CAMERA_X_CENTER / 8);
				double ymult = Math.abs(yval - CAMERA_Y_CENTER) / (CAMERA_Y_CENTER / 8);
				if (xval > CAMERA_X_CENTER + DEADZONE) {
					// servoH.set(DEGREE * xmult);
					drive.tankDrive(-.025 * xmult, .025 * xmult, false);
					// System.out.println("too far left");
				} else if (xval < CAMERA_X_CENTER - DEADZONE) {
					drive.tankDrive(.025 * xmult, -.025 * xmult, false);
					// servoH.set(-DEGREE * xmult);
					// System.out.println("too far right");
				}
				if (yval > CAMERA_Y_CENTER + DEADZONE) {
					servoV.set(servoV.get() + DEGREE * ymult / 2);
					// System.out.println("too far up");
				} else if (yval < CAMERA_Y_CENTER - DEADZONE) {
					servoV.set(servoV.get() - DEGREE * ymult / 2);
					// System.out.println("too far down");
				}
			}
		}
	}

	public void stickAim(Joystick cameraStick) {
		double hSet, vSet;
		hSet = cameraStick.getX() * 100 + 80;
		servoH.setAngle(hSet);
		vSet = cameraStick.getY() * -100 + 120; // Pilot Style
		// System.out.println("X " + servoH.getAngle());
		// System.out.println("Y " + servoV.getAngle());
		if (vSet > 156) {
			vSet = 156;
		}
		if (vSet < 80) {
			vSet = 80;
		}
		servoV.setAngle(vSet);
	}

	public void servoDebugOutput() {
		System.out.println("servoH get: " + servoH.get());
		System.out.println("servoV get: " + servoV.get());
		System.out.println("servoH getAngle: " + servoH.getAngle());
		System.out.println("servoV getAngle: " + servoV.getAngle() + "\n\n");
	}

	public void gripDebugOutput() {
		double[] defaultValue = { 0 };
		double[] widths = table.getNumberArray("width", defaultValue);
		double[] heights = table.getNumberArray("heights", defaultValue);
		double[] centerX = table.getNumberArray("centerX", defaultValue);
		double[] centerY = table.getNumberArray("centerY", defaultValue);
		double[] areas = table.getNumberArray("area", defaultValue);

		System.out.println("It's width is " + arrOutput(widths) + ".");
		System.out.println(" It's height is " + arrOutput(heights) + ".");
		System.out.println(" It's centerX is " + arrOutput(centerX) + ".");
		System.out.println(" It's centerY is " + arrOutput(centerY) + ".\n");
		System.out.println(" It's area is " + arrOutput(areas) + ".\n\n");

		System.out.println("X " + servoH.getAngle());
		System.out.println("Y " + servoV.getAngle());
	}
}
