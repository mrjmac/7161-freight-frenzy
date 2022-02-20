package NightfallLinearOpMode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
@Config
public class Lift {
    public DcMotor lift; //lift motor - [0 c]

    public Servo hatch; //output servo - [port number]
    public Servo cap; //cap servo - [port number]
    public static double pMulti = 103;
    ElapsedTime liftTime = new ElapsedTime();

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
        hatch.setPosition(1);
    }

    public void hatchDown() {
        hatch.setPosition(.7);
    }
    public void topHatchDown() {hatch.setPosition(.63);}

    public void setLift(int height, double p) {
        liftTime.reset();
        /*lift.setTargetPosition(ticks);
        hatchDown();
        this.opMode.sleep(2000);
        hatchUp();
        nonJankLiftReset();
         */
        double ticks;
        if (height == 1) {
            ticks = 200;
        } else if (height == 2) {
            ticks = 700;
        } else if (height == 3) {
            ticks = 1450;
        } else {
            ticks = 1650;
        }

        //int heightModifier = 700;
     //   double ticks = (height - 1) * heightModifier;
        while (this.opMode.opModeIsActive() && !this.opMode.isStopRequested()) {

            double kP = p / pMulti;
            while (getEncoder() <= ticks && this.opMode.opModeIsActive()) {
                double error = (ticks - getEncoder());
                double ChangeP = error * kP;
                lift.setPower(ChangeP);
                this.opMode.telemetry.addData("error:", error);
                this.opMode.telemetry.addData("changeP", ChangeP);
                this.opMode.telemetry.update();
                if (error < 50 || Math.abs(ChangeP) < .02) {
                    lift.setPower(0.08);
                    break;
                }
                if (height == 3)
                    if (liftTime.milliseconds() > 750)
                        hatchDown();
            }
            hatchDown();
            lift.setPower(.06);
            break;
        }
        this.opMode.sleep(700);
        hatchUp();
        liftReset(1/110.0);
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
    } //jank

    public void liftReset(double kP) {
        while (getEncoder() > 50) {
            double power = getEncoder() * kP;
            lift.setPower(-power);
            if (Math.abs(power) < .05)
                break;
        }
        lift.setPower(0);
    }
}

