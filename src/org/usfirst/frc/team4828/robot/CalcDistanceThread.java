package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CalcDistanceThread extends Thread {
	private Ultrasonic ultra;
	private boolean isAlive = true;

	public CalcDistanceThread(int ultrasonicPort) {
		ultra = new Ultrasonic(ultrasonicPort, ultrasonicPort);
	}

	public static double getShooterAngle(double dist, double velocity) {
		double temp = dist * dist - 85 * 85;
		double squareroot = Math
				.sqrt(temp - 4 * (192 * temp / (velocity * velocity)) * (97 + 192 * temp / (velocity * velocity)));
		double phi = Math.atan((Math.sqrt(temp) - squareroot) / (384 * temp / (velocity * velocity)));
		return phi;
	}

	public static double getShooterError(double dist, double velocity, double phi, double errordist,
			double errorvelocity, double errorphi) {
		double temp = Math.sqrt(dist * dist - 85 * 85);
		double error1 = (dist * Math.tan(phi) / temp
				- 384 * dist / (velocity * velocity * Math.cos(phi) * Math.cos(phi))) * errordist;
		double error2 = (384 * temp * temp / (velocity * velocity * velocity * Math.cos(phi) * Math.cos(phi)))
				* errorvelocity;
		double error3 = (temp / (Math.cos(phi) * Math.cos(phi)) - 384 * Math.sin(phi) * temp * temp
				/ (Math.cos(phi) * Math.cos(phi) * Math.cos(phi) * velocity * velocity)) * errorphi;
		return error1 + error2 + error3;
	}

	public static double getShooterPercentError(double error, double errorweight) {
		double errorweight2 = (Math.exp(errorweight) - 1) / 10;
		double percent = 100 * Math.atan(6 * errorweight2 / error) / Math.atan(errorweight2);
		if (percent < 100)
			return percent;
		else
			return 100;
	}

	private double angle = 0;

	public double getAngle() {
		return angle;
	}
	
	public void kill(){
		isAlive = false;
	}

	@Override
	public void run() {
		while (isAlive) {
			double distance = ultra.getRangeInches();
			double velocity = 4000; // based on motor speed
			angle = getShooterAngle(distance, velocity);
			double chance = getShooterError(distance, velocity, angle, 1, 50, 0.02);
			SmartDashboard.putNumber("Probability: ", (int)(getShooterPercentError(chance, 2.5)/10));
			Timer.delay(0.25);
		}
	}
}
