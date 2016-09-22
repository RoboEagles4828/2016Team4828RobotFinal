package org.usfirst.frc.team4828.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Time;
import java.time.Duration;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CameraMotors {
	Shooter shooter;
	AimThread aim;
	public static volatile double centerX = 1;
	public static volatile double centerY = 1;

	public class AimThread extends Thread {
		public static final String HOST = "safevision.local";
		public static final int PORT = 5800;

		@Override
		public void run() {
			while (true) {
				try {
					Socket soc = new Socket(HOST, PORT);
					BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
					while (true) {
						String visionData = in.readLine();
		//					System.out.println(visionData);
						centerX = Integer.parseInt(visionData.substring(0, visionData.indexOf(",")));
						centerY = Integer.parseInt(visionData.substring(visionData.indexOf(",") + 1));
						//System.out.println(centerX+" "+centerY);
					}
				} catch (Exception e) {
					System.out.println("Exception in AimThread run");	
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							System.out.println("Sleep interrupted");
						}
					//e.printStackTrace();
				}
			}
		}
	}

	public CameraMotors(Shooter s) {
		shooter = s;
		aim = new AimThread();
		aim.start();
	}

	public void printDebugCenters(){
			System.out.println(centerX+" "+centerY);
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

	private final static double DEADZONE = 0.0; // degree of error
	// private final static int CAMERA_X_CENTER = 160; // center of camera x
	// coord
	// private final static int CAMERA_Y_CENTER = 120; // center of camera y
	// coord
	private final static double CAMERA_X_CENTER = 40.0; // center of camera x coord
													// =200
	private final static double CAMERA_Y_CENTER = 46.0; // center of camera y coord
													// =180

	private boolean isCenteredY = false;
	private boolean isCenteredX = false;
	private double runTime = 0.0;
	
	public void aimCamera() {
		System.out.println("--- Start AutoAim ---");
		isCenteredX = false;
		isCenteredY = false;
		runTime = Timer.getFPGATimestamp() + 10;
		while(!isCenteredX && !isCenteredY) {
			try {
				if (!isCenteredX) {
					SmartDashboard.putNumber("GRIP X: ", centerX);
					SmartDashboard.putNumber("GRIP Y: ", centerY);
					if (centerX < CAMERA_X_CENTER + DEADZONE / 2) {
						System.out.println("rotating left");
						shooter.rotateLeft(.1);
						SmartDashboard.putBoolean("Centered X: ", false);
					} else if (centerX > CAMERA_X_CENTER - DEADZONE / 2) {
						System.out.println("rotating right");
						shooter.rotateRight(.1);
						SmartDashboard.putBoolean("Centered X: ", false);
					} else {
						shooter.rotateStop();
						isCenteredX = true;
						SmartDashboard.putBoolean("Centered X: ", true);
					}
					if (!isCenteredY) {
						if (centerY > CAMERA_Y_CENTER + DEADZONE) {
							System.out.println("flipping up");
							shooter.changePosition(500);
							SmartDashboard.putBoolean("Centered Y: ", false);
							isCenteredY = false;
						} else if (centerY < CAMERA_Y_CENTER - DEADZONE) {
							System.out.println("flip down");
							shooter.changePosition(-750);
							SmartDashboard.putBoolean("Centered Y: ", false);
							isCenteredY = false;
						} else {
							shooter.flipStop();
							shooter.lockPosition();
							isCenteredY = true;
							SmartDashboard.putBoolean("Centered Y: ", true);
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Exception in aimCamera");
				//e.printStackTrace();
			}
			if (Timer.getFPGATimestamp() == runTime) {
				isCenteredY = true;
				isCenteredX = true;
			}
		}
		System.out.println("--- End AutoAim ---");
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

//	public void gripDebugOutput() {
//		double[] defaultValue = { 0 };
//		double[] widths = table.getNumberArray("width", defaultValue);
//		double[] heights = table.getNumberArray("heights", defaultValue);
//		double[] centerX = table.getNumberArray("centerXnew", defaultValue);
//		double[] centerY = table.getNumberArray("centerYnew", defaultValue);
//		double[] areas = table.getNumberArray("area", defaultValue);
//
//		System.out.println("It's width is " + arrOutput(widths) + ".");
//		System.out.println(" It's height is " + arrOutput(heights) + ".");
//		System.out.println(" It's centerX is " + arrOutput(centerX) + ".");
//		System.out.println(" It's centerY is " + arrOutput(centerY) + ".\n");
//		System.out.println(" It's area is " + arrOutput(areas) + ".\n\n");
//	}
}
