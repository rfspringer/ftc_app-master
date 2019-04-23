package org.firstinspires.ftc.teamcode.Library;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
    private DcMotor[] motors;
    private TrajectoryGenerator trajectory;
    private RubiesLinearOpMode opMode;

    public MecanumTrajectoryFollower(RubiesLinearOpMode opMode, DcMotor[] motors, TrajectoryGenerator trajectory, double heading, double kA, boolean usesFeedback){
        this.opMode = opMode;
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
        timer.reset();
        while (!trajectoryIsComplete()) {
            opMode.telemetry.addData("velocity", trajectory.getCurrentVelocity());
            opMode.telemetry.update();
            robot.drive.setPowers(getMagnitude(timer), x, y, targetHeading);
        }
        robot.drive.stop();
    }

    public void runBackwards() {
        timer.reset();
        while (!trajectoryIsComplete()) {
            opMode.telemetry.addData("velocity", trajectory.getCurrentVelocity());
            opMode.telemetry.update();
            robot.drive.setPowers(getMagnitude(timer), -x, -y, targetHeading);
        }
        robot.drive.stop();
    }

    private double getMagnitude(ElapsedTime currentTime){
        trajectory.calculatePositionalDerivatives(currentTime);
        maxVelocity = mecanumEnhanced.getMaxVel(1, x, y);
        double kV = 0.9 / maxVelocity;
        return kV * trajectory.getCurrentVelocity() + kA * trajectory.getCurrentAcceleration();
    }

    public boolean trajectoryIsComplete() {
        return trajectory.getCurrentVelocity() < 0;
    }
}
