package org.firstinspires.ftc.Roadrunner.drive;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.drive.TankDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.kinematics.Kinematics;
import com.acmerobotics.roadrunner.kinematics.MecanumKinematics;
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

public class TankLocalizer implements Localizer {

    public static double TICKS_PER_REV = DriveConstants.TICKS_PER_REV;
    public static double WHEEL_RADIUS = DriveConstants.WHEEL_RADIUS; // in
    public static double GEAR_RATIO = DriveConstants.GEAR_RATIO; // output (wheel) speed / input (encoder) speed

    public static double TRACKWIDTH = DriveConstants.TRACK_WIDTH;

    TankDrive drive;
    private Encoder leftEncoder, rightEncoder;

    public TankLocalizer(HardwareMap hardwareMap) {
        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "FR"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "FL"));
    }

    double x = 0;
    double y = 0;
    double theta = 0;
    Pose2d previousPose = new Pose2d(0, 0, 0);
    Pose2d poseEstimate = new Pose2d(0, 0, 0);
    Pose2d poseVelocity;

    double prevLeft = 0;
    double prevRight = 0;
    double leftChange = 0;
    double rightChange = 0;

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

    }
}
