package NightfallOpMode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TestTeleOp", group = "opMode")
public class TestTeleOp extends OpMode {

    DcMotor rightDT;
    DcMotor leftDT;
    DcMotor arm;
    Servo rightGraby;
    Servo leftGraby;
    boolean grab = false;

    ElapsedTime grabTime = new ElapsedTime();

    public void init() {
        rightGraby = hardwareMap.servo.get("right");
        leftGraby = hardwareMap.servo.get("left");
        rightDT = hardwareMap.dcMotor.get("right");
        leftDT = hardwareMap.dcMotor.get("left");
        arm = hardwareMap.dcMotor.get("arm");
        rightDT.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Init:", "finished");
    }

    public void loop() {
        if (Math.abs(gamepad1.left_stick_y) > .1) {
            rightDT.setPower(gamepad1.left_stick_y);
            leftDT.setPower(gamepad1.left_stick_y);
        } else if (Math.abs(gamepad1.right_stick_x) > .1) {
            rightDT.setPower(gamepad1.right_stick_x);
            leftDT.setPower(-gamepad1.right_stick_x);
        }

        if (Math.abs(gamepad2.left_stick_y) > .1) {
            arm.setPower(gamepad1.left_stick_y);
        }

        if (gamepad2.left_bumper && !grab && grabTime.milliseconds() > 500) {
            grabTime.reset();
            rightGraby.setPosition(0);
            leftGraby.setPosition(1);
            grab = true;
        } else if (gamepad2.left_bumper && grab && grabTime.milliseconds() > 500) {
            grabTime.reset();
            rightGraby.setPosition(1);
            leftGraby.setPosition(0);
        }
    }
}

