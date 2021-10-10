package MirageOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "teleop", group = "opMode")
public class TankTeleOp extends MirageOpMode {

    public void loop() {
        //================================= DRIVE ==================================================
        //speed constant allows driver 1 to scale the speed of the robot
        /*
        double speedControl;
        if (gamepad1.right_trigger > 0.1) {
            speedControl = .4;
        }
        else {
            speedControl = 1;
        }

        double left = 0;
        double right = 0;
        double max;

        double forward = deadstick ((gamepad1.left_stick_y * Math.abs(gamepad1.left_stick_y)));
        double side= deadstick (gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x));

        left = forward - side;
        right = forward + side;

        max = Math.max(Math.abs(left), Math.abs(right));
        if (max > 1.0) {
            left /= max;
            right /= max;
        }

        startMotors(left * speedControl, right * speedControl);

         */
        double turnControl;
        if (gamepad1.left_trigger > 0.1) {
            turnControl = 1.5;
        }
        else {
            turnControl = 1;
        }

        double speedControl;
        if (gamepad1.right_trigger > 0.1) {
            speedControl = 0.4;
        }
        else
            speedControl = 1;

        double leftPower = 0;
        double rightPower = 0;
        double max;

        double power = deadstick(gamepad1.left_stick_y);
        double percentage = gamepad1.right_stick_x * turnControl;

        if (percentage > 1) {
            percentage = 1;
        }

        if (power != 0) {
            leftPower = power * (1 + percentage);
            rightPower = power * (1 - percentage);
        }
        else {
            leftPower = percentage;
            rightPower = -percentage;
        }

        max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
        if (max > 1.0){
            leftPower /= max;
            rightPower /= max;
        }

        startMotors(leftPower * speedControl, rightPower * speedControl);

    }
}
