package org.firstinspires.ftc.Roadrunner.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.Roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.Roadrunner.drive.SampleTankDrive;

import java.util.Objects;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
@TeleOp(group = "drive")
public class LocalizationTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleTankDrive drive = new SampleTankDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (!isStopRequested()) {

            double left = 0;
            double right = 0;
            double max;

            double forward = ((gamepad1.left_stick_y * Math.abs(gamepad1.left_stick_y)));
            double side = (gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x));

            left = forward - side;
            right = forward + side;

            max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0) {
                left /= max;
                right /= max;
            }

            drive.startMotors(left , right );
            drive.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            //telemetry.addData("x", poseEstimate.getX());
            //telemetry.addData("y", poseEstimate.getY());
            //telemetry.addData("", drive.getData());
            //telemetry.addData("heading", poseEstimate.getHeading());
            //telemetry.addData("rawHeading:", drive.getRawExternalHeading());
            //telemetry.addData("motor stuff: ", drive.test());
            telemetry.addData("speed ", drive.getData2());
            telemetry.update();
        }
    }

}
