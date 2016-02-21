package org.usfirst.frc.team4828.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class PneumaticPiston {
	Solenoid solenoidOpen, solenoidClose;
	
	public PneumaticPiston(int s1, int s2){
		solenoidOpen = new Solenoid(s1);
		solenoidClose = new Solenoid(s2);
	}
	
	public void open(){
		solenoidOpen.set(false);
		solenoidClose.set(true);
	}
	
	public void close(){
		solenoidOpen.set(true);
		solenoidClose.set(false);
	}

}
