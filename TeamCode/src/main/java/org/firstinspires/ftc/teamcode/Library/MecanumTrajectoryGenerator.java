package org.firstinspires.ftc.teamcode.Library;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardwareMaps.Robot;

public class MecanumTrajectoryGenerator {
    MecanumEnhanced mecanumEnhanced = new MecanumEnhanced();
    Robot robot = Robot.getInstance();
    // Units are inches and seconds
    private double maxVelocity;
    private double maxAcceleration;

    private double currentVelocity;
    private double currentAcceleration;

    private double x;
    private double y;

    private double trajectoryLength;
    private double totalTime;
//    private double trajectoryDirection;

    public MecanumTrajectoryGenerator(double x, double y, double maxAcceleration) {
        this.x = x;
        this.y = y;
        this.trajectoryLength = Math.abs(getTrajectoryLength());
        this.maxVelocity = mecanumEnhanced.getMaxVel(1, x, y);
        this.maxAcceleration = maxAcceleration;
        this.totalTime = calculateTotalTime();
    }

    public void calculatePositionalDerivatives(ElapsedTime currentTime) {
        if (velocityIfConstantAcceleration(currentTime) < velocityIfCruising()
                && velocityIfConstantAcceleration(currentTime)
                < velocityIfConstantDeceleration(currentTime)) {
            currentVelocity = velocityIfConstantAcceleration(currentTime);
            currentAcceleration = maxAcceleration;
        } else if (velocityIfConstantDeceleration(currentTime) < velocityIfCruising()
                && velocityIfConstantDeceleration(currentTime)
                < velocityIfConstantAcceleration(currentTime)) {
            currentVelocity = velocityIfConstantDeceleration(currentTime);
            currentAcceleration = -maxAcceleration;
        } else {
            currentVelocity = maxVelocity;
            currentAcceleration = 0;
        }
        robot.logger.writeLine(velocityIfConstantAcceleration(currentTime), velocityIfConstantDeceleration(currentTime), velocityIfCruising(), currentVelocity, currentAcceleration);
    }

    private double getTrajectoryLength() {
        return Math.sqrt(x*x + y*y);
    }

    private double velocityIfConstantAcceleration(ElapsedTime currentTime) {
        return maxAcceleration * currentTime.seconds();
    }

    private double velocityIfCruising(){
        return maxVelocity;
    }

    private double velocityIfConstantDeceleration(ElapsedTime currentTime) {
        double finalVelocity  = Math.sqrt(2 * maxAcceleration * trajectoryLength);
        return  finalVelocity - maxAcceleration * currentTime.seconds();
    }

    private double calculateTotalTime() {
        return Math.sqrt(2 * maxAcceleration * trajectoryLength)/maxAcceleration;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getCurrentVelocity() {
        return currentVelocity;
    }

    public double getCurrentAcceleration() {
        return currentAcceleration;
    }
}