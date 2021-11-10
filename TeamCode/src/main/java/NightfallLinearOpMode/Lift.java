package NightfallLinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {
    public DcMotor lift; //lift motor - [port number]

    public Servo hatch; //output servo - [port number]
    public Servo cap; //cap servo - [port number]


    LinearOpMode opMode;

    public Lift(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        lift = this.opMode.hardwareMap.dcMotor.get("lift");

        hatch = this.opMode.hardwareMap.servo.get("hatch");
        cap = this.opMode.hardwareMap.servo.get("cap");

        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        capUp();

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

    public void capUp() {cap.setPosition(.7); }

    public void capDown() {cap.setPosition(.3); }

    public void hatchUp() {
        hatch.setPosition(.82);
    }

    public void hatchDown() {
        hatch.setPosition(.55);
    }

    public void setLift(int height, double p) {
        /*lift.setTargetPosition(ticks);
        hatchDown();
        this.opMode.sleep(2000);
        hatchUp();
        nonJankLiftReset();

*/
        int heightModifier = 550;
        double ticks = (height - 1) * heightModifier;
        while (this.opMode.opModeIsActive() && !this.opMode.isStopRequested()) {

            double kP = p / 10;
            while (getEncoder() <= ticks && this.opMode.opModeIsActive()) {
                double error = (ticks - getEncoder());
                double ChangeP = error * kP;
                double power = ChangeP;
                power /= power;
                lift.setPower(power);
                this.opMode.telemetry.addData("error:", error);
                this.opMode.telemetry.addData("changeP", ChangeP);
                this.opMode.telemetry.update();
                if (error < 50 || Math.abs(ChangeP) < .02) {
                    lift.setPower(0.06);
                    break;
                }
            }
            hatchDown();
            lift.setPower(.06);
            break;
        }
        this.opMode.sleep(1000);
        hatchUp();
        liftReset(.5);
    }
        /*
        while (getEncoder() < (height - 1) * heightModifier) {
            lift.setPower(1);
        }
        lift.setPower(0);
        hatchDown();
        this.opMode.sleep(2000);
        hatchUp();
        liftReset();

         */

    public void nonJankLiftReset() {
        lift.setTargetPosition(0);
    }

    public void liftReset(double kP) {
        while (getEncoder() > 50) {
            double power = getEncoder() * kP;
            power /= power;
            lift.setPower(-power);
            if (Math.abs(power) < .05)
                break;
        }
        lift.setPower(0);
    }
}

