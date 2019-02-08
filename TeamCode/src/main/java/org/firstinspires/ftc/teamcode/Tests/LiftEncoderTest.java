package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.HardwareMaps.Archived.Robotv3;
import org.firstinspires.ftc.teamcode.HardwareMaps.Robot;

@TeleOp(name="Liftv3 Encoder Test", group="Tests")
public class LiftEncoderTest extends LinearOpMode {
    private Robotv3 robot = Robotv3.getInstance();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Instructions", "This code will display the encoder reading of the lift motor, which can be moved by hand to certain positions");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Liftv3 Encoder Pos", robot.lift.getEncoderCounts());
            telemetry.update();
        }
    }

}
