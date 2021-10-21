package NightfallOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TankTeleOp", group = "opMode")
public class TankTeleOp extends NightfallOpMode {

    public void loop() {
        //================================= DRIVE ==================================================
        //speed constant allows driver 1 to scale the speed of the robot

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



    }
}
