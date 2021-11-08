package NightfallLinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {
    public DcMotor lift; //lift motor - [port number]

    public Servo hatch; //output servo - [port number]
    public Servo cap; //cap servo - [port number]
    private int heightModifier = 1400;


    LinearOpMode opMode;

    public Lift(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        lift = this.opMode.hardwareMap.dcMotor.get("lift");

        hatch = this.opMode.hardwareMap.servo.get("hatch");
        cap = this.opMode.hardwareMap.servo.get("cap");

        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

    public void capUp() {cap.setPosition(.5); }

    public void capDown() {cap.setPosition(.7); }

    public void hatchUp() {
        hatch.setPosition(.5);
    }

    public void hatchDown() {
        hatch.setPosition(.6);
    }

    public void setLift(int height) {
        while (getEncoder() < (height - 1) * heightModifier) {
            lift.setPower(1);
        }
        lift.setPower(0);
        hatchDown();
        this.opMode.sleep(2000);
        hatchUp();
        liftReset();
    }

    public void liftReset() {
        while (getEncoder() > 0) {
            lift.setPower(-1);
        }
        lift.setPower(0);
    }
}

