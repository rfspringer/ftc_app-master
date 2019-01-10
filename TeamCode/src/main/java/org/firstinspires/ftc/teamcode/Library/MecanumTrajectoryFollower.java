package org.firstinspires.ftc.teamcode.Library;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardwareMaps.Robot;

public class MecanumTrajectoryFollower {
    private Robot robot = Robot.getInstance();
    private MecanumEnhanced mecanumEnhanced = new MecanumEnhanced();
    private ElapsedTime timer = new ElapsedTime();
    private double targetHeading;
    private double maxVelocity = 0;
    private double kA;
    private double x;
    private double y;
    private boolean usesFeedback = false;
    private boolean hasResetTimer = false;
    private DcMotor[] motors;
    private MecanumTrajectoryGenerator trajectory;

    public MecanumTrajectoryFollower(DcMotor[] motors, MecanumTrajectoryGenerator trajectory, double heading, double kA, boolean usesFeedback){
        this.usesFeedback = usesFeedback;
        this.motors = motors;
        this.trajectory = trajectory;
        this.x = trajectory.getX();
        this.y = trajectory.getY();
        this.kA = kA;
        this.targetHeading = heading;
        MotorEnhanced.setRunMode(motors, DcMotor.RunMode.RUN_USING_ENCODER);
        mecanumEnhanced.setInAutonomous(true);
    }

    public void run(){
        while (!trajectoryIsComplete()) {
            if (!hasResetTimer) {
                timer.reset();
                hasResetTimer = true;
            }

            robot.drive.setPowers(getMagnitude(timer), x, y, targetHeading);
            robot.logger.writeLine(robot.drive.getAllMotors()[0].getPower(), robot.drive.getAllMotors()[1].getPower());
        }
        MotorEnhanced.setPower(motors, 0);
    }

    private double getMagnitude(ElapsedTime currentTime){
        trajectory.calculatePositionalDerivatives(currentTime);
        double velocity = trajectory.getCurrentVelocity();
        double acceleration = trajectory.getCurrentAcceleration();
        maxVelocity = mecanumEnhanced.getMaxVel(1, x, y);
        double kV = 0.8 / maxVelocity;
        return kV * trajectory.getCurrentVelocity();
       // return kV * trajectory.getCurrentVelocity() + kA * trajectory.getCurrentAcceleration();
    }

    public boolean trajectoryIsComplete() {
        return trajectory.getCurrentVelocity() < 0;
    }
}
