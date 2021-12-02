package NightfallLinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    DcMotor intake; //intake - [0 c]

    CRServo spinRight; //surgical tubing servo - [0 c]
    CRServo spinLeft; //surgical tubing servo - [3 e2]
    Servo gate; //gate servo - [2 e2]

    LinearOpMode opMode;

    public Intake(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        intake = this.opMode.hardwareMap.dcMotor.get("intake");

        //    pivot1 = hardwareMap.servo.get("in1");
        //    pivot2 = hardwareMap.servo.get("in2");
        gate = this.opMode.hardwareMap.servo.get("gate");
        spinRight = this.opMode.hardwareMap.crservo.get("sR");
        spinLeft = this.opMode.hardwareMap.crservo.get("lR");
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        spinRight.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        gateUp();
    }

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

    public void setIntake() {
        //double kP = 1 / 10.0;
        double error = (300 - getIntakeEncoder());
        if (getIntakeEncoder() <= 300)
            intake.setPower(-.5);
                //  double ChangeP = error * kP;
        //  double power = ChangeP;
        //  power /= power;
        /*   if (error < 50 || Math.abs(ChangeP) < .02) {
                lift.setPower(0.06);
            }
         */

        //  macro.reset();
    }




    public void intakeReset(double kP) {
        if (getIntakeEncoder() > 20)
            //    double power = getLiftEncoder() * kP;
            //    power /= power;
            intake.setPower(.5);
        runIntake(-1);
    }

    public void resetIntakeEncoder() {
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /* //TODO: I thought I wasn't plugging into an encoder here but I can if we have extras
    public int getEncoder() {
        return Math.abs(intake.getCurrentPosition());
    }


    public void resetEncoder() {
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
     */
}
