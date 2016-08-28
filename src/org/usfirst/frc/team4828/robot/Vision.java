package org.usfirst.frc.team4828.robot;

import java.io.IOException;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Vision {
	NetworkTable nt;
	
	public Vision(NetworkTable grip) {
		nt = grip;
	}
	
	public void startGrip() {
		try {
            new ProcessBuilder("/home/lvuser/grip").inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public void getValues() {
		for (double area : nt.getNumberArray("targets/area", new double[0])) {
            System.out.println("Got contour with area=" + area);
        }
	}
}
