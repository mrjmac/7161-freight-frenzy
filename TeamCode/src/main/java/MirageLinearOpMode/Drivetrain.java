package MirageLinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.hardware.bosch.BNO055IMU;

public class Drivetrain {

    public DcMotor BL; //back left - [port number]
    public DcMotor ML; //middle left - [port number]
    public DcMotor FL; //front left - [port number]
    public DcMotor BR; //back right - [port number]
    public DcMotor MR; //middle right - [port number]
    public DcMotor FR; //front right - [port number]
    // 2 vex motors

    LinearOpMode opMode;
    private final String LOG_TAG = "DriveTrain";

    public Drivetrain(LinearOpMode opMode)throws InterruptedException {

        this.opMode = opMode;
        FR = this.opMode.hardwareMap.dcMotor.get("FR");
        FL = this.opMode.hardwareMap.dcMotor.get("FL");
        BR = this.opMode.hardwareMap.dcMotor.get("BR");
        BL = this.opMode.hardwareMap.dcMotor.get("BL");
        ML = this.opMode.hardwareMap.dcMotor.get("ML");
        MR = this.opMode.hardwareMap.dcMotor.get("MR");

        this.opMode.telemetry.addData(LOG_TAG + "init", "finished init");
        this.opMode.telemetry.update();

        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        MR.setDirection(DcMotorSimple.Direction.REVERSE);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);

        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ML.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }


    public void startMotors(double l, double r) {
        FR.setPower(r);
        MR.setPower(r);
        BR.setPower(r);
        FL.setPower(l);
        ML.setPower(l);
        BL.setPower(l);
    }

    public void stopMotors() {
        FR.setPower(0);
        MR.setPower(0);
        BR.setPower(0);
        FL.setPower(0);
        ML.setPower(0);
        BL.setPower(0);
    }

    public void resetEncoders() throws InterruptedException {
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ML.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ML.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
