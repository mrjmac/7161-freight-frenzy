package NightfallOpMode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public abstract class NightfallOpMode extends OpMode {

    DcMotor BL; //back left - [port number]
    DcMotor ML; //middle left - [port number]
    DcMotor FL; //front left - [port number]
    DcMotor BR; //back right - [port number]
    DcMotor MR; //middle right - [port number]
    DcMotor FR; //front right - [port number]
 /*   DcMotor intake; //intake - [port number]
    DcMotor lift; //lift - [port number]

    Servo pivot1; //pivot servo - [port number]
    Servo pivot2; //pivot servo - [port number]

    Servo hatch; //output servo - [port number]
    Servo cap; //cap servo - [port number]
    Servo release; //release servo - [port number]

    CRServo duckL; //left duck - [port number]
    CRServo duckR; //right duck - [port number]
*/

    public void init() {

        FR = hardwareMap.dcMotor.get("FR");
        FL = hardwareMap.dcMotor.get("FL");
        BR = hardwareMap.dcMotor.get("BR");
        BL = hardwareMap.dcMotor.get("BL");
        ML = hardwareMap.dcMotor.get("ML");
        MR = hardwareMap.dcMotor.get("MR");
/*
        intake = hardwareMap.dcMotor.get("intake");
        lift = hardwareMap.dcMotor.get("lift");

        pivot1 = hardwareMap.servo.get("servo1");
        pivot2 = hardwareMap.servo.get("servo2");
        hatch = hardwareMap.servo.get("hatch");

        cap = hardwareMap.servo.get("cap");
        release = hardwareMap.servo.get("release");

        duckL = hardwareMap.crservo.get("duckL");
        duckR = hardwareMap.crservo.get("duckR");
*/
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        MR.setDirection(DcMotorSimple.Direction.REVERSE);
        BR.setDirection(DcMotorSimple.Direction.FORWARD);
        ML.setDirection(DcMotorSimple.Direction.REVERSE);

        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ML.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //  intake.setDirection(DcMotorSimple.Direction.REVERSE);
   //     lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ML.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

     //   lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        ML.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        MR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

       // lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("init ", "completed");
        telemetry.update();
    }

    //============================= Drivetrain =====================================================

    public void startMotors(double l, double r){
        FR.setPower(r);
        MR.setPower(r);
        BR.setPower(r);
        FL.setPower(l);
        ML.setPower(l);
        BL.setPower(l);
    }

    public void stopMotors(){
        FR.setPower(0);
        MR.setPower(0);
        BR.setPower(0);
        FL.setPower(0);
        ML.setPower(0);
        BL.setPower(0);
    }



    public float deadstick (float value){

        if (value > -0.1 && value < 0.1)
            return 0 ;
        else
            return value;
    }

    //============================= Intake =========================================================




    //============================= Lift ===========================================================
    /*
    public void setLift(double power){
        lift.setPower(power);
    }

    public int getLiftEncoder(){
        return (Math.abs(lift.getCurrentPosition()));
    }

    public void resetLiftEncoder(){
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    */

}
