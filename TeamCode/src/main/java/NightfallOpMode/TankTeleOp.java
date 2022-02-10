package NightfallOpMode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import NightfallLinearOpMode.Intake;

@Config
@TeleOp(name = "TankTeleOp", group = "opMode")
public class TankTeleOp extends NightfallOpMode {

    public static double intakeSpeed = .85;
    double speedControl;
    int macroHeight = 3;
    boolean manual = false;
    boolean hatchDown = false;
    double hatchbruh = 0;
    double macro2 = 0;
    int motor = 1;
    public enum LiftState {
        LIFT_START,
        LIFT_RAISE,
        LIFT_LOWER
    }
    int heightModifier = 610;

    ElapsedTime capbruh = new ElapsedTime();
    ElapsedTime heightMod = new ElapsedTime();
    boolean liftActive = false;

    LiftState liftState = LiftState.LIFT_START;

    public void loop() {
        //================================= DRIVE ==================================================
        //speed constant allows driver 1 to scale the speed of the robot
        //servo on lift side goes from 0 to 1; servo on non lift side goes from 1 to 0
   /*     if (macroHeight == 4) {
            heightModifier = 533;
        } else {
            heightModifier = 570;
        }

    */

        if (gamepad1.right_trigger > 0.1) {
            speedControl = .5;
        } else if (gamepad1.left_trigger > 0.1) {
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



        //telemetry.addData("macro height", macro2);
        //telemetry.addData("gamepad2 left stick:", gamepad2.left_stick_y);
        telemetry.addData("manual:", manual);
        telemetry.addData("macroHeight:", macroHeight);
        //telemetry.addData("lift height", getLiftEncoder());
        //telemetry.addData("hatch bruh", hatchbruh);
        //telemetry.addData("ducks on?", gamepad2.right_bumper);
        //telemetry.addData("ducks reverse?", gamepad2.left_bumper);
        //telemetry.addData("hatch open", gamepad2.y);
        //telemetry.addData("hatch closed", gamepad2.x);
        //telemetry.addData("leftmotor:", FL.getCurrentPosition());
        //telemetry.addData("right motor:", FR.getCurrentPosition());

        telemetry.addData("liftstate: ", liftState);
        //telemetry.addData("liftEncoder:", lift.getCurrentPosition());
        //telemetry.addData("liftPower:", lift.getPower());
        telemetry.addData("macroTime:", macro.milliseconds());
        telemetry.addData("heightModTime:", heightMod.milliseconds());
        telemetry.addData("Red", color.red());
        telemetry.addData("Green", color.green());
        telemetry.addData("Blue", color.blue());
        //telemetry.addData("capbruhTime:", capbruh.milliseconds());
        /*
        telemetry.addData("leftFront:", FL.getCurrentPosition());
        telemetry.addData("leftMiddle:", ML.getCurrentPosition());
        telemetry.addData("leftBack:", BL.getCurrentPosition());
        telemetry.addData("rightFront:", FR.getCurrentPosition());
        telemetry.addData("rightMiddle:", MR.getCurrentPosition());
        telemetry.addData("rightBack:", BR.getCurrentPosition());
        telemetry.addData("motor:", motor);
        telemetry.addData("time:", macro);

         */
        telemetry.update();

        //================================= INTAKE =================================================



        if (gamepad1.a)
        {
            spinLeft.setPower(-1);
            spinRight.setPower(-1);
        }
        else if(gamepad1.b) {
            spinRight.setPower(1);
            spinLeft.setPower(-1);
        }
        else{
            spinLeft.setPower(0);
            spinRight.setPower(0);
        }



        if(gamepad1.right_bumper){
            intake.setPower(intakeSpeed);
        }
        if (gamepad1.left_bumper){
            intake.setPower(0);
        }
        if (gamepad1.left_bumper && gamepad1.right_bumper) {
            intake.setPower(-intakeSpeed);
        }





        //================================= DUCKS ==================================================
        if (Math.abs(gamepad2.right_trigger) > 0.1) {
            duckR.setPower(.8);
            duckL.setPower(-.8);
        } else if (Math.abs(gamepad2.left_trigger) > 0.1) {
            duckR.setPower(-.8);
            duckL.setPower(.8);
  /*      } else if (color.red() > 60 && color.green() > 60) {
            duckR.setPower(.8);
            duckL.setPower(-.8);

   */
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

        if (gamepad2.a && level.milliseconds() > 250 && hatchDown) {
            level.reset();
            hatchUp();
            hatchDown = false;
        } else if (gamepad2.a && level.milliseconds() > 250 && !hatchDown) {
            level.reset();
            hatchDown();
            hatchDown = true;
        }

        switch (macroHeight) {
            case 1:
                macro2 = 130;
                break;
            case 2:
                macro2 = 550;
                break;
            case 3:
                macro2 = 1450;
                break;
            case 4:
                macro2 = 1700;
        }

        if (!manual) {
            switch (liftState) {
                case LIFT_START:
                    if (gamepad2.left_bumper && !liftActive) {
                        liftActive = true;
                        liftState = LiftState.LIFT_RAISE;
                    }
                    break;
                case LIFT_RAISE:
                    if (lift.getCurrentPosition() < (macro2 - 50)) {//heightModifier * (macroHeight - 1) - 50)) {
                        setLiftReal(macro2);
                    } else {
                        if (macroHeight != 4 || macroHeight != 2) {
                            hatchDown();
                        } else {
                            hatchHalf();
                        }
                        lift.setPower(.1);
                        if (macro.milliseconds() > 750) {
                            liftState = LiftState.LIFT_LOWER;
                        }
                    }
                    break;
                case LIFT_LOWER:
     /*               hatchDown();
                    lift.setPower(.06);
        took this out, but it might have been what made it work            if (macro.milliseconds() < 1000) {
                    }

      */
                    hatchUp();
                    if (macroHeight != 0) {
                        if (lift.getCurrentPosition() > 65)
                            liftReset(.5);
                        else {
                            lift.setPower(0);
                            if (lift.getCurrentPosition() < 15)
                                resetLiftEncoder();
                            liftState = LiftState.LIFT_START;
                            liftActive = false;
                        }
                        break;
                    }
                    liftState = LiftState.LIFT_START;
                    break;
                default:
                    liftState = LiftState.LIFT_START;
            }
        }


        if (gamepad2.y && macro.milliseconds() > 1000 && !manual) {
            macro.reset();
            liftState = LiftState.LIFT_START;
            manual = true;
        } else if (gamepad2.y && macro.milliseconds() > 1000 && manual) {
            macro.reset();
            manual = false;
        }


        //manual code
        if (manual) {
            lift.setPower(-deadstick(gamepad2.left_stick_y));
            if (lift.getCurrentPosition() > 100 && Math.abs(gamepad2.left_stick_y) < .05)
                lift.setPower(0.06);
            if (gamepad2.a && macro.milliseconds() > 100 && hatchDown) {
                macro.reset();
                hatchUp();
                hatchDown = false;
            } else if (gamepad2.a && macro.milliseconds() > 100 && !hatchDown) {
                macro.reset();
                hatchDown();
                hatchDown = true;
            }
        }

        /*
        if(gamepad2.dpad_left && (capbruh.milliseconds() > 30))
        {
            hatchbruh -= 0.01;
            capbruh.reset();
        }
        else if (gamepad2.dpad_right && capbruh.milliseconds() > 30)
        {
            hatchbruh += 0.01;
            capbruh.reset();
        }

        if(gamepad2.b)
        {
            setHatch(hatchbruh);
        }

         */



/*
        if (gamepad1.x)
        {
            if (motor == 1) {
                FL.setPower(1);
            } else if (motor == 2) {
                ML.setPower(1);
            } else if (motor == 3) {//R
                BL.setPower(1);
            } else if (motor == 4) {
                FR.setPower(1);
            } else if (motor == 5) {
                MR.setPower(1);
            } else if (motor == 6) {
                BR.setPower(1);
            }
        } else {
            stopMotors();
        }
*/
     /*   if (gamepad1.dpad_up && level.milliseconds() > 250) {
            level.reset();
            motor++;
        } else if (gamepad1.dpad_down && level.milliseconds() > 250) {
            motor--;
            level.reset();
        }

      */

        if (gamepad1.dpad_right)
            resetDT();
        }


}

