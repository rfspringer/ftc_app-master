package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Trajectory {
    // All in milliseconds
    private double timeToChangeAcceleration;
    private double timeOfAcceleration;
    private double timeOfConstantVelocity;

    private double maxVelocity;
    private double maxAcceleration;
    private double maxJerk;

    public Trajectory() {

    }

    public void run(ElapsedTime currentTime) {
        if (currentTime.milliseconds() < timeOfAcceleration) {
            accelerate();
        } else if (timeOfAcceleration <= currentTime.milliseconds() && currentTime.milliseconds() < timeOfAcceleration + timeOfConstantVelocity) {
            cruise();
        } else if (timeOfAcceleration + timeOfConstantVelocity < currentTime.milliseconds()) {
            decelerate();
        }
    }

    public void accelerate() {

    }

    public void cruise() {

    }

    public void decelerate() {

    }
}
