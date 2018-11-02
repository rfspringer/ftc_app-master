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

package org.firstinspires.ftc.teamcode.HWMaps;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This class stores all objects on our robot's drivetrain
 * It also includes functionality specific to our drive base
 */
public class Lift {
    private static final Lift instance = new Lift();
    /* Public OpMode members. */
    private DcMotor  lift   = null;

    private double MAX_VELOCITY;
    private double MAX_ACCELERATION;

    private double kV = 0.8/MAX_VELOCITY;
    private double kA;

    /* local OpMode members. */
    private HardwareMap hwMap = null;

    private int EXTENDED_ENCODER_COUNTS = -4725;

    /* Constructor */
    private Lift(){
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;
        lift = hwMap.get(DcMotor.class, "lift");
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setDirection(DcMotorSimple.Direction.FORWARD);
        lift.setPower(0);
    }

    public void setPower(double power) {
        lift.setPower(power);
    }

    public DcMotor getMotor() {
        return lift;
    }

    public static Lift getInstance(){
        return instance;
    }

    public int getCurrentPosition() {
        return lift.getCurrentPosition();
    }

    public void setTargetPosition(int targetPosition) {
        lift.setTargetPosition(targetPosition);
    }

    public void setMode(DcMotor.RunMode runMode) {
        lift.setMode(runMode);
    }

    public int getEncoderCounts() {
        return lift.getCurrentPosition();
    }

    public int extendedLiftPosition() {
        return EXTENDED_ENCODER_COUNTS;
    }

    public void lowerRobotToGround() {
        boolean actionIsComplete = false;
        ElapsedTime time = new ElapsedTime();
        setTargetPosition(EXTENDED_ENCODER_COUNTS);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0.5);
        while (!actionIsComplete) {
            actionIsComplete = robotIsCloseToGround(time);
        }
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        setPower(0);
    }

    private boolean robotIsCloseToGround(ElapsedTime time) {
        return (getCurrentPosition() <= (extendedLiftPosition() - 10)) || time.seconds() > 4;
    }
}

