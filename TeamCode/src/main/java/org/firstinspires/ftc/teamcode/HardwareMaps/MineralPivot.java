/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.HardwareMaps;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Library.AccelerationController;
import org.firstinspires.ftc.teamcode.Library.Archived.TrajectoryGenerator;

/**
 * This class stores all objects on our robot's drivetrain
 * It also includes functionality specific to our drive base
 */
public class MineralPivot {
    private static final MineralPivot instance = new MineralPivot();

    private AccelerationController pivotAccelerationControl = new AccelerationController(2.5);change max
    private DcMotor motor1 = null;
    private DcMotor motor2 = null;
    private HardwareMap hwMap = null;

    private double DUMPING_ANGLE;change
    private double MAX_ANGULAR_VELOCITY;change    //radians per second
    private double MAX_ANGULAR_ACCELERATION;change    //radians per second^2

    private double ACCELERATION_FROM_GRAVITY = 9.8; //meters per second^2
    private double MAX_TORQUE;change

    /* Constructor */
    private MineralPivot(){
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;
        motor1 = hwMap.dcMotor.get("arm1");
        motor2 = hwMap.dcMotor.get("arm2");
        initializeMotor(motor1);
        initializeMotor(motor2);
    }

    private void initializeMotor(DcMotor motor) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        motor.setPower(0);
    }

    public void setPowers(double power) {
        DcMotor[] motors = {motor1, motor2};
        pivotAccelerationControl.run(power, motors);
    }

    public double getAccelerationControlledPower(double dTime, double previousExtensionPower, double targetExtensionPower) {
        return pivotAccelerationControl.accelerate(dTime, previousExtensionPower, targetExtensionPower);
    }

    /**
     * Creates a trajectory for pivoting the arm between two angles
     * @return a TrajectoryGenerator class with parameters based on the arm's constraints
     */
    public TrajectoryGenerator createTrajectory(double initialAngle, double targetAngle) {
         return new TrajectoryGenerator(getAngleDifference(initialAngle, targetAngle), MAX_ANGULAR_VELOCITY, MAX_ANGULAR_ACCELERATION);
    }

    private double getAngleDifference(double initialAngle, double targetAngle) {
        return targetAngle - initialAngle;
    }

    public double getForceFromGravity(double angle, double mass) {
        return mass * ACCELERATION_FROM_GRAVITY * Math.cos(angle);
    }

    public double getPower(double targetAngularVelocity, double torque, double inertia) {
        return (inertia * MAX_ANGULAR_ACCELERATION + torque) / MAX_TORQUE;
    }

    public double getCurrentAngle() {
        return 9890;change
    }

    public static MineralPivot getInstance(){
        return instance;
    }

}

