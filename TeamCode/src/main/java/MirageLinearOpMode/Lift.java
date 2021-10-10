package MirageLinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {
    public DcMotor lift; //lift motor - [port number]

    public Servo hatch; //output servo - [port number]
    public Servo cap; //cap servo - [port number]
    public Servo release; //release servo - [port number]

    LinearOpMode opMode;

    public Lift(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        lift = this.opMode.hardwareMap.dcMotor.get("lift");

        hatch = this.opMode.hardwareMap.servo.get("hatch");
        cap = this.opMode.hardwareMap.servo.get("cap");
        release = this.opMode.hardwareMap.servo.get("release");

        lift.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void setPower(double power) {
        lift.setPower(power);
    }

    public int getEncoder() {
        return Math.abs(lift.getCurrentPosition());
    }

    public void resetEncoder() {
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}

