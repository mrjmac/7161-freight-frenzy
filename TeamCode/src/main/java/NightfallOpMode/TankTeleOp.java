package NightfallOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TankTeleOp", group = "opMode")
public class TankTeleOp extends NightfallOpMode {

    boolean capup = true;
    double speedControl;
    double macroHeight = 3;
    boolean manual = false;
    public enum LiftState {
        LIFT_START,
        LIFT_RAISE,
        LIFT_LOWER

    };
    int heightModifier = 550;

    ElapsedTime capbruh = new ElapsedTime();
    ElapsedTime heightMod = new ElapsedTime();
    boolean liftActive = false;

    LiftState liftState = LiftState.LIFT_START;

    public void loop() {
        //================================= DRIVE ==================================================
        //speed constant allows driver 1 to scale the speed of the robot
        //servo on lift side goes from 0 to 1; esrvo on non lift side goes from 1 to 0
        if (gamepad1.right_trigger > 0.1) {
            speedControl = .4;
        } else {
            speedControl = 1;
        }

        double left = 0;
        double right = 0;
        double max;

        double forward = deadstick((gamepad1.left_stick_y * Math.abs(gamepad1.left_stick_y)));
        double side = deadstick(gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x));

        left = forward - side;
        right = forward + side;

        max = Math.max(Math.abs(left), Math.abs(right));
        if (max > 1.0) {
            left /= max;
            right /= max;
        }

        startMotors(left * speedControl, right * speedControl);

        telemetry.addData("gamepad2 left stick:", gamepad2.left_stick_y);
        telemetry.addData("manual:", manual);
        telemetry.addData("macroHeight:", macroHeight);
        telemetry.addData("lift height", getLiftEncoder());
        telemetry.addData("ducks on?", gamepad2.right_bumper);
        telemetry.addData("ducks reverse?", gamepad2.left_bumper);
        telemetry.addData("hatch open", gamepad2.y);
        telemetry.addData("hatch closed", gamepad2.x);
        telemetry.addData("leftmotor:", FL.getCurrentPosition());
        telemetry.addData("right motor:", FR.getCurrentPosition());
        telemetry.update();

        //================================= INTAKE =================================================

        if (gamepad1.a) {
            pivotDown();
        }

        if (gamepad1.left_bumper && gamepad1.right_bumper) {
            intake.setPower(1);
        } else if (gamepad1.right_bumper) {
            pivotCross();
            intake.setPower(-1);
        } else if (gamepad1.left_bumper) {
            pivotCross();
            intake.setPower(0);
        }



        //================================= DUCKS ==================================================
        if (Math.abs(gamepad2.right_trigger) > 0.1) {
            duckR.setPower(.8);
            duckL.setPower(.8);
        } else if (Math.abs(gamepad2.left_trigger) > 0.1) {
            duckR.setPower(-.8);
            duckL.setPower(-.8 );
        } else {
            duckR.setPower(0);
            duckL.setPower(0);
        }


        //================================= LIFT ===================================================

        //macros
        if (gamepad2.dpad_up && macroHeight < 4 && !manual && heightMod.milliseconds() > 200) {
            macroHeight += 1;
            heightMod.reset();
        }
        if (gamepad2.dpad_down && macroHeight > 0 && !manual && heightMod.milliseconds() > 200) {
            macroHeight -= 1;
            heightMod.reset();
        }

        switch (liftState) {
            case LIFT_START:
                if (gamepad2.left_bumper && !liftActive) {
                    liftActive = true;
                    liftState = LiftState.LIFT_RAISE;
                    manual = false;
                }
                break;
            case LIFT_RAISE:
                setLiftReal(macroHeight);
                liftState = LiftState.LIFT_LOWER;
                break;
            case LIFT_LOWER:
                hatchUp();
                if (macroHeight != 1)
                    liftReset(0.5);
                liftState = LiftState.LIFT_START;
                liftActive = false;
                break;
            default:
                liftState = LiftState.LIFT_START;
        }

        if (gamepad1.y && liftState != LiftState.LIFT_START) {
            liftState = LiftState.LIFT_START;
            manual = true;
        }


        //manual code
        if (manual) {
            lift.setPower(-deadstick(gamepad2.left_stick_y));
            if (gamepad2.b) {
                hatchUp();
            } else if (gamepad2.a) {
                hatchDown();
            }
        }


        if (gamepad2.x && capup && capbruh.milliseconds() > 200) {
            capDown();
            capup = false;
            capbruh.reset();
        }
        if (gamepad2.x && !capup && capbruh.milliseconds() > 200) {
            capUp();
            capup = true;
            capbruh.reset();
        }
    }
}

