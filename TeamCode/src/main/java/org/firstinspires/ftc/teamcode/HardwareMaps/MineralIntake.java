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

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Library.VexMotorEnhanced;

/**
 * This class stores all objects on our robot's drivetrain
 * It also includes functionality specific to our drive base
 */
public class MineralIntake {
    private static final MineralIntake instance = new MineralIntake();

    private CRServo intake = null;
    private Servo bucket = null;
    private HardwareMap hwMap = null;

    private double INTAKE_POWER = 1;
    private double OUTTAKE_POWER = -1;

    private double INTAKE_POSITION = 0.55;
    private double STORAGE_POSITION = 0.4;
    private double DUMP_POSITION = 0.7;

    /* Constructor */
    private MineralIntake(){
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;
        bucket = hwMap.servo.get("bucket");
        intake = hwMap.crservo.get("intake");
        bucket.setPosition(1);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setPower(0);
    }

    public void setToIntake() {
        setScaledPower(1);
        bucket.setPosition(INTAKE_POSITION);
    }

    public void storeMinerals() {
        bucket.setPosition(STORAGE_POSITION);
    }

    public void dumpMinerals() {
        bucket.setPosition(DUMP_POSITION);
    }

    public void setRawPower(double power) {
        intake.setPower(power);
    }

    public double getRawPower() {
        return intake.getPower();
    }

    public void setScaledPower(double power) {
        VexMotorEnhanced.setScaledPower(intake, power);
    }

    public double getScaledPower() {
        return VexMotorEnhanced.getScaledPower(intake);
    }

    public static MineralIntake getInstance(){
        return instance;
    }
}

