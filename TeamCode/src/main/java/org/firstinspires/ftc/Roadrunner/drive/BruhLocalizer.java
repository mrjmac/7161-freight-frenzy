package org.firstinspires.ftc.Roadrunner.drive;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.drive.TankDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.kinematics.Kinematics;
import com.acmerobotics.roadrunner.kinematics.MecanumKinematics;
import com.acmerobotics.roadrunner.kinematics.TankKinematics;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileBuilder;
import com.acmerobotics.roadrunner.profile.MotionSegment;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.firstinspires.ftc.Roadrunner.util.Encoder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.Roadrunner.drive.DriveConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//copyright 2021 bigmac

public class BruhLocalizer implements Localizer {

    public static double TICKS_PER_REV = DriveConstants.TICKS_PER_REV;
    public static double WHEEL_RADIUS = DriveConstants.WHEEL_RADIUS; // in
    public static double GEAR_RATIO = DriveConstants.GEAR_RATIO; // output (wheel) speed / input (encoder) spee
    public static double TRACKWIDTH = DriveConstants.TRACK_WIDTH;

    SampleTankDrive drive;
    private Encoder leftEncoder, rightEncoder;

    public BruhLocalizer(HardwareMap hardwareMap, SampleTankDrive drive) {
        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "FR"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "FL"));
        this.drive = drive;
    }


    double theta = 0;
    double angle = 0;
    //ElapsedTime jeffu = new ElapsedTime();
    Pose2d previousPose = new Pose2d(0, 0, 0);
    Pose2d poseEstimate = new Pose2d(0, 0, 0);
    Pose2d poseVelocity;

    double prevLeft = 0;
    double prevRight = 0;
    double leftChange = 0;
    double rightChange = 0;

    double globalX = 0;
    double globalY = 0;

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    public double getLeftEncoder() {
        double currentInches = -1 * encoderTicksToInches(leftEncoder.getCurrentPosition());
        leftChange = currentInches - prevLeft;
        prevLeft = currentInches;
        //jeffu.reset();
        return currentInches;
    }

    public double getRightEncoder() {
        double currentInches = encoderTicksToInches(rightEncoder.getCurrentPosition());
        rightChange = currentInches - prevRight;
        prevRight = currentInches;
        return currentInches;
    }
    public double angleWrapDeg(double angle) {
        double correctAngle = angle;      //convert to 0-360
        while (correctAngle > 180) {
            correctAngle -=360;
        }
        while (correctAngle < -180) {
            correctAngle += 360;
        }
        return correctAngle;
    }

    boolean epsilonEquals(double value1, double value2) {
        return (Math.abs(value1 - value2) < 1.0e-6);
    }
    @NotNull
    @Override
    public Pose2d getPoseEstimate() {
        return poseEstimate;
    }

    @Override
    public void setPoseEstimate(@NotNull Pose2d pose2d) {
        previousPose = pose2d;
        poseEstimate = pose2d;
    }

    @Nullable
    @Override
    public Pose2d getPoseVelocity() {
        return poseVelocity;
    }

    @Override
    public void update() {
        getLeftEncoder();
        getRightEncoder();
        double initialHeadingRad = angle;
        //double initialHeadingRad = Math.toRadians(initialHeading);

        double angleChangeRad = (leftChange - rightChange) / (TRACKWIDTH / 2);
        angle = (angle + angleChangeRad);
        //double angleChangeDeg = Math.toDegrees(angleChangeRad);
        //theta = angleWrapDeg(theta + angleChangeDeg);
        angle = Math.toRadians(angleWrapDeg(Math.toDegrees(angle)));
        double movement = (leftChange + rightChange) / 2.0; // total change in movement by robot (dx)
        double dTheta = angleChangeRad;


        double sinTheta = Math.sin(dTheta);
        double cosTheta = Math.cos(dTheta);

        double sineTerm;
        double cosTerm;

        if (epsilonEquals(dTheta, 0)) {
            sineTerm = 1.0 - 1.0 / 6.0 * dTheta * dTheta;
            cosTerm = dTheta / 2.0;
        }
        else{ //we have angle change{
            sineTerm = sinTheta / dTheta;
            cosTerm = (1 - cosTheta) / dTheta;
        }



        Vector2d deltaVector = new Vector2d(sineTerm * movement, cosTerm * movement); //translation
        deltaVector = deltaVector.rotated(initialHeadingRad);

        globalX += deltaVector.getX();
        globalY += deltaVector.getY();
        //TODO: STUFF TO TRY IF THIS DOESN'T WORK (I WILL GENUINELY BE VERY SAD AND UPSET CUZ IT'S 1 AM AND I HAVEN'T STARTED WHAP AND I'M ALSO NO LONGER A DAY AHEAD ON HOMEWORK + I HAVE UIL STUDY)
        // 2. get encoder vel instead of motor vel
        // 4. CHECK DIVISON STUFF IN WHEEL VELOCITY FUNCTION, IF WHEEL VELOCITY FUNCTION IS FINE WE KNOW THE ISSUE IS IN TankKinematics.wheelToRobotVelocities in which case I will cry

        poseEstimate = new Pose2d(globalX, globalY, angle);
        //poseEstimate = new Pose2d(0, 0, angle);
        List<Double> wheelVelcoities = drive.getWheelVelocities();
        double extHeadingVel = drive.getExternalHeadingVelocity();
        if (wheelVelcoities != null) {
            poseVelocity = TankKinematics.wheelToRobotVelocities(
                    wheelVelcoities,
                    DriveConstants.TRACK_WIDTH
            );
        }
    }


    public List<Double> getWheelVelocities() {
        return drive.getWheelVelocities();
    }

    public String toString() {
        String bruh = "left encoder: " + getLeftEncoder() + "\n" + "right encoder: " + getRightEncoder() + "\n"
                + "prev left: " + prevLeft + "\n" + "prev right: " + prevRight + "\n"
                + "angle change rad: " + (((getLeftEncoder() - getRightEncoder()) / TRACKWIDTH)) + "\n" +
                "heading" + Math.toDegrees(angle);
        return bruh;
    }
}
