package NightfallLinearOpMode.Blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import NightfallLinearOpMode.Intake;
import NightfallLinearOpMode.Drivetrain;
import NightfallLinearOpMode.Lift;
import NightfallLinearOpMode.Vision;

@Autonomous(name = "Blue Cycles", group = "blue")
public class Cycles extends LinearOpMode {

    private Drivetrain drivetrain;
    private Vision vision;
    private Lift lift;
    private Intake intake;
    private int pos;

    public static double kpTurn5 = .670; //PID
    public static double kdTurn5 = 0.03; //PID
    public static double kpTurn45 = .335;
    public static double kdTurn45 = 0.05;
    public static double timeoutTurn = 1.5;
    public static double kpTurn90 = 0.176;
    public static double kdTurn90 = 0.07;
    public static double kpTurn180 = 0.129;
    public static double kdTurn180 = 0.00;

    private int liftHeight = 3;

    @Override
    public void runOpMode() throws InterruptedException {

        drivetrain = new Drivetrain(this);
        vision = new Vision(this);
        lift = new Lift(this);
        intake = new Intake(this);

        while (!isStarted()) {
            pos = vision.getPosNewMethodBlue();
            telemetry.addData("team marker pos: ", pos);
            telemetry.update();
        }
        telemetry.addData("team marker pos: ", pos);
        telemetry.update();

        //trajectories/pose go here

        idle();
        liftHeight = pos;

        if (liftHeight != 3) {
            drivetrain.gyroEncoderInch(1, 30, 1.5, 0);
            sleep(100);
            drivetrain.gyroEncoderInch(-1, 22, 1.5, -10);
        } else {
            drivetrain.gyroEncoderInch(1, 15, 1.5, 0);
            drivetrain.turnPD(-12.5, kpTurn5, kdTurn5, 2);
            drivetrain.gyroEncoderInch(1, 12, 1.5, -7.5);
            drivetrain.gyroEncoderInch(-1, 17.5, 1.5, -7.5);
        }
        //turn towards hub
        drivetrain.turnPD(30, kpTurn45, kdTurn45, 1.5);
        //drive to hub
        if (liftHeight != 1)
        drivetrain.gyroEncoderInch(1, 19, 1.5, 30);
        else
            drivetrain.gyroEncoderInch(1, 16.5, 1.5, 30);
        //deposit
        lift.setLift(liftHeight, 1);
        //drive back a little
        drivetrain.gyroEncoderInch(-1, 7, 1, 30);
        //turn towards crater
        drivetrain.turnPD(90, kpTurn45, kdTurn45, 2);
        //go into crater
        drivetrain.gyroEncoderInch(-1, 48, 2, 90);
        //intake.goatIntake(.85);
        //turn
        drivetrain.turnPD(45, kpTurn45, kdTurn45, 2);
        //forwards
        drivetrain.getElementDrive(-.7, 6, 1.5, 45, .85, 1.5);
        drivetrain.getElementDrive(1, 10, 1.5, 45, .85, 1.5);
        intake.goatIntake(0);
        //turn to back out
        drivetrain.turnPD(90, kpTurn45, kdTurn45, 2);
        //back out
        drivetrain.gyroEncoderInch(1, 45, 2.5, 90);
        //turn towards hub
        drivetrain.turnPD(50, kpTurn45, kdTurn45, 2);
        drivetrain.gyroEncoderInch(1, 8, 1.5, 50);
        //deposit
        lift.setLift(3, 1);
        drivetrain.gyroEncoderInch(-1, 12, 1.5, 30);
        drivetrain.turnPD(90, kpTurn45, kdTurn45, 1.5);
        //go into crater
        drivetrain.gyroEncoderInch(-1, 50, 3.5, 90);
        //intake.goatIntake(.85);
        //turn
        drivetrain.turnPD(45, kpTurn45, kdTurn45, 2);
        //forwards
        drivetrain.getElementDrive(.6, -8, 1.5, 45, .65, 1.5);


    }
}
