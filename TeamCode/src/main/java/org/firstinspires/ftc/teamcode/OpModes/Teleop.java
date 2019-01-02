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

package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HWMaps.Robot;
import org.firstinspires.ftc.teamcode.Lib.AccelerationController;
import org.firstinspires.ftc.teamcode.Lib.GamepadEnhanced;


@TeleOp(name="Teleop", group="Iterative Opmode")
public class Teleop extends OpMode
{
    private Robot robot = Robot.getInstance();
    private ElapsedTime runtime = new ElapsedTime();
    private GamepadEnhanced gamepadA = new GamepadEnhanced();
    private AccelerationController liftAccelerationController = new AccelerationController(3.0);

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        gamepadA.update(gamepad1);
        robot.drive.getAllMotors();

        telemetry.addData("Magnitude", gamepadA.getMagnitude(GamepadEnhanced.STICK.RIGHT_STICK));
        telemetry.addData("X", gamepadA.left_stick_x);
        telemetry.addData("Y", gamepadA.left_stick_y);
        telemetry.addData("right x", gamepadA.right_stick_x);
        telemetry.update();
    }

    private void controlDrive() {
        robot.drive.setPowers(gamepadA.getMagnitude(GamepadEnhanced.STICK.RIGHT_STICK),
                gamepadA.left_stick_x, -gamepadA.left_stick_y, getHeadingCorrection());
    }


    private double getHeadingCorrection() {
        if (Math.abs(gamepadA.right_stick_x) < 0.2) {
            return 0;
        } else {
            return -gamepadA.right_stick_x;
        }
    }

    private void controlArm() {
        if (gamepadA.left_bumper) {
            robot.mineral.intake.setScaledPower(0);
            robot.mineral.arm.setPower(1);
        } else if (gamepadA.right_bumper) {
            robot.mineral.intake.setScaledPower(0);
            robot.mineral.arm.setPower(-1);
        } else {
            robot.mineral.arm.setPower(0);
        }
    }

    private void controlIntake() {
        if (gamepadA.getAxisAsButton(GamepadEnhanced.AXIS.AXIS_LEFT_TRIGGER)) {
            robot.mineral.intake.setScaledPower(-1);
        } else if (gamepadA.getAxisAsButton(GamepadEnhanced.AXIS.AXIS_RIGHT_TRIGGER)) {
            robot.mineral.intake.setScaledPower(1);
        } else if (gamepadA.getAxisAsButton(GamepadEnhanced.AXIS.AXIS_LEFT_TRIGGER) && gamepadA.getAxisAsButton(GamepadEnhanced.AXIS.AXIS_RIGHT_TRIGGER)){
            robot.mineral.intake.setScaledPower(0);
        }
    }

    private void controlLift() {
        if (gamepadA.dpad_up){
            liftAccelerationController.run(1, robot.lift.getMotor());
        } else if (gamepadA.dpad_down) {
            liftAccelerationController.run(-1, robot.lift.getMotor());
        } else {
            liftAccelerationController.run(0, robot.lift.getMotor());
        }
    }



    @Override
    public void stop() {
    }
}
