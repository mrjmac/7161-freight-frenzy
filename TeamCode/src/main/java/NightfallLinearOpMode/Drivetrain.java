package NightfallLinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Drivetrain {

    public DcMotor BL; //back left - [3 e1]
    public DcMotor ML; //middle left - [3 e2]
    public DcMotor FL; //front left - [2 e2]
    public DcMotor BR; //back right - [2 e1]
    public DcMotor MR; //middle right - [1 e1]
    public DcMotor FR; //front right - [0 e1]

    public CRServo duckL; //left duck - [port number]
    public CRServo duckR; //right duck - [port number]

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

        duckL = this.opMode.hardwareMap.crservo.get("duckL");
        duckR = this.opMode.hardwareMap.crservo.get("duckR");

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

    //---------------------------------------DUCKS--------------------------------------------------

    public void duckR(double pow) {
        duckR.setPower(pow);
    }

    public void duckL(double pow) {
        duckL.setPower(pow);
    }

    public void duckStop() {
        duckR.setPower(0);
        duckL.setPower(0);
    }
}
