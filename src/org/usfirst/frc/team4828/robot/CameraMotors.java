package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CameraMotors {
	Shooter shooter;
	NetworkTable table;
	AimThread aim;

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

		@Override
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

	public CameraMotors(Shooter s) {
		shooter = s;
		table = NetworkTable.getTable("GRIP/myContoursReport");
		aim = new AimThread(0.02);
		aim.disable();
		aim.start();
	}

	public void enableAutoAim() {
		aim.enable();
		SmartDashboard.putBoolean("Camera Tracking: ", true);
	}

	public void disableAutoAim() {
		aim.disable();
		SmartDashboard.putBoolean("Camera Tracking: ", false);
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

	private final static int DEADZONE = 9; // degree of error
	// private final static int CAMERA_X_CENTER = 160; // center of camera x
	// coord
	// private final static int CAMERA_Y_CENTER = 120; // center of camera y
	// coord
	private final static int CAMERA_X_CENTER = 208; // center of camera x coord
													// =200
	private final static int CAMERA_Y_CENTER = 195; // center of camera y coord
													// =200

	private boolean isCenteredY = false;
	public void aimCamera() {
		double[] defaultValue = { -1 };
		double[] centerX = table.getNumberArray("centerX", defaultValue);
		double[] centerY = table.getNumberArray("centerY", defaultValue);
		try {
			if (centerX[0] > 0) {
				// System.out.println("contour found");
				int contourUsed = parseContourMap(table.getNumberArray("width", defaultValue));
				double xval = centerX[contourUsed];
				double yval = centerY[contourUsed];
				SmartDashboard.putNumber("GRIP X: ", xval);
				SmartDashboard.putNumber("GRIP Y: ", yval);
				SmartDashboard.putNumber("Contour Count: ", centerX.length);
				if (centerX.length > 0) {
					if (xval > CAMERA_X_CENTER + DEADZONE) {
						shooter.rotateRight();
					} else if (xval < CAMERA_X_CENTER - DEADZONE) {
						shooter.rotateLeft();
					} else {
						shooter.rotateStop();
						System.out.println("CENTERED X");
					}
					if (yval > CAMERA_Y_CENTER + DEADZONE) {
						shooter.flipUp(0.05);
						isCenteredY = false;
						System.out.println("flipping up");
					} else if (yval < CAMERA_Y_CENTER - DEADZONE) {
						shooter.flipDown(-0.18);
						isCenteredY = false;
						System.out.println("flipping down");
					} else {
						if(!isCenteredY){
							shooter.flipStop();
							shooter.lockPosition();
							isCenteredY = true;
						}
						System.out.println("CENTERED Y");
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("GRIP sees no contours.");
			shooter.flipStop();
			shooter.lockPosition();
			shooter.rotateStop();
		}
	}

	public int parseContourMap(double[] map) { // index of longest contour
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

	/*
	 * public void aimCameraMotors(RobotDrive drive) { double[] defaultValue = {
	 * 0 }; double[] centerX = table.getNumberArray("centerX", defaultValue);
	 * double[] centerY = table.getNumberArray("centerY", defaultValue); if
	 * (centerX.length > 0) { // System.out.println("contour found"); int
	 * contourUsed = parseContourMap(table.getNumberArray("width",
	 * defaultValue)); double xval = centerX[contourUsed]; double yval =
	 * centerY[contourUsed]; if (centerX.length > 0) { double xmult =
	 * Math.abs(xval - CAMERA_X_CENTER) / (CAMERA_X_CENTER / 8); double ymult =
	 * Math.abs(yval - CAMERA_Y_CENTER) / (CAMERA_Y_CENTER / 8); if (xval >
	 * CAMERA_X_CENTER + DEADZONE) { // motorH.set(DEGREE * xmult);
	 * drive.tankDrive(-.025 * xmult, .025 * xmult, false); //
	 * System.out.println("too far left"); } else if (xval < CAMERA_X_CENTER -
	 * DEADZONE) { drive.tankDrive(.025 * xmult, -.025 * xmult, false); //
	 * motorH.set(-DEGREE * xmult); // System.out.println("too far right"); } if
	 * (yval > CAMERA_Y_CENTER + DEADZONE) { motorV.set(motorV.get() + DEGREE *
	 * ymult / 2); // System.out.println("too far up"); } else if (yval <
	 * CAMERA_Y_CENTER - DEADZONE) { motorV.set(motorV.get() - DEGREE * ymult /
	 * 2); // System.out.println("too far down"); } } } }
	 */

	/*
	 * public void stickAim(Joystick cameraStick) { double hSet, vSet; hSet =
	 * cameraStick.getX() * 100 + 80; motorH.setAngle(hSet); vSet =
	 * cameraStick.getY() * -100 + 120; // Pilot Style // System.out.println(
	 * "X " + motorH.getAngle()); // System.out.println("Y " +
	 * motorV.getAngle()); if (vSet > 156) { vSet = 156; } if (vSet < 80) { vSet
	 * = 80; } motorV.setAngle(vSet); }
	 */

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
	}
}
