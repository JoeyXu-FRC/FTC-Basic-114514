package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class HelloWorld extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor motorTestFR;
    private DcMotor motorTestFL;
    private DigitalChannel digitalTouch;
    private DistanceSensor sensorColorRange;
    private Servo servoTest;


    @Override
    public void runOpMode() {
//        imu = hardwareMap.get(Gyroscope.class, "imu");
        motorTestFR = hardwareMap.get(DcMotor.class, "FrontRight");
        motorTestFL = hardwareMap.get(DcMotor.class, "FrontLeft");
//        digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
//        sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
        servoTest = hardwareMap.get(Servo.class, "Servo_0");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        double tgtMotorPower = 0;
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            tgtMotorPower = -this.gamepad1.left_stick_y;
            motorTestFR.setPower(-tgtMotorPower);
            motorTestFL.setPower(tgtMotorPower);
            if(gamepad1.y) {
                servoTest.setPosition(0);
            }else if(gamepad1.b){
                servoTest.setPosition(0.5);
            }else if(gamepad1.a){
                servoTest.setPosition(1);
            }
            telemetry.addData("Target Power", tgtMotorPower);
            telemetry.addData("Left Power", motorTestFL.getPower());
            telemetry.addData("Right Power", motorTestFR.getPower());
            telemetry.addData("Servo Position", servoTest.getPosition());
            telemetry.addData("Status", "Running");
            telemetry.update();


        }
    }
}