package NightfallOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "rl", group = "opMode")
public class RocketLeagueTeleOp extends NightfallOpMode {

    public void loop() {
        //================================= DRIVE ==================================================

        double turnControl;
        if (gamepad1.left_bumper) {
            turnControl = 1.5;
        }
        else {
            turnControl = 1;
        }

        double leftPower = 0;
        double rightPower = 0;
        double max;
        double power;

        if(gamepad1.right_trigger > 0.1) {
            power = 0.5 + (gamepad1.left_stick_y/2);
        }
        else if(gamepad1.left_trigger > 0.1) {
            power = -0.5 - (gamepad1.left_stick_y/2);
        }
        else {
            power = 0;
        }

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

        startMotors(leftPower, rightPower);


        /*
        double turnControl;
        if (gamepad1.left_trigger > 0.1) {
            turnControl = 1.5;
        }
        else {
            turnControl = 1;
        }


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

        startMotors(leftPower, rightPower);

         */

    }
}
