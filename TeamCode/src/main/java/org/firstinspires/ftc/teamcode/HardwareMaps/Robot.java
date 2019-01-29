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

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Library.FTCLogger;
import org.firstinspires.ftc.teamcode.Library.MecanumTrajectoryFollower;
import org.firstinspires.ftc.teamcode.Library.TensorFlow;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for our robot
 * This class stores functions that use a combination of subsystems on our robot
 */
public class Robot
{
    private static final Robot instance = new Robot();

    public Drive drive = Drive.getInstance();
    public Lift lift = Lift.getInstance();
    public Mineral mineral = Mineral.getInstance();
    public Claim claim = Claim.getInstance();
    public Sensors sensors = Sensors.getInstance();

    public FTCLogger logger = new FTCLogger();

    private MecanumTrajectoryFollower leftMineralTrajectory;
    private MecanumTrajectoryFollower centerMineralTrajectory;
    private MecanumTrajectoryFollower rightMineralTrajectory;
    private MecanumTrajectoryFollower depotTrajectory;

    /* Constructor */
    private Robot(){
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap hwMap) {
        drive.init(hwMap);
        lift.init(hwMap);
        mineral.init(hwMap);
        sensors.init(hwMap);
        claim.init(hwMap);
        initializeSamplingTrajectories();
    }

    public void turnToHeadingCenterPivot(double targetHeading) {
        double error = sensors.getIntegratedError(targetHeading);
        while (Math.abs(error) > 2.5) {
            error = sensors.getIntegratedError(targetHeading);
            drive.turnToHeading(error);
        }
        drive.stop();
    }

    public void turnToHeadingLeftPivot(double targetHeading) {
        double error = sensors.getIntegratedError(targetHeading);
        while (Math.abs(error) > 2.5) {
            error = sensors.getIntegratedError(targetHeading);
            drive.turnToHeadingLeftWheels(error);
        }
        drive.stop();
    }


    public void turnToHeadingRightPivot(double targetHeading) {
        double error = sensors.getIntegratedError(targetHeading);
        while (Math.abs(error) > 2.5) {
            error = sensors.getIntegratedError(targetHeading);
            drive.turnToHeadingRightWheels(error);
        }
        drive.stop();
    }

    public void sample(TensorFlow.GoldPosition goldLocation) {
        if (goldLocation == TensorFlow.GoldPosition.LEFT) {
            turnToHeadingCenterPivot(sensors.getLeftMineralHeading());
            leftMineralTrajectory.run();
        } else if (goldLocation == TensorFlow.GoldPosition.RIGHT) {
            turnToHeadingCenterPivot(sensors.getRightMineralHeading());
            rightMineralTrajectory.run();
        } else {
            turnToHeadingCenterPivot(sensors.getCenterMineralHeading());
            centerMineralTrajectory.run();
        }
    }

    public void goToDepot(TensorFlow.GoldPosition goldLocation) {
        double heading;
        if (goldLocation == TensorFlow.GoldPosition.LEFT) {
            heading = sensors.getLeftDepotHeading();
            turnToHeadingLeftPivot(heading);
        } else if (goldLocation == TensorFlow.GoldPosition.RIGHT) {
            heading = sensors.getRightDepotHeading();
            turnToHeadingRightPivot(heading);
        } else {
            heading = sensors.getCenterDepotHeading();
            turnToHeadingCenterPivot(heading);
        }
        depotTrajectory = drive.initializeTrajectory(0, 19, heading);
        depotTrajectory.run();
    }

    public void unlatch() {
        turnToHeadingRightPivot(45);
    }

    private void initializeSamplingTrajectories() {
        leftMineralTrajectory = drive.initializeTrajectory(0, 54, sensors.getLeftMineralHeading());
        centerMineralTrajectory = drive.initializeTrajectory(0, 50, sensors.getCenterMineralHeading());
        rightMineralTrajectory = drive.initializeTrajectory(0, 57, sensors.getRightMineralHeading());
    }

    public static Robot getInstance() {
        return instance;
    }
 }

