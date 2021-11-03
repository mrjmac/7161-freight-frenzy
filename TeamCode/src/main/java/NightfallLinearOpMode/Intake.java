package NightfallLinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    public DcMotor intake; //intake motor - [port number]

    public Servo pivot1; //pivot servo - [port number]
    public Servo pivot2; //pivot servo - [port number]

    LinearOpMode opMode;

    public Intake(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        intake = this.opMode.hardwareMap.dcMotor.get("intake");
        pivot1 = this.opMode.hardwareMap.servo.get("in1"); //lift side
        pivot2 = this.opMode.hardwareMap.servo.get("in2"); //non lift side
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void intakeDown() {
        pivot1.setPosition(.45);
        pivot2.setPosition(.55);
    }

    public void intakeUp() {
        pivot1.setPosition(0);
        pivot2.setPosition(1);
    }

    public void intakePow(double pow) {
        intake.setPower(pow);
    }

    public void intakeStop() {
        intake.setPower(0);
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
