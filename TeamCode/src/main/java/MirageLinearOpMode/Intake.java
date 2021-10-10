package MirageLinearOpMode;

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
        pivot1 = this.opMode.hardwareMap.servo.get("servo1");
        pivot2 = this.opMode.hardwareMap.servo.get("servo2");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public int getEncoder() {
        return Math.abs(intake.getCurrentPosition());
    }

    public void resetEncoder() {
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
