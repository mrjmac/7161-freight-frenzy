package NightfallOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import NightfallLinearOpMode.Intake;

@TeleOp(name = "TankTeleOp", group = "opMode")
public class TankTeleOp extends NightfallOpMode {

    boolean capup = true;
    double speedControl;
    double macroHeight = 3;
    boolean manual = false;
    boolean hatchDown = false;
    public enum LiftState {
        LIFT_START,
        LIFT_RAISE,
        LIFT_LOWER
    }
    int heightModifier = 610;
    public enum IntakeState {
        INTAKE_START,
        INTAKE_MOVE,
        INTAKE_BACK
    }

    ElapsedTime capbruh = new ElapsedTime();
    ElapsedTime heightMod = new ElapsedTime();
    boolean liftActive = false;
    boolean intakeActive = false;

    LiftState liftState = LiftState.LIFT_START;
    IntakeState intakeState = IntakeState.INTAKE_START;

    public void loop() {
        //================================= DRIVE ==================================================
        //speed constant allows driver 1 to scale the speed of the robot
        //servo on lift side goes from 0 to 1; servo on non lift side goes from 1 to 0
        if (macroHeight == 4) {
            heightModifier = 533;
        } else {
            heightModifier = 570;
        }

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
        telemetry.addData("liftstate: ", liftState);
        telemetry.addData("liftEncoder:", lift.getCurrentPosition());
        telemetry.addData("liftPower:", lift.getPower());
        telemetry.addData("macroTime:", macro.milliseconds());
        telemetry.addData("heightModTime:", heightMod.milliseconds());
        telemetry.addData("capbruhTime:", capbruh.milliseconds());
        telemetry.update();

        //================================= INTAKE =================================================
/*
        if (gamepad1.a) {
            pivotDown();
        }

        if (gamepad1.left_bumper && gamepad1.right_bumper) {
            intake.setPower(1);
        } else if (gamepad1.right_bumper) {
            pivotDown();
            intake.setPower(-1);
        } else if (gamepad1.left_bumper) {
            pivotCross();
            intake.setPower(0);
        }
 */
        //TODO: test this, if you want to make a manual toggle please do, other than that test auto and tune it
        // BE VERY CAREFUL ESPECIALLY WITH THE INTAKE MOTOR SPINNING TOO MUCH, START WITH SMALL VALUES WHEN MOVING IT, OR AT LEAST TEST IN TELEOP
        // FIRST, IF YOU SPIN IT TOO MUCH, THE INTAKE WALL WILL SNAP, AND I WILL CRY SO DONT DO THAT
 /*       if (gamepad1.right_bumper){
            runIntake(1);
        }
        else {
            runIntake(0);
        }
        if (gamepad2.a) {
            gateUp();
        }
        if (gamepad2.b) {
            gateDown();
        }



        if (gamepad1.a) {
            intake.setPower(-.4);
        } else if (gamepad1.b) {
            intake.setPower(.4);
        } else {
            intake.setPower(0);
        }
        telemetry.addData("intakeEncoder:", getIntakeEncoder());

  */


        switch (intakeState) {
            case INTAKE_START:
                if (gamepad1.right_bumper && !intakeActive) {
                    intakeActive = true;
                    intakeState = IntakeState.INTAKE_MOVE;
                }
                break;
            case INTAKE_MOVE:
                gateDown();
                if (getIntakeEncoder() <= 290) {
                    setIntake();
                } else {
                    runIntake(1);
                    intake.setPower(.06);
                    if (gamepad1.right_bumper) {
                        intakeState = IntakeState.INTAKE_BACK;
                    }
                }
                break;
            case INTAKE_BACK:
                if (getIntakeEncoder() > 20) {
                    intakeReset(1);
                } else {
                    runIntake(0);
                    gateUp();
                    intake.setPower(0);
                    if (intake.getCurrentPosition() < 5)
                        resetIntakeEncoder();
                    intakeState = IntakeState.INTAKE_START;
                    intakeActive = false;
                }
                break;
            default:
                intakeState = IntakeState.INTAKE_START;
        }








        //================================= DUCKS ==================================================
        if (Math.abs(gamepad2.right_trigger) > 0.1) {
            duckR.setPower(.8);
            duckL.setPower(-.8);
        } else if (Math.abs(gamepad2.left_trigger) > 0.1) {
            duckR.setPower(-.8);
            duckL.setPower(.8 );
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

        if (!manual) {
            switch (liftState) {
                case LIFT_START:
                    if (gamepad2.left_bumper && !liftActive) {
                        liftActive = true;
                        liftState = LiftState.LIFT_RAISE;
                    }
                    break;
                case LIFT_RAISE:
                    if (lift.getCurrentPosition() < (heightModifier * (macroHeight - 1) - 50)) {
                        setLiftReal(macroHeight, heightModifier);
                    } else {
                        hatchDown();
                        lift.setPower(.06);
                        if (macro.milliseconds() > 1000) {
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
                    if (macroHeight != 1) {
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
            if (gamepad2.a && macro.milliseconds() > 250 && hatchDown) {
                macro.reset();
                hatchUp();
                hatchDown = false;
            } else if (gamepad2.a && macro.milliseconds() > 250 && !hatchDown) {
                macro.reset();
                hatchDown();
                hatchDown = true;
            }
        }


/*
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

 */
    }
}

