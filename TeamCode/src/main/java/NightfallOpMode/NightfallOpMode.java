    package NightfallOpMode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class NightfallOpMode extends OpMode {

    DcMotor BL; //back left - [3 e2]
    DcMotor ML; //middle left - [3 c]
    DcMotor FL; //front left - [2 c]
    DcMotor BR; //back right - [2 e2]
    DcMotor MR; //middle right - [1 e2]
    DcMotor FR; //front right - [0 e2]
    DcMotor intake; //intake - [0 c]
    DcMotor lift; //lift - [1 c]

    CRServo spinRight; //surgical tubing servo - [0 c]
    CRServo spinLeft; //surgical tubing servo - [3 e2]
    Servo gate; //gate servo - [2 e2]
//    Servo pivot1; //pivot servo - [3 c]
//    Servo pivot2; //pivot servo - [2 e2]

    Servo hatch; //output servo - [5 c]
 //   Servo cap; //cap servo - [4 c]

    CRServo duckL; //left duck - [0 e2]
    CRServo duckR; //right duck - [1 e2]

    ElapsedTime macro = new ElapsedTime();


    // lowest EXPANSION HUB = surgicalright
    // C HUB 0 = surgical left
    // one above lowest on expansion - gate

    public void init() {

        FR = hardwareMap.dcMotor.get("FR");
        FL = hardwareMap.dcMotor.get("FL");
        BR = hardwareMap.dcMotor.get("BR");
        BL = hardwareMap.dcMotor.get("BL");
        ML = hardwareMap.dcMotor.get("ML");
        MR = hardwareMap.dcMotor.get("MR");

        intake = hardwareMap.dcMotor.get("intake");
        lift = hardwareMap.dcMotor.get("lift");

    //    pivot1 = hardwareMap.servo.get("in1");
    //    pivot2 = hardwareMap.servo.get("in2");
        gate = hardwareMap.servo.get("gate");
        spinRight = hardwareMap.crservo.get("sR");
        spinLeft = hardwareMap.crservo.get("lR");

    //    cap = hardwareMap.servo.get("cap");
        hatch = hardwareMap.servo.get("hatch");

        duckL = hardwareMap.crservo.get("duckL");
        duckR = hardwareMap.crservo.get("duckR");

        FR.setDirection(DcMotorSimple.Direction.FORWARD);
        MR.setDirection(DcMotorSimple.Direction.FORWARD);
        BR.setDirection(DcMotorSimple.Direction.FORWARD);
        ML.setDirection(DcMotorSimple.Direction.FORWARD);
        BL.setDirection(DcMotorSimple.Direction.REVERSE);

        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ML.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        spinRight.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);

        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ML.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        ML.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        MR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setPower(0);

        telemetry.addData("init ", "completed");
        telemetry.update();
      //  capUp();
        hatchUp();
        gateUp();

    }

    //============================= Drivetrain =====================================================

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


    public double deadstick(double value) {

        if (value > -0.1 && value < 0.1)
            return 0;
        else
            return value;
    }

    public void resetDT() {
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ML.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ML.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //============================= Intake =========================================================
/*
    public void pivotCross() {
        pivot1.setPosition(.28);
        pivot2.setPosition(.72);
    }

    public void pivotDown() {
        pivot1.setPosition(.425);
        pivot2.setPosition(.575);
    }

    public void pivotUp() {
        pivot1.setPosition(0);
        pivot2.setPosition(1); //1
    }
 */
    public void gateDown() {
        gate.setPosition(1);
    }
    public void gateUp() {
        gate.setPosition(.9);
    }

    public void runIntake(double power) {
        spinLeft.setPower(-power);
        spinRight.setPower(-power);
    }

    public int getIntakeEncoder() {
        return (Math.abs(intake.getCurrentPosition()));
    }

    /*
    public void setIntake() {
        //double kP = 1 / 10.0;
        double error = (300 - getIntakeEncoder());
        if (getIntakeEncoder() <= 300)
            intake.setPower(-.5);
        if (error < 50)
            intake.setPower(0.06);
            //  double ChangeP = error * kP;
            //  double power = ChangeP;
            //  power /= power;
        /*   if (error < 50 || Math.abs(ChangeP) < .02) {
                lift.setPower(0.06);
            }


      //  macro.reset();
    }
    */



    /*
    public void intakeReset(double kP) {
        if (getIntakeEncoder() > 20)
            //    double power = getLiftEncoder() * kP;
            //    power /= power;
            intake.setPower(.5);
            runIntake(-1);
    }

     */

    public void resetIntakeEncoder() {
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //============================= Lift ===========================================================

    public void setLift(double power) {
        lift.setPower(power);
    }

    public int getLiftEncoder() {
        return (Math.abs(lift.getCurrentPosition()));
    }

    public void setLiftReal(double macro2) {
        double ticks = macro2;//(macroHeight - 1) * heightModifier;
        double kP = 1 / 10.0;
        if (getLiftEncoder() <= ticks - 50) {
            //     double error = (ticks - getLiftEncoder());
            //  double ChangeP = error * kP;
            //  double power = ChangeP;
            //  power /= power;
            lift.setPower(1);
        /*   if (error < 50 || Math.abs(ChangeP) < .02) {
                lift.setPower(0.06);
            }
         */
        }
        macro.reset();
    }




    public void liftReset(double kP) {
        if (getLiftEncoder() > 65)
        //    double power = getLiftEncoder() * kP;
        //    power /= power;
            lift.setPower(-.8);
    }

    public void resetLiftEncoder(){
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void zero() {
        while (getLiftEncoder() > 800) {
            lift.setPower(-.5);
        }
        resetLiftEncoder();
    }
/*
    public void macro(double macroHeight) {
        if (macroHeight == 1) {
            //lift.setTargetPosition(2);
        }
        else if (macroHeight == 2) {
            //lift.setTargetPosition(3);
        }
        else {
            //lift.setTargetPosition(4);x
        }
    }
 */

    public void hatchUp() {
        hatch.setPosition(.87);
    }

    public void hatchDown() {
        hatch.setPosition(.55);
    }
/*
    public void capUp() {
        cap.setPosition(.7);
    }

    public void capDown(){
        cap.setPosition(.3);
    }

 */


}
