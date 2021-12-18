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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

public class BruhLocalizer implements Localizer {

    public static double TICKS_PER_REV = DriveConstants.TICKS_PER_REV;
    public static double WHEEL_RADIUS = DriveConstants.WHEEL_RADIUS; // in
    public static double GEAR_RATIO = DriveConstants.GEAR_RATIO; // output (wheel) speed / input (encoder) speed

    public static double TRACKWIDTH = DriveConstants.TRACK_WIDTH;

    TankDrive drive;
    private Encoder leftEncoder, rightEncoder;

    public BruhLocalizer(HardwareMap hardwareMap, SampleTankDrive drive) {
        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "FR"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "FL"));
        this.drive = drive;
    }

    double theta = 0;
    Pose2d previousPose = new Pose2d(0, 0, 0);
    Pose2d poseEstimate = new Pose2d(0, 0, 0);
    Pose2d poseVelocity;

    double prevLeft = 0;
    double prevRight = 0;
    double leftChange = 0;
    double rightChange = 0;


    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    public double getLeftEncoder() {
        double currentInches = encoderTicksToInches(leftEncoder.getCurrentPosition());
        leftChange = currentInches - prevLeft;
        prevLeft = currentInches;
        return currentInches;

    }

    public double getRightEncoder() {
        double currentInches = encoderTicksToInches(rightEncoder.getCurrentPosition());
        rightChange = currentInches - prevRight;
        prevRight = currentInches;
        return currentInches;
    }
    public double angleWrapDeg(double angle) {
        double zeroTo360 = angle + 180;      //convert to 0-360
        double start = zeroTo360 % 360; //will work for positive angles
        //angle is (-360, 0), add 360 to make it from 0-360
        if (start < 0)
        {
            start += 360;
        }
        return start - 180; //bring it back to -180 to 180
    }

    boolean epsilonEquals(double value1, double value2)
    {
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
        double initialHeading = theta;
        double initialHeadingRad = Math.toRadians(initialHeading);

        double angleChangeRad = (leftChange - rightChange) / TRACKWIDTH;
        double angleChangeDeg = Math.toDegrees(angleChangeRad);
        theta = angleWrapDeg(theta + angleChangeDeg);

        double movement = (leftChange + rightChange) / 2.0; // total change in movement by robot (dx)
        double dTheta = angleChangeRad;

        double sinTheta = Math.sin(dTheta);
        double cosTheta = Math.cos(dTheta);

        double sineTerm;
        double cosTerm;

        if (epsilonEquals(dTheta, 0))
        {
            sineTerm = 1.0 - 1.0 / 6.0 * dTheta * dTheta;
            cosTerm = dTheta / 2.0;
        }
        else //we have angle change
        {
            sineTerm = sinTheta / dTheta;
            cosTerm = (1 - cosTheta) / dTheta;
        }

        Vector2d deltaVector = new Vector2d(sineTerm * movement, cosTerm * movement); //translation
        deltaVector = deltaVector.rotated(initialHeadingRad);
        //heading in rad
        poseEstimate = new Pose2d(50, 50, 50);//deltaVector.getX(), deltaVector.getY(), 0);
        poseVelocity = calculatePoseDeltaEncoders();
    }


    public Pose2d calculatePoseDeltaEncoders() {
        Pose2d poseVelocity;
        List<Double> wheelVelocities = new ArrayList<>();
        wheelVelocities = getWheelVelocities();
        if (wheelVelocities != null) {
            poseVelocity = TankKinematics.wheelToRobotVelocities(
                    wheelVelocities,
                    DriveConstants.TRACK_WIDTH
            );
            poseVelocity = new Pose2d(poseVelocity.vec(), drive.getExternalHeadingVelocity()!=null?drive.getExternalHeadingVelocity():0);

            return poseVelocity;
        }
        return new Pose2d(0,0,0); //should be return 0, 0, 0 imo
    }



    public List<Double> getWheelVelocities() {
        return drive.getWheelVelocities();
    }
}
