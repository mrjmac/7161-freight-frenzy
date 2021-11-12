package NightfallLinearOpMode;

import android.transition.ChangeBounds;

import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;

import java.util.logging.ConsoleHandler;

public class Drivetrain {

    public DcMotor BL; //back left - [3 e1]
    public DcMotor ML; //middle left - [3 c]
    public DcMotor FL; //front left - [2 c]
    public DcMotor BR; //back right - [2 e1]
    public DcMotor MR; //middle right - [1 e1]
    public DcMotor FR; //front right - [0 e1]

    public CRServo duckL; //left duck - [port number]
    public CRServo duckR; //right duck - [port number]

    LinearOpMode opMode;
    private final String LOG_TAG = "DriveTrain";
    private ElapsedTime runtime = new ElapsedTime();

    private Orientation angles;
    public BNO055IMU imu;
    private BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();



    public Drivetrain(LinearOpMode opMode)throws InterruptedException {

        this.opMode = opMode;
        FR = this.opMode.hardwareMap.dcMotor.get("FR");
        FL = this.opMode.hardwareMap.dcMotor.get("FL");
        BR = this.opMode.hardwareMap.dcMotor.get("BR");
        BL = this.opMode.hardwareMap.dcMotor.get("BL");
        ML = this.opMode.hardwareMap.dcMotor.get("ML");
        MR = this.opMode.hardwareMap.dcMotor.get("MR");

        duckL = this.opMode.hardwareMap.crservo.get("duckL");
        duckR = this.opMode.hardwareMap.crservo.get("duckR");

        this.opMode.telemetry.addData(LOG_TAG + "init", "finished init");
        this.opMode.telemetry.update();

        FR.setDirection(DcMotorSimple.Direction.FORWARD);
        MR.setDirection(DcMotorSimple.Direction.FORWARD);
        BR.setDirection(DcMotorSimple.Direction.FORWARD);
        FL.setDirection(DcMotorSimple.Direction.FORWARD);
        ML.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.FORWARD);



        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ML.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        MR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = this.opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

    }


    public void startMotors(double l, double r) {
        FR.setPower(r);
        MR.setPower(r);
        BR.setPower(r);
        FL.setPower(l);
        ML.setPower(l);
        BL.setPower(l);
    }

    public void stopMotors() {
        FR.setPower(0);
        MR.setPower(0);
        BR.setPower(0);
        FL.setPower(0);
        ML.setPower(0);
        BL.setPower(0);
    }

    public void resetEncoders() throws InterruptedException {
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ML.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ML.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public int getEncoderAvg() {
        return (Math.abs(FL.getCurrentPosition()) + Math.abs(FR.getCurrentPosition())) / 2;
    }

    public void gyroEncoderInch(double speed, double inches, double timeoutS, int heading) throws InterruptedException {
        runtime.reset();
        while (this.opMode.opModeIsActive() && !this.opMode.isStopRequested() && runtime.seconds() >= timeoutS) {
            // Ticks is the math for the amount of inches, ticks is paired with getcurrentposition
            double ticks = inches * 32;
            double kP = speed / 29.3; //nice for 24 inches, should scale up to whatever movement up
            heading = -heading;
            //runtime isn't used, this is just a backup call which we don't need
            //if the position is less than the number of inches, than it sets the motors to speed
            double leftChange = 0;
            double rightChange = 0;
            double prevLeft = 0;
            double prevRight = 0;

            resetEncoders();
            while (getEncoderAvg() <= ticks && this.opMode.opModeIsActive()) {
                double error = (ticks - getEncoderAvg()) / 32 ;
                double ChangeP = error * kP;
                double AngleDiff = getTrueDiff(heading);

                double currentInchesL = Math.abs(FL.getCurrentPosition());
                leftChange = currentInchesL - prevLeft;
                prevLeft = currentInchesL;

                double currentInchesR = Math.abs(FR.getCurrentPosition());
                rightChange = currentInchesR - prevRight;
                prevRight = currentInchesR;

                double angleChangeRad = ((leftChange * 32) - (rightChange * 32)) / 14.72145669;
                double angleChangeDeg = getTrueDiff(Math.toDegrees(angleChangeRad));

                // double multiplierR = 1;
                //double multiplierL = 1;
                // double fudgeFactor = (1.0 - AngleDiff / 40.0)/.93;
                double gyroScalePower = AngleDiff * .02;

                /*
                if (Math.abs(AngleDiff) > 2) {
                    multiplierR = 1.05;//more power left positive
                    multiplierL = 0.95;
                    GyroScalePower = 0;
                }
                else if ((AngleDiff) < -2) {
                    multiplierL = 1.05;
                    multiplierR = 0.95;
                    GyroScalePower = 0;
                }
                else {
                    multiplierL = 1;
                    multiplierR = 1;
                    GyroScalePower = AngleDiff * .02;
                }

                 */
                double left = (ChangeP + gyroScalePower);
                double right = (ChangeP - gyroScalePower);
                if (runtime.seconds() > timeoutS)
                    break;
                double max = Math.max(Math.abs(left), Math.abs(right));
                if (max > 1.0) {
                    left /= max;
                    right /= max;
                }
                startMotors(left, right);
                this.opMode.telemetry.addData("MotorPowLeft:", left);
                this.opMode.telemetry.addData("angle change manual", angleChangeDeg);
                this.opMode.telemetry.addData("MotorPowRight:", right);
         //     this.opMode.telemetry.addData("fudge:", fudgeFactor);
                this.opMode.telemetry.addData("encoders:", getEncoderAvg());
                this.opMode.telemetry.addData("error:", error);
                this.opMode.telemetry.addData("changeP", ChangeP);
                this.opMode.telemetry.addData("gyro", getTrueDiff(heading));
                this.opMode.telemetry.addData("runtime:", runtime.seconds());
                this.opMode.telemetry.update();
                if (error < .25 || runtime.seconds() >= timeoutS || Math.abs(ChangeP) < .02) {
                    stopMotors();
                    break;
                }
            }
            stopMotors();
            break;
        }


    }

    public void turnPI(double angle, double p, double i, double timeout) {
        while (this.opMode.opModeIsActive() && !this.opMode.isStopRequested()) {
            runtime.reset();
            double kP = p / 33;
            double kI = i / 100000;
            double currentTime = runtime.milliseconds();
            double pastTime = 0;
            double angleDiff = getTrueDiff(-angle);
            double changePID;
            while (Math.abs(angleDiff) > .95 && runtime.seconds() < timeout && this.opMode.opModeIsActive()) {
                pastTime = currentTime;
                currentTime = runtime.milliseconds();
                double dT = currentTime - pastTime;
                angleDiff = getTrueDiff(- angle);
                changePID = (angleDiff * kP) + (dT * angleDiff * kI);
                if (changePID <= 0) {
                    startMotors(changePID - .10, -changePID + .10);
                } else {
                    startMotors(changePID + .10, -changePID - .10);
                }
                opMode.telemetry.addData("PID: ", changePID);
                opMode.telemetry.addData("diff", angleDiff);
                opMode.telemetry.addData("P", angleDiff * kP);
                opMode.telemetry.addData("I", dT * angleDiff * kI);
                opMode.telemetry.update();

            }
            stopMotors();

            angleDiff = getTrueDiff(-angle);
            if (!(Math.abs(angleDiff) > .95)) {
                break;
            }

        }
    }

    public void turnPID(double angle, double p, double i, double d, double timeout) {
        while (this.opMode.opModeIsActive() && !this.opMode.isStopRequested()) {
            runtime.reset();
            double kP = p / 33;
            double kI = i / 100000;
            double kD = d / .70;
            double currentTime = runtime.milliseconds();
            double pastTime = 0;
            double prevAngleDiff = getTrueDiff(-angle);
            double angleDiff = prevAngleDiff;
            double changePID;
            while (Math.abs(angleDiff) > .95 && runtime.seconds() < timeout && this.opMode.opModeIsActive()) {
                pastTime = currentTime;
                currentTime = runtime.milliseconds();
                double dT = currentTime - pastTime;
                angleDiff = getTrueDiff(-angle);
                changePID = (angleDiff * kP) + (dT * angleDiff * kI) + ((angleDiff - prevAngleDiff) / dT * kD);
                if (changePID <= 0) {
                    startMotors(changePID - .10, -changePID + .10);
                } else {
                    startMotors(changePID + .10, -changePID - .10);
                }
                opMode.telemetry.addData("PID: ", changePID);
                opMode.telemetry.addData("diff", angleDiff);
                opMode.telemetry.addData("P", angleDiff * kP);
                opMode.telemetry.addData("I", dT * angleDiff * kI);
                this.opMode.telemetry.addData("D", ((Math.abs(angleDiff) - Math.abs(prevAngleDiff)) / dT * kD));
                this.opMode.telemetry.addData("angle:", getGyroYaw());
                opMode.telemetry.update();

            }
            stopMotors();

            angleDiff = getTrueDiff(-angle);
            if (!(Math.abs(angleDiff) > .95)) {
                break;
            }

        }
    }


    public void turnPD(double angle, double p, double d, double timeout) {
        runtime.reset();
        while (this.opMode.opModeIsActive() && !this.opMode.isStopRequested() && runtime.seconds() <= timeout) {
            double kP = p / 33;
            double kD = d / .70;
            double currentTime = runtime.milliseconds();
            double pastTime = 0;
            double prevAngleDiff = getTrueDiff(-angle);
            double angleDiff = prevAngleDiff;
            double changePID;
            while (Math.abs(angleDiff) > .95 && runtime.seconds() < timeout && this.opMode.opModeIsActive()) {
                pastTime = currentTime;
                currentTime = runtime.milliseconds();
                double dT = currentTime - pastTime;
                angleDiff = getTrueDiff(-angle);
                changePID = (angleDiff * kP) + ((angleDiff - prevAngleDiff) / dT * kD);
                if (changePID <= 0) {
                    startMotors(changePID - .10, -changePID + .10);
                } else {
                    startMotors(changePID + .10, -changePID - .10);
                }
                this.opMode.telemetry.addData("P", (angleDiff * kP));
                this.opMode.telemetry.addData("D", ((Math.abs(angleDiff) - Math.abs(prevAngleDiff)) / dT * kD));
                this.opMode.telemetry.addData("angle:", getGyroYaw());
                this.opMode.telemetry.update();
                if (runtime.seconds() > timeout || changePID < 0.06)
                    break;
                prevAngleDiff = angleDiff;
            }
            stopMotors();

            angleDiff = getTrueDiff(-angle);
            if (!(Math.abs(angleDiff) > .95)) {
                break;
            }


        }
    }

    public void arcTurnPD(double angle, double p, double d, double timeout) {
        while (this.opMode.opModeIsActive() && !this.opMode.isStopRequested()) {
            runtime.reset();
            double kP = p / 33;            //TODO: tune this
            double kD = d / .70;
            double currentTime = runtime.milliseconds();
            double pastTime = 0;
            double prevAngleDiff = getTrueDiff(-angle);
            double angleDiff = prevAngleDiff;
            double changePID;
            while (Math.abs(angleDiff) > .95 && runtime.seconds() < timeout && this.opMode.opModeIsActive()) {
                pastTime = currentTime;
                currentTime = runtime.milliseconds();
                double dT = currentTime - pastTime;
                angleDiff = getTrueDiff(-angle);
                changePID = (angleDiff * kP) + ((angleDiff - prevAngleDiff) / dT * kD);
                if (changePID <= 0) {
                    startMotors(0, -changePID + .10);
                } else {
                    startMotors(changePID + .10, 0);
                }
                this.opMode.telemetry.addData("P", (angleDiff * kP));
                this.opMode.telemetry.addData("D", ((Math.abs(angleDiff) - Math.abs(prevAngleDiff)) / dT * kD));
                this.opMode.telemetry.addData("angle:", getGyroYaw());
                this.opMode.telemetry.update();

                prevAngleDiff = angleDiff;
            }
            stopMotors();

            angleDiff = getTrueDiff(-angle);
            if (!(Math.abs(angleDiff) > .95)) {
                break;
            }


        }
    }


    public void updateGyroValues() {
        angles = imu.getAngularOrientation();
    }

    public double getGyroYaw() {
            updateGyroValues();
            return angles.firstAngle;
    }

    public double getTrueDiff(double origAngle) {
        double angleDiff;
        double curAngle = getGyroYaw();
        if (origAngle - curAngle >= 180)
            angleDiff = -1 * ((360 + curAngle) - origAngle);
        else if (curAngle - origAngle >= 180)
            angleDiff = (360 + origAngle) - curAngle;
        else
            angleDiff = origAngle - curAngle;

        return angleDiff;
    }





    //---------------------------------------DUCKS--------------------------------------------------

    public void duckStart(double pow) {
        duckR.setPower(pow * .8);
        duckL.setPower(pow * .8);
    }

    public void duckStop() {
        duckR.setPower(0);
        duckL.setPower(0);
    }

    //--------------------------------------TESTING-------------------------------------------------

    public Acceleration getGravity() {

        return imu.getGravity();

    }

    public Temperature getTemp() {

        return imu.getTemperature();

    }
}
